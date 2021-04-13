package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.Parameters;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public abstract class BaseFracktailTest {
    @Mock
    protected CommandUseContext context;

    @Mock
    protected Platform platform;

    @Mock
    protected Bot bot;

    @Mock
    protected BotSpec botSpec;

    @Mock
    protected Scheduler scheduler;

    @Mock
    protected CommandList commandList;

    @Mock
    protected ScheduledEvents scheduledEvents;

    @Mock
    protected CommandAction commandAction;

    @Spy
    protected Command command = new Command.Builder("test")
            .withNames("test")
            .withHelp("test hello")
            .withAction(commandAction)
            .build();

    @Mock
    protected Parameters parameters;

    private AutoCloseable mocks;

    protected void parentSetup() {
        mocks = MockitoAnnotations.openMocks(this);
        mock();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        mocks.close();
    }

    private void mock() {
        when(context.getBot()).thenReturn(bot);
        when(context.getPlatform()).thenReturn(platform);
        when(context.getParameters()).thenReturn(parameters);
        when(context.getCommand()).thenReturn(command);

        when(bot.getSpec()).thenReturn(botSpec);
        when(bot.getPlatforms()).thenReturn(Collections.singletonList(platform));
        when(bot.getPlatform(anyString())).thenReturn(Optional.of(platform));
        when(bot.getScheduler()).thenReturn(scheduler);

        when(botSpec.getCommandList()).thenReturn(commandList);
        when(botSpec.getPlatforms()).thenReturn(Collections.singleton(platform));
        when(botSpec.getPlatform(anyString())).thenReturn(Optional.of(platform));
        when(botSpec.getScheduledEvents()).thenReturn(scheduledEvents);

        when(commandList.getCommands()).thenReturn(Collections.singletonList(command));
        when(commandList.getNumberOfCommands()).thenReturn(1);
        when(commandList.getCommandById(anyString())).thenReturn(Optional.of(command));
        when(commandList.getCommandsById()).thenReturn(Collections.singletonMap("test", command));

        when(scheduledEvents.getAll()).thenReturn(Collections.emptyList());
        when(scheduledEvents.getById(anyString())).thenReturn(Optional.empty());
        when(scheduledEvents.size()).thenReturn(0);
    }
}
