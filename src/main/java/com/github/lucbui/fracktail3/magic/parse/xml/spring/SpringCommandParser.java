package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.parse.xml.BehaviorParser;
import com.github.lucbui.fracktail3.magic.parse.xml.DefaultCommandParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringCommandParser extends DefaultCommandParser implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBehaviorParser.class);
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public SpringCommandParser(BehaviorParser behaviorParser) {
        super(behaviorParser);
    }

    @Override
    protected Command getCustomCommandBySpringBean(String spring){
        try {
            LOGGER.debug("Creating Command by retrieving bean {}", spring);
            return applicationContext.getBean(spring, Command.class);
        } catch (NoSuchBeanDefinitionException e) {
            throw new BotConfigurationException("No bean with name " + spring, e);
        } catch (BeanNotOfRequiredTypeException e) {
            throw new BotConfigurationException("Bean " + spring + " is not of type Command", e);
        } catch (BeansException e) {
            throw new BotConfigurationException("Bean " + spring + " could not be created", e);
        }
    }
}
