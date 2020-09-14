package com.github.lucbui.fracktail3.magic.handlers.filter;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.Validated;
import reactor.core.publisher.Mono;

public class BaseFilter implements Validated {
    public static final BaseFilter DEFAULT = new BaseFilter(true);

    private boolean enabled;

    public BaseFilter(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Mono<Boolean> matches(Bot bot, CommandContext ctx) {
        return Mono.just(enabled);
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        //NOOP for now
    }
}
