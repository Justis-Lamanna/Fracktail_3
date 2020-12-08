package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;

public class ParameterRangeToStringConverterFunction implements ParameterComponent.ParameterConverterFunction {
    @Override
    public Object apply(CommandUseContext<?> context) {
        return context.getParameters().getRaw();
    }
}
