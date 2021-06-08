package com.github.milomarten.fracktail3.spring.command.annotation.strategy;

import com.github.milomarten.fracktail3.magic.params.AnyType;
import com.github.milomarten.fracktail3.spring.command.annotation.Parameter;
import com.github.milomarten.fracktail3.spring.command.annotation.Parameters;
import com.github.milomarten.fracktail3.spring.command.model.MethodComponent;
import com.github.milomarten.fracktail3.spring.command.plugin.MethodComponentStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Order(0)
public class ParametersStrategy implements MethodComponentStrategy {
    @Override
    public MethodComponent decorate(Object obj, Method method, MethodComponent base) {
        addViaParameters(base, method.getName(), method.getAnnotation(Parameters.class));
        return base;
    }

    @Override
    public MethodComponent decorate(Object obj, Field field, MethodComponent base) {
        addViaParameters(base, field.getName(), field.getAnnotation(Parameters.class));
        return base;
    }

    private void addViaParameters(MethodComponent base, String methodName, Parameters parameters) {
        for(Parameter p : parameters.value()) {
            String placeholder = methodName + "_" + p.value();
            base.addAdditionalParam(new MethodComponent.AdditionalParam(
                    AnyType.INSTANCE,
                    p.value(),
                    StringUtils.firstNonBlank(p.name(), placeholder),
                    StringUtils.firstNonBlank(p.description(), p.name(), placeholder)
            ));
        }
    }
}
