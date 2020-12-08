package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class FieldCallingAction implements CommandAction {
    private final MethodComponent methodComponent;
    private final Object objToInvokeOn;
    private final Field fieldToRetrieve;
    private final ReturnComponent returnComponent;
    private final ExceptionComponent exceptionComponent;

    public FieldCallingAction(MethodComponent methodComponent, Object objToInvokeOn, Field fieldToRetrieve, ReturnComponent returnComponent, ExceptionComponent exceptionComponent) {
        this.methodComponent = methodComponent;
        this.objToInvokeOn = objToInvokeOn;
        this.fieldToRetrieve = fieldToRetrieve;
        this.returnComponent = returnComponent;
        this.exceptionComponent = exceptionComponent;
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        return Mono.fromCallable(() -> fieldToRetrieve.get(objToInvokeOn))
                .doOnNext(o -> returnComponent.consumers.forEach(c -> c.accept(o)))
                .flatMap(o -> returnComponent.func.apply(context, o))
                .onErrorResume(InvocationTargetException.class, ex ->
                        Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getClass()))
                                .switchIfEmpty(Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getTargetException().getClass())))
                                .flatMap(handler -> handler.apply(context, ex)))
                .onErrorResume(ex ->
                        Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getClass()))
                                .flatMap(func -> func.apply(context, ex)))
                .onErrorResume(ex -> Mono.fromRunnable(ex::printStackTrace));
    }

    @Override
    public Mono<Boolean> guard(PlatformBaseContext<?> context) {
        return methodComponent.guards.stream()
                .map(guard -> guard.matches(context))
                .reduce(Mono.just(true), BooleanUtils::and);
    }
}
