package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.command.action.PlatformBasicAction;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.trigger.CronTrigger;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ScheduleEventTrigger;
import com.github.lucbui.fracktail3.spring.annotation.Name;
import com.github.lucbui.fracktail3.spring.annotation.Schedule;
import com.github.lucbui.fracktail3.spring.annotation.Usage;
import com.github.lucbui.fracktail3.spring.plugin.CommandPlugin;
import com.github.lucbui.fracktail3.spring.plugin.Plugin;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.util.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * The PostProcessor which compiles and registers commands with Fracktail
 * Beans of type Command are not processed, and simply registered in the command list.
 * Beans of type CommandAction or PlatformBasicAction are given the id and name of their corresponding bean.
 * All beans are scanned for methods or fields bearing the @Command annotation. These are compiled into a full Command,
 * and added to the command list.
 *
 * A minimal command would be a zero-parameter method annotated with @Command, or a field annotated with @Command. Additional
 * parameters may be used to fully customize the created command:
 * - @Name allows for specifying of one or more names for the command to use. If omitted, the method or field name is used.
 * - @Usage allows for specifying usage details (to be retrieved via a help command).
 *
 * Additional annotations allow for customization of the Command's action (such as injecting parameters or handling return
 * values in a certain way)
 */
@Component
public class CommandListPostProcessor implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListPostProcessor.class);

    @Autowired
    private ReflectiveCommandActionFactory factory;

    @Autowired
    private CommandList commandList;

    @Autowired
    private ScheduledEvents scheduledEvents;

    @Autowired
    private Plugins plugins;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Plugin) {
            LOGGER.debug("Installing Plugin {}", ((Plugin) bean).getId());
            plugins.addPlugin((Plugin) bean);
            if(bean instanceof CommandPlugin) {
                List<Command> commands = ((CommandPlugin) bean).addAdditionalCommands();
                commands.forEach(c -> LOGGER.debug("Adding Command Bean from plugin {} of id {}", bean.getClass(), c.getId()));
                commands.forEach(this::addOrMerge);
            }
        }

        if(bean instanceof Command) {
            LOGGER.debug("Adding Command Bean of id {}", beanName);
            Command c = (Command)bean;
            addOrMerge(c);
        } else if(bean instanceof CommandAction) {
            LOGGER.debug("Adding CommandAction Bean of id {}", beanName);
            Command c = new Command.Builder(beanName)
                        .withAction((CommandAction) bean)
                        .build();
            addOrMerge(c);
        } else if(bean instanceof PlatformBasicAction) {
            LOGGER.debug("Adding PlatformBasicAction Bean of id {}", beanName);
            Command c = new Command.Builder(beanName)
                        .withAction((PlatformBasicAction) bean)
                        .build();
            addOrMerge(c);
        } else {
            LOGGER.trace("Investigating Bean {} for command candidates", beanName);
            CommandAnnotationParser parser = new CommandAnnotationParser(bean);
            ReflectionUtils.doWithMethods(bean.getClass(), parser,
                    method -> method.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Command.class));

            ReflectionUtils.doWithFields(bean.getClass(), parser,
                    field -> field.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Command.class));

            ScheduledAnnotationParser scheduledParser = new ScheduledAnnotationParser(bean);
            ReflectionUtils.doWithMethods(bean.getClass(), scheduledParser,
                    method -> method.isAnnotationPresent(Schedule.class));

            ReflectionUtils.doWithFields(bean.getClass(), scheduledParser,
                    field -> field.isAnnotationPresent(Schedule.class));
        }
        return bean;
    }

    private void addOrMerge(Command c) {
        Optional<Command> old = commandList.getCommandById(c.getId());
        if(old.isPresent()) {
            LOGGER.debug("Overwriting command. One day, the commands will be merged, instead");
            plugins.onCommandMerge(old.get(), c);
        } else {
            commandList.add(c);
            plugins.onCommandAdd(c);
        }
    }

    private void addOrMerge(ScheduledEvent event) {
        Optional<ScheduledEvent> old = scheduledEvents.getById(event.getId());
        if(old.isPresent()) {
            LOGGER.debug("Overwriting command, so ignoring");
            // plugins.onScheduledEventMerge(old.get(), event);
        } else {
            scheduledEvents.add(event);
            // plugins.onCommandAdd(event);
        }
    }

    private static String returnStringOrMemberName(String id, Member member) {
        if(StringUtils.isBlank(id)) {
            return member.getName();
        } else {
            return id;
        }
    }

    private class CommandAnnotationParser implements ReflectionUtils.MethodCallback, ReflectionUtils.FieldCallback {
        private final Object bean;

        private CommandAnnotationParser(Object bean) {
            this.bean = bean;
        }

        @Override
        public void doWith(Method method) throws IllegalArgumentException {
            String id = getId(method);
            LOGGER.debug("Adding @Command-annotated method {}", id);
            Command.Builder c = new Command.Builder(id);

            c.withAction(factory.createAction(bean, method));

            if(method.isAnnotationPresent(Name.class)) {
                Name nameAnnotation = method.getAnnotation(Name.class);
                LOGGER.debug("Method {} named via annotation as {}", method.getName(), nameAnnotation.value());
                c.withNames(nameAnnotation.value());
            }

            if(method.isAnnotationPresent(Usage.class)) {
                Usage usageAnnotation = method.getAnnotation(Usage.class);
                LOGGER.debug("Method {} with help text as {}", method.getName(), usageAnnotation.value());
                c.withHelp(AnnotationUtils.fromUsage(usageAnnotation));
            }

            addOrMerge(c.build());
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException {
            String id = getId(field);
            LOGGER.debug("Adding @Command-annotated field {}", id);
            Command.Builder c = new Command.Builder(id);

            c.withAction(factory.createAction(bean, field));

            if(field.isAnnotationPresent(Name.class)) {
                Name nameAnnotation = field.getAnnotation(Name.class);
                LOGGER.debug("Field {} named via annotation as {}", field.getName(), nameAnnotation.value());
                c.withNames(nameAnnotation.value());
            }

            if(field.isAnnotationPresent(Usage.class)) {
                Usage usageAnnotation = field.getAnnotation(Usage.class);
                LOGGER.debug("Field {} with help text as {}", field.getName(), usageAnnotation.value());
                c.withHelp(AnnotationUtils.fromUsage(usageAnnotation));
            }

            addOrMerge(c.build());
        }

        private <T extends AnnotatedElement & Member> String getId(T member) {
            return CommandListPostProcessor.returnStringOrMemberName(
                    member.getAnnotation(com.github.lucbui.fracktail3.spring.annotation.Command.class).value(), member);
        }
    }

    private class ScheduledAnnotationParser implements ReflectionUtils.MethodCallback, ReflectionUtils.FieldCallback {
        private final Object bean;

        private ScheduledAnnotationParser(Object bean) {
            this.bean = bean;
        }

        @Override
        public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
            String id = getId(method);
            LOGGER.debug("Adding @Scheduled-annotated method {}", id);

            ScheduledEvent event = getEvent(id, bean, method);
            addOrMerge(event);
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            String id = getId(field);
            LOGGER.debug("Adding @Scheduled-annotated field {}", id);

            ScheduledEvent event = getEvent(id, bean, field);
            addOrMerge(event);
        }

        private <T extends AnnotatedElement & Member> String getId(T member) {
            return returnStringOrMemberName(member.getAnnotation(Schedule.class).value(), member);
        }

        private ScheduledEvent getEvent(String id, Object bean, AnnotatedElement member) {
            return new ScheduledEvent(id, getTrigger(member), c -> c.respond("Hello, world!"));
        }

        private ScheduleEventTrigger getTrigger(AnnotatedElement member) {
            return new CronTrigger("0 * * ? * SUN-THU");
        }
    }
}
