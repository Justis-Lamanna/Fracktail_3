package com.github.lucbui.fracktail3.discord.hook.action.role;

import com.github.lucbui.fracktail3.discord.hook.context.role.RoleUpdateContext;
import reactor.core.publisher.Mono;

public interface RoleUpdateAction {
    Mono<Void> doAction(RoleUpdateContext ctx);
}