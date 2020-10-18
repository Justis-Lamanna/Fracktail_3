package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.guards.channel.Channelset;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

/**
 * Action which responds to the user of the command in some way
 */
public class RespondAction implements BaseAction, Action {
    private final Channelset channels;
    private final FormattedString msg;

    public RespondAction(Channelset channels, FormattedString msg) {
        this.channels = channels;
        this.msg = msg;
    }


    @Override
    public Mono<Void> doAction(CommandUseContext<?> context) {
        System.out.println(msg.getRaw());
        return Mono.empty();
    }

    @Override
    public Mono<Void> doAction(BaseContext<?> context) {
        System.out.println(msg.getRaw());
        return Mono.empty();
    }
}
