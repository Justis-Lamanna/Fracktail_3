package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.command.BotResponse;
import com.github.lucbui.fracktail3.spring.command.ReturnComponent;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ReturnConverters {
    public Optional<ReturnComponent.ReturnConverterFunction> getHandlerForType(Class<?> clazz) {
        if (clazz.equals(Void.class) || clazz.equals(Void.TYPE)) {
            return Optional.of(new StdReturnHandlers.Voids());
        } else if (clazz.equals(Mono.class)) {
            return Optional.of(new StdReturnHandlers.Monos());
        } else if (clazz.equals(Flux.class)) {
            return Optional.of(new StdReturnHandlers.Fluxs());
        } else if(clazz.equals(String.class)) {
            return Optional.of(new StdReturnHandlers.Strings());
        } else if(clazz.equals(FormattedString.class)) {
            return Optional.of(new StdReturnHandlers.FStrings());
        } else if(ClassUtils.isAssignable(clazz, BotResponse.class)) {
            return Optional.of(new StdReturnHandlers.BotResponses());
        }
        return Optional.empty();
    }
}
