package com.github.lucbui.fracktail3.spring.service;

import com.github.lucbui.fracktail3.magic.platform.Platform;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class PlatformService implements ApplicationContextAware {
    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public <T extends Platform> T getPlatform(Class<T> clazz) {
        try {
            return context.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }
}
