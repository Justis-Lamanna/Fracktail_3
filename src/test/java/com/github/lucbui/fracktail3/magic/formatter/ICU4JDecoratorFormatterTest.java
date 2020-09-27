package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ICU4JDecoratorFormatterTest {
    @Mock
    private CommandContext context;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testFormatNoPlaceholders() {
        ICU4JDecoratorFormatter formatter = new ICU4JDecoratorFormatter();
        when(context.getExtendedVariableMap()).thenReturn(Mono.just(Collections.emptyMap()));

        StepVerifier.create(formatter.format("hello, world", context))
                .expectNext("hello, world")
                .verifyComplete();
    }

    @Test
    void testFormatPlaceholders() {
        ICU4JDecoratorFormatter formatter = new ICU4JDecoratorFormatter();
        when(context.getExtendedVariableMap()).thenReturn(Mono.just(Collections.singletonMap("planet", "world")));

        StepVerifier.create(formatter.format("hello, {planet}", context))
                .expectNext("hello, world")
                .verifyComplete();

    }

    @Test
    void testFormatPlaceholders_WithWrappedFormatter() {
        ContextFormatter dummy = Mockito.mock(ContextFormatter.class);
        ICU4JDecoratorFormatter formatter = new ICU4JDecoratorFormatter(dummy);
        when(dummy.format(anyString(), any())).thenReturn(Mono.just("hello, {planet}"));
        when(context.getExtendedVariableMap()).thenReturn(Mono.just(Collections.singletonMap("planet", "world")));

        StepVerifier.create(formatter.format("", context))
                .expectNext("hello, world")
                .verifyComplete();
    }

    @Test
    void testFormatPlaceholders_NoVariables() {
        ICU4JDecoratorFormatter formatter = new ICU4JDecoratorFormatter();
        when(context.getExtendedVariableMap()).thenReturn(Mono.empty());

        StepVerifier.create(formatter.format("hello, {planet}", context))
                .expectNext("hello, {planet}")
                .verifyComplete();
    }
}