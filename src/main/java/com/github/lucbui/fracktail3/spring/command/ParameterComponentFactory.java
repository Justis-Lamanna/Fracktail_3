package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.plugin.v2.ParameterComponentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ParameterComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParameterComponentFactory.class);

    @Autowired
    public ParameterComponentFactory(ConversionService conversionService, Plugins plugins) {
        super(conversionService, plugins);
    }

    public List<ParameterComponent> compileParameters(Object obj, Method method) {
        LOGGER.debug("Compiling parameters of method {}", method.getName());
        return Arrays.stream(method.getParameters())
                .map(param -> compileParameter(obj, method, param))
                .collect(Collectors.toList());
    }

    public ParameterComponent compileParameter(Object obj, Method method, Parameter parameter) {
        LOGGER.debug("Compiling parameter {} of method {}", parameter.getName(), method.getName());
        List<ParameterComponentStrategy> strategies = getParameterStrategies(parameter);
        for(ParameterComponentStrategy s : strategies) {
            Optional<ParameterComponent> componentOpt = s.create(obj, method, parameter);
            if(componentOpt.isPresent()) {
                ParameterComponent component = componentOpt.get();
                for(ParameterComponentStrategy strategy : strategies) {
                    component = strategy.decorate(obj, method, parameter, component);
                }
                return component;
            }
        }
        throw new BotConfigurationException("Unequipped to compile parameter " + parameter.getName() + " for method " + method.getName());
//        Class<?> type = parameter.getType();
//        ParameterComponent component;
//        if (parameter.isAnnotationPresent(com.github.lucbui.fracktail3.spring.annotation.Parameter.class)) {
//            LOGGER.debug("Compiling parameter {} annotated with @Parameter", parameter.getName());
//            component = compileParameterAnnotated(obj, method, parameter);
//        } else if(parameter.isAnnotationPresent(ParameterRange.class)) {
//            LOGGER.debug("Compiling parameter {} annotated with @ParameterRange", parameter.getName());
//            component = compileParameterRangeAnnotated(obj, method, parameter);
//        } else if(parameter.isAnnotationPresent(Variable.class)) {
//            LOGGER.debug("Compiling parameter {} annotated with @Variable", parameter.getName());
//            component = compileVariableAnnotated(obj, method, parameter);
//        } else if (parameter.isAnnotationPresent(Platform.class)) {
//            LOGGER.warn("Compiling parameter {} annotated with @Platform", parameter.getName());
//            component = compilePlatformAnnotated(obj, method, parameter);
//        } else if(ClassUtils.isAssignable(Bot.class, type)) {
//            LOGGER.warn("Injecting parameter {} annotated with Bot instance", parameter.getName());
//            component = new ParameterComponent(BaseContext::getBot);
//        } else {
//            LOGGER.warn("Parameter {} matches no acceptable types. Looking in plugins...", parameter.getName());
//            component = plugins.createCompiledParameter(obj, method, parameter)
//                    .orElseThrow(() -> new BotConfigurationException("Unable to parse parameter " + parameter.getName() +
//                            " of type " + type.getCanonicalName() +
//                            "in method " + method.getName()));
//        }
//        return plugins.enhanceCompiledParameter(obj, method, parameter, component);
    }

//
//    protected ParameterComponent compileVariableAnnotated(Object obj, Method method, Parameter parameter) {
//        Variable vAnnot = parameter.getAnnotation(Variable.class);
//        String value = vAnnot.value();
//        Class<?> paramType = parameter.getType();
//
//        ParameterComponent component = new ParameterComponent(ctx -> {
//            AsynchronousMap<String, Object> map = ctx.getMap();
//            if(paramType.equals(Mono.class)) {
//                return map.getAsync(value);
//            }
//            return convertObjectForParam(map.get(value), parameter);
//        });
//
//        if(isNotOptional(parameter, vAnnot.optional())) {
//            component.addGuard(new MapContainsKeyGuard(value));
//        }
//
//        return component;
//    }
//
//    protected ParameterComponent compilePlatformAnnotated(Object obj, Method method, Parameter parameter) {
//        return new ParameterComponent(ctx -> convertObjectForParam(ctx.getPlatform(), parameter));
//    }
//
}
