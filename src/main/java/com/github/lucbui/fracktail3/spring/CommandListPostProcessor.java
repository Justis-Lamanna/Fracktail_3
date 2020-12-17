package com.github.lucbui.fracktail3.spring;

import com.github.lucbui.fracktail3.spring.configurer.BotConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * The PostProcessor which compiles and registers commands with Fracktail
 * Beans of type Command are not processed, and simply registered in the command list.
 * Beans of type CommandAction or PlatformBasicAction are given the id and name of their corresponding bean.
 * All beans are scanned for methods or fields bearing the @Command annotation. These are compiled into a full Command,
 * and added to the command list.
 *
 * A minimal command would be a zero-parameter method annotated with @Command, or a field annotated with @Command. Additional
 * parameters may be used to fully customize the created command:
 * - @Name allows for specifying of one or more names for the command to use. If omitted, the method or field name is used.
 * - @Usage allows for specifying usage details (to be retrieved via a help command).
 *
 * Additional annotations allow for customization of the Command's action (such as injecting parameters or handling return
 * values in a certain way)
 */
@Component
public class CommandListPostProcessor implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListPostProcessor.class);

    @Autowired
    private BotConfigurer[] configurers;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (BotConfigurer configurer : configurers) {
            configurer.configure(bean, beanName);
        }
        return bean;
    }
}
