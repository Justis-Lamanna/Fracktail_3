package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.VoiceStateUpdateContext;
import reactor.core.publisher.Mono;

public interface VoiceStateUpdateAction {
    Mono<Void> doAction(VoiceStateUpdateContext ctx);
}