package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.spring.command.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

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

    public CommandAction createAction(Object obj, Method method) {
        MethodComponent methodComponent = methodComponentFactory.compileMethod(obj, method);
        List<ParameterComponent> components = parameterComponentFactory.compileParameters(obj, method);
        ReturnComponent returnComponent = returnComponentFactory.compileReturn(obj, method);
        ExceptionComponent exceptionComponent = exceptionComponentFactory.compileException(obj, method);
        LOGGER.debug("Finished compiling method {}", method.getName());
        return new MethodCallingAction(methodComponent, components, obj, method, returnComponent, exceptionComponent);
    }

    public CommandAction createAction(Object obj, Field field) {
        MethodComponent methodComponent = methodComponentFactory.compileField(obj, field);
        ReturnComponent returnComponent = returnComponentFactory.compileReturn(obj, field);
        ExceptionComponent exceptionComponent = new ExceptionComponent(); //exceptionComponentFactory.compileException(obj, field);
        LOGGER.debug("Finished compiling field {}", field.getName());
        return new FieldCallingAction(methodComponent, obj, field, returnComponent, exceptionComponent);
    }
}
