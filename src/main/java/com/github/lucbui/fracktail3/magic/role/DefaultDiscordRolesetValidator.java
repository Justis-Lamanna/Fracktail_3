package com.github.lucbui.fracktail3.magic.role;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import discord4j.core.object.util.Snowflake;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

public class DefaultDiscordRolesetValidator implements DiscordRolesetValidator {
    private Set<Snowflake> legalSnowflakes;
    private Set<Snowflake> legalRoles;

    public DefaultDiscordRolesetValidator() {
        legalSnowflakes = null;
        legalRoles = null;
    }

    public DefaultDiscordRolesetValidator(Set<Snowflake> legalSnowflakes, Set<Snowflake> legalRoles) {
        this.legalSnowflakes = legalSnowflakes;
        this.legalRoles = legalRoles;
    }

    public static DefaultDiscordRolesetValidator forUser(Snowflake user) {
        return new DefaultDiscordRolesetValidator(Collections.singleton(user), Collections.emptySet());
    }

    public static DefaultDiscordRolesetValidator forRole(Snowflake role) {
        return new DefaultDiscordRolesetValidator(Collections.emptySet(), Collections.singleton(role));
    }

    public Set<Snowflake> getLegalSnowflakes() {
        return legalSnowflakes;
    }

    public Set<Snowflake> getLegalRoles() {
        return legalRoles;
    }

    public void setLegalSnowflakes(Set<Snowflake> legalSnowflakes) {
        this.legalSnowflakes = legalSnowflakes;
    }

    public void setLegalRoles(Set<Snowflake> legalRoles) {
        this.legalRoles = legalRoles;
    }

    public Mono<Boolean> validateInDiscordRole(BotSpec botSpec, DiscordContext ctx) {
        if(CollectionUtils.isEmpty(legalSnowflakes) && CollectionUtils.isEmpty(legalRoles)) {
            return Mono.just(true);
        }

        if(ctx.isDm()) {
            return Mono.justOrEmpty(ctx.getEvent().getMessage().getAuthor())
                    .map(user -> isLegalUserId(user.getId()) && CollectionUtils.isEmpty(legalRoles))
                    .defaultIfEmpty(false);
        } else {
            return Mono.justOrEmpty(ctx.getEvent().getMember())
                    .map(member -> isLegalUserId(member.getId()) && containsLegalRole(member.getRoleIds()))
                    .defaultIfEmpty(false);
        }
    }

    private boolean isLegalUserId(Snowflake userId) {
        return CollectionUtils.isEmpty(legalSnowflakes) || legalSnowflakes.contains(userId);
    }

    private boolean containsLegalRole(Set<Snowflake> roles) {
        return CollectionUtils.isEmpty(legalRoles) || !SetUtils.intersection(legalRoles, roles).isEmpty();
    }
}
