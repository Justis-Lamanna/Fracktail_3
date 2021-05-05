package com.github.lucbui.fracktail3.spring.command.annotation.strategy;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.lucbui.fracktail3.spring.command.annotation.InjectPerson;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.lucbui.fracktail3.spring.schedule.model.ParameterScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.plugin.ParameterScheduledComponentStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;

@Order(0)
public class InjectPersonStrategy implements ParameterComponentStrategy, ParameterScheduledComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(InjectPersonStrategy.class);

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        InjectPerson annotation = parameter.getAnnotation(InjectPerson.class);
        if(LOGGER.isDebugEnabled()) {
            if(StringUtils.isAllBlank(annotation.platform(), annotation.id())) {
                LOGGER.debug("+-Injecting sender");
            } else if(StringUtils.isBlank(annotation.platform()) && StringUtils.isNotBlank(annotation.id())) {
                LOGGER.debug("+-Injecting user {} of current platform", annotation.id());
            } else {
                LOGGER.debug("+-Injecting user {} of platform {}", annotation.id(), annotation.platform());
            }
        }
        base.setFunc(new PersonInjector(annotation.platform(), annotation.id()));
        return base;
    }

    @Override
    public ParameterScheduledComponent decorateSchedule(Object obj, Method method, Parameter parameter, ParameterScheduledComponent base) {
        InjectPerson annotation = parameter.getAnnotation(InjectPerson.class);
        if(StringUtils.isAnyBlank(annotation.platform(), annotation.id())) {
            throw new BotConfigurationException("Must specify platform and ID when using @InjectPerson on a scheduled event.");
        }
        LOGGER.debug("+-Injecting user {} of platform {}", annotation.id(), annotation.platform());
        base.setFunc(new PersonInjector(annotation.platform(), annotation.id()));
        return base;
    }

    @Data
    @AllArgsConstructor
    protected static class PersonInjector implements ParameterComponent.PCFunction, ParameterScheduledComponent.PCSFunction {
        private String platform;
        private String id;

        @Override
        public Object apply(CommandUseContext context) {
            if(platform.equals("")) {
                if(id.equals("")) {
                    return context.getSender();
                } else {
                    return context.getPlatform().getPerson(id).block(Duration.ofSeconds(15));
                }
            } else {
                return context.getBot().getPlatform(platform)
                        .map(p -> p.getPerson(id))
                        .orElseThrow(() -> new BotConfigurationException("Unknown platform " + platform));
            }
        }

        @Override
        public Object apply(ScheduleUseContext context) {
            return context.getBot().getPlatform(platform)
                    .map(p -> p.getPerson(id))
                    .orElseThrow(() -> new BotConfigurationException("Unknown platform " + platform));
        }
    }
}
