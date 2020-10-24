package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.MemberUpdateContext;
import reactor.core.publisher.Mono;

public interface MemberUpdateAction {
    Mono<Void> doAction(MemberUpdateContext ctx);
}