package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.Parameters;
import discord4j.core.event.domain.message.MessageCreateEvent;

public class DiscordCommandUseContext extends DiscordCommandSearchContext implements CommandUseContext<MessageCreateEvent> {
    private final Command command;
    private final Parameters parameters;

    public DiscordCommandUseContext(DiscordBasePlatformContext<MessageCreateEvent> base, Command command, String rawParameters, String[] parameters) {
        super(base.getBot(), base.getPlatform(), base.getPayload());
        this.command = command;
        this.parameters = new Parameters(rawParameters, parameters);
    }

    @Override
    public Command getCommand() {
        return command;
    }

    @Override
    public Parameters getParameters() {
        return parameters;
    }
}
