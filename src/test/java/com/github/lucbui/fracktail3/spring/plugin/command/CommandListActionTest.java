package com.github.lucbui.fracktail3.spring.plugin.command;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.command.action.CompositeAction;
import org.apache.commons.collections4.SetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CommandListActionTest extends BaseFracktailTest {
    private CommandListAction commandListAction;

    private final Command lockedCommand = new Command.Builder("locked")
            .withName("lock")
            .withAction(new CompositeAction(ctx -> Mono.just(false), CommandAction.NOOP))
            .build();

    @BeforeEach
    protected void parentSetup() {
        super.parentSetup();
        commandListAction = new CommandListAction();
    }

    @Test
    public void returnsListWhenRun() {
        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("Commands are: test"))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void returnsListWithAllNamesWhenRun() {
        when(command.getNames()).thenReturn(SetUtils.hashSet("test", "example"));

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("Commands are: example, test"))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void returnsEmptyListWhenNoCommandsWhenRun() {
        when(commandList.getCommands()).thenReturn(Collections.emptyList());

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("No commands are available."))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void returnsEmptyListWhenAllCommandsBlocked() {
        when(command.matches(any())).thenReturn(Mono.just(false));

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("No commands are available."))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void returnsListWithOneNameWhenOneBlocked() {
        when(commandList.getCommands()).thenReturn(Arrays.asList(command, lockedCommand));

        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(context.respond(eq("Commands are: test"))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}