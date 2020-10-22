package com.github.lucbui.fracktail3.magic.command.action;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.guard.channel.Channelset;
import com.github.lucbui.fracktail3.magic.platform.MessagingPlatform;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

/**
 * An action which sends a message to all channels of a channelset.
 * The platform should implement "MessagingPlatform" to fully support this action.
 * To simply respond to an event, consider RespondingAction.
 */
public class MessageAction implements BaseAction {
    private final Channelset channelset;
    private final FormattedString text;

    /**
     * Initialize this action
     * @param channelset The channelset to send to
     * @param text The text to send
     */
    public MessageAction(Channelset channelset, FormattedString text) {
        this.channelset = channelset;
        this.text = text;
    }

    @Override
    public Mono<Void> doAction(BaseContext<?> context) {
        if(context.getPlatform() instanceof MessagingPlatform) {
            MessagingPlatform platform = (MessagingPlatform) context.getPlatform();
            return text.getFor(context)
                    .flatMap(s -> {
                        if(channelset != null) {
                            return platform.message(channelset, s);
                        }  else {
                            return Mono.empty();
                        }
                    });
        }
        return Mono.empty();
    }
}
