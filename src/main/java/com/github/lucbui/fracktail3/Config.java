package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfigurationBuilder;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHook;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHookStoreBuilder2;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.github.lucbui.fracktail3.spring.annotation.Parameter;
import com.github.lucbui.fracktail3.spring.plugin.Plugin;
import com.github.lucbui.fracktail3.spring.plugin.command.CommandLookupPlugin;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Config {
    @Bean
    public Platform discord(@Value("${token}") String token) {
        return new DiscordPlatform.Builder()
            .withConfiguration(new DiscordConfigurationBuilder(token)
            .withPrefix("!")
            .withOwner(248612704019808258L)
            .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
            .withHandlers(new DiscordEventHookStoreBuilder2()
                    .withHook(new DiscordEventHook<>("rer", new RerHook()))
            ))
            .build();
    }

    @Bean
    public Plugin clp() {
        return new CommandLookupPlugin();
    }

    @Command
    public String hello(@com.github.lucbui.fracktail3.spring.annotation.Platform DiscordPlatform platform, @Parameter(value = 0, optional = true) String value) {
        System.out.println(value);
        return "Hello, World!";
    }
}
