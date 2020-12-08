package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

/**
 * Encapsulates a method call on an object, with relevant components.
 */
public class MethodCallingAction implements CommandAction {
    private final MethodComponent methodComponent;
    private final List<ParameterComponent> parameterComponents;
    private final Object objToInvokeOn;
    private final Method methodToCall;
    private final ReturnComponent returnComponent;
    private final ExceptionComponent exceptionComponent;

    /**
     * Initialize this action
     * @param methodComponent A component containing the high-level guards
     * @param parameterComponents A list of ParameterComponent, one for each parameter in the method. Determines
     *                            how to inject a value into each parameter
     * @param objToInvokeOn The object to call the method on
     * @param methodToCall The method to call
     * @param returnComponent A component which determines what to do with the retrieved object
     * @param exceptionComponent A component which determines what to do when an exception is thrown during retrieval
     */
    public MethodCallingAction(
            MethodComponent methodComponent,
            List<ParameterComponent> parameterComponents,
            Object objToInvokeOn, Method methodToCall,
            ReturnComponent returnComponent,
            ExceptionComponent exceptionComponent) {
        this.parameterComponents = parameterComponents;
        this.objToInvokeOn = objToInvokeOn;
        this.methodToCall = methodToCall;
        this.returnComponent = returnComponent;
        this.methodComponent = methodComponent;
        this.exceptionComponent = exceptionComponent;
    }

    public MethodComponent getMethodComponent() {
        return methodComponent;
    }

    public List<ParameterComponent> getParameterComponents() {
        return parameterComponents;
    }

    public Object getObjToInvokeOn() {
        return objToInvokeOn;
    }

    public Method getMethodToCall() {
        return methodToCall;
    }

    public ReturnComponent getReturnComponent() {
        return returnComponent;
    }

    public ExceptionComponent getExceptionComponent() {
        return exceptionComponent;
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        return doActionUnguarded(context);
    }

    public Mono<Void> doActionUnguarded(CommandUseContext<?> context) {
        Object[] params = parameterComponents.stream()
                .map(pc -> pc.func.apply(context))
                .toArray();
        return Mono.fromCallable(() -> methodToCall.invoke(objToInvokeOn, params))
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
        return Stream.concat(
                    methodComponent.guards.stream(),
                    parameterComponents.stream().flatMap(pc -> pc.guards.stream())
                )
                .map(guard -> guard.matches(context))
                .reduce(Mono.just(true), BooleanUtils::and);
    }
}
