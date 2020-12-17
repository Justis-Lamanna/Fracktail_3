package com.github.lucbui.fracktail3.spring.command.annotation;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.ContextFormatter;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.schedule.trigger.CronTrigger;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ExecuteAfterDurationTrigger;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ExecuteOnInstantTrigger;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ExecuteRepeatedlyTrigger;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Cron;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAfter;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunAt;
import com.github.lucbui.fracktail3.spring.schedule.annotation.RunEvery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static com.github.lucbui.fracktail3.spring.command.annotation.AnnotationUtilsTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AnnotationUtilsTest {
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

    @Test
    void testCron() {
        Cron cron = createCron("0", "0", "0", "*", "*", "?", "");
        CronTrigger trigger = (CronTrigger) AnnotationUtils.fromCron(cron);

        assertEquals("0 0 0 * * ?", trigger.getCron());
        assertEquals(TimeZone.getDefault(), trigger.getTimeZone());
    }

    @Test
    void testRunAfter() {
        RunAfter runAfter = createRunAfter("PT5S");
        ExecuteAfterDurationTrigger trigger = (ExecuteAfterDurationTrigger) AnnotationUtils.fromRunAfter(runAfter);

        assertEquals(Duration.ofSeconds(5), trigger.getDuration());
    }

    @Test
    void testRunAfterWithInvalidExpression() {
        RunAfter runAfter = createRunAfter("boop");
        assertThrows(BotConfigurationException.class, () -> {
            ExecuteAfterDurationTrigger trigger = (ExecuteAfterDurationTrigger) AnnotationUtils.fromRunAfter(runAfter);
        });
    }

    @Test
    void testRunEvery() {
        RunEvery runEvery = createRunEvery("PT5S");
        ExecuteRepeatedlyTrigger trigger = (ExecuteRepeatedlyTrigger) AnnotationUtils.fromRunEvery(runEvery);

        assertEquals(Duration.ofSeconds(5), trigger.getDuration());
    }

    @Test
    void testRunEveryWithInvalidExpression() {
        RunEvery runEvery = createRunEvery("boop");
        assertThrows(BotConfigurationException.class, () -> {
            ExecuteRepeatedlyTrigger trigger = (ExecuteRepeatedlyTrigger) AnnotationUtils.fromRunEvery(runEvery);
        });
    }

    @Test
    void testRunAtNoTimezone() {
        ZonedDateTime zdt = ZonedDateTime.of(2020, 12, 16, 10, 30, 0, 0, ZoneId.systemDefault());
        RunAt runAt = createRunAt("2020-12-16T10:30:00");
        ExecuteOnInstantTrigger trigger = (ExecuteOnInstantTrigger) AnnotationUtils.fromRunAt(runAt);

        assertEquals(zdt.toInstant(), trigger.getInstant());
    }

    @Test
    void testRunAtWithTimezone() {
        ZonedDateTime zdt = ZonedDateTime.of(2020, 12, 16, 10, 30, 0, 0, ZoneId.of("+01:00"));
        RunAt runAt = createRunAt("2020-12-16T10:30:00+01:00");
        ExecuteOnInstantTrigger trigger = (ExecuteOnInstantTrigger) AnnotationUtils.fromRunAt(runAt);

        assertEquals(zdt.toInstant(), trigger.getInstant());
    }

    @Test
    void testRunAtWithEnhancedTimezone() {
        ZonedDateTime zdt = ZonedDateTime.of(2020, 12, 16, 10, 30, 0, 0, ZoneId.of("+01:00"));
        RunAt runAt = createRunAt("2020-12-16T10:30:00+01:00[Europe/Paris]");
        ExecuteOnInstantTrigger trigger = (ExecuteOnInstantTrigger) AnnotationUtils.fromRunAt(runAt);

        assertEquals(zdt.toInstant(), trigger.getInstant());
    }

    @Test
    void testRunAtWithBadTime() {
        RunAt runAt = createRunAt("boop");
        assertThrows(BotConfigurationException.class, () -> {
            ExecuteOnInstantTrigger trigger = (ExecuteOnInstantTrigger) AnnotationUtils.fromRunAt(runAt);
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