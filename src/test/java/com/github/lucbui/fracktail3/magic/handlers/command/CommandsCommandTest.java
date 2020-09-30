package com.github.lucbui.fracktail3.magic.handlers.command;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.TestContext;
import com.github.lucbui.fracktail3.magic.guards.Guard;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.action.CommandsAction;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandsCommandTest {
    @Spy private CommandContext context = new TestContext("!help");

    @Mock private Bot bot;
    @Mock private BotSpec spec;
    @Mock private BehaviorList behaviorList;
    @Mock private CommandList commandList;

    @Mock private Guard one;
    @Mock private Guard two;

    private Command command;
    private AutoCloseable mocks;

    private Command c1;
    private Command c2;

    @BeforeEach
    void setUp() {
        command = new Command("commands", new CommandsAction());
        context.setCommand(command);
        mocks = MockitoAnnotations.openMocks(this);
        c1 = new Command("one", one, Action.NOOP);
        c2 = new Command("two", two, Action.NOOP);

        when(bot.getSpec()).thenReturn(spec);
        when(spec.getCommandList()).thenReturn(commandList);
        when(behaviorList.getCommandList()).thenReturn(commandList);
        when(commandList.getCommands()).thenReturn(Arrays.asList(c1, c2));
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testRetrieveCommands_AllPermissible() {
        when(one.matches(any(), any())).thenReturn(Mono.just(true));
        when(two.matches(any(), any())).thenReturn(Mono.just(true));

        command.doAction(bot, context).block();

        verify(context).respond("Commands are: one, two");
    }

    @Test
    void testRetrieveCommands_OnePermissible() {
        when(one.matches(any(), any())).thenReturn(Mono.just(true));
        when(two.matches(any(), any())).thenReturn(Mono.just(false));

        command.doAction(bot, context).block();

        verify(context).respond("Commands are: one");
    }

    @Test
    void testRetrieveCommands_NonePermissible() {
        when(one.matches(any(), any())).thenReturn(Mono.just(false));
        when(two.matches(any(), any())).thenReturn(Mono.just(false));

        command.doAction(bot, context).block();

        verify(context).respond("You have access to no commands");
    }
}