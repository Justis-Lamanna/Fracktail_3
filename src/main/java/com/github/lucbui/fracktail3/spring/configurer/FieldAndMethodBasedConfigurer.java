package com.github.lucbui.fracktail3.spring.configurer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public abstract class FieldAndMethodBasedConfigurer implements BotConfigurer {
    private final Class<? extends Annotation> annotationToSearch;

    protected FieldAndMethodBasedConfigurer(Class<? extends Annotation> annotationToSearch) {
        this.annotationToSearch = annotationToSearch;
    }

    @Override
    public void configure(Object bean, String name) {
        FieldAndMethodBasedParser parser = new FieldAndMethodBasedParser(bean);
        ReflectionUtils.doWithMethods(bean.getClass(), parser,
                method -> method.isAnnotationPresent(annotationToSearch));

        ReflectionUtils.doWithFields(bean.getClass(), parser,
                field -> field.isAnnotationPresent(annotationToSearch));
    }

    protected abstract void handleMethod(Object obj, Method method);

    protected abstract void handleField(Object obj, Field field);

    protected static String returnStringOrMemberName(String id, Member member) {
        if(StringUtils.isBlank(id)) {
            return member.getName();
        } else {
            return id;
        }
    }

    private class FieldAndMethodBasedParser implements ReflectionUtils.MethodCallback, ReflectionUtils.FieldCallback {
        private final Object bean;

        private FieldAndMethodBasedParser(Object bean) {
            this.bean = bean;
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            handleField(bean, field);
        }

        @Override
        public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
            handleMethod(bean, method);
        }
    }
}
