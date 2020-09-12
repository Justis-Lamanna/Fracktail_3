package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.filterset.user.Userset;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.utils.MonoUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

public abstract class AbstractComplexFilterSetValidator extends AbstractFilterSetValidator {
    private final String name;
    private final boolean blacklist;
    private final String extendsRoleset;

    public AbstractComplexFilterSetValidator(String name, boolean blacklist, String extendsRoleset) {
        this.name = name;
        this.blacklist = blacklist;
        this.extendsRoleset = extendsRoleset;
    }

    @Override
    public Mono<Boolean> validateInRole(BotSpec botSpec, CommandContext ctx) {
        Mono<Boolean> matches = super.validateInRole(botSpec, ctx);

        if(blacklist) {
            matches = MonoUtils.not(matches);
        }

        if(StringUtils.isNotBlank(extendsRoleset)) {
            Userset extension = botSpec.getUserset(extendsRoleset)
                    .orElseThrow(() -> new CommandUseException("Somehow, roleset stuff failed?"));
            return MonoUtils.and(extension.validateInRole(botSpec, ctx), matches);
        }

        return matches;
    }

    public String getName() {
        return name;
    }

    public boolean isBlacklist() {
        return blacklist;
    }

    public String getExtends() {
        return extendsRoleset;
    }
}
