package com.github.lucbui.fracktail3.magic.parse.xml.spring;

import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.parse.xml.ActionParser;
import com.github.lucbui.fracktail3.magic.parse.xml.DefaultBehaviorParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBehaviorParser extends DefaultBehaviorParser implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBehaviorParser.class);
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public SpringBehaviorParser(ActionParser actionParser) {
        super(actionParser);
    }

    @Override
    public Behavior getFromSpringBean(String spring) {
        LOGGER.debug("Creating Behavior by retrieving bean {}", spring);
        return getFromSpringBean(applicationContext, spring);
    }
}
