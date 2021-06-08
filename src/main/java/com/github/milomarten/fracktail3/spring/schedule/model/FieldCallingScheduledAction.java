package com.github.milomarten.fracktail3.spring.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.milomarten.fracktail3.magic.schedule.action.ScheduledAction;
import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Encapsulates a field call on an object, with relevant components, for scheduled actions
 * A Field call functions identically to a no-parameter method call. All annotations supported by the Method call
 * are also supported by the Field call
 */
public class FieldCallingScheduledAction implements ScheduledAction {
    @JsonIgnore private final Object objToInvokeOn;
    @JsonIgnore private final Field fieldToRetrieve;
    @JsonIgnore private final ReturnScheduledComponent returnComponent;
    @JsonIgnore private final ExceptionScheduledComponent exceptionComponent;

    /**
     * Initializes this action
     * @param objToInvokeOn The object to retrieve the field from
     * @param fieldToRetrieve the field to retrieve
     * @param returnComponent A component which determines what to do with the retrieved object
     * @param exceptionComponent A component which determines what to do when an exception is thrown during retrieval
     */
    public FieldCallingScheduledAction(
            Object objToInvokeOn,
            Field fieldToRetrieve,
            ReturnScheduledComponent returnComponent,
            ExceptionScheduledComponent exceptionComponent) {
        this.objToInvokeOn = objToInvokeOn;
        this.fieldToRetrieve = fieldToRetrieve;
        this.returnComponent = returnComponent;
        this.exceptionComponent = exceptionComponent;
    }

    public Object getObjToInvokeOn() {
        return objToInvokeOn;
    }

    public Field getFieldToRetrieve() {
        return fieldToRetrieve;
    }

    public ReturnScheduledComponent getReturnComponent() {
        return returnComponent;
    }

    public ExceptionScheduledComponent getExceptionComponent() {
        return exceptionComponent;
    }

    @Override
    public Mono<Void> execute(ScheduleUseContext context) {
        return Mono.fromCallable(() -> fieldToRetrieve.get(objToInvokeOn))
                .doOnNext(o -> returnComponent.getConsumers().forEach(c -> c.accept(o)))
                .flatMap(o -> returnComponent.getFunc().apply(context, o))
                .onErrorResume(InvocationTargetException.class, ex ->
                        Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getClass()))
                                .switchIfEmpty(Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getTargetException().getClass())))
                                .flatMap(handler -> handler.apply(context, ex)))
                .onErrorResume(ex ->
                        Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getClass()))
                                .flatMap(func -> func.apply(context, ex)))
                .onErrorResume(ex -> Mono.fromRunnable(ex::printStackTrace));
    }
}
