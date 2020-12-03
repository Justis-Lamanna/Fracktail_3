package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.annotation.RespondType;
import com.github.lucbui.fracktail3.spring.command.BotResponse;
import com.github.lucbui.fracktail3.spring.command.ReturnComponent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class ReturnHandlers {
    public static class Voids implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return Mono.empty();
        }
    }

    public static class Monos implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return ((Mono<?>)o).then();
        }
    }

    public static class Fluxs implements ReturnComponent.ReturnConverterFunction {
        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return ((Flux<?>)o).then();
        }
    }

    public static class Strings implements ReturnComponent.ReturnConverterFunction {
        private final RespondType respondType;

        public Strings(RespondType respondType) {
            this.respondType = respondType;
        }

        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return respondType.outputString().apply(context, (String)o);
        }
    }

    public static class FStrings implements ReturnComponent.ReturnConverterFunction {
        private final RespondType respondType;

        public FStrings(RespondType respondType) {
            this.respondType = respondType;
        }

        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return respondType.outputFString().apply(context, (FormattedString) o, Collections.emptyMap());
        }
    }

    public static class BotResponses implements ReturnComponent.ReturnConverterFunction {
        private final RespondType respondType;

        public BotResponses(RespondType respondType) {
            this.respondType = respondType;
        }

        @Override
        public Mono<Void> apply(CommandUseContext<?> context, Object o) {
            return respondType.outputFString().apply(context, ((BotResponse)o).respondWith(), Collections.emptyMap());
        }
    }
}
