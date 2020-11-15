package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.github.lucbui.fracktail3.spring.annotation.Variable;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MethodCallingActionFactory {
    @Autowired
    ConversionService conversionService;

    @Autowired
    private Plugins plugins;

    public CommandAction createAction(Object obj, Method method) {
        List<ParameterComponent> components = compileParameters(obj, method);
        ReturnComponent returnComponent = compileReturn(obj, method);
        return new MethodCallingAction(components, Collections.emptyList(), obj, method, returnComponent);
    }

    protected List<ParameterComponent> compileParameters(Object obj, Method method) {
        return Arrays.stream(method.getParameters())
                .map(param -> compileParameter(obj, method, param))
                .collect(Collectors.toList());
    }

    protected ParameterComponent compileParameter(Object obj, Method method, Parameter parameter) {
        Class<?> type = parameter.getType();
        if (parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Parameter.class)) {
            return compileParameterAnnotated(obj, method, parameter);
        } else if(parameter.isAnnotationPresent(Variable.class)) {
            return compileParameterVariable(obj, method, parameter);
        } else if (ClassUtils.isAssignable(CommandUseContext.class, type)) {
            return plugins.enhanceCompiledParameter(obj, method, parameter, new ParameterComponent(ctx -> ctx));
        } else {
            ParameterComponent component = plugins.createCompiledParameter(obj, method, parameter)
                    .orElseThrow(() -> new BotConfigurationException("Unable to parse parameter " + parameter.getName() +
                             " of type " + type.getCanonicalName() +
                             "in method " + method.getName()));
            return plugins.enhanceCompiledParameter(obj, method, parameter, component);
        }
    }

    protected ReturnComponent compileReturn(Object obj, Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType.equals(Void.class)) {
            return new ReturnComponent((ctx, o) -> Mono.empty());
        } else if (returnType.equals(Mono.class)) {
            return new ReturnComponent((ctx, o) -> ((Mono<?>)o).then());
        } else if (returnType.equals(Flux.class)) {
            return new ReturnComponent((ctx, o) -> ((Flux<?>)o).then());
        } else {
            ReturnComponent component = plugins.createCompiledReturn(obj, method)
                    .orElseThrow(() -> new BotConfigurationException("Unable to parse return type " + returnType.getCanonicalName() +
                            "in method " + method.getName()));
            return plugins.enhanceCompiledReturn(obj, method, component);
        }
    }

    protected ParameterComponent compileParameterAnnotated(Object obj, Method method, Parameter parameter) {
        com.github.lucbui.fracktail3.spring.annotation.Parameter pAnnot =
                parameter.getAnnotation(com.github.lucbui.fracktail3.spring.annotation.Parameter.class);
        int value = pAnnot.value();
        Class<?> paramType = parameter.getType();
        if(!conversionService.canConvert(String.class, paramType)) {
            throw new BotConfigurationException("Cannot convert String parameter to type " + paramType.getCanonicalName());
        }

        ParameterComponent component = new ParameterComponent(ctx -> {
            String[] params = ctx.getParameters();
            if (value < params.length) {
                return conversionService.convert(params[value], paramType);
            } else {
                if (paramType.equals(Optional.class)) {
                    return Optional.empty();
                } else if (paramType.equals(Integer.TYPE)) {
                    return 0;
                } else if (paramType.equals(Long.TYPE)) {
                    return 0L;
                } else if (paramType.equals(Double.TYPE)) {
                    return 0.0;
                }
                return null;
            }
        });

        if (!isOptional(paramType) && !pAnnot.optional()) {
            //Enforce parameter count (unless optional is expected)
            component.addGuard(ctx -> {
                if (ctx instanceof CommandUseContext) {
                    CommandUseContext<?> cctx = (CommandUseContext<?>) ctx;
                    return Mono.just(value < cctx.getParameters().length);
                }
                return Mono.just(true);
            });
        }

        return plugins.enhanceCompiledParameter(obj, method, parameter, component);
    }

    private boolean isOptional(Class<?> clazz) {
        return clazz.equals(Optional.class) || clazz.equals(OptionalInt.class) ||
                clazz.equals(OptionalLong.class) || clazz.equals(OptionalDouble.class);
    }

    protected ParameterComponent compileParameterVariable(Object obj, Method method, Parameter parameter) {
        Variable vAnnot = parameter.getAnnotation(Variable.class);
        String value = vAnnot.value();
        Class<?> paramType = parameter.getType();

        ParameterComponent component = new ParameterComponent(ctx -> {
            AsynchronousMap<String, Object> map = ctx.getMap();
            if(paramType.equals(Mono.class)) {
                return map.getAsync(value);
            }
            Object v = map.get(value);
            if (paramType.equals(Optional.class)) {
                if(v == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(v);
                }
            }
            if(conversionService.canConvert(v.getClass(), paramType)) {
                return conversionService.convert(v, paramType);
            } else {
                throw new BotConfigurationException("Cannot convert object " + v.getClass() + " to type " + paramType.getCanonicalName());
            }
        });

        return plugins.enhanceCompiledParameter(obj, method, parameter, component);
    }

    protected static class MethodCallingAction implements CommandAction {
        private final List<ParameterComponent> parameterComponents;
        private final List<Guard> addlGuards;
        private final Object objToInvokeOn;
        private final Method methodToCall;
        private final ReturnComponent postTransformer;

        public MethodCallingAction(List<ParameterComponent> parameterComponents, List<Guard> addlGuards, Object objToInvokeOn, Method methodToCall, ReturnComponent postTransformer) {
            this.parameterComponents = parameterComponents;
            this.objToInvokeOn = objToInvokeOn;
            this.methodToCall = methodToCall;
            this.postTransformer = postTransformer;
            this.addlGuards = addlGuards;
        }

        @Override
        public Mono<Void> doAction(CommandUseContext<?> context) {
            Object[] params = parameterComponents.stream()
                    .map(pc -> pc.func.apply(context))
                    .toArray();
            return Mono.fromCallable(() -> methodToCall.invoke(objToInvokeOn, params))
                    .doOnNext(o -> postTransformer.consumers.forEach(c -> c.accept(o)))
                    .flatMap(o -> postTransformer.basic.apply(context, o));
        }

        @Override
        public Mono<Boolean> guard(PlatformBaseContext<?> context) {
            return Stream.concat(
                        parameterComponents.stream().flatMap(pc -> pc.guards.stream()),
                        addlGuards.stream())
                    .map(guard -> guard.matches(context))
                    .reduce(Mono.just(true), BooleanUtils::and);
        }
    }

}
