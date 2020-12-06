package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.BotResponse;
import com.github.lucbui.fracktail3.spring.command.ReturnComponent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class StdReturnHandlers {
    public static class Voids implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return Mono.empty();
        }
    }

    public static class Monos implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : ((Mono<?>)o).then();
        }
    }

    public static class Fluxs implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : ((Flux<?>)o).then();
        }
    }

    public static class Strings implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : context.respond((String)o);
        }
    }

    public static class FStrings implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : context.respond((FormattedString) o, Collections.emptyMap());
        }
    }

    public static class BotResponses implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return o == null ? Mono.empty() : context.respond(((BotResponse)o).respondWith(), Collections.emptyMap());
        }
    }
}
