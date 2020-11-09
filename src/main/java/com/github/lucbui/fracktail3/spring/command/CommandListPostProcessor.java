package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.command.action.PlatformBasicAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

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
        } else {
            LOGGER.trace("Investigating Bean {} for command candidates", beanName);
            ReflectionUtils.doWithMethods(bean.getClass(),
                    new CommandAnnotationParser(bean),
                    method -> method.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Command.class));
        }
        return bean;
    }

    private class CommandAnnotationParser implements ReflectionUtils.MethodCallback {
        private final Object bean;

        private CommandAnnotationParser(Object bean) {
            this.bean = bean;
        }

        @Override
        public void doWith(Method method) throws IllegalArgumentException {
            LOGGER.debug("Adding @Command-annotated annotation {}", method.getName());
            Command c = new Command.Builder(method.getName())
                    .withAction(new MethodCallingAction(bean, method))
                    .build();
            commandList.add(c);
        }
    }
}
