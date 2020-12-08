package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;

/**
 * A ParameterConverterFunction which converts a range of parameters into a single string
 * As of right now, this simply returns the raw parameters (the entire message, minus the command name)
 */
public class ParameterRangeToStringConverterFunction implements ParameterComponent.ParameterConverterFunction {
    @Override
    public Object apply(CommandUseContext<?> context) {
        return context.getParameters().getRaw();
    }
}
