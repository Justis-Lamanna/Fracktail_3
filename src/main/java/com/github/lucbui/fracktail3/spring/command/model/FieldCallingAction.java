package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Encapsulates a field call on an object, with relevant components.
 * A Field call functions identically to a no-parameter method call. All annotations supported by the Method call
 * are also supported by the Field call
 */
public class FieldCallingAction implements CommandAction {
    private final MethodComponent methodComponent;
    private final Object objToInvokeOn;
    private final Field fieldToRetrieve;
    private final ReturnComponent returnComponent;
    private final ExceptionComponent exceptionComponent;

    /**
     * Initializes this action
     * @param methodComponent A component containing the high-level guards
     * @param objToInvokeOn The object to retrieve the field from
     * @param fieldToRetrieve the field to retrieve
     * @param returnComponent A component which determines what to do with the retrieved object
     * @param exceptionComponent A component which determines what to do when an exception is thrown during retrieval
     */
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