package com.github.milomarten.fracktail3.spring.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.milomarten.fracktail3.magic.schedule.action.ScheduledAction;
import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Encapsulates a method call on an object, with relevant components, for scheduled actions
 */
@Data
public class MethodCallingScheduledAction implements ScheduledAction {
    @JsonIgnore private final List<ParameterScheduledComponent> parameterComponents;
    @JsonIgnore private final Object objToInvokeOn;
    @JsonIgnore private final Method methodToCall;
    @JsonIgnore private final ReturnScheduledComponent returnComponent;
    @JsonIgnore private final ExceptionScheduledComponent exceptionComponent;

    /**
     * Initialize this action
     * @param parameterComponents A list of ParameterComponent, one for each parameter in the method. Determines
     *                            how to inject a value into each parameter
     * @param objToInvokeOn The object to call the method on
     * @param methodToCall The method to call
     * @param returnComponent A component which determines what to do with the retrieved object
     * @param exceptionComponent A component which determines what to do when an exception is thrown during retrieval
     */
    public MethodCallingScheduledAction(
            List<ParameterScheduledComponent> parameterComponents,
            Object objToInvokeOn,
            Method methodToCall,
            ReturnScheduledComponent returnComponent,
            ExceptionScheduledComponent exceptionComponent) {
        this.parameterComponents = parameterComponents;
        this.objToInvokeOn = objToInvokeOn;
        this.methodToCall = methodToCall;
        this.returnComponent = returnComponent;
        this.exceptionComponent = exceptionComponent;
    }

    @Override
    public Mono<Void> execute(ScheduleUseContext context) {
        Object[] params = parameterComponents.stream()
                .map(pc -> pc.getFunc().apply(context))
                .toArray();
        return Mono.fromCallable(() -> methodToCall.invoke(objToInvokeOn, params))
                .doOnNext(o -> returnComponent.getConsumers().forEach(c -> c.accept(o)))
                .flatMap(o -> returnComponent.getFunc().apply(context, o))
                .onErrorResume(InvocationTargetException.class, ex ->
                        Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getClass()))
                                .switchIfEmpty(Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getTargetException().getClass())))
                                .flatMap(handler -> handler.apply(context, ex.getTargetException())))
                .onErrorResume(ex ->
                        Mono.justOrEmpty(exceptionComponent.getBestHandlerFor(ex.getClass()))
                                .flatMap(func -> func.apply(context, ex)))
                .onErrorResume(ex -> Mono.fromRunnable(ex::printStackTrace));
    }
}
