package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.utils.MonoUtils;
import com.github.lucbui.fracktail3.magic.utils.Range;
import reactor.core.publisher.Mono;

public class BehaviorTrigger extends BaseTrigger {
    public static final BehaviorTrigger DEFAULT = new BehaviorTrigger(true,null, null);

    private final Range parameters;

    public BehaviorTrigger(boolean enabled, Range parameters, String role) {
        super(enabled, role);
        this.parameters = parameters;
    }

    public Range getParameters() {
        return parameters;
    }

    public Mono<Boolean> matches(Bot bot, CommandContext<?> context) {
        return MonoUtils.and(
                super.matches(bot, context),
                matchesParameters(context.getParameterCount()));
    }

    private Mono<Boolean> matchesParameters(int paramCount) {
        return parameters == null ? Mono.just(true) : Mono.just(parameters.isInside(paramCount));
    }
}
