package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.annotation.Respond;
import com.github.lucbui.fracktail3.spring.annotation.RespondType;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

@Component
public class ReturnComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    @Autowired
    public ReturnComponentFactory(ConversionService conversionService, Plugins plugins) {
        super(conversionService, plugins);
    }

    public ReturnComponent compileReturn(Object obj, Method method) {
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
            RespondType type = getRespondType(method);
            LOGGER.debug("Compiling return of method {} as String (responding as {})", method.getName(), type);
            ret = new ReturnComponent(type.forString());
        } else if(returnType.equals(FormattedString.class)) {
            RespondType type = getRespondType(method);
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

    private RespondType getRespondType(Method method) {
        return method.isAnnotationPresent(Respond.class) ?
                method.getAnnotation(Respond.class).value() : RespondType.INLINE;
    }
}
