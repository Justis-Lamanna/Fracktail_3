package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.xsd.DTDCustomClass;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public interface SupportsCustom<T> {
    Class<T> getParsedClass();

    default T getFromCustom(DTDCustomClass custom) {
        if(custom.getClazz() != null) {
            return getFromClassElement(custom.getClazz(), custom.getMethod());
        } else if(custom.getSpring() != null) {
            return getFromSpringBean(custom.getSpring());
        }
        throw new BotConfigurationException("Custom actions must be specified by <class> or <spring> element");
    }

    default T getFromClassElement(String className, String methodName) {
        try {
            Class<?> clazz = Class.forName(className);
            if(StringUtils.isEmpty(methodName)) {
                return getFromClass(clazz);
            } else {
                return getFromStaticMethod(clazz, methodName);
            }
        } catch (ClassNotFoundException e) {
            throw new BotConfigurationException("Unknown class " + className, e);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new BotConfigurationException("Unable to instantiate class type " + className, e);
        } catch (NoSuchMethodException e) {
            throw new BotConfigurationException("Unknown zero-param method " + className + "." + methodName, e);
        } catch (InvocationTargetException e) {
            throw new BotConfigurationException("Unable to invoke method " + className + "." + methodName, e);
        }
    }

    default T getFromClass(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        if(!getParsedClass().isAssignableFrom(clazz)) {
            throw new BotConfigurationException("Class " + clazz + " is not a subtype of " + getParsedClass().getSimpleName());
        }
        return (T) clazz.newInstance();
    }

    default T getFromStaticMethod(Class<?> clazz, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = clazz.getMethod(methodName);
        if(!Modifier.isStatic(method.getModifiers())) {
            throw new BotConfigurationException("Method " + clazz.getCanonicalName() + "." + methodName + " is not static");
        }
        if(!getParsedClass().isAssignableFrom(method.getReturnType())) {
            throw new BotConfigurationException("Return type " + method.getReturnType() + " is not a subtype of " + clazz.getCanonicalName());
        }
        return (T) method.invoke(null);
    }

    default T getFromSpringBean(String spring) {
        throw new BotConfigurationException("Spring bean " + getClass().getSimpleName() + " are not permitted");
    }
}
