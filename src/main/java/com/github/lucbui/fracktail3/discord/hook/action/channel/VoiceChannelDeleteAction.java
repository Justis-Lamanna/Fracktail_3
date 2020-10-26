package com.github.lucbui.fracktail3.discord.hook.action.channel;

import com.github.lucbui.fracktail3.discord.hook.context.channel.VoiceChannelDeleteContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface VoiceChannelDeleteAction {
    Mono<Void> doAction(VoiceChannelDeleteContext ctx);

	default Mono<Boolean> guard(VoiceChannelDeleteContext ctx){ return Mono.just(true); }
}