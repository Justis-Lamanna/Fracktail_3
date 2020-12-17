package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.github.lucbui.fracktail3.spring.command.model.ParameterBaseComponent;
import com.github.lucbui.fracktail3.spring.service.Defaults;
import com.github.lucbui.fracktail3.spring.service.ParameterConverters;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Mono;

/**
 * A ParameterConverterFunction which converts a map value into some object
 */
public class VariableToObjectConverterFunction implements ParameterBaseComponent.ParameterConverterFunction<BaseContext<?>> {
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
    public Object apply(BaseContext<?> context) {
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
