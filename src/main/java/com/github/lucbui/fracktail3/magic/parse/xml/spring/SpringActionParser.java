package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.parse.xml.DefaultActionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
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
    public Action getFromSpringBean(String spring) {
        LOGGER.debug("Creating Action by retrieving bean {}", spring);
        return getFromSpringBean(applicationContext, spring);
    }
}
