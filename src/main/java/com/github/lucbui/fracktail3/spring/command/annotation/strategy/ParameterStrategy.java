package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.params.ClassLimit;
import com.github.lucbui.fracktail3.magic.params.TypeLimits;
import com.github.lucbui.fracktail3.spring.command.handler.ParameterToObjectConverterFunction;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.service.TypeLimitService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.*;

@Component
@Order(0)
public class ParameterStrategy implements ParameterComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterStrategy.class);

    private static final TypeDescriptor BOOLEAN = TypeDescriptor.valueOf(Boolean.class);

    private static final TypeDescriptor BIG_INTEGER = TypeDescriptor.valueOf(BigInteger.class);
    private static final TypeDescriptor BIG_DECIMAL = TypeDescriptor.valueOf(BigDecimal.class);
    private static final TypeDescriptor BYTE = TypeDescriptor.valueOf(Byte.class);
    private static final TypeDescriptor SHORT = TypeDescriptor.valueOf(Short.class);
    private static final TypeDescriptor INTEGER = TypeDescriptor.valueOf(Integer.class);
    private static final TypeDescriptor LONG = TypeDescriptor.valueOf(Long.class);
    private static final TypeDescriptor[] NUMBER_TYPES = {
            BIG_DECIMAL, BIG_INTEGER, BYTE, SHORT, INTEGER, LONG
    };

    private static final TypeDescriptor DATE = TypeDescriptor.valueOf(Date.class);
    private static final TypeDescriptor CALENDAR = TypeDescriptor.valueOf(Calendar.class);
    private static final TypeDescriptor INSTANT = TypeDescriptor.valueOf(Instant.class);
    private static final TypeDescriptor LOCAL_DATE = TypeDescriptor.valueOf(LocalDate.class);
    private static final TypeDescriptor LOCAL_DATE_TIME = TypeDescriptor.valueOf(LocalDateTime.class);
    private static final TypeDescriptor LOCAL_TIME = TypeDescriptor.valueOf(LocalTime.class);
    private static final TypeDescriptor MONTH_DAY = TypeDescriptor.valueOf(MonthDay.class);
    private static final TypeDescriptor OFFSET_DATE_TIME = TypeDescriptor.valueOf(OffsetDateTime.class);
    private static final TypeDescriptor OFFSET_TIME = TypeDescriptor.valueOf(OffsetTime.class);
    private static final TypeDescriptor YEAR = TypeDescriptor.valueOf(Year.class);
    private static final TypeDescriptor YEAR_MONTH = TypeDescriptor.valueOf(YearMonth.class);
    private static final TypeDescriptor ZONED_DATE_TIME = TypeDescriptor.valueOf(ZonedDateTime.class);
    private static final TypeDescriptor HIJRAH_DATE = TypeDescriptor.valueOf(HijrahDate.class);
    private static final TypeDescriptor JAPANESE_DATE = TypeDescriptor.valueOf(JapaneseDate.class);
    private static final TypeDescriptor MINGUO_DATE = TypeDescriptor.valueOf(MinguoDate.class);
    private static final TypeDescriptor THAI_BUDDHIST_DATE = TypeDescriptor.valueOf(ThaiBuddhistDate.class);
    private static final TypeDescriptor[] DATE_TIME_TYPES = {
            DATE, CALENDAR, INSTANT, LOCAL_DATE, LOCAL_DATE_TIME, LOCAL_TIME, MONTH_DAY, OFFSET_DATE_TIME,
            OFFSET_TIME, YEAR, YEAR_MONTH, ZONED_DATE_TIME, HIJRAH_DATE, JAPANESE_DATE, MINGUO_DATE, THAI_BUDDHIST_DATE
    };

    private static final TypeDescriptor CHAR_SEQUENCE = TypeDescriptor.valueOf(CharSequence.class);
    private static final TypeDescriptor COLLECTION = TypeDescriptor.valueOf(Collection.class);
    private static final TypeDescriptor MAP = TypeDescriptor.valueOf(Map.class);
    private static final TypeDescriptor ARRAY = TypeDescriptor.valueOf(Object[].class);

    private static final TypeDescriptor OBJECT = TypeDescriptor.valueOf(Object.class);

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

        Set<JSR380> annotations = getAnnotationSet(parameter);
        TypeLimits limit = new ClassLimit(paramType);

        return limit;
    }

    private boolean assertOneOf(TypeDescriptor test, TypeDescriptor... options) {
        for (TypeDescriptor option : options) {
            if (!test.isAssignableTo(option)) {
                return false;
            }
        }
        return true;
    }

    private Set<JSR380> getAnnotationSet(Parameter parameter) {
        TypeDescriptor paramType = new TypeDescriptor(MethodParameter.forParameter(parameter));
        Set<JSR380> set = EnumSet.noneOf(JSR380.class);

        for(JSR380 item : JSR380.values()) {
            if(parameter.isAnnotationPresent(item.annotationClass)) {
                if(assertOneOf(paramType, item.types)){
                    set.add(item);
                } else {
                    LOGGER.warn("Parameter {} is annotated with @{}, but type {} is not supported. Ignoring...",
                            parameter.getName(),
                            item.annotationClass.getSimpleName(),
                            paramType);
                }
            }
        }

        return set;
    }

    public enum JSR380 {
        //All
        NOT_NULL(NotNull.class),
        NULL(Null.class),
        //Boolean
        ASSERT_FALSE(AssertFalse.class, BOOLEAN),
        ASSERT_TRUE(AssertTrue.class, BOOLEAN),
        //Numbers
        MAX(Max.class, NUMBER_TYPES),
        MIN(Min.class, NUMBER_TYPES),
        DECIMAL_MAX(DecimalMax.class, BIG_DECIMAL, BIG_INTEGER, CHAR_SEQUENCE, BYTE, SHORT, INTEGER, LONG),
        DECIMAL_MIN(DecimalMin.class, BIG_DECIMAL, BIG_INTEGER, CHAR_SEQUENCE, BYTE, SHORT, INTEGER, LONG),
        DIGITS(Digits.class, BIG_DECIMAL, BIG_INTEGER, CHAR_SEQUENCE, BYTE, SHORT, INTEGER, LONG),
        POSITIVE(Positive.class, NUMBER_TYPES),
        POSITIVE_OR_ZERO(PositiveOrZero.class, NUMBER_TYPES),
        NEGATIVE(Negative.class, NUMBER_TYPES),
        NEGATIVE_OR_ZERO(NegativeOrZero.class, NUMBER_TYPES),
        //Strings
        EMAIL(Email.class, CHAR_SEQUENCE),
        NOT_BLANK(NotBlank.class, CHAR_SEQUENCE),
        PATTERN(Pattern.class, CHAR_SEQUENCE),
        //Times
        FUTURE(Future.class, DATE_TIME_TYPES),
        FUTURE_OR_PRESENT(FutureOrPresent.class, DATE_TIME_TYPES),
        PAST(Past.class, DATE_TIME_TYPES),
        PAST_OR_PRESENT(PastOrPresent.class, DATE_TIME_TYPES),
        //Sequences
        NOT_EMPTY(NotEmpty.class, CHAR_SEQUENCE, COLLECTION, MAP, ARRAY),
        SIZE(Size.class, CHAR_SEQUENCE, COLLECTION, MAP, ARRAY);

        private final Class<? extends Annotation> annotationClass;
        private final TypeDescriptor[] types;

        JSR380(Class<? extends Annotation> annotationClass, TypeDescriptor... types) {
            this.annotationClass = annotationClass;
            this.types = types;
        }
    }
}
