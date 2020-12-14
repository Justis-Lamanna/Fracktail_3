package com.github.lucbui.fracktail3.spring.command.service;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.handler.StdReturnConverterFunctions;
import com.github.lucbui.fracktail3.spring.command.model.BotResponse;
import com.github.lucbui.fracktail3.spring.command.model.ReturnBaseComponent;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class ReturnConverters {
    public Optional<ReturnBaseComponent.ReturnConverterFunction<? super CommandUseContext<?>>> getHandlerForType(Class<?> clazz) {
        if (clazz.equals(Void.class) || clazz.equals(Void.TYPE)) {
            return Optional.of(new StdReturnConverterFunctions.Voids());
        } else if (clazz.equals(Mono.class)) {
            return Optional.of(new StdReturnConverterFunctions.Monos());
        } else if (clazz.equals(Flux.class)) {
            return Optional.of(new StdReturnConverterFunctions.Fluxs());
        } else if(clazz.equals(String.class)) {
            return Optional.of(new StdReturnConverterFunctions.Strings());
        } else if(clazz.equals(FormattedString.class)) {
            return Optional.of(new StdReturnConverterFunctions.FStrings());
        } else if(ClassUtils.isAssignable(clazz, BotResponse.class)) {
            return Optional.of(new StdReturnConverterFunctions.BotResponses());
        }
        return Optional.empty();
    }
}
