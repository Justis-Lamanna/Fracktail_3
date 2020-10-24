package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.MemberLeaveContext;
import reactor.core.publisher.Mono;

public interface MemberLeaveAction {
    Mono<Void> doAction(MemberLeaveContext ctx);
}