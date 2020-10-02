package com.github.lucbui.fracktail3.discord.event;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

public interface HasUser {
    Snowflake getUserId();
    Mono<User> getUser();
}
