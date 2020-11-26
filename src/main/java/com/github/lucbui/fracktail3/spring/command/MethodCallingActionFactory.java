package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import com.github.lucbui.fracktail3.spring.annotation.*;
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
import java.lang.reflect.Array;
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
        LOGGER.debug("Finished compiling method {}", method.getName());
        return new MethodCallingAction(methodComponent, components, obj, method, returnComponent, exceptionComponent);
    }

    private MethodComponent compileMethod(Object obj, Method method) {
        LOGGER.debug("Compiling method {}", method.getName());
        MethodComponent component = new MethodComponent();
        if(method.isAnnotationPresent(ForPlatform.class)) {
            Class<? extends Platform> platform = method.getAnnotation(ForPlatform.class).value();
            LOGGER.debug("Limiting command to usage with {} platform", platform.getCanonicalName());
            component.addGuard(ctx -> Mono.just(ClassUtils.isAssignable(ctx.getPlatform().getClass(), platform)));
        }
        return plugins.enhanceCompiledMethod(obj, method, component);
    }

    protected List<ParameterComponent> compileParameters(Object obj, Method method) {
        LOGGER.debug("Compiling parameters of method {}", method.getName());
        return Arrays.stream(method.getParameters())
                .map(param -> compileParameter(obj, method, param))
                .collect(Collectors.toList());
    }

    protected ParameterComponent compileParameter(Object obj, Method method, Parameter parameter) {
        Class<?> type = parameter.getType();
        ParameterComponent component;
        if (parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Parameter.class)) {
            LOGGER.debug("Compiling parameter {} annotated with @Parameter", parameter.getName());
            component = compileParameterAnnotated(obj, method, parameter);
        } else if(parameter.isAnnotationPresent(ParameterRange.class)) {
            LOGGER.debug("Compiling parameter {} annotated with @ParameterRange", parameter.getName());
            component = compileParameterRangeAnnotated(obj, method, parameter);
        } else if(parameter.isAnnotationPresent(Variable.class)) {
            LOGGER.debug("Compiling parameter {} annotated with @Variable", parameter.getName());
            component = compileParameterVariable(obj, method, parameter);
        } else if (parameter.isAnnotationPresent(Platform.class)) {
            LOGGER.warn("Compiling parameter {} annotated with @Platform", parameter.getName());
            component = compileParameterPlatform(obj, method, parameter);
        } else if(ClassUtils.isAssignable(Bot.class, type)) {
            LOGGER.warn("Injecting parameter {} annotated with Bot instance", parameter.getName());
            component = new ParameterComponent(BaseContext::getBot);
        } else {
            LOGGER.warn("Parameter {} matches no acceptable types. Looking in plugins...", parameter.getName());
            component = plugins.createCompiledParameter(obj, method, parameter)
                    .orElseThrow(() -> new BotConfigurationException("Unable to parse parameter " + parameter.getName() +
                             " of type " + type.getCanonicalName() +
                             "in method " + method.getName()));
        }
        return plugins.enhanceCompiledParameter(obj, method, parameter, component);
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

        ParameterComponent component = new ParameterComponent(ctx ->
                ctx.getParameters().getParameter(value)
                .map(s -> convertObjectForParam(s, parameter, pAnnot.optional()))
                .orElseGet(() -> Defaults.getDefault(paramType)));

        if (isNotOptional(paramType) && !pAnnot.optional()) {
            //Enforce parameter count (unless optional is expected)
            component.addGuard(ctx -> {
                if (ctx instanceof CommandUseContext) {
                    CommandUseContext<?> cctx = (CommandUseContext<?>) ctx;
                    return Mono.just(value < cctx.getParameters().getNumberOfParameters());
                }
                return Mono.just(true);
            });
        }
        return component;
    }

    protected ParameterComponent compileParameterRangeAnnotated(Object obj, Method method, Parameter parameter) {
        ParameterRange range = parameter.getAnnotation(ParameterRange.class);
        int start = range.lower();
        int end = range.value();
        Class<?> paramType = parameter.getType();

        ParameterComponent component;
        if(paramType.isArray()) {
            Class<?> innerType = paramType.getComponentType();
            return new ParameterComponent(context -> context.getParameters().getParameters(start, end).stream()
                .map(opt -> {
                    if(opt.isPresent()) {
                        return convertObjectForClass(opt.get(), parameter, innerType, range.optional());
                    } else if(range.optional()) {
                        return Defaults.getDefault(innerType);
                    } else {
                        throw new BotConfigurationException("Cannot cast object " +
                                obj.getClass().getCanonicalName() + " to parameter type " + innerType.getCanonicalName());
                    }
                })
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Object arr = Array.newInstance(innerType, list.size());
                    for(int idx = 0; idx < list.size(); idx++) {
                        Array.set(arr, idx, list.get(idx));
                    }
                    return arr;
                })));
        } else if(ClassUtils.isAssignable(List.class, paramType)) {
            return new ParameterComponent(context -> context.getParameters().getParameters(start, end).stream()
                .map(opt -> {
                    if(opt.isPresent()) {
                        return convertObjectForClass(opt.get(), parameter, String.class, range.optional());
                    } else if(range.optional()) {
                        return Defaults.getDefault(String.class);
                    } else {
                        throw new BotConfigurationException("Cannot cast object " +
                                obj.getClass().getCanonicalName() + " to parameter type String (assuming List<String>)");
                    }
                })
                .collect(Collectors.toList()));
        } else if(ClassUtils.isAssignable(String.class, paramType)) {
            //TODO: Substring?
            return new ParameterComponent(context -> context.getParameters().getRaw());
        } else {
            throw new BotConfigurationException("Cannot convert parameters to type " + paramType.getCanonicalName());
        }
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
            return convertObjectForParam(map.get(value), parameter, vAnnot.optional());
        });

        if(isNotOptional(paramType) && !vAnnot.optional()) {
            component.addGuard(ctx -> Mono.just(ctx.getMap().containsKey(value)));
        }

        return component;
    }

    private ParameterComponent compileParameterPlatform(Object obj, Method method, Parameter parameter) {
        Platform p = parameter.getAnnotation(Platform.class);
        Class<?> paramType = parameter.getType();

        ParameterComponent component = new ParameterComponent(ctx -> convertObjectForParam(ctx.getPlatform(), parameter, p.optional()));

        if(isNotOptional(paramType) && !p.optional()) {
            component.addGuard(ctx -> Mono.just(ClassUtils.isAssignable(ctx.getPlatform().getClass(), paramType)));
        }
        return component;
    }

    private Object convertObjectForParam(Object obj, Parameter param, boolean isOptional) {
        return convertObjectForClass(obj, param, param.getType(), isOptional);
    }

    private Object convertObjectForClass(Object obj, Parameter param, Class<?> paramType, boolean isOptional) {
        if(obj == null) {
            if(isOptional) {
                return Defaults.getDefault(paramType);
            }
            throw new BotConfigurationException("Encountered null for non-optional parameter " + param.getName());
        } else if(paramType.equals(Optional.class)) {
            return Optional.of(obj); //All we can do is assume the types are right.
        } else if(ClassUtils.isAssignable(obj.getClass(), paramType)) {
            return obj;
        } else if(conversionService.canConvert(obj.getClass(), paramType)) {
            return conversionService.convert(obj, paramType);
        } else {
            if(isOptional) {
                return Defaults.getDefault(paramType);
            }
            throw new BotConfigurationException("Cannot cast object " + obj.getClass().getCanonicalName() + " to parameter type " + paramType.getCanonicalName());
        }
    }

    private boolean isNotOptional(Class<?> clazz) {
        return !clazz.equals(Optional.class) && !clazz.equals(OptionalInt.class) &&
                !clazz.equals(OptionalLong.class) && !clazz.equals(OptionalDouble.class);
    }
}
