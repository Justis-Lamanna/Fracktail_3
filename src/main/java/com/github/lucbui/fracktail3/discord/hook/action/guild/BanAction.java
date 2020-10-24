package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.BanContext;
import reactor.core.publisher.Mono;

public interface BanAction {
    Mono<Void> doAction(BanContext ctx);
}