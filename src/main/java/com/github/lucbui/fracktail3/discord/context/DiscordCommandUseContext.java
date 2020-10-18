package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import discord4j.core.event.domain.message.MessageCreateEvent;

public class DiscordCommandUseContext extends DiscordBaseContext<MessageCreateEvent> implements CommandUseContext<MessageCreateEvent> {
    private final Command command;
    private final String rawParameters;
    private final String[] parameters;

    public DiscordCommandUseContext(DiscordBaseContext<MessageCreateEvent> base, Command command, String rawParameters, String[] parameters) {
        super(base.getBot(), base.getPlatform(), base.getLocale(), base.getPayload());
        this.command = command;
        this.rawParameters = rawParameters;
        this.parameters = parameters;
    }

    @Override
    public Command getCommand() {
        return command;
    }

    @Override
    public String getRawParameters() {
        return rawParameters;
    }

    @Override
    public String[] getParameters() {
        return parameters;
    }
}
