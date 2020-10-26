package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.VoiceChannelCreateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface VoiceChannelCreateAction {
    Mono<Void> doAction(VoiceChannelCreateContext ctx);

	default Mono<Boolean> guard(VoiceChannelCreateContext ctx){ return Mono.just(true); }
}