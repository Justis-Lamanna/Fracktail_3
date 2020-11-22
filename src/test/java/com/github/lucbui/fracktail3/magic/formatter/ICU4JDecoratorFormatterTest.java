package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.util.AsynchronousMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static org.mockito.Mockito.when;

class ICU4JDecoratorFormatterTest extends BaseFracktailTest {
    private ICU4JDecoratorFormatter formatter;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        formatter = new ICU4JDecoratorFormatter();
    }

    @Test
    void formatStringUsingPlaceholders() {
        when(context.getMap()).thenReturn(new AsynchronousMap<>(Collections.singletonMap("planet", "world")));
        StepVerifier.create(formatter.format("hello, {planet}", context, Collections.emptyMap()))
                .expectSubscription()
                .expectNext("hello, world")
                .verifyComplete();
    }

    @Test
    void formatStringIgnoringPlaceholdersIfNotRelevant() {
        when(context.getMap()).thenReturn(new AsynchronousMap<>(Collections.singletonMap("galaxy", "milky way")));
        StepVerifier.create(formatter.format("hello, {planet}", context, Collections.emptyMap()))
                .expectSubscription()
                .expectNext("hello, {planet}")
                .verifyComplete();
    }

    @Test
    void formatStringUsingAsyncPlaceholders() {
        AsynchronousMap<String, Object> map = new AsynchronousMap<>();
        map.putAsync("planet", Mono.just("world"));
        when(context.getMap()).thenReturn(map);
        StepVerifier.create(formatter.format("hello, {planet}", context, Collections.emptyMap()))
                .expectSubscription()
                .expectNext("hello, world")
                .verifyComplete();
    }

    @Test
    void formatStringIgnoringPlaceholdersIfMapIsNull() {
        when(context.getMap()).thenReturn(null);
        StepVerifier.create(formatter.format("hello, {planet}", context, Collections.emptyMap()))
                .expectSubscription()
                .expectNext("hello, {planet}")
                .verifyComplete();
    }

    @Test
    void formatStringUsingPlaceholdersFromAddlVariables() {
        StepVerifier.create(formatter.format("hello, {planet}", context, Collections.singletonMap("planet", "world")))
                .expectSubscription()
                .expectNext("hello, world")
                .verifyComplete();
    }

    @Test
    void formatStringUsingPlaceholdersFromContextAndAddlVariables() {
        when(context.getMap()).thenReturn(new AsynchronousMap<>(Collections.singletonMap("greeting", "hello")));
        StepVerifier.create(formatter.format("{greeting}, {planet}", context, Collections.singletonMap("planet", "world")))
                .expectSubscription()
                .expectNext("hello, world")
                .verifyComplete();
    }

    @Test
    void formatStringUsingDatePlaceholder() {
        when(context.getLocale()).thenReturn(Mono.just(Locale.ENGLISH));
        Date now = Date.from(LocalDate.of(2020, 11, 22).atStartOfDay(ZoneId.systemDefault()).toInstant());
        StepVerifier.create(formatter.format("hello at {now, date}", context, Collections.singletonMap("now", now)))
                .expectSubscription()
                .expectNext("hello at Nov 22, 2020")
                .verifyComplete();
    }

    @Test
    void formatStringUsingDatePlaceholderInGerman() {
        when(context.getLocale()).thenReturn(Mono.just(Locale.GERMANY));
        Date now = Date.from(LocalDate.of(2020, 11, 22).atStartOfDay(ZoneId.systemDefault()).toInstant());
        StepVerifier.create(formatter.format("hello at {now, date}", context, Collections.singletonMap("now", now)))
                .expectSubscription()
                .expectNext("hello at 22.11.2020")
                .verifyComplete();
    }
}