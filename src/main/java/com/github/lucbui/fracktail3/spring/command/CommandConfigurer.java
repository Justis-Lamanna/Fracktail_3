package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.github.lucbui.fracktail3.spring.annotation.Name;
import com.github.lucbui.fracktail3.spring.annotation.Usage;
import com.github.lucbui.fracktail3.spring.util.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CommandConfigurer extends FieldAndMethodBasedConfigurer {
    public static final Logger LOGGER = LoggerFactory.getLogger(CommandConfigurer.class);

    @Autowired
    private ReflectiveCommandActionFactory factory;

    @Autowired
    private CommandList commandList;

    protected CommandConfigurer() {
        super(Command.class);
    }

    @Override
    public void configure(Object bean, String beanName) {
        if(bean instanceof com.github.lucbui.fracktail3.magic.command.Command) {
            LOGGER.debug("Adding Command Bean of id {}", beanName);
            com.github.lucbui.fracktail3.magic.command.Command c = (com.github.lucbui.fracktail3.magic.command.Command)bean;
            addOrMerge(c);
        } else if(bean instanceof CommandAction) {
            LOGGER.debug("Adding CommandAction Bean of id {}", beanName);
            com.github.lucbui.fracktail3.magic.command.Command.Builder c = new com.github.lucbui.fracktail3.magic.command.Command.Builder(beanName)
                    .withAction((CommandAction) bean);
            handleAnnotations(bean.getClass(), c);
            addOrMerge(c.build());
        }

        LOGGER.trace("Investigating Bean {} for command candidates", beanName);
        super.configure(bean, beanName);
    }

    @Override
    protected void handleMethod(Object bean, Method method) {
        String id = returnStringOrMemberName(method.getAnnotation(Command.class).value(), method);
        LOGGER.debug("Adding @Command-annotated method {}", id);
        com.github.lucbui.fracktail3.magic.command.Command.Builder c = new com.github.lucbui.fracktail3.magic.command.Command.Builder(id);
        c.withAction(factory.createAction(bean, method));
        handleAnnotations(method, c);

        addOrMerge(c.build());
    }

    @Override
    protected void handleField(Object bean, Field field) {
        String id = returnStringOrMemberName(field.getAnnotation(Command.class).value(), field);
        LOGGER.debug("Adding @Command-annotated method {}", id);
        com.github.lucbui.fracktail3.magic.command.Command.Builder c = new com.github.lucbui.fracktail3.magic.command.Command.Builder(id);
        c.withAction(factory.createAction(bean, field));
        handleAnnotations(field, c);

        addOrMerge(c.build());
    }

    private void handleAnnotations(AnnotatedElement element, com.github.lucbui.fracktail3.magic.command.Command.Builder c) {
        if(element.isAnnotationPresent(Name.class)) {
            Name nameAnnotation = element.getAnnotation(Name.class);
            LOGGER.debug("+-Named as {}", Arrays.toString(nameAnnotation.value()));
            c.withNames(nameAnnotation.value());
        }

        if(element.isAnnotationPresent(Usage.class)) {
            Usage usageAnnotation = element.getAnnotation(Usage.class);
            LOGGER.debug("+-With help text as {}", usageAnnotation.value());
            c.withHelp(AnnotationUtils.fromUsage(usageAnnotation));
        }
    }

    private void addOrMerge(com.github.lucbui.fracktail3.magic.command.Command c) {
        Optional<com.github.lucbui.fracktail3.magic.command.Command> old = commandList.getCommandById(c.getId());
        if(old.isPresent()) {
            LOGGER.debug("+-Overwriting command. One day, the commands will be merged, instead");
            commandList.replace(c);
        } else {
            commandList.add(c);
        }
    }
}
