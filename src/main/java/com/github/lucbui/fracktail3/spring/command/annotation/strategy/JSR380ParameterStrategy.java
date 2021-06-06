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
import java.lang.annotation.Annotation;
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
            TypeLimits limits = getTypeLimits(new TypeDescriptor(MethodParameter.forParameter(parameter)));
            if(limits != null) {
                base.setType(limits);
                LOGGER.info("+-Adding JSR380 data: {}", base.getType());
            }
        }
        return base;
    }

    private TypeLimits getTypeLimits(TypeDescriptor paramType) {
        JSR380Types jsr380Type = JSR380TypeDescriptors.getJSR380ForType(paramType);

        boolean isOptional = !isAnnotationPresent(paramType, NotNull.class);
        List<TypeLimits> limits = getLimits(paramType, jsr380Type);
        if(limits.isEmpty()) {
            return isOptional ?  null : new ClassLimit(paramType).optional(false);
        } else if(limits.size() == 1) {
            return CollectionUtils.extractSingleton(limits).optional(isOptional);
        } else {
            LOGGER.warn("Multiple annotations specified...I don't know how to handle this yet, sorry.");
            return null;
        }
    }

    private List<TypeLimits> getLimits(TypeDescriptor element, JSR380Types jsr380Type) {
        List<TypeLimits> list = new ArrayList<>();
        if(isAnnotationPresent(element, Null.class)) list.add(jsr380Type.isNull());
        if(isAnnotationPresent(element, AssertFalse.class)) list.add(jsr380Type.assertFalse());
        if(isAnnotationPresent(element, AssertTrue.class))  list.add(jsr380Type.assertTrue());
        if(isAnnotationPresent(element, Min.class)) list.add(jsr380Type.min(element.getAnnotation(Min.class).value()));
        if(isAnnotationPresent(element, Max.class)) list.add(jsr380Type.max(element.getAnnotation(Max.class).value()));
        if(isAnnotationPresent(element, DecimalMin.class)) list.add(jsr380Type.decimalMin(element.getAnnotation(DecimalMin.class).value()));
        if(isAnnotationPresent(element, DecimalMax.class)) list.add(jsr380Type.decimalMax(element.getAnnotation(DecimalMax.class).value()));
        if(isAnnotationPresent(element, Positive.class)) list.add(jsr380Type.positive(false));
        if(isAnnotationPresent(element, PositiveOrZero.class)) list.add(jsr380Type.positive(true));
        if(isAnnotationPresent(element, Negative.class)) list.add(jsr380Type.negative(false));
        if(isAnnotationPresent(element, NegativeOrZero.class)) list.add(jsr380Type.negative(true));
        if(isAnnotationPresent(element, Digits.class)) {
            Digits digits = element.getAnnotation(Digits.class);
            list.add(jsr380Type.digits(digits.integer(), digits.fraction()));
        }
        if(isAnnotationPresent(element, Email.class)) list.add(jsr380Type.email());
        if(isAnnotationPresent(element, NotBlank.class)) list.add(jsr380Type.notBlank());
        if(isAnnotationPresent(element, Pattern.class)) list.add(jsr380Type.pattern(element.getAnnotation(Pattern.class).regexp()));
        if(isAnnotationPresent(element, NotEmpty.class)) list.add(jsr380Type.notEmpty());
        if(isAnnotationPresent(element, Size.class)) {
            Size size = element.getAnnotation(Size.class);
            list.add(jsr380Type.size(size.min(), size.max()));
        }
        if(isAnnotationPresent(element, Past.class)) list.add(jsr380Type.past(false));
        if(isAnnotationPresent(element, PastOrPresent.class)) list.add(jsr380Type.past(true));
        if(isAnnotationPresent(element, Future.class)) list.add(jsr380Type.future(false));
        if(isAnnotationPresent(element, FutureOrPresent.class)) list.add(jsr380Type.future(true));
        return list;
    }

    private boolean isAnnotationPresent(TypeDescriptor typeDescriptor, Class<? extends Annotation> annotation) {
        return typeDescriptor.getAnnotation(annotation) != null;
    }
}
