package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.exception.CommandUseException;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import com.github.lucbui.fracktail3.magic.utils.MonoUtils;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

public class Userset extends AbstractUsersetValidator {
    private final String name;
    private final boolean blacklist;
    private final String extendsRoleset;
    private DiscordRolesetValidator discordRolesetValidator;

    public Userset(String name, boolean blacklist, String extendsRoleset, DiscordRolesetValidator discordRolesetValidator) {
        this.name = name;
        this.blacklist = blacklist;
        this.extendsRoleset = extendsRoleset;
        this.discordRolesetValidator = discordRolesetValidator;
    }

    public Userset(String name, boolean blacklist, String extendsRoleset) {
        this.name = name;
        this.blacklist = blacklist;
        this.extendsRoleset = extendsRoleset;
    }

    public Userset(String name, DiscordRolesetValidator discordRolesetValidator) {
        this(name, false, null, discordRolesetValidator);
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

    public DiscordRolesetValidator getDiscordRolesetValidator() {
        return discordRolesetValidator;
    }

    public void setDiscordRolesetValidator(DiscordRolesetValidator discordRolesetValidator) {
        this.discordRolesetValidator = discordRolesetValidator;
    }

    @Override
    public Mono<Boolean> validateInRole(BotSpec botSpec, CommandContext<?> ctx) {
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

    @Override
    protected Mono<Boolean> validateInUnknownRole(BotSpec botSpec, CommandContext<?> ctx) {
        return Mono.just(false);
    }

    @Override
    protected Mono<Boolean> validateInDiscordRole(BotSpec botSpec, DiscordContext ctx) {
        return discordRolesetValidator.validateInDiscordRole(botSpec, ctx);
    }
}
