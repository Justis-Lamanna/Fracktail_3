package com.github.lucbui.fracktail3.discord.hook.action.role;

import com.github.lucbui.fracktail3.discord.hook.context.role.RoleDeleteContext;
import reactor.core.publisher.Mono;

public interface RoleDeleteAction {
    Mono<Void> doAction(RoleDeleteContext ctx);
}