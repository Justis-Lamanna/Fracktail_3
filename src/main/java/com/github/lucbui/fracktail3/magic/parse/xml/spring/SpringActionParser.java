package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.parse.xml.DefaultActionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringActionParser extends DefaultActionParser implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringActionParser.class);
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected Action getCustomActionBySpringBean(String spring) {
        try {
            LOGGER.debug("Creating Action by retrieving bean {}", spring);
            return applicationContext.getBean(spring, Action.class);
        } catch (NoSuchBeanDefinitionException e) {
            throw new BotConfigurationException("No bean with name " + spring, e);
        } catch (BeanNotOfRequiredTypeException e) {
            throw new BotConfigurationException("Bean " + spring + " is not of type Action", e);
        } catch (BeansException e) {
            throw new BotConfigurationException("Bean " + spring + " could not be created", e);
        }
    }
}
