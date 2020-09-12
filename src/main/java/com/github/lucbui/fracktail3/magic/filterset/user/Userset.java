package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.filterset.AbstractComplexFilterSetValidator;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import reactor.core.publisher.Mono;

public class Userset extends AbstractComplexFilterSetValidator {
    private DiscordUserset discord;

    public Userset(String name, boolean blacklist, String extendsRoleset, DiscordUserset discord) {
        super(name, blacklist, extendsRoleset);
        this.discord = discord;
    }

    public Userset(String name, boolean blacklist, String extendsRoleset) {
        super(name, blacklist, extendsRoleset);
    }

    public Userset(String name, DiscordUserset discord) {
        this(name, false, null, discord);
    }

    public DiscordUserset getDiscord() {
        return discord;
    }

    public void setDiscord(DiscordUserset discord) {
        this.discord = discord;
    }

    @Override
    protected Mono<Boolean> validateInUnknownRole(BotSpec botSpec, CommandContext ctx) {
        return Mono.just(false);
    }

    @Override
    protected Mono<Boolean> validateInDiscordRole(BotSpec botSpec, DiscordContext ctx) {
        return discord.validate(botSpec, ctx);
    }
}
