package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordContext;
import discord4j.core.object.util.Snowflake;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Set;

public class DiscordUserset {
    private Set<Snowflake> userSnowflakes;
    private Set<Snowflake> roleSnowflakes;

    public DiscordUserset() {
        userSnowflakes = null;
        roleSnowflakes = null;
    }

    public DiscordUserset(Set<Snowflake> userSnowflakes, Set<Snowflake> roleSnowflakes) {
        this.userSnowflakes = userSnowflakes;
        this.roleSnowflakes = roleSnowflakes;
    }

    public static DiscordUserset forUser(Snowflake user) {
        return new DiscordUserset(Collections.singleton(user), Collections.emptySet());
    }

    public static DiscordUserset forRole(Snowflake role) {
        return new DiscordUserset(Collections.emptySet(), Collections.singleton(role));
    }

    public Set<Snowflake> getUserSnowflakes() {
        return userSnowflakes;
    }

    public Set<Snowflake> getRoleSnowflakes() {
        return roleSnowflakes;
    }

    public void setUserSnowflakes(Set<Snowflake> userSnowflakes) {
        this.userSnowflakes = userSnowflakes;
    }

    public void setRoleSnowflakes(Set<Snowflake> roleSnowflakes) {
        this.roleSnowflakes = roleSnowflakes;
    }

    public Mono<Boolean> validateInDiscordRole(BotSpec botSpec, DiscordContext ctx) {
        if(userSnowflakes == null && roleSnowflakes == null) {
            return Mono.just(true);
        }

        if(ctx.isDm()) {
            return Mono.justOrEmpty(ctx.getEvent().getMessage().getAuthor())
                    .map(user -> isLegalUserId(user.getId()) && roleSnowflakes == null)
                    .defaultIfEmpty(false);
        } else {
            return Mono.justOrEmpty(ctx.getEvent().getMember())
                    .map(member -> isLegalUserId(member.getId()) && containsLegalRole(member.getRoleIds()))
                    .defaultIfEmpty(false);
        }
    }

    private boolean isLegalUserId(Snowflake userId) {
        return userSnowflakes == null || userSnowflakes.contains(userId);
    }

    private boolean containsLegalRole(Set<Snowflake> roles) {
        return roleSnowflakes == null || roles.containsAll(roleSnowflakes);
    }
}
