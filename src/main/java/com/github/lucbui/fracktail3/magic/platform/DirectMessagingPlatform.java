package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.magic.guard.user.Userset;
import reactor.core.publisher.Mono;

/**
 * A platform which supports direct text messaging
 */
public interface DirectMessagingPlatform {
    /**
     * Send a message directly to all users in the userset
     * @param userset The users to send to
     * @param message The message to send
     * @return Asynchronous indication of completion
     */
    Mono<Void> dm(Userset userset, String message);
}
