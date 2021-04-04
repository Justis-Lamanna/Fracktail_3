package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;

class ExceptionRespondHandlerTest extends BaseFracktailTest {
    @BeforeEach
    void setup() {
        super.parentSetup();
    }

    @Test
    void shouldRespondWithMessageInMap() {
        NoSuchMethodException exception = new NoSuchMethodException("hello");
        ExceptionRespondHandler handler = new ExceptionRespondHandler(FormattedString.literal("hello, world"));

        StepVerifier.create(handler.apply(context, exception))
                .expectSubscription()
                .verifyComplete();

        verify(context).respond("hello, world");
    }
}