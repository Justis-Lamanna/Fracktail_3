package com.github.lucbui.fracktail3.magic.handlers.filter;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.FilterSetValidator;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.Validated;
import com.github.lucbui.fracktail3.magic.utils.MonoUtils;
import reactor.core.publisher.Mono;

public class BaseFilter implements Validated, Filter {
    public static final BaseFilter DEFAULT = new BaseFilter(true);

    private boolean enabled;
    private FilterSetValidator setValidator = FilterSetValidator.identity(true);

    public BaseFilter(boolean enabled) {
        this.enabled = enabled;
    }

    public BaseFilter(boolean enabled, FilterSetValidator setValidator) {
        this.enabled = enabled;
        this.setValidator = setValidator;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public FilterSetValidator getSetValidator() {
        return setValidator;
    }

    public void setSetValidator(FilterSetValidator setValidator) {
        this.setValidator = setValidator;
    }

    @Override
    public Mono<Boolean> matches(Bot bot, CommandContext ctx) {
        return MonoUtils.and(Mono.just(enabled), setValidator.validate(bot.getSpec(), ctx));
    }

    @Override
    public void validate(BotSpec botSpec) throws BotConfigurationException {
        //NOOP for now
    }
}
