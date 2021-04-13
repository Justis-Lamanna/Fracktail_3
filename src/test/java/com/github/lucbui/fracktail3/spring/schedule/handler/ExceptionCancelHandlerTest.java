package com.github.lucbui.fracktail3.spring.schedule.handler;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.schedule.trigger.ExecuteRepeatedlyTrigger;
import com.github.lucbui.fracktail3.spring.schedule.exception.CancelTaskException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.Instant;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExceptionCancelHandlerTest extends BaseFracktailTest {
    @Mock
    ScheduledUseContext context;

    @Spy
    ScheduledEvent event =
            new ScheduledEvent("scheduled", new ExecuteRepeatedlyTrigger(Duration.ofSeconds(5)), ctx -> Mono.empty());

    @BeforeEach
    void setUp() {
        super.parentSetup();

        when(context.getBot()).thenReturn(bot);
        when(context.getScheduledEvent()).thenReturn(event);
        when(context.getPayload()).thenReturn(Instant.EPOCH);
    }

    @Test
    void cancelEventWhenInvoked() {
        ExceptionCancelHandler handler = new ExceptionCancelHandler();

        Mono<Void> t = handler.apply(context, new CancelTaskException());

        StepVerifier.create(t)
                .expectSubscription()
                .verifyComplete();

        verify(event).cancel();
    }
}