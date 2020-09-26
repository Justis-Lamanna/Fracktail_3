package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;

import java.util.ListResourceBundle;
import java.util.Optional;
import java.util.ResourceBundle;

import static org.mockito.Mockito.when;

class TranslatorFormatterTest {
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

    private ResourceBundle makeBundle(String key, String value) {
        return new ListResourceBundle() {
            @Override
            protected Object[][] getContents() {
                return new Object[][] {{key, value}};
            }
        };
    }

    @Test
    void testFormat_BundlePresentWithKey() {
        when(context.getResourceBundle()).thenReturn(Optional.of(makeBundle("greeting", "hello, world")));
        TranslatorFormatter formatter = new TranslatorFormatter();

        StepVerifier.create(formatter.format("greeting", context))
                .expectNext("hello, world")
                .verifyComplete();
    }

    @Test
    void testFormat_BundlePresentWithoutKey() {
        when(context.getResourceBundle()).thenReturn(Optional.of(makeBundle("farewell", "goodbye, world")));
        TranslatorFormatter formatter = new TranslatorFormatter();

        StepVerifier.create(formatter.format("greeting", context))
                .expectNext("greeting")
                .verifyComplete();
    }

    @Test
    void testFormat_BundlePresentWithoutKey_WithDefault() {
        when(context.getResourceBundle()).thenReturn(Optional.of(makeBundle("farewell", "goodbye, world")));
        TranslatorFormatter formatter = new TranslatorFormatter("howdy");

        StepVerifier.create(formatter.format("greeting", context))
                .expectNext("howdy")
                .verifyComplete();
    }

    @Test
    void testFormat_NoBundle() {
        when(context.getResourceBundle()).thenReturn(Optional.empty());
        TranslatorFormatter formatter = new TranslatorFormatter();

        StepVerifier.create(formatter.format("greeting", context))
                .expectNext("greeting")
                .verifyComplete();
    }

    @Test
    void testFormat_NoBundle_WithDefault() {
        when(context.getResourceBundle()).thenReturn(Optional.empty());
        TranslatorFormatter formatter = new TranslatorFormatter("howdy");

        StepVerifier.create(formatter.format("greeting", context))
                .expectNext("howdy")
                .verifyComplete();
    }
}