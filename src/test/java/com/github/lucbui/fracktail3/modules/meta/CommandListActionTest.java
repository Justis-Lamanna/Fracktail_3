package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.command.action.CompositeAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.Message;
import org.apache.commons.collections4.SetUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CommandListActionTest extends BaseFracktailTest {
    private CommandListAction commandListAction;

    private final Command lockedCommand = new Command.Builder("locked")
            .withName("lock")
            .withAction(new CompositeAction(ctx -> Mono.just(false), CommandAction.NOOP))
            .build();

    @Mock
    private Command lookupCommand;

    @BeforeEach
    protected void parentSetup() {
        super.parentSetup();
        commandListAction = new CommandListAction();

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
    public void returnsListWhenRun() {
        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("Commands are: test"))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void returnsListWithAllNamesWhenRun() {
        when(lookupCommand.getNames()).thenReturn(SetUtils.hashSet("test", "example"));

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("Commands are: example, test"))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void returnsEmptyListWhenNoCommandsWhenRun() {
        when(commandList.getCommands()).thenReturn(Collections.emptyList());

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("No commands are available."))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void returnsEmptyListWhenAllCommandsBlocked() {
        when(lookupCommand.matches(any())).thenReturn(Mono.just(false));

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("No commands are available."))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }

    @Test
    public void returnsListWithOneNameWhenOneBlocked() {
        when(commandList.getCommands()).thenReturn(Arrays.asList(lookupCommand, lockedCommand));

        PublisherProbe<Message> probe = PublisherProbe.empty();
        when(context.respond(eq("Commands are: test"))).thenReturn(probe.mono());

        StepVerifier.create(commandListAction.doAction(context)).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}