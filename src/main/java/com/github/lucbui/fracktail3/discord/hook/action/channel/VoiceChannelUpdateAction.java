package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.VoiceChannelUpdateContext;
import reactor.core.publisher.Mono;

public interface VoiceChannelUpdateAction {
    Mono<Void> doAction(VoiceChannelUpdateContext ctx);
}