package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.Localizable;
import com.github.lucbui.fracktail3.magic.platform.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TranslatorFormatterTest {
    @Mock
    private Localizable localizable;

    private final BaseContext<?> ctx = new TestContext();
    private final BaseContext<?> emptyContext = new BaseContext<Object>() {
        @Override
        public Bot getBot() {
            return null;
        }

        @Override
        public Object getPayload() {
            return null;
        }

        @Override
        public Mono<Message> respond(String message) {
            return Mono.empty();
        }
    };

    private TranslatorFormatter formatter = new TranslatorFormatter();

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        when(localizable.isLocalizationEnabled()).thenReturn(true);

        formatter = new TranslatorFormatter();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void translateKeyToValue() {
        when(localizable.getResourceBundle(any())).thenReturn(createTestBundle());

        StepVerifier.create(formatter.format("greeting", ctx, Collections.emptyMap()))
                .expectSubscription()
                .expectNext("hello, world")
                .verifyComplete();
    }

    @Test
    public void returnInputUnchangedWhenBundleDoesntHaveRelevantKey() {
        when(localizable.getResourceBundle(any())).thenReturn(createTestBundle());

        StepVerifier.create(formatter.format("planet", ctx, Collections.emptyMap()))
                .expectSubscription()
                .expectNext("planet")
                .verifyComplete();
    }

    @Test
    public void returnInputUnchangedWhenBundleIsOff() {
        when(localizable.isLocalizationEnabled()).thenReturn(false);

        StepVerifier.create(formatter.format("planet", ctx, Collections.emptyMap()))
                .expectSubscription()
                .expectNext("planet")
                .verifyComplete();
    }

    @Test
    public void returnInputUnchangedWhenContextDoesntSupportLocalization() {
        StepVerifier.create(formatter.format("planet", emptyContext, Collections.emptyMap()))
                .expectSubscription()
                .expectNext("planet")
                .verifyComplete();
    }

    protected ListResourceBundle createTestBundle() {
        return new ListResourceBundle() {
            @Override
            protected Object[][] getContents() {
                return new Object[][]{
                        {"greeting", "hello, world"}
                };
            }
        };
    }

    private class TestContext implements BaseContext<Object>, Localizable {
        @Override
        public Bot getBot() {
            return null;
        }

        @Override
        public Object getPayload() {
            return null;
        }

        @Override
        public Mono<Message> respond(String message) {
            return Mono.empty();
        }

        @Override
        public ResourceBundle getResourceBundle(Locale locale) {
            return localizable.getResourceBundle(locale);
        }

        @Override
        public boolean isLocalizationEnabled() {
            return localizable.isLocalizationEnabled();
        }
    }
}