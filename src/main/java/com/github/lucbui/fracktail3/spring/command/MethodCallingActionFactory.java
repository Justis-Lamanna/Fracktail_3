package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.github.lucbui.fracktail3.spring.annotation.OnExceptionRespond;
import com.github.lucbui.fracktail3.spring.annotation.Respond;
import com.github.lucbui.fracktail3.spring.annotation.RespondType;
import com.github.lucbui.fracktail3.spring.annotation.Variable;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.util.AnnotationUtils;
import com.github.lucbui.fracktail3.spring.util.Defaults;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MethodCallingActionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodCallingActionFactory.class);
    @Autowired
    ConversionService conversionService;

    @Autowired
    private Plugins plugins;

    public CommandAction createAction(Object obj, Method method) {
        MethodComponent methodComponent = compileMethod(obj, method);
        List<ParameterComponent> components = compileParameters(obj, method);
        ReturnComponent returnComponent = compileReturn(obj, method);
        ExceptionComponent exceptionComponent = compileException(obj, method);
        return new MethodCallingAction(methodComponent, components, obj, method, returnComponent, exceptionComponent);
    }

    private MethodComponent compileMethod(Object obj, Method method) {
        LOGGER.debug("Compiling method {}", method.getName());
        return plugins.enhanceCompiledMethod(obj, method, new MethodComponent());
    }

    protected List<ParameterComponent> compileParameters(Object obj, Method method) {
        LOGGER.debug("Compiling parameters of method {}", method.getName());
        return Arrays.stream(method.getParameters())
                .map(param -> compileParameter(obj, method, param))
                .collect(Collectors.toList());
    }

    protected ParameterComponent compileParameter(Object obj, Method method, Parameter parameter) {
        Class<?> type = parameter.getType();
        if (parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Parameter.class)) {
            LOGGER.debug("Compiling parameter {} annotated with @Parameter", parameter.getName());
            return compileParameterAnnotated(obj, method, parameter);
        } else if(parameter.isAnnotationPresent(Variable.class)) {
            LOGGER.debug("Compiling parameter {} annotated with @Variable", parameter.getName());
            return compileParameterVariable(obj, method, parameter);
        } else if (ClassUtils.isAssignable(CommandUseContext.class, type)) {
            LOGGER.warn("Compiling parameter {} of type CommandUseContext. THIS IS DEPRECATED!", parameter.getName());
            return plugins.enhanceCompiledParameter(obj, method, parameter, new ParameterComponent(ctx -> ctx));
        } else {
            LOGGER.warn("Parameter {} matches no acceptable types. Looking in plugins...", parameter.getName());
            ParameterComponent component = plugins.createCompiledParameter(obj, method, parameter)
                    .orElseThrow(() -> new BotConfigurationException("Unable to parse parameter " + parameter.getName() +
                             " of type " + type.getCanonicalName() +
                             "in method " + method.getName()));
            return plugins.enhanceCompiledParameter(obj, method, parameter, component);
        }
    }

    protected ReturnComponent compileReturn(Object obj, Method method) {
        LOGGER.debug("Compiling return of method {}", method.getName());
        Class<?> returnType = method.getReturnType();
        ReturnComponent ret;
        if (returnType.equals(Void.class)) {
            LOGGER.debug("Compiling return of method {} as void", method.getName());
            ret = new ReturnComponent((ctx, o) -> Mono.empty());
        } else if (returnType.equals(Mono.class)) {
            LOGGER.debug("Compiling return of method {} as Mono<?>", method.getName());
            ret = new ReturnComponent((ctx, o) -> ((Mono<?>) o).then());
        } else if (returnType.equals(Flux.class)) {
            LOGGER.debug("Compiling return of method {} as Flux<?>", method.getName());
            ret = new ReturnComponent((ctx, o) -> ((Flux<?>) o).then());
        } else if(returnType.equals(String.class)) {
            RespondType type = method.isAnnotationPresent(Respond.class) ?
                    method.getAnnotation(Respond.class).value() : RespondType.INLINE;
            LOGGER.debug("Compiling return of method {} as String (responding as {})", method.getName(), type);
            ret = new ReturnComponent(type.forString());
        } else if(returnType.equals(FormattedString.class)) {
            RespondType type = method.isAnnotationPresent(Respond.class) ?
                    method.getAnnotation(Respond.class).value() : RespondType.INLINE;
            LOGGER.debug("Compiling return of method {} as FormattedString (responding as {})", method.getName(), type);
            ret = new ReturnComponent(type.forFString());
        } else {
            LOGGER.debug("Method {} matches no acceptable types. Looking in plugins...", method.getName());
            ret = plugins.createCompiledReturn(obj, method)
                    .orElseThrow(() -> new BotConfigurationException("Unable to parse return type " + returnType.getCanonicalName() +
                            "in method " + method.getName()));
        }
        return plugins.enhanceCompiledReturn(obj, method, ret);
    }

    private ExceptionComponent compileException(Object obj, Method method) {
        LOGGER.debug("Compiling exception handlers of method {}", method.getName());
        ExceptionComponent component = new ExceptionComponent();
        compileOnExceptionRespond(component, obj.getClass());
        compileOnExceptionRespond(component, method);
        return plugins.enhanceCompiledException(obj, method, component);
    }

    private void compileOnExceptionRespond(ExceptionComponent component, AnnotatedElement element) {
        Set<OnExceptionRespond> annotations = AnnotatedElementUtils.getMergedRepeatableAnnotations(element, OnExceptionRespond.class, OnExceptionRespond.Wrapper.class);

        for(OnExceptionRespond annotation : annotations) {
            FormattedString fString = AnnotationUtils.fromFString(annotation.value());
            RespondType type = annotation.respondType();
            ExceptionComponent.ExceptionHandler handler =
                    (ctx, ex) -> type.outputFString()
                            .apply(ctx, fString, Collections.singletonMap("message", ex.getMessage()));
            for(Class<? extends Throwable> clazz : annotation.exception()) {
                LOGGER.debug("On exception {} will respond with {}", clazz.getCanonicalName(), annotation.value().value());
                component.addHandler(clazz, handler);
            }
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
            return ctx.getParameters().getParameter(value)
                    .map(s -> (Object)conversionService.convert(s, paramType))
                    .orElseGet(() -> Defaults.getDefault(paramType));
        });

        if (!isOptional(paramType) && !pAnnot.optional()) {
            //Enforce parameter count (unless optional is expected)
            component.addGuard(ctx -> {
                if (ctx instanceof CommandUseContext) {
                    CommandUseContext<?> cctx = (CommandUseContext<?>) ctx;
                    return Mono.just(value < cctx.getParameters().getNumberOfParameters());
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

}
