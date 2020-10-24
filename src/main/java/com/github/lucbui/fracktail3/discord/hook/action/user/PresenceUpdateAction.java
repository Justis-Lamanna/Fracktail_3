package com.github.lucbui.fracktail3.discord.hook.action.user;

import com.github.lucbui.fracktail3.discord.hook.context.user.PresenceUpdateContext;
import reactor.core.publisher.Mono;

public interface PresenceUpdateAction {
    Mono<Void> doAction(PresenceUpdateContext ctx);
}