package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.AbstractAction;
import com.github.lucbui.fracktail3.magic.handlers.Parameters;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordContext;
import discord4j.core.object.reaction.ReactionEmoji;
import reactor.core.publisher.Mono;

public class RespondAction extends AbstractAction {
    @Override
    public Mono<Void> doDiscordAction(Bot bot, DiscordContext ctx, Parameters params) {
        return ctx.getMessage().getMessage().addReaction(ReactionEmoji.unicode("\uD83C\uDF46"));
    }
}