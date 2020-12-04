package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.command.handler.ReturnHandlers;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class ReturnComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    @Autowired
    public ReturnComponentFactory(ConversionService conversionService, Plugins plugins) {
        super(conversionService, plugins);
    }

    public ReturnComponent compileReturn(Object obj, Method method) {
        LOGGER.debug("Compiling return of method {}", method.getName());
        Class<?> returnType = method.getReturnType();
        ReturnComponent ret = createReturnComponent(method, returnType)
                .orElseGet(() -> plugins.createCompiledReturn(obj, method)
                    .orElseThrow(() -> new BotConfigurationException("Unable to parse return type " + returnType.getCanonicalName() +
                            "in method " + method.getName())));
        return plugins.enhanceCompiledReturn(obj, method, ret);
    }

    public ReturnComponent compileReturn(Object obj, Field field) {
        LOGGER.debug("Compiling return of field {}", field.getName());
        Class<?> returnType = field.getType();
        ReturnComponent ret = createReturnComponent(field, returnType)
                .orElseGet(() -> plugins.createCompiledFieldReturn(obj, field)
                        .orElseThrow(() -> new BotConfigurationException("Unable to parse return type " + returnType.getCanonicalName() +
                                "in field " + field.getName())));
        return plugins.enhanceCompiledFieldReturn(obj, field, ret);
    }

    private <T extends AnnotatedElement & Member> Optional<ReturnComponent>
        createReturnComponent(T member, Class<?> returnType) {
        if (returnType.equals(Void.class) || returnType.equals(Void.TYPE)) {
            LOGGER.debug("Compiling return of {} {} as void", member.getClass().getSimpleName(), member.getName());
            return Optional.of(new ReturnComponent(new ReturnHandlers.Voids()));
        } else if (returnType.equals(Mono.class)) {
            LOGGER.debug("Compiling return of {} {} as Mono<?>", member.getClass().getSimpleName(), member.getName());
            return Optional.of(new ReturnComponent(new ReturnHandlers.Monos()));
        } else if (returnType.equals(Flux.class)) {
            LOGGER.debug("Compiling return of {} {} as Flux<?>", member.getClass().getSimpleName(), member.getName());
            return Optional.of(new ReturnComponent(new ReturnHandlers.Fluxs()));
        } else if(returnType.equals(String.class)) {
            LOGGER.debug("Compiling return of {} {} as String (responding)", member.getClass().getSimpleName(), member.getName());
            return Optional.of(new ReturnComponent(new ReturnHandlers.Strings()));
        } else if(returnType.equals(FormattedString.class)) {
            LOGGER.debug("Compiling return of {} {} as FormattedString (responding)", member.getClass().getSimpleName(), member.getName());
            return Optional.of(new ReturnComponent(new ReturnHandlers.FStrings()));
        } else if(ClassUtils.isAssignable(returnType, BotResponse.class)) {
            LOGGER.debug("Compiling return of {} {} as BotResponse (responding)", member.getClass().getSimpleName(), member.getName());
            return Optional.of(new ReturnComponent(new ReturnHandlers.BotResponses()));
        }
        return Optional.empty();
    }
}
