package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.params.ClassLimit;
import com.github.lucbui.fracktail3.magic.params.TypeLimits;
import com.github.lucbui.fracktail3.magic.params.jsr380.JSR380TypeDescriptors;
import com.github.lucbui.fracktail3.magic.params.jsr380.JSR380Types;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterToObjectConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.TypeLimitService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Order(0)
public class ParameterStrategy implements ParameterComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterStrategy.class);

    @Autowired
    private TypeLimitService typeLimitService;

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        if(parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.command.annotation.Parameter.class)) {
            com.github.lucbui.fracktail3.spring.command.annotation.Parameter pAnnot =
                    parameter.getAnnotation(com.github.lucbui.fracktail3.spring.command.annotation.Parameter.class);
            int value = pAnnot.value();

            base.setType(getTypeLimits(parameter));
            base.setIndex(pAnnot.value());
            base.setName(StringUtils.defaultIfEmpty(pAnnot.name(), parameter.getName()));
            base.setHelp(StringUtils.defaultIfEmpty(pAnnot.description(), base.getName()));
            base.setFunc(new ParameterToObjectConverterFunction(value, typeLimitService));
            base.setOptional(parameter.getType() == Optional.class || pAnnot.optional());

            LOGGER.info("+-Parameter {},name:{},type:{},description:{},optional:{}", value,
                    base.getName(), base.getType(), base.getHelp(), base.isOptional());
        }
        return base;
    }

    private TypeLimits getTypeLimits(Parameter parameter) {
        TypeDescriptor paramType = new TypeDescriptor(MethodParameter.forParameter(parameter));

        JSR380Types type = JSR380TypeDescriptors.getJSR380ForType(paramType);

        boolean isOptional = !parameter.isAnnotationPresent(NotNull.class);
        List<TypeLimits> limits = getLimits(parameter, type);
        if(limits.isEmpty()) {
            return new ClassLimit(paramType).optional(isOptional);
        } else if(limits.size() == 1) {
            return CollectionUtils.extractSingleton(limits).optional(isOptional);
        } else {
            LOGGER.warn("Multiple annotations specified...I don't know how to handle this yet, sorry.");
            return new ClassLimit(paramType).optional(isOptional);
        }
    }

    private List<TypeLimits> getLimits(Parameter parameter, JSR380Types type) {
        List<TypeLimits> list = new ArrayList<>();
        if(parameter.isAnnotationPresent(Null.class)) list.add(type.isNull());
        if(parameter.isAnnotationPresent(AssertFalse.class)) list.add(type.assertFalse());
        if(parameter.isAnnotationPresent(AssertTrue.class))  list.add(type.assertTrue());
        if(parameter.isAnnotationPresent(Min.class)) list.add(type.min(parameter.getAnnotation(Min.class).value()));
        if(parameter.isAnnotationPresent(Max.class)) list.add(type.max(parameter.getAnnotation(Max.class).value()));
        if(parameter.isAnnotationPresent(DecimalMin.class)) list.add(type.decimalMin(parameter.getAnnotation(DecimalMin.class).value()));
        if(parameter.isAnnotationPresent(DecimalMax.class)) list.add(type.decimalMax(parameter.getAnnotation(DecimalMax.class).value()));
        if(parameter.isAnnotationPresent(Positive.class)) list.add(type.positive(false));
        if(parameter.isAnnotationPresent(PositiveOrZero.class)) list.add(type.positive(true));
        if(parameter.isAnnotationPresent(Negative.class)) list.add(type.negative(false));
        if(parameter.isAnnotationPresent(NegativeOrZero.class)) list.add(type.negative(true));
        if(parameter.isAnnotationPresent(Digits.class)) {
            Digits digits = parameter.getAnnotation(Digits.class);
            list.add(type.digits(digits.integer(), digits.fraction()));
        }
        if(parameter.isAnnotationPresent(Email.class)) list.add(type.email());
        if(parameter.isAnnotationPresent(NotBlank.class)) list.add(type.notBlank());
        if(parameter.isAnnotationPresent(Pattern.class)) list.add(type.pattern(parameter.getAnnotation(Pattern.class).regexp()));
        if(parameter.isAnnotationPresent(NotEmpty.class)) list.add(type.notEmpty());
        if(parameter.isAnnotationPresent(Size.class)) {
            Size size = parameter.getAnnotation(Size.class);
            list.add(type.size(size.min(), size.max()));
        }
        if(parameter.isAnnotationPresent(Past.class)) list.add(type.past(false));
        if(parameter.isAnnotationPresent(PastOrPresent.class)) list.add(type.past(true));
        if(parameter.isAnnotationPresent(Future.class)) list.add(type.future(false));
        if(parameter.isAnnotationPresent(FutureOrPresent.class)) list.add(type.future(true));
        return list;
    }
}
