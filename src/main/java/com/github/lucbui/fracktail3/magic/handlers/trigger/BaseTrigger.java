package com.github.lucbui.fracktail3.magic.handlers.trigger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.role.Userset;
import com.github.lucbui.fracktail3.magic.utils.MonoUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

public abstract class BaseTrigger {
    private boolean enabled;
    private final String role;

    public BaseTrigger(boolean enabled, String role) {
        this.enabled = enabled;
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    @JsonIgnore
    public boolean hasRole() {
        return StringUtils.isNotEmpty(role);
    }

    public Mono<Boolean> matches(Bot bot, CommandContext<?> context) {
        return MonoUtils.and(
                Mono.just(enabled),
                matchesRoles(bot.getSpec(), context)
        );
    }

    private Mono<Boolean> matchesRoles(BotSpec spec, CommandContext<?> context) {
        if(!hasRole()) {
            return Mono.just(true);
        }
        Userset userset = spec.getUserset(role)
                .orElseThrow(() -> new CommandUseException("Unknown Roleset: " + role));
        return userset.validateInRole(spec, context);
    }
}
