package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CommandHelpActionTest extends BaseFracktailTest {
    private CommandHelpAction commandHelpAction;

    @BeforeEach
    protected void parentSetup() {
        super.parentSetup();
        commandHelpAction = new CommandHelpAction();
    }

    @Test
    void shouldReturnHelpIfValidCommandProvided() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("test"));

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("test hello"))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void shouldReturnErrorIfInvalidCommandProvided() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("unknown"));

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("I'm sorry, I don't know the command 'unknown'."))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void shouldReturnErrorIfValidCommandProvidedButGuardLocks() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("test"));
        when(command.matches(any())).thenReturn(Mono.just(false));

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("I'm sorry, I don't know the command 'test'."))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void shouldReturnErrorIfValidCommandProvidedButNoHelp() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("test"));
        when(command.getHelp()).thenReturn(null);

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("I'm sorry, I don't know the command 'test'."))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void shouldReturnSelfHelpIfNoCommandProvided() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.empty());

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("test hello"))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}