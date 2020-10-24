package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.MemberChunkContext;
import reactor.core.publisher.Mono;

public interface MemberChunkAction {
    Mono<Void> doAction(MemberChunkContext ctx);
}