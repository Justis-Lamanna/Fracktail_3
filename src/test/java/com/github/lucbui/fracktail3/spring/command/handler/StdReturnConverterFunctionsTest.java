package com.github.lucbui.fracktail3.spring.command.handler;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.command.model.BotResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StdReturnConverterFunctionsTest extends BaseFracktailTest {

    @BeforeEach
    public void setup() {
        super.parentSetup();
    }

    @Test
    void voidsShouldReturnEmptyMono() {
        StdReturnConverterFunctions.Voids rcf = new StdReturnConverterFunctions.Voids();

        StepVerifier.create(rcf.apply(context, null))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void monosShouldReturnMonoWhichCompletesWhenMonoCompletes() {
        StdReturnConverterFunctions.Monos rcf = new StdReturnConverterFunctions.Monos();
        PublisherProbe<Void> probe = PublisherProbe.empty();

        StepVerifier.create(rcf.apply(context, probe.mono()))
                .expectSubscription()
                .verifyComplete();

        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void monosShouldReturnEmptyWhenNull() {
        StdReturnConverterFunctions.Monos rcf = new StdReturnConverterFunctions.Monos();

        StepVerifier.create(rcf.apply(context, null))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void fluxsShouldReturnMonoWhichCompletesWhenFluxCompletes() {
        StdReturnConverterFunctions.Fluxs rcf = new StdReturnConverterFunctions.Fluxs();
        PublisherProbe<Void> probe = PublisherProbe.empty();

        StepVerifier.create(rcf.apply(context, probe.flux()))
                .expectSubscription()
                .verifyComplete();

        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void fluxsShouldReturnEmptyWhenNull() {
        StdReturnConverterFunctions.Fluxs rcf = new StdReturnConverterFunctions.Fluxs();

        StepVerifier.create(rcf.apply(context, null))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    void stringsShouldRespond() {
        StdReturnConverterFunctions.Strings rcf = new StdReturnConverterFunctions.Strings();

        StepVerifier.create(rcf.apply(context, "hello, world"))
                .expectSubscription()
                .verifyComplete();

        verify(context).respond(eq("hello, world"));
    }

    @Test
    void fstringsShouldRespond() {
        StdReturnConverterFunctions.FStrings rcf = new StdReturnConverterFunctions.FStrings();

        StepVerifier.create(rcf.apply(context, FormattedString.from("hello, world")))
                .expectSubscription()
                .verifyComplete();

        verify(context).respond(eq("hello, world"));
    }

    @Test
    void botResponseShouldRespond() {
        StdReturnConverterFunctions.BotResponses rcf = new StdReturnConverterFunctions.BotResponses();

        BotResponse mock = Mockito.mock(BotResponse.class);
        when(mock.respondWith()).thenReturn(FormattedString.literal("hello, world"));

        StepVerifier.create(rcf.apply(context, mock))
                .expectSubscription()
                .verifyComplete();

        verify(context).respond(eq("hello, world"));
        verify(mock).respondWith();
    }
}