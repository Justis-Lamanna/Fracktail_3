package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfigurationBuilder;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHook;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHookStoreBuilder2;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.command.action.RespondingAction;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Config {
    @Value("${token}")
    private String token;

    @Bean
    public Platform discord() {
        DiscordEventHook<RerHook> rer = new DiscordEventHook<>("rer", new RerHook());
        return new DiscordPlatform.Builder()
            .withConfiguration(new DiscordConfigurationBuilder(token)
            .withPrefix("!")
            .withOwner(248612704019808258L)
            .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
            .withHandlers(new DiscordEventHookStoreBuilder2()
                    .withHook(rer)
            ))
            .build();
    }

    @Bean
    public RespondingAction hello() {
        return new RespondingAction("Hey there!!");
    }
}
