package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.MemberJoinContext;
import reactor.core.publisher.Mono;

public interface MemberJoinAction {
    Mono<Void> doAction(MemberJoinContext ctx);
}