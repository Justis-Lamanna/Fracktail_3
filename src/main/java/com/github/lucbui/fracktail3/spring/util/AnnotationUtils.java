package com.github.lucbui.fracktail3.spring.util;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.annotation.FString;
import com.github.lucbui.fracktail3.spring.annotation.Formatter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.IntStream;

public class AnnotationUtils {
    public static FormattedString fromFString(FString fString) {
        String base = fString.value();
        Formatter[] formatters = fString.formatters();
        if(formatters.length == 0) {
            return FormattedString.from(base);
        } else {
            return FormattedString.from(base, fromFormatterArray(formatters));
        }
    }

    private static ContextFormatter fromFormatterArray(Formatter[] formatters) {
        return Arrays.stream(formatters)
                .map(AnnotationUtils::fromFormatter)
                .reduce(ContextFormatter.identity(), ContextFormatter::pipe);
    }

    private static ContextFormatter fromFormatter(Formatter formatter) {
        Class<? extends ContextFormatter> clazz = formatter.value();
        Class<?>[] constructorSignature = IntStream.range(0, formatter.params().length)
                .mapToObj(x -> String.class)
                .toArray(Class[]::new);
        try {
            Constructor<? extends ContextFormatter> constructor = clazz.getConstructor(constructorSignature);
            return constructor.newInstance((Object[]) formatter.params());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new BotConfigurationException("Cannot invoke constructor in class " + clazz.getCanonicalName() + " with " + constructorSignature.length + " string args", ex);
        }
    }
}
