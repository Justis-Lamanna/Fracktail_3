package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.schedule.model.ReturnScheduledComponent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        private ReturnComponent.RCFunction thenFunc = (ctx, obj) -> Mono.empty();
        private ReturnScheduledComponent.RCSFunction thenFuncSchedule = (ctx, obj) -> Mono.empty();

        public Monos(ReturnComponent.RCFunction thenFunc) {
            this.thenFunc = thenFunc;
        }

        public Monos(ReturnScheduledComponent.RCSFunction thenFuncSchedule) {
            this.thenFuncSchedule = thenFuncSchedule;
        }

        @Override
        public Mono<Void> apply(CommandUseContext context, Object o) {
            return o == null ? Mono.empty() : ((Mono<?>)o).flatMap(s -> thenFunc.apply(context, s));
        }

        @Override
        public Mono<Void> apply(ScheduleUseContext context, Object o) {
            return o == null ? Mono.empty() : ((Mono<?>)o).flatMap(s -> thenFuncSchedule.apply(context, s));
        }
    }

    /**
     * ReturnConverterFunction which handles a Flux return.
     * The returned Mono completes when the input flux completes.
     * If null, this returns an empty Mono
     */
    public static class Fluxs implements ReturnComponent.RCFunction, ReturnScheduledComponent.RCSFunction {
        private ReturnComponent.RCFunction thenFunc = (ctx, obj) -> Mono.empty();
        private ReturnScheduledComponent.RCSFunction thenFuncSchedule = (ctx, obj) -> Mono.empty();

        public Fluxs(ReturnComponent.RCFunction thenFunc) {
            this.thenFunc = thenFunc;
        }

        public Fluxs(ReturnScheduledComponent.RCSFunction thenFuncSchedule) {
            this.thenFuncSchedule = thenFuncSchedule;
        }

        @Override
        public Mono<Void> apply(CommandUseContext context, Object o) {
            return o == null ? Mono.empty() : ((Flux<?>)o).flatMap(s -> thenFunc.apply(context, s)).then();
        }

        @Override
        public Mono<Void> apply(ScheduleUseContext context, Object o) {
            return o == null ? Mono.empty() : ((Flux<?>)o).flatMap(s -> thenFuncSchedule.apply(context, s)).then();
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
}
