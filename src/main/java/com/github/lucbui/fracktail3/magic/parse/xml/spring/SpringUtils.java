package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class SpringUtils {
    public static <T> T getFromSpringBean(BeanFactory beanFactory, String beanName, Class<T> clazz) {
        try {
            return beanFactory.getBean(beanName, clazz);
        } catch (NoSuchBeanDefinitionException e) {
            throw new BotConfigurationException("No bean with name " + beanName, e);
        } catch (BeanNotOfRequiredTypeException e) {
            throw new BotConfigurationException("Bean " + beanName + " is not of type " + clazz, e);
        } catch (BeansException e) {
            throw new BotConfigurationException("Bean " + beanName + " could not be created", e);
        }
    }
}
