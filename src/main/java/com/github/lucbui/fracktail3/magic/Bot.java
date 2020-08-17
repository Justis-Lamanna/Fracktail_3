package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.config.GlobalConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.discord.DiscordHandler;
import com.github.lucbui.fracktail3.magic.role.Rolesets;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private GlobalConfiguration globalConfig;
    private DiscordConfiguration discordConfig;
    private DiscordHandler discordHandler;
    private Rolesets rolesets;

    public Optional<GlobalConfiguration> getGlobalConfiguration() {
        return Optional.ofNullable(globalConfig);
    }

    public void setGlobalConfig(@Nullable GlobalConfiguration globalConfig) {
        this.globalConfig = globalConfig;
    }

    public Optional<DiscordConfiguration> getDiscordConfiguration() {
        return Optional.ofNullable(discordConfig);
    }

    public void setDiscordConfig(@Nullable DiscordConfiguration discordConfig) {
        this.discordConfig = discordConfig;
    }

    public Optional<DiscordHandler> getDiscordHandler() {
        return Optional.ofNullable(discordHandler);
    }

    public void setDiscordHandler(@Nullable DiscordHandler discordHandler) {
        this.discordHandler = discordHandler;
    }

    public Optional<Rolesets> getRolesets() {
        return Optional.ofNullable(rolesets);
    }

    public void setRolesets(@Nullable Rolesets rolesets) {
        this.rolesets = rolesets;
    }

    public Mono<Boolean> start() {
        List<Mono<Boolean>> starters = new ArrayList<>();
        if(discordConfig != null) {
            DiscordClient discordClient = new DiscordClientBuilder(discordConfig.getToken())
                    .setInitialPresence(discordConfig.getPresence())
                    .build();

            discordClient.getEventDispatcher().on(MessageCreateEvent.class)
                    .doOnNext(msg -> LOGGER.debug("Received a message: {}", msg.getMessage()))
                    .flatMap(msg -> discordHandler.execute(this, this.discordConfig, msg))
                    .subscribe();

            starters.add(discordClient.login().thenReturn(true));
        }
        if(starters.isEmpty()) {
            throw new BotConfigurationException("No specific Bot Configurations specified.");
        }
        return Flux.merge(starters)
                .then(Mono.just(true));
    }
}
