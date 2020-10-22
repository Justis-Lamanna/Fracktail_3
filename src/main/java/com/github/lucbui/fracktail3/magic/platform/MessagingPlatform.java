package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.magic.guard.channel.Channelset;
import reactor.core.publisher.Mono;

/**
 * A platform which supports general text messaging
 */
public interface MessagingPlatform {
    /**
     * Send a message to all channels in a channelset
     * @param channels The channels to send to
     * @param message The message to send
     * @return Asynchronous indication of completion
     */
    Mono<Void> message(Channelset channels, String message);
}
