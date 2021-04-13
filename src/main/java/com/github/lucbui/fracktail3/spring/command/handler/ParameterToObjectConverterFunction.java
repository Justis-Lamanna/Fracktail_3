package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ParameterBaseComponent;
import com.github.lucbui.fracktail3.spring.service.Defaults;
import com.github.lucbui.fracktail3.spring.service.ParameterConverters;

/**
 * A ParameterConverterFunction which converts a parameter into some type
 */
public class ParameterToObjectConverterFunction implements ParameterBaseComponent.ParameterConverterFunction<CommandUseContext> {
    private final Class<?> paramType;
    private final int param;
    private final ParameterConverters converters;

    /**
     * Initialize
     * @param paramType The type to convert the parameter to
     * @param param The parameter index to retrieve
     * @param converters The converters to use
     */
    public ParameterToObjectConverterFunction(Class<?> paramType, int param, ParameterConverters converters) {
        this.paramType = paramType;
        this.param = param;
        this.converters = converters;
    }

    @Override
    public Object apply(CommandUseContext context) {
        return context.getParameters().getParameter(param)
                .map(s -> (Object)converters.convertToType(s, paramType))
                .orElseGet(() -> Defaults.getDefault(paramType));
    }
}
