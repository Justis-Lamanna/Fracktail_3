package com.github.lucbui.fracktail3.magic.handlers.discord;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class CommandListDiscordHandler implements DiscordHandler {

    private final CommandList commandList;

    public CommandListDiscordHandler(CommandList commandList) {
        this.commandList = commandList;
    }

    @Override
    public Mono<Void> execute(Bot bot, MessageCreateEvent event) {
        return Mono.empty();
    }
}
