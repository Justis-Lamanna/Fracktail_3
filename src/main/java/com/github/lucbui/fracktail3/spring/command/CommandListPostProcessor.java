package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.command.action.PlatformBasicAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class CommandListPostProcessor implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListPostProcessor.class);
    private final CommandList commandList;

    public CommandListPostProcessor(CommandList commandList) {
        this.commandList = commandList;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Command) {
            LOGGER.debug("Adding Command Bean of name {}", beanName);
            commandList.add((Command)bean);
        } else if(bean instanceof CommandAction) {
            LOGGER.debug("Adding CommandAction Bean of name {}", beanName);
            Command c = new Command.Builder(beanName)
                    .withAction((CommandAction)bean)
                    .build();
            commandList.add(c);
        } else if(bean instanceof PlatformBasicAction) {
            LOGGER.debug("Adding PlatformBasicAction Bean of name {}", beanName);
            Command c = new Command.Builder(beanName)
                    .withAction((PlatformBasicAction)bean)
                    .build();
            commandList.add(c);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
