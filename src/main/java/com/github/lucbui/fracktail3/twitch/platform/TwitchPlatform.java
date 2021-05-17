package com.github.lucbui.fracktail3.twitch.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.*;
import com.github.lucbui.fracktail3.twitch.config.TwitchConfig;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TwitchPlatform implements Platform {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitchPlatform.class);

    @Autowired
    private TwitchConfig config;

    private TwitchClient client;

    @Override
    public Object getConfiguration() {
        return config;
    }

    @Override
    public String getId() {
        return "twitch";
    }

    @Override
    public Mono<Boolean> start(Bot bot) {
        if(this.client != null) {
            throw new BotConfigurationException("Twitch already started");    
        }

        OAuth2Credential credential = new OAuth2Credential("twitch", config.getOauth());
        this.client = TwitchClientBuilder.builder()
                .withClientId(config.getClientId())
                .withClientSecret(config.getClientSecret())
                .withDefaultAuthToken(credential)
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .build();

        this.client.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            System.out.println("[" + event.getChannel().getName() + "]["+event.getPermissions().toString()+"] " + event.getUser().getName() + ": " + event.getMessage());
        });

        return Mono.fromRunnable(() -> this.client.getChat().connect()).thenReturn(true);
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        if(this.client == null) {
            throw new BotConfigurationException("Twitch not started");
        }
        return Mono.fromRunnable(() -> this.client.close()).thenReturn(true);
    }

    @Override
    public Mono<Person> getPerson(String id) {
        return Mono.just(NonePerson.INSTANCE);
    }

    @Override
    public Mono<Place> getPlace(String id) {
        return Mono.just(NonePlace.INSTANCE);
    }
}
