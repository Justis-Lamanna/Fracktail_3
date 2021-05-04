package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.schedule.model.ReturnScheduledComponent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * A collection of standard return conversion function
 */
public class StdReturnConverterFunctions {
    /**
     * ReturnConverterFunction which handles a void or Void return.
     * This simply does nothing with the return, since there is no return
     */
    public static class Voids implements ReturnComponent.RCFunction, ReturnScheduledComponent.RCSFunction {
        @Override
        public Mono<Void> apply(CommandUseContext context, Object o) {
            return Mono.empty();
        }

        @Override
        public Mono<Void> apply(ScheduleUseContext context, Object returnVal) {
            return Mono.empty();
        }
    }

    /**
     * ReturnConverterFunction which handles a Mono return.
     * The returned Mono completes when the input mono completes.
     * If null, this returns an empty Mono
     */
    public static class Monos implements ReturnComponent.RCFunction, ReturnScheduledComponent.RCSFunction {
        @Override
        public Mono<Void> apply(CommandUseContext context, Object o) {
            return o == null ? Mono.empty() : ((Mono<?>)o).flatMap(s -> {
                if(s == null) {
                    return new Monos().apply(context, null);
                } else if (s instanceof Mono) {
                    return new Monos().apply(context, s);
                } else if (s instanceof Flux) {
                    return new Fluxs().apply(context, s);
                } else if (s instanceof String) {
                    return new Strings().apply(context, s);
                } else if (s instanceof FormattedString) {
                    return new FStrings().apply(context, s);
                } else {
                    return Mono.error(new BotConfigurationException("Mono returned unknown type " + s.getClass().getCanonicalName()));
                }
            });
        }

        @Override
        public Mono<Void> apply(ScheduleUseContext context, Object o) {
            return o == null ? Mono.empty() : ((Mono<?>)o).then();
        }
    }

    /**
     * ReturnConverterFunction which handles a Flux return.
     * The returned Mono completes when the input flux completes.
     * If null, this returns an empty Mono
     */
    public static class Fluxs implements ReturnComponent.RCFunction, ReturnScheduledComponent.RCSFunction {
        @Override
        public Mono<Void> apply(CommandUseContext context, Object o) {
            return o == null ? Mono.empty() : ((Flux<?>)o).then();
        }

        @Override
        public Mono<Void> apply(ScheduleUseContext context, Object o) {
            return o == null ? Mono.empty() : ((Flux<?>)o).then();
        }
    }

    /**
     * ReturnConverterFunction which handles a String return.
     * The returned string is sent to the command user as a response.
     * If null, this returns an empty Mono and does nothing.
     */
    public static class Strings implements ReturnComponent.RCFunction {
        @Override
        public Mono<Void> apply(CommandUseContext context, Object o) {
            return o == null ?
                    Mono.empty() :
                    context.getTriggerPlace().flatMap(p -> p.sendMessage((String)o)).then();
        }
    }

    /**
     * ReturnConverterFunction which handles a FormattedString return.
     * The returned FormattedString is resolved and sent to the command user as a response.
     * If null, this returns an empty Mono and does nothing.
     */
    public static class FStrings implements ReturnComponent.RCFunction {
        @Override
        public Mono<Void> apply(CommandUseContext context, Object o) {
            return o == null ?
                    Mono.empty() :
                    ((FormattedString)o).getFor(Collections.emptyMap())
                        .zipWith(context.getTriggerPlace())
                        .flatMap(tuple -> tuple.getT2().sendMessage(tuple.getT1()))
                        .then();
        }
    }
}
