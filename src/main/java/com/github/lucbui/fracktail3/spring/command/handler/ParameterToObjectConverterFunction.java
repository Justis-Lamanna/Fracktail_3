package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.service.TypeLimitService;

/**
 * A ParameterConverterFunction which converts a parameter into some type
 */
public class ParameterToObjectConverterFunction implements ParameterComponent.PCFunction {
    private final int param;
    private final TypeLimitService typeLimitService;

    /**
     * Initialize
     * @param param The parameter index to retrieve
     * @param typeLimitService The converters to use
     */
    public ParameterToObjectConverterFunction(int param, TypeLimitService typeLimitService) {
        this.param = param;
        this.typeLimitService = typeLimitService;
    }

    @Override
    public Object apply(CommandUseContext context) {
        Command.Parameter parameterInfo = context.getCommand().getParameters().get(param);
        return context.getParameters().getParameter(param)
                .map(s -> typeLimitService.convertAndValidate(s, parameterInfo.getType(), parameterInfo.isOptional()))
                .orElseThrow(() -> new IllegalArgumentException("No parameter " + param + " for context"));
    }
}
