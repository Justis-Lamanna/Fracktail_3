package com.github.lucbui.fracktail3.discord.util.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import discord4j.common.util.Snowflake;

public abstract class SnowflakeMixin {
    @JsonValue
    long id;

    @JsonCreator
    public Snowflake value(long id){
        return Snowflake.of(id);
    }
}
