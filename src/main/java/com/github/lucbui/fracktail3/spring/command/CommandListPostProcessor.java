package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.command.action.PlatformBasicAction;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.annotation.Name;
import com.github.lucbui.fracktail3.spring.plugin.Plugin;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CommandListPostProcessor implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListPostProcessor.class);

    @Autowired
    private Plugins plugins;

    @Autowired
    private MethodCallingActionFactory factory;

    @Autowired
    private CommandList commandList;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Command) {
            LOGGER.debug("Adding Command Bean of name {}", beanName);
            Command c;
            Optional<Plugin> p = plugins.getPlugin(plugin -> plugin.canProcessCommandBean((Command)bean));
            if(p.isPresent()) {
                c = p.get().processCommandBean((Command)bean);
            } else {
                c = (Command)bean;
            }
            addOrMerge(c);
        } else if(bean instanceof CommandAction) {
            LOGGER.debug("Adding CommandAction Bean of name {}", beanName);
            Command c;
            Optional<Plugin> p = plugins.getPlugin(plugin -> plugin.canProcessActionBean(beanName, (CommandAction)bean));
            if(p.isPresent()) {
                c = p.get().processActionBean(beanName, (CommandAction)bean);
            } else {
                c = new Command.Builder(beanName)
                        .withAction((CommandAction) bean)
                        .build();
            }
            addOrMerge(c);
        } else if(bean instanceof PlatformBasicAction) {
            LOGGER.debug("Adding PlatformBasicAction Bean of name {}", beanName);
            Command c;
            Optional<Plugin> p = plugins.getPlugin(plugin -> plugin.canProcessActionBean(beanName, (PlatformBasicAction)bean));
            if(p.isPresent()) {
                c = p.get().processActionBean(beanName, (PlatformBasicAction)bean);
            } else {
                c = new Command.Builder(beanName)
                        .withAction((PlatformBasicAction) bean)
                        .build();
            }
            addOrMerge(c);
        } else {
            LOGGER.trace("Investigating Bean {} for command candidates", beanName);
            ReflectionUtils.doWithMethods(bean.getClass(),
                    new CommandAnnotationParser(bean),
                    method -> method.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Command.class));
        }
        return bean;
    }

    private void addOrMerge(Command c) {
        Optional<Command> old = commandList.getCommandById(c.getId());
        if(old.isPresent()) {
            throw new IllegalArgumentException("I'll do this later");
        }
        commandList.add(c);
    }

    private class CommandAnnotationParser implements ReflectionUtils.MethodCallback {
        private final Object bean;

        private CommandAnnotationParser(Object bean) {
            this.bean = bean;
        }

        @Override
        public void doWith(Method method) throws IllegalArgumentException {
            LOGGER.debug("Adding @Command-annotated method {}", method.getName());
            Command.Builder c = new Command.Builder(method.getName());

            c.withAction(factory.createAction(bean, method));

            if(method.isAnnotationPresent(Name.class)) {
                Name nameAnnotation = method.getAnnotation(Name.class);
                LOGGER.debug("Method {} named via annotation as {}", method.getName(), nameAnnotation.value());
                c.withNames(nameAnnotation.value());
            }

            Optional<Plugin> plugin = plugins.getPlugin(p -> p.canProcessMethodCommand(c, bean, method));
            plugin.ifPresent(value -> value.processMethodCommand(c, bean, method));

            addOrMerge(c.build());
        }

        private Method getGuardMethod(Class<?> aClass, String value) {
            List<Method> methods = Arrays.stream(aClass.getMethods())
                    .filter(m -> m.getName().equals(value))
                    .collect(Collectors.toList());
            if(methods.isEmpty()) {
                throw new BotConfigurationException("Attempted Guard Method - Method " + value + " does not exist in class " + aClass.getCanonicalName());
            } else if(methods.size() == 1) {
                return methods.get(0);
            } else {
                throw new BotConfigurationException("Attempted Guard Method - Multiple methods with name " + value + " exist in class " + aClass.getCanonicalName());
            }
        }
    }
}
