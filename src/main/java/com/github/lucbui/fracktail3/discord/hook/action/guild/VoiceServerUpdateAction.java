package com.github.lucbui.fracktail3.discord.hook.action.guild;

import com.github.lucbui.fracktail3.discord.hook.context.guild.VoiceServerUpdateContext;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface VoiceServerUpdateAction {
    Mono<Void> doAction(VoiceServerUpdateContext ctx);

	default Mono<Boolean> guard(VoiceServerUpdateContext ctx){ return Mono.just(true); }
}