package com.github.lucbui.fracktail3.twitch.platform;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.*;
import com.github.lucbui.fracktail3.magic.platform.context.BasicContextConstructor;
import com.github.lucbui.fracktail3.magic.platform.context.ParameterParser;
import com.github.lucbui.fracktail3.twitch.config.TwitchConfig;
import com.github.lucbui.fracktail3.twitch.context.TwitchEverywhere;
import com.github.lucbui.fracktail3.twitch.context.TwitchPerson;
import com.github.lucbui.fracktail3.twitch.context.TwitchPlace;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.helix.domain.UserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Component
public class TwitchPlatform extends BasePlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitchPlatform.class);

    @Autowired
    private TwitchConfig config;

    @Autowired
    private ParameterParser parameterParser;

    private TwitchClient client;

    @Override
    public TwitchConfig getConfiguration() {
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

        Disposable d = SimpleTextCommandProcessor.listenForCommands(
                getEverywherePlace(), bot, this, config.getPrefix(), new BasicContextConstructor(parameterParser));
        registerSubscription(COMMAND_FEED, d);

        this.client.getChat().connect();
        config.getAutojoinChannels().forEach(channel -> this.client.getChat().joinChannel(channel));

        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        if(this.client == null) {
            throw new BotConfigurationException("Twitch not started");
        }
        return Mono.fromRunnable(() -> this.client.close()).thenReturn(true);
    }

    @Override
    protected Mono<Person> getPersonByNonUri(String id) {
        if(id.equals("self")) {
            return getSelf();
        }
        return getPersonByName(id);
    }

    @Override
    protected Mono<Person> getPersonByUri(URI person) {
        String[] args = person.getSchemeSpecificPart().split(URN_DELIMITER);
        switch (person.getScheme()) {
            case "user": return getPersonByName(args[0]);
            case "id": return getPersonById(args[0]);
            default: return Mono.error(
                    new IllegalArgumentException("Unknown person ID format " + person.getScheme()));
        }
    }

    public Mono<Person> getPersonById(String name) {
        CompletableFuture<UserList> future = CompletableFuture.supplyAsync(() -> client.getHelix().getUsers(null, Collections.singletonList(name), null).execute());
        return Mono.fromFuture(future)
                .map(UserList::getUsers)
                .filter(l -> l.size() > 0)
                .map(l -> l.get(0))
                .map(user -> new TwitchPerson(client, user))
                .cast(Person.class)
                .defaultIfEmpty(NonePerson.INSTANCE)
                .onErrorReturn(NonePerson.INSTANCE);
    }

    public Mono<Person> getPersonByName(String name) {
        CompletableFuture<UserList> future = CompletableFuture.supplyAsync(() -> client.getHelix().getUsers(null, null, Collections.singletonList(name)).execute());
        return Mono.fromFuture(future)
                .map(UserList::getUsers)
                .filter(l -> l.size() > 0)
                .map(l -> l.get(0))
                .map(user -> new TwitchPerson(client, user))
                .cast(Person.class)
                .defaultIfEmpty(NonePerson.INSTANCE)
                .onErrorReturn(NonePerson.INSTANCE);
    }

    public Mono<Person> getSelf() {
        CompletableFuture<UserList> future = CompletableFuture.supplyAsync(() -> client.getHelix().getUsers(null, null, null).execute());
        return Mono.fromFuture(future)
                .map(UserList::getUsers)
                .filter(l -> l.size() > 0)
                .map(l -> l.get(0))
                .map(user -> new TwitchPerson(client, user))
                .cast(Person.class)
                .defaultIfEmpty(NonePerson.INSTANCE)
                .onErrorReturn(NonePerson.INSTANCE);
    }

    @Override
    protected Mono<Place> getPlaceByNonUri(String id) {
        if(id.equals(EVERYTHING_ID)) {
            return getEverywherePlace();
        }
        return getPlaceByName(id);
    }

    @Override
    protected Mono<Place> getPlaceByUri(URI place) {
        String[] args = place.getSchemeSpecificPart().split(URN_DELIMITER);
        switch (place.getScheme()) {
            case "channel": return getPlaceByName(args[0]);
            default:
                return Mono.error(
                        new IllegalArgumentException("Unknown place ID format " + place.getScheme()));
        }
    }

    public Mono<Place> getEverywherePlace() {
        return Mono.just(new TwitchEverywhere(client));
    }

    public Mono<Place> getPlaceByName(String name) {
        CompletableFuture<UserList> future = CompletableFuture.supplyAsync(() -> client.getHelix().getUsers(null, null, Collections.singletonList(name)).execute());
        return Mono.fromFuture(future)
                .map(UserList::getUsers)
                .filter(l -> l.size() > 0)
                .map(l -> l.get(0))
                .map(user -> new TwitchPlace(client, new EventChannel(user.getId(), user.getDisplayName())))
                .cast(Place.class)
                .defaultIfEmpty(NonePlace.INSTANCE).onErrorReturn(NonePlace.INSTANCE);
    }
}
