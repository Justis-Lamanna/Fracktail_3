package com.github.milomarten.fracktail3.spring.command.annotation.strategy;

import com.github.milomarten.fracktail3.magic.exception.BotConfigurationException;
import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;
import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.milomarten.fracktail3.spring.command.annotation.InjectPlace;
import com.github.milomarten.fracktail3.spring.command.model.ParameterComponent;
import com.github.milomarten.fracktail3.spring.command.plugin.ParameterComponentStrategy;
import com.github.milomarten.fracktail3.spring.schedule.model.ParameterScheduledComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;

public class InjectPlaceStrategy implements ParameterComponentStrategy {
    private static final Logger LOGGER = LoggerFactory.getLogger(InjectPlaceStrategy.class);

    @Override
    public ParameterComponent decorate(Object obj, Method method, Parameter parameter, ParameterComponent base) {
        InjectPlace annotation = parameter.getAnnotation(InjectPlace.class);
        if(LOGGER.isDebugEnabled()) {
            if(StringUtils.isAllBlank(annotation.platform(), annotation.id())) {
                LOGGER.debug("+-Injecting sent location");
            } else if(StringUtils.isBlank(annotation.platform()) && StringUtils.isNotBlank(annotation.id())) {
                LOGGER.debug("+-Injecting place {} of current platform", annotation.id());
            } else {
                LOGGER.debug("+-Injecting place {} of platform {}", annotation.id(), annotation.platform());
            }
        }
        base.setFunc(new PlaceInjector(annotation.platform(), annotation.id()));
        return base;
    }

    @Data
    @AllArgsConstructor
    protected static class PlaceInjector implements ParameterComponent.PCFunction, ParameterScheduledComponent.PCSFunction {
        private String platform;
        private String id;

        @Override
        public Object apply(CommandUseContext context) {
            if(platform.equals("")) {
                if(id.equals("")) {
                    return context.getTriggerPlace().block(Duration.ofSeconds(15));
                } else {
                    return context.getPlatform().getPlace(id).block(Duration.ofSeconds(15));
                }
            }
            return context.getBot().getPlatform(platform)
                    .map(p -> p.getPlace(id).block(Duration.ofSeconds(15)))
                    .orElseThrow(() -> new BotConfigurationException("Unknown platform " + platform));
        }

        @Override
        public Object apply(ScheduleUseContext context) {
            return context.getBot().getPlatform(platform)
                    .map(p -> p.getPlace(id).block(Duration.ofSeconds(15)))
                    .orElseThrow(() -> new BotConfigurationException("Unknown platform " + platform));
        }
    }
}
