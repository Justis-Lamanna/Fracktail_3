package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public interface SupportsCustom<T> {
    Class<T> getParsedClass();

    default Logger logger() {
        return LoggerFactory.getLogger(getClass());
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
        logger().debug("Instantiating {} by creating a new instance of class {}", clazz.getSimpleName(), clazz.getCanonicalName());
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
        logger().debug("Instantiating {} by invoking {}.{}", clazz.getSimpleName(), clazz.getCanonicalName(), method.getName());
        return (T) method.invoke(null);
    }
}
