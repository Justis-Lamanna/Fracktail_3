package com.github.lucbui.fracktail3.spring.util;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.annotation.FString;
import com.github.lucbui.fracktail3.spring.annotation.Formatter;
import com.github.lucbui.fracktail3.spring.annotation.Usage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AnnotationUtilsTest {
    private static Formatter createFormatter(Class<? extends ContextFormatter> clazz, String... params) {
        return new Formatter(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return Formatter.class;
            }

            @Override
            public Class<? extends ContextFormatter> value() {
                return clazz;
            }

            @Override
            public String[] params() {
                return params;
            }
        };
    }

    private static FString createFString(String base, Formatter... formatters) {
        return new FString(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return FString.class;
            }

            @Override
            public String value() {
                return base;
            }

            @Override
            public Formatter[] formatters() {
                return formatters;
            }
        };
    }

    private static Usage createUsage(String base, Formatter... formatters) {
        return new Usage(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return Usage.class;
            }

            @Override
            public String value() {
                return base;
            }

            @Override
            public Formatter[] formatters() {
                return formatters;
            }
        };
    }

    @Mock
    private CommandUseContext<?> ctx;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        when(ctx.getLocale()).thenReturn(Mono.just(Locale.getDefault()));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void FStringValueWithFormatterShouldInvokeFormatters() {
        FString fString = createFString("fstring", createFormatter(TestContextFormatter.class));
        FormattedString fs = AnnotationUtils.fromFString(fString);
        String s = fs.getFor(ctx).block();
        assertEquals("testing-fstring", s);
    }

    @Test
    void FStringValueWithFormatterShouldInvokeFormattersWithParams() {
        FString fString = createFString("fstring", createFormatter(TestContextFormatter.class, "custom-"));
        FormattedString fs = AnnotationUtils.fromFString(fString);
        String s = fs.getFor(ctx).block();
        assertEquals("custom-fstring", s);
    }

    @Test
    void FStringValueWithFormatterShouldInvokeFormattersAsPipe() {
        FString fString = createFString("fstring",
                createFormatter(TestContextFormatter.class, "one-"),
                createFormatter(TestContextFormatter.class, "two-"));
        FormattedString fs = AnnotationUtils.fromFString(fString);
        String s = fs.getFor(ctx).block();
        assertEquals("two-one-fstring", s);
    }

    @Test
    void FStringValueWithNoFormattersShouldInvokeDefault() {
        FString fString = createFString("fstring");
        FormattedString fs = AnnotationUtils.fromFString(fString);
        String s = fs.getFor(ctx).block();
        assertEquals("fstring", s);
    }

    @Test
    void FStringValueWithIncorrectFormatters() {
        Assertions.assertThrows(BotConfigurationException.class, () -> {
            FString fString = createFString("fstring", createFormatter(TestContextFormatter.class, "one", "two", "three"));
            AnnotationUtils.fromFString(fString);
        });
    }

    @Test
    void UsageValueWithFormatterShouldInvokeFormatters() {
        Usage usage = createUsage("usage", createFormatter(TestContextFormatter.class));
        FormattedString fs = AnnotationUtils.fromUsage(usage);
        String s = fs.getFor(ctx).block();
        assertEquals("testing-usage", s);
    }

    @Test
    void UsageValueWithFormatterShouldInvokeFormattersWithParams() {
        Usage usage = createUsage("usage", createFormatter(TestContextFormatter.class, "custom-"));
        FormattedString fs = AnnotationUtils.fromUsage(usage);
        String s = fs.getFor(ctx).block();
        assertEquals("custom-usage", s);
    }

    @Test
    void UsageValueWithFormatterShouldInvokeFormattersAsPipe() {
        FString fString = createFString("usage",
                createFormatter(TestContextFormatter.class, "one-"),
                createFormatter(TestContextFormatter.class, "two-"));
        FormattedString fs = AnnotationUtils.fromFString(fString);
        String s = fs.getFor(ctx).block();
        assertEquals("two-one-usage", s);
    }

    @Test
    void UsageValueWithNoFormattersShouldInvokeDefault() {
        Usage usage = createUsage("usage");
        FormattedString fs = AnnotationUtils.fromUsage(usage);
        String s = fs.getFor(ctx).block();
        assertEquals("usage", s);
    }

    @Test
    void UsageValueWithIncorrectFormatters() {
        Assertions.assertThrows(BotConfigurationException.class, () -> {
            Usage usage = createUsage("usage", createFormatter(TestContextFormatter.class, "one", "two", "three"));
            AnnotationUtils.fromUsage(usage);
        });
    }

    private static class TestContextFormatter implements ContextFormatter {
        private final String prefix;

        public TestContextFormatter() {
            prefix = "testing-";
        }

        public TestContextFormatter(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Mono<String> format(String raw, BaseContext<?> ctx, Map<String, Object> addlVars) {
            return Mono.just(this.prefix + raw);
        }
    }
}