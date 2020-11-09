package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MethodCallingAction implements CommandAction {
    private final Function<CommandUseContext<?>, Mono<Void>> actionFunc;
    private final Function<PlatformBaseContext<?>, Mono<Boolean>> guardFunc;

    public MethodCallingAction(Object obj, Method action) {
        this.actionFunc = compile(obj, action);
        this.guardFunc = ctx -> Mono.just(true);
    }

    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        return actionFunc.apply(context);
    }

    @Override
    public Mono<Boolean> guard(PlatformBaseContext<?> context) {
        return guardFunc.apply(context);
    }

    private Function<CommandUseContext<?>, Mono<Void>> compile(Object obj, Method method) {
        Function<CommandUseContext<?>, Object[]> parameterFunc = compileParameters(obj, method);
        BiFunction<CommandUseContext<?>, Object, Mono<Void>> returnFunc = compileReturn(obj, method);
        return ctx -> {
            Object[] params = parameterFunc.apply(ctx);
            return Mono.fromCallable(() -> method.invoke(obj, params))
                    .flatMap(o -> returnFunc.apply(ctx, o));
        };
    }

    protected Function<CommandUseContext<?>, Object[]> compileParameters(Object obj, Method method) {
        List<Function<CommandUseContext<?>, Object>> components = Arrays.stream(method.getParameters())
                .map(param -> compileParameter(obj, method, param))
                .collect(Collectors.toList());
        return ctx -> components.stream().map(func -> func.apply(ctx)).toArray();
    }

    protected Function<CommandUseContext<?>, Object> compileParameter(Object obj, Method method, Parameter parameter) {
        Class<?> type = parameter.getType();
        if(ClassUtils.isAssignable(CommandUseContext.class, type)) {
            return ctx -> ctx;
        } else {
            throw new BotConfigurationException("Unable to parse parameter " + parameter.getName() +
                    " of type " + type.getCanonicalName() +
                    "in method " + method.getName());
        }
    }

    protected BiFunction<CommandUseContext<?>, Object, Mono<Void>> compileReturn(Object obj, Method method) {
        Class<?> returnType = method.getReturnType();
        if(returnType.equals(Void.class)) {
            return (ctx, o) -> Mono.empty();
        } else if(returnType.equals(String.class)) {
            return (ctx, o) -> ctx.respond((String)o);
        } else if(returnType.equals(FormattedString.class)) {
            return (ctx, o) -> ((FormattedString)o).getFor(ctx).flatMap(ctx::respond);
        }
        return (ctx, o) -> o instanceof Mono ? ((Mono<?>)o).then() : Mono.empty();
    }
}
