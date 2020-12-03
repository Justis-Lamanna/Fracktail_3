package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.annotation.OnExceptionRespond;
import com.github.lucbui.fracktail3.spring.command.handler.ExceptionRespondHandler;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import com.github.lucbui.fracktail3.spring.util.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Set;

@Component
public class ExceptionComponentFactory extends BaseFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodComponentFactory.class);

    @Autowired
    public ExceptionComponentFactory(ConversionService conversionService, Plugins plugins) {
        super(conversionService, plugins);
    }

    public ExceptionComponent compileException(Object obj, Method method) {
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
            ExceptionComponent.ExceptionHandler handler = new ExceptionRespondHandler(annotation.respondType(), fString);
            for(Class<? extends Throwable> clazz : annotation.exception()) {
                LOGGER.debug("On exception {} will respond with {}", clazz.getCanonicalName(), annotation.value().value());
                component.addHandler(clazz, handler);
            }
        }
    }
}
