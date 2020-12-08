package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.service.ParameterConverters;
import com.github.lucbui.fracktail3.spring.util.Defaults;

public class ParameterToObjectConverterFunction implements ParameterComponent.ParameterConverterFunction {
    private final Class<?> paramType;
    private final int param;
    private final ParameterConverters converters;

    public ParameterToObjectConverterFunction(Class<?> paramType, int param, ParameterConverters converters) {
        this.paramType = paramType;
        this.param = param;
        this.converters = converters;
    }

    @Override
    public Object apply(CommandUseContext<?> context) {
        return context.getParameters().getParameter(param)
                .map(s -> (Object)converters.convertToType(s, paramType))
                .orElseGet(() -> Defaults.getDefault(paramType));
    }
}
