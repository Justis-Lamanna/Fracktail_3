package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.service.ParameterConverters;
import com.github.lucbui.fracktail3.spring.util.Defaults;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Mono;

/**
 * A ParameterConverterFunction which converts a map value into some object
 */
public class VariableToObjectConverterFunction implements ParameterComponent.ParameterConverterFunction {
    private final Class<?> paramType;
    private final String key;
    private final ParameterConverters converters;

    /**
     * Initialize
     * @param paramType The parameter type
     * @param key The key to retrieve
     * @param converters The converters to use
     */
    public VariableToObjectConverterFunction(Class<?> paramType, String key, ParameterConverters converters) {
        this.paramType = paramType;
        this.key = key;
        this.converters = converters;
    }

    @Override
    public Object apply(CommandUseContext<?> context) {
        AsynchronousMap<String, Object> map = context.getMap();
        if(map.containsKey(key)) {
            if(ClassUtils.isAssignable(Mono.class, paramType)) {
                return map.getAsync(key);
            }
            return converters.convertToType(map.get(key), paramType);
        }
        return Defaults.getDefault(paramType);
    }
}
