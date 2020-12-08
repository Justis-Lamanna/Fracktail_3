package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.service.ParameterConverters;
import com.github.lucbui.fracktail3.spring.util.Defaults;

import java.lang.reflect.Array;
import java.util.List;
import java.util.stream.Collectors;

public class ParameterRangeToArrayConverterFunction implements ParameterComponent.ParameterConverterFunction {
    private final int start;
    private final int end;
    private final Class<?> memberType;
    private final ParameterConverters converters;

    public ParameterRangeToArrayConverterFunction(int start, int end, Class<?> memberType, ParameterConverters converters) {
        this.start = start;
        this.end = end;
        this.memberType = memberType;
        this.converters = converters;
    }

    @Override
    public Object apply(CommandUseContext<?> context) {
        return context.getParameters().getParameters(start, end).stream()
                .map(opt -> opt.map(s -> (Object)converters.convertToType(s, memberType))
                        .orElseGet(() -> Defaults.getDefault(memberType)))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> createFrom(memberType, list)));
    }

    protected Object createFrom(Class<?> innerClass, List<?> objs) {
        Object arr = Array.newInstance(innerClass, objs.size());
        for(int idx = 0; idx < objs.size(); idx++) {
            Array.set(arr, idx, objs.get(idx));
        }
        return arr;
    }
}
