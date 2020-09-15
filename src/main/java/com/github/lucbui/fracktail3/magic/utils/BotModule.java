package com.github.lucbui.fracktail3.magic.utils;

import com.fasterxml.jackson.databind.module.SimpleModule;
import discord4j.common.util.Snowflake;

public class BotModule extends SimpleModule {
    public BotModule() {
        addSerializer(Snowflake.class, new SnowflakeSerializer());
        addDeserializer(Snowflake.class, new SnowflakeDeserializer());
    }
}
