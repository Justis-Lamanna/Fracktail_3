package com.github.milomarten.fracktail3.spring.command.handler;

import com.github.milomarten.fracktail3.magic.command.Command;
import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;
import com.github.milomarten.fracktail3.spring.command.model.ParameterComponent;
import com.github.milomarten.fracktail3.spring.service.TypeLimitService;

import java.util.Optional;

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
        Optional<Object> optional = context.getParameters().getParameter(param)
                .map(s -> typeLimitService.convert(s, parameterInfo.getType()));

        if(optional.isPresent()) {
            return optional.get();
        } else if(parameterInfo.isOptional()) {
            return typeLimitService.convert(null, parameterInfo.getType());
        } else {
            throw new IllegalArgumentException("No parameter " + param + " for context");
        }
    }
}
