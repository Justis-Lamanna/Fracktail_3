package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.VoiceChannelUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface VoiceChannelUpdateAction {
    Mono<Void> doAction(VoiceChannelUpdateContext ctx);

	default Mono<Boolean> guard(VoiceChannelUpdateContext ctx){ return Mono.just(true); }
}