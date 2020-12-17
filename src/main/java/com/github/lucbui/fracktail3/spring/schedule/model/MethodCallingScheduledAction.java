package com.github.lucbui.fracktail3.spring.schedule.model;

import com.github.lucbui.fracktail3.magic.platform.context.ScheduledUseContext;
import com.github.lucbui.fracktail3.magic.schedule.action.ScheduledAction;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates a method call on an object, with relevant components, for scheduled actions
 */
public class MethodCallingScheduledAction implements ScheduledAction {
    private final List<ParameterScheduledComponent> parameterComponents;
    private final Object objToInvokeOn;
    private final Method methodToCall;
    private final ReturnScheduledComponent returnComponent;
    private final ExceptionScheduledComponent exceptionComponent;

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

    public List<ParameterScheduledComponent> getParameterComponents() {
        return Collections.unmodifiableList(parameterComponents);
    }

    public Object getObjToInvokeOn() {
        return objToInvokeOn;
    }

    public Method getMethodToCall() {
        return methodToCall;
    }

    public ReturnScheduledComponent getReturnComponent() {
        return returnComponent;
    }

    public ExceptionScheduledComponent getExceptionComponent() {
        return exceptionComponent;
    }

    @Override
    public Mono<Void> execute(ScheduledUseContext context) {
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
