package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.DTDCustomClass;
import com.github.lucbui.fracktail3.xsd.I18NString;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class AbstractParser<T> implements Parser<T> {
    private final Class<T> parsedClass;

    public AbstractParser(Class<T> parsedClass) {
        this.parsedClass = parsedClass;
    }

    @Override
    public Class<T> getParsedClass() {
        return parsedClass;
    }

    protected T getFromCustom(DTDCustomClass custom) {
        if(custom.getClazz() != null) {
            return getFromClassElement(custom.getClazz(), custom.getMethod());
        }
        throw new BotConfigurationException("Custom actions must be specified by <class> or <spring> element");
    }

    protected T getFromClassElement(String className, String methodName) {
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

    protected T getFromClass(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        if(!parsedClass.isAssignableFrom(clazz)) {
            throw new BotConfigurationException("Class " + clazz + " is not a subtype of " + parsedClass);
        }
        return (T) clazz.newInstance();
    }

    protected T getFromStaticMethod(Class<?> clazz, String methodName) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = clazz.getMethod(methodName);
        if(!Modifier.isStatic(method.getModifiers())) {
            throw new BotConfigurationException("Method " + method.getDeclaringClass().getCanonicalName() + "." + method.getName() + " is not static");
        }
        if(!parsedClass.isAssignableFrom(method.getReturnType())) {
            throw new BotConfigurationException("Return type " + method.getReturnType() + " is not a subtype of " + parsedClass);
        }
        return (T) method.invoke(null);
    }

    protected static String getDebugString(String type, I18NString string) {
        return BooleanUtils.isTrue(string.isI18N()) ?
                type + " From Key: " + string.getValue() :
                type + ": " + string.getValue();
    }

    protected static Resolver<String> fromI18NString(I18NString string) {
        return BooleanUtils.isTrue(string.isI18N()) ?
                new I18NResolver(string.getValue()) :
                Resolver.identity(string.getValue());
    }
}
