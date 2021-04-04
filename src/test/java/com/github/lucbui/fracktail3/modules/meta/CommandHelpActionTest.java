package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CommandHelpActionTest extends BaseFracktailTest {
    private CommandHelpAction commandHelpAction;

    @Mock
    private Command lookupCommand;

    @BeforeEach
    protected void parentSetup() {
        super.parentSetup();
        commandHelpAction = new CommandHelpAction();

        when(commandList.getCommands()).thenReturn(Collections.singletonList(lookupCommand));
        when(commandList.getNumberOfCommands()).thenReturn(1);
        when(commandList.getCommandById(anyString())).thenReturn(Optional.of(lookupCommand));
        when(commandList.getCommandsById()).thenReturn(Collections.singletonMap("test", lookupCommand));

        when(lookupCommand.getId()).thenReturn("test");
        when(lookupCommand.getNames()).thenReturn(Collections.singleton("test"));
        when(lookupCommand.matches(any())).thenReturn(Mono.just(true));
        when(lookupCommand.getHelp()).thenReturn(FormattedString.literal("test hello"));
        when(lookupCommand.doAction(any())).thenReturn(Mono.empty());
    }

    @Test
    void shouldReturnHelpIfValidCommandProvided() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("test"));

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("test hello"))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void shouldReturnErrorIfInvalidCommandProvided() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("unknown"));

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("I'm sorry, I don't know the command 'unknown'."))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void shouldReturnErrorIfValidCommandProvidedButGuardLocks() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("test"));
        when(lookupCommand.matches(any())).thenReturn(Mono.just(false));

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("I'm sorry, I don't know the command 'test'."))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void shouldReturnErrorIfValidCommandProvidedButNoHelp() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("test"));
        when(lookupCommand.getHelp()).thenReturn(null);

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("I'm sorry, I don't know the command 'test'."))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    void shouldReturnSelfHelpIfNoCommandProvided() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.empty());

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("test hello"))).thenReturn(probe.mono());

        StepVerifier.create(commandHelpAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}