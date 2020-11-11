package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.github.lucbui.fracktail3.spring.annotation.Variable;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.bool.BooleanUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
public class MethodCallingActionFactory {
    @Autowired
    ConversionService conversionService;

    public CommandAction createAction(Object obj, Method method) {
        List<ParameterComponent> components = compileParameters(obj, method);
        BiFunction<CommandUseContext<?>, Object, Mono<Void>> returnFunc = compileReturn(obj, method);
        return new MethodCallingAction(components, obj, method, returnFunc);
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
            return new ParameterComponent(ctx -> ctx);
        } else {
            throw new BotConfigurationException("Unable to parse parameter " + parameter.getName() +
                    " of type " + type.getCanonicalName() +
                    "in method " + method.getName());
        }
    }

    protected BiFunction<CommandUseContext<?>, Object, Mono<Void>> compileReturn(Object obj, Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType.equals(Void.class)) {
            return (ctx, o) -> Mono.empty();
        } else if (returnType.equals(String.class)) {
            return (ctx, o) -> ctx.respond((String) o);
        } else if (returnType.equals(FormattedString.class)) {
            return (ctx, o) -> ((FormattedString) o).getFor(ctx).flatMap(ctx::respond);
        }
        return (ctx, o) -> o instanceof Mono ? ((Mono<?>) o).then() : Mono.empty();
    }

    protected ParameterComponent compileParameterAnnotated(Object obj, Method method, Parameter parameter) {
        com.github.lucbui.fracktail3.spring.annotation.Parameter pAnnot =
                parameter.getAnnotation(com.github.lucbui.fracktail3.spring.annotation.Parameter.class);
        int value = pAnnot.value();
        Class<?> paramType = parameter.getType();
        if(!conversionService.canConvert(String.class, paramType)) {
            throw new BotConfigurationException("Cannot convert String parameter to type " + paramType.getCanonicalName());
        }

        return new ParameterComponent(ctx -> {
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
        }, ctx -> {
            if (paramType.equals(Optional.class) || paramType.equals(OptionalInt.class) ||
                    paramType.equals(OptionalLong.class) || paramType.equals(OptionalDouble.class)) {
                return Mono.just(true);
            }
            if (ctx instanceof CommandUseContext) {
                CommandUseContext<?> cctx = (CommandUseContext<?>) ctx;
                return Mono.just(value < cctx.getParameters().length);
            }
            return Mono.just(true);
        });
    }

    protected ParameterComponent compileParameterVariable(Object obj, Method method, Parameter parameter) {
        Variable vAnnot = parameter.getAnnotation(Variable.class);
        String value = vAnnot.value();
        Class<?> paramType = parameter.getType();

        return new ParameterComponent(ctx -> {
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
    }

    protected static class MethodCallingAction implements CommandAction {
        private final List<ParameterComponent> parameterComponents;
        private final Object objToInvokeOn;
        private final Method methodToCall;
        private final BiFunction<CommandUseContext<?>, Object, Mono<Void>> postTransformer;

        public MethodCallingAction(List<ParameterComponent> parameterComponents, Object objToInvokeOn, Method methodToCall, BiFunction<CommandUseContext<?>, Object, Mono<Void>> postTransformer) {
            this.parameterComponents = parameterComponents;
            this.objToInvokeOn = objToInvokeOn;
            this.methodToCall = methodToCall;
            this.postTransformer = postTransformer;
        }

        @Override
        public Mono<Void> doAction(CommandUseContext<?> context) {
            Object[] params = parameterComponents.stream()
                    .map(pc -> pc.func.apply(context))
                    .toArray();
            return Mono.fromCallable(() -> methodToCall.invoke(objToInvokeOn, params))
                    .flatMap(o -> postTransformer.apply(context, o));
        }

        @Override
        public Mono<Boolean> guard(PlatformBaseContext<?> context) {
            return parameterComponents.stream()
                    .filter(pc -> pc.guard != null)
                    .map(pc -> pc.guard.apply(context))
                    .reduce(Mono.just(true), BooleanUtils::and);
        }
    }

}
