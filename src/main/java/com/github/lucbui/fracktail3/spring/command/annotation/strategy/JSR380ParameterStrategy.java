package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.params.ClassLimit;
import com.github.lucbui.fracktail3.magic.params.TypeLimits;
import com.github.lucbui.fracktail3.magic.params.jsr380.JSR380TypeDescriptors;
import com.github.lucbui.fracktail3.magic.params.jsr380.JSR380Types;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.TypeDescriptor;

import javax.validation.constraints.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Order(1)
public class JSR380ParameterStrategy implements ParameterComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSR380ParameterStrategy.class);

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        if(parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.command.annotation.Parameter.class)) {
            TypeLimits limits = getTypeLimits(parameter, new TypeDescriptor(MethodParameter.forParameter(parameter)));
            if(limits != null) {
                base.setType(limits);
                LOGGER.info("+-Adding JSR380 data: {}", base.getType());
            }
        }
        return base;
    }

    private TypeLimits getTypeLimits(AnnotatedElement element, TypeDescriptor paramType) {
        JSR380Types type = JSR380TypeDescriptors.getJSR380ForType(paramType);

        boolean isOptional = !element.isAnnotationPresent(NotNull.class);
        List<TypeLimits> limits = getLimits(element, type);
        if(limits.isEmpty()) {
            return isOptional ?  null : new ClassLimit(paramType).optional(false);
        } else if(limits.size() == 1) {
            return CollectionUtils.extractSingleton(limits).optional(isOptional);
        } else {
            LOGGER.warn("Multiple annotations specified...I don't know how to handle this yet, sorry.");
            return new ClassLimit(paramType).optional(isOptional);
        }
    }

    private List<TypeLimits> getLimits(AnnotatedElement element, JSR380Types type) {
        List<TypeLimits> list = new ArrayList<>();
        if(element.isAnnotationPresent(Null.class)) list.add(type.isNull());
        if(element.isAnnotationPresent(AssertFalse.class)) list.add(type.assertFalse());
        if(element.isAnnotationPresent(AssertTrue.class))  list.add(type.assertTrue());
        if(element.isAnnotationPresent(Min.class)) list.add(type.min(element.getAnnotation(Min.class).value()));
        if(element.isAnnotationPresent(Max.class)) list.add(type.max(element.getAnnotation(Max.class).value()));
        if(element.isAnnotationPresent(DecimalMin.class)) list.add(type.decimalMin(element.getAnnotation(DecimalMin.class).value()));
        if(element.isAnnotationPresent(DecimalMax.class)) list.add(type.decimalMax(element.getAnnotation(DecimalMax.class).value()));
        if(element.isAnnotationPresent(Positive.class)) list.add(type.positive(false));
        if(element.isAnnotationPresent(PositiveOrZero.class)) list.add(type.positive(true));
        if(element.isAnnotationPresent(Negative.class)) list.add(type.negative(false));
        if(element.isAnnotationPresent(NegativeOrZero.class)) list.add(type.negative(true));
        if(element.isAnnotationPresent(Digits.class)) {
            Digits digits = element.getAnnotation(Digits.class);
            list.add(type.digits(digits.integer(), digits.fraction()));
        }
        if(element.isAnnotationPresent(Email.class)) list.add(type.email());
        if(element.isAnnotationPresent(NotBlank.class)) list.add(type.notBlank());
        if(element.isAnnotationPresent(Pattern.class)) list.add(type.pattern(element.getAnnotation(Pattern.class).regexp()));
        if(element.isAnnotationPresent(NotEmpty.class)) list.add(type.notEmpty());
        if(element.isAnnotationPresent(Size.class)) {
            Size size = element.getAnnotation(Size.class);
            list.add(type.size(size.min(), size.max()));
        }
        if(element.isAnnotationPresent(Past.class)) list.add(type.past(false));
        if(element.isAnnotationPresent(PastOrPresent.class)) list.add(type.past(true));
        if(element.isAnnotationPresent(Future.class)) list.add(type.future(false));
        if(element.isAnnotationPresent(FutureOrPresent.class)) list.add(type.future(true));
        return list;
    }
}
