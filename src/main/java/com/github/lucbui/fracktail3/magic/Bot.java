package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.config.GlobalConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDConfiguration;
import com.github.lucbui.fracktail3.xsd.DTDDiscordConfiguration;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Bot {
    GlobalConfiguration globalConfig;
    DiscordConfiguration discordConfig;

    public Optional<GlobalConfiguration> getGlobalConfiguration() {
        return Optional.of(globalConfig);
    }

    public void setGlobalConfig(@Nullable GlobalConfiguration globalConfig) {
        this.globalConfig = globalConfig;
    }

    public Optional<GlobalConfiguration> getDiscordConfiguration() {
        return Optional.of(discordConfig);
    }

    public void setDiscordConfig(@Nullable DiscordConfiguration discordConfig) {
        this.discordConfig = discordConfig;
    }

    public Mono<Boolean> start() {
        List<Mono<Boolean>> starters = new ArrayList<>();
        if(discordConfig != null) {
            DiscordClient discordClient = new DiscordClientBuilder(discordConfig.getToken())
                    .build();

            starters.add(discordClient.login().thenReturn(true));
        }
        if(starters.isEmpty()) {
            throw new IllegalArgumentException("No specific Bot Configurations specified.");
        }
        return Flux.merge(starters)
                .then(Mono.just(true));
    }
}
