package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.Parameters;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvent;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public abstract class BaseFracktailTest {
    @Mock
    protected CommandUseContext<?> context;

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
    protected Command command;

    @Mock
    protected CommandAction commandAction;

    @Mock
    protected ScheduledEvent scheduledEvent;

    @Mock
    protected Parameters parameters;

    private AutoCloseable mocks;

    protected void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        mock();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        mocks.close();
    }

    private void mock() {
        when(command.getAction()).thenReturn(commandAction);
        when(commandAction.guard(any())).thenReturn(Mono.just(true));
        command.setEnabled(true);

        when(context.getBot()).thenReturn(bot);
        when(context.getPlatform()).thenReturn(platform);
        when(context.getParameters()).thenReturn(parameters);
        when(context.getCommand()).thenReturn(command);
        when(context.getLocale()).thenReturn(Mono.just(Locale.ENGLISH));

        when(context.respond(anyString())).thenReturn(Mono.empty());
        when(context.respond(any(FormattedString.class))).thenCallRealMethod();
        when(context.respond(any(FormattedString.class), anyMap())).thenCallRealMethod();

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

        when(scheduledEvents.getAll()).thenReturn(Collections.singletonList(scheduledEvent));
        when(scheduledEvents.getById(anyString())).thenReturn(Optional.of(scheduledEvent));
        when(scheduledEvents.size()).thenReturn(1);
    }
}
