package com.github.lucbui.fracktail3.spring.service;

import com.github.lucbui.fracktail3.magic.params.ClassLimit;
import com.github.lucbui.fracktail3.magic.params.TypeLimits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

/**
 * Service which, given a TypeLimit, performs relevant conversion and validation.
 * Running any object through this service is guaranteed to result in an object that conforms
 * to the type limit, or throws an exception.
 */
@Service
public class TypeLimitService {
    @Autowired
    private ConversionService conversionService;

    /**
     * Convert and validate the input object
     * @param input The input object
     * @param limits The TypeLimits to verify against
     * @return The converted object which meets the TypeLimits
     * @throws IllegalArgumentException The input object does not match the TypeLimits
     */
    public Object convertAndValidate(Object input, TypeLimits limits, boolean optional) {
        Object converted = convert(input, limits, optional);
        if(limits.matches(converted)) {
            return converted;
        } else {
            throw new IllegalArgumentException("Unable to convert " + input + " to match TypeLimit " + limits);
        }
    }

    private Object convert(Object input, TypeLimits limits, boolean optional) {
        if(limits instanceof ClassLimit) {
            ClassLimit cl = (ClassLimit) limits;
            TypeDescriptor inputType = TypeDescriptor.forObject(input);
            if(conversionService.canConvert(inputType, cl.getTypeDescriptor())) {
                try {
                    Object finalVal = conversionService.convert(input, inputType, cl.getTypeDescriptor());
                    if (finalVal == null && !optional) {
                        throw new IllegalArgumentException("Unable to convert " + input + " to match TypeLimit " + limits + "(null + non-optional)");
                    }
                    return finalVal;
                } catch (ConversionException ex) {
                    if (optional) return limits.getDefault();
                    else throw ex;
                }
            } else {
                if(optional) return limits.getDefault();
                else throw new ConverterNotFoundException(inputType, cl.getTypeDescriptor());
            }
        } else {
            if(input == null) {
                if(optional) return limits.getDefault();
                else throw new IllegalArgumentException("Unable to convert null to match TypeLimit " + limits + "(null + non-optional)");
            } else {
                return input;
            }
        }
    }
}
