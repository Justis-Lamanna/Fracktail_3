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
    private MethodCallingActionFactory factory;

    @Autowired
    private CommandList commandList;

    @Autowired
    private Plugins plugins;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Plugin) {
            LOGGER.debug("Installing Plugin {}", bean.getClass());
            plugins.addPlugin((Plugin) bean);
            List<Command> commands = ((Plugin)bean).addAdditionalCommands();
            commands.forEach(c -> LOGGER.debug("Adding Command Bean from plugin {} of id {}", bean.getClass(), c.getId()));
            commandList.addAll(commands);
        }

        if(bean instanceof Command) {
            LOGGER.debug("Adding Command Bean of id {}", beanName);
            Command c = (Command)bean;
            addOrMerge(c);
        } else if(bean instanceof CommandAction) {
            LOGGER.debug("Adding CommandAction Bean of id {}", beanName);
            Command c = new Command.Builder(beanName)
                        .withAction((CommandAction) bean)
                        .build();
            addOrMerge(c);
        } else if(bean instanceof PlatformBasicAction) {
            LOGGER.debug("Adding PlatformBasicAction Bean of id {}", beanName);
            Command c = new Command.Builder(beanName)
                        .withAction((PlatformBasicAction) bean)
                        .build();
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
            LOGGER.debug("Overwriting command. One day, the commands will be merged, instead");
            plugins.getPlugins().forEach(p -> p.onCommandMerge(old.get(), c));
        } else {
            commandList.add(c);
            plugins.getPlugins().forEach(p -> p.onCommandAdd(c));
        }
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
