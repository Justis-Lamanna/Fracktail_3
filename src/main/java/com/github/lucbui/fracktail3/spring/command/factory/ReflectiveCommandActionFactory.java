package com.github.lucbui.fracktail3.spring.command.factory;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.guard.AndGuard;
import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.schedule.action.ScheduledAction;
import com.github.lucbui.fracktail3.spring.command.model.*;
import com.github.lucbui.fracktail3.spring.schedule.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A factory which combines multiple factories to create a complete action for an object and method/field
 */
@Component
public class ReflectiveCommandActionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectiveCommandActionFactory.class);

    @Autowired
    private MethodComponentFactory methodComponentFactory;

    @Autowired
    private ParameterComponentFactory parameterComponentFactory;

    @Autowired
    private ReturnComponentFactory returnComponentFactory;

    @Autowired
    private ExceptionComponentFactory exceptionComponentFactory;

    /**
     * Create an action from an object + method
     * @param obj The bean object
     * @param method The method to compile
     * @return A created CommandAction, constructed via object and method annotations
     */
    public Command createCommand(Object obj, Method method) {
        MethodComponent methodComponent = methodComponentFactory.compileMethod(obj, method);
        List<ParameterComponent> components = parameterComponentFactory.compileParameters(obj, method);
        ReturnComponent returnComponent = returnComponentFactory.compileReturn(obj, method);
        ExceptionComponent exceptionComponent = exceptionComponentFactory.compileException(obj, method);
        CommandAction action = new MethodCallingAction(methodComponent, components, obj, method, returnComponent, exceptionComponent);

        return new Command(methodComponent.getId(), methodComponent.getNames(), methodComponent.getHelp(),
                compileGuards(methodComponent, components), action, compileParameter(methodComponent, components));
    }

    /**
     * Create an action from an object + field
     * @param obj The bean object
     * @param field The field to compile
     * @return A created CommandAction, constructed via object and field annotations
     */
    public Command createCommand(Object obj, Field field) {
        MethodComponent methodComponent = methodComponentFactory.compileField(obj, field);
        ReturnComponent returnComponent = returnComponentFactory.compileReturn(obj, field);
        ExceptionComponent exceptionComponent = exceptionComponentFactory.compileException(obj, field);

        CommandAction action = new FieldCallingAction(methodComponent, obj, field, returnComponent, exceptionComponent);
        return new Command(methodComponent.getId(), methodComponent.getNames(), methodComponent.getHelp(),
                compileGuards(methodComponent, null), action, compileParameter(methodComponent, null));
    }

    /**
     * Create a scheduled action from an object + method
     * @param obj The bean object
     * @param method The method to compile
     * @return A created ScheduledAction, constructed via object and method annotations
     */
    public ScheduledEvent createScheduledAction(Object obj, Method method) {
        MethodScheduledComponent methodComponent = methodComponentFactory.compileScheduleMethod(obj, method);
        if(methodComponent.getTrigger() == null) {
            throw new BotConfigurationException("No trigger for Scheduled method " + methodComponent.getId());
        }

        List<ParameterScheduledComponent> components = parameterComponentFactory.compileScheduleParameters(obj, method);
        ReturnScheduledComponent returnComponent = returnComponentFactory.compileScheduledReturn(obj, method);
        ExceptionScheduledComponent exceptionComponent = exceptionComponentFactory.compileScheduleException(obj, method);

        ScheduledAction action = new MethodCallingScheduledAction(components, obj, method, returnComponent, exceptionComponent);
        return new ScheduledEvent(methodComponent.getId(), methodComponent.getTrigger(), action);
    }

    /**
     * Create a scheduled action from an object + field
     * @param obj The bean object
     * @param field The field to compile
     * @return A created ScheduledAction, constructed via object and field annotations
     */
    public ScheduledEvent createScheduledAction(Object obj, Field field) {
        MethodScheduledComponent methodComponent = methodComponentFactory.compileScheduleMethod(obj, field);
        if(methodComponent.getTrigger() == null) {
            throw new BotConfigurationException("No trigger for Scheduled method " + methodComponent.getId());
        }

        ReturnScheduledComponent returnComponent = returnComponentFactory.compileScheduledReturn(obj, field);
        ExceptionScheduledComponent exceptionComponent = exceptionComponentFactory.compileScheduleException(obj, field);

        ScheduledAction action = new FieldCallingScheduledAction(obj, field, returnComponent, exceptionComponent);
        return new ScheduledEvent(methodComponent.getId(), methodComponent.getTrigger(), action);
    }

    private Guard compileGuards(MethodComponent methods, List<ParameterComponent> parameters) {
        Stream<Guard> paramGuards = parameters == null ?
                Stream.empty() :
                parameters.stream().flatMap(pc -> pc.getGuards().stream());
        Guard[] guards = Stream.concat(methods.getGuards().stream(), paramGuards).toArray(Guard[]::new);
        if(guards.length == 0) return null;
        else if(guards.length == 1) return guards[0];
        else return new AndGuard(guards);
    }

    private List<Command.Parameter> compileParameter(MethodComponent methods, List<ParameterComponent> parameters) {
        Map<Integer, Command.Parameter> bookkeeping = new HashMap<>();

        if(parameters != null) {
            parameters.stream()
                    .filter(pc -> Objects.nonNull(pc.getName()))
                    .map(pc -> new Command.Parameter(pc.getIndex(), pc.getName(), pc.getHelp(), pc.getType()))
                    .forEach(pc -> bookkeeping.put(pc.getIndex(), pc));
        }

        methods.getAdditionalParams().stream()
                .map(ap -> new Command.Parameter(ap.getIndex(), ap.getName(), ap.getHelp(), ap.getType()))
                .forEach(pc -> bookkeeping.put(pc.getIndex(), pc));

        return bookkeeping.values()
                .stream()
                .sorted(Comparator.comparing(Command.Parameter::getIndex))
                .collect(Collectors.toList());
    }
}
