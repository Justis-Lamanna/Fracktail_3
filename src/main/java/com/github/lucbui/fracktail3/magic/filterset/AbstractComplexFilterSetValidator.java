package com.github.lucbui.fracktail3.magic.filterset;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.utils.MonoUtils;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * A specific type of Filter which allows for extension and negation
 */
public abstract class AbstractComplexFilterSetValidator implements Filter {
    private final String name;
    private final boolean blacklist;
    private final String extendsRoleset;

    /**
     * Create the filter
     * @param name The name of the filter
     * @param blacklist If true, negation is applied
     * @param extendsRoleset If non-null, [this validator] && [other validator] must be true to return true.
     */
    public AbstractComplexFilterSetValidator(String name, boolean blacklist, String extendsRoleset) {
        this.name = name;
        this.blacklist = blacklist;
        this.extendsRoleset = extendsRoleset;
    }

    @Override
    public Mono<Boolean> matches(Bot bot, CommandContext ctx) {
        Mono<Boolean> matches = matches2(bot, ctx);

        if(blacklist) {
            matches = MonoUtils.not(matches);
        }

        /*
        if(StringUtils.isNotBlank(extendsRoleset)) {
            Userset extension = bot.getSpec().getUserset(extendsRoleset)
                    .orElseThrow(() -> new CommandUseException("Somehow, roleset stuff failed?"));
            return MonoUtils.and(extension.matches(bot, ctx), matches);
        }
        */

        return matches;
    }

    /**
     * Get the name of this filter
     * @return The name of this filter
     */
    public String getName() {
        return name;
    }

    /**
     * Return if this is a blacklist or not
     * @return True if this is a blacklist filter
     */
    public boolean isBlacklist() {
        return blacklist;
    }

    /**
     * Return the extended filter, if applicable.
     * @return The extended role, or an empty Optional if does not extend.
     */
    public Optional<String> getExtends() {
        return Optional.ofNullable(extendsRoleset);
    }

    /**
     * Method called to determine the filter state, without considering complex common functionality
     * @param spec The spec of the bot
     * @param context The context of the command usage
     * @return Asynchronous boolean, true if the subfilter matches, false if not.
     */
    public abstract Mono<Boolean> matches2(Bot spec, CommandContext context);
}
