package com.github.lucbui.fracktail3.discord.util.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import discord4j.common.util.Snowflake;
import discord4j.core.event.ReactiveEventAdapter;
import org.springframework.stereotype.Component;

@Component
public class DiscordJacksonModule extends SimpleModule {
    private static final Version VERSION = new Version(0, 1, 0,
            null, "com.github.lucbui", "fracktail3");

    public DiscordJacksonModule() {
        super("Discord", VERSION);
        setMixInAnnotation(Snowflake.class, SnowflakeMixin.class);
        setMixInAnnotation(ReactiveEventAdapter.class, ReactiveEventAdapterMixin.class);
    }
}
