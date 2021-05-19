package com.github.lucbui.fracktail3.twitch.platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lucbui.fracktail3.discord.exception.CancelHookException;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.platform.BasePlatform;
import com.github.lucbui.fracktail3.magic.platform.Person;
import com.github.lucbui.fracktail3.magic.platform.Place;
import com.github.lucbui.fracktail3.magic.platform.SimpleTextCommandProcessor;
import com.github.lucbui.fracktail3.magic.platform.context.BasicContextConstructor;
import com.github.lucbui.fracktail3.magic.platform.context.ParameterParser;
import com.github.lucbui.fracktail3.twitch.config.TwitchConfig;
import com.github.lucbui.fracktail3.twitch.context.TwitchEverywhere;
import com.github.lucbui.fracktail3.twitch.context.TwitchPerson;
import com.github.lucbui.fracktail3.twitch.context.TwitchPlace;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.reactor.ReactorEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class TwitchPlatform extends BasePlatform implements HealthIndicator, InfoContributor {
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
        LOGGER.info("Starting Twitch API Client");
        this.client = TwitchClientBuilder.builder()
                .withClientId(config.getClientId())
                .withClientSecret(config.getClientSecret())
                .withDefaultAuthToken(credential)
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .withDefaultEventHandler(ReactorEventHandler.class)
                .build();

        config.getHooks().forEach(hook -> registerHook(hook.getId(), hook.getHook()));

        Disposable d = SimpleTextCommandProcessor.listenForCommands(
                getEverywherePlace(), bot, this, config.getPrefix(), new BasicContextConstructor(parameterParser));
        registerSubscription(COMMAND_FEED, d);

        LOGGER.info("Starting Twitch Chat Client");
        this.client.getChat().connect();
        config.getAutojoinChannels().forEach(channel -> {
            LOGGER.info("Joining channel #{}", channel);
            this.client.getChat().joinChannel(channel);
        });
        this.client.getClientHelper().enableStreamEventListener(config.getAutojoinChannels());
        this.client.getClientHelper().enableFollowEventListener(config.getAutojoinChannels());

        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        if(this.client == null) {
            throw new BotConfigurationException("Twitch not started");
        }
        LOGGER.info("Stopping all Twitch Clients");
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
                .map(user -> new TwitchPerson(client, user));
    }

    public Mono<Person> getPersonByName(String name) {
        CompletableFuture<UserList> future = CompletableFuture.supplyAsync(() -> client.getHelix().getUsers(null, null, Collections.singletonList(name)).execute());
        return Mono.fromFuture(future)
                .map(UserList::getUsers)
                .filter(l -> l.size() > 0)
                .map(l -> l.get(0))
                .map(user -> new TwitchPerson(client, user));
    }

    @JsonIgnore
    public Mono<Person> getSelf() {
        CompletableFuture<UserList> future = CompletableFuture.supplyAsync(() -> client.getHelix().getUsers(null, null, null).execute());
        return Mono.fromFuture(future)
                .map(UserList::getUsers)
                .filter(l -> l.size() > 0)
                .map(l -> l.get(0))
                .map(user -> new TwitchPerson(client, user));
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

    @JsonIgnore
    public Mono<Place> getEverywherePlace() {
        return Mono.just(new TwitchEverywhere(client));
    }

    public Mono<Place> getPlaceByName(String name) {
        CompletableFuture<UserList> future = CompletableFuture.supplyAsync(() -> client.getHelix().getUsers(null, null, Collections.singletonList(name)).execute());
        return Mono.fromFuture(future)
                .map(UserList::getUsers)
                .filter(l -> l.size() > 0)
                .map(l -> l.get(0))
                .map(user -> new TwitchPlace(client, new EventChannel(user.getId(), user.getDisplayName())));
    }

    public void registerHook(String id, TwitchEventAdapter hook) {
        LOGGER.debug("Applying custom hook {}",id);
        Disposable d = Flux.push(sink -> client.getEventManager().onEvent(Object.class, sink::next))
                .flatMap(hook::onEvent)
                .doOnError(CancelHookException.class, ex -> deregisterHook(id))
                .subscribe();
        registerSubscription(id, d);
    }

    public void registerHook(TwitchEventAdapter hook) {
        registerHook(UUID.randomUUID().toString(), hook);
    }

    public void deregisterHook(String id) {
        LOGGER.debug("Removing custom hook {}",id);
        clearSubscription(id);
    }

    @Override
    public Health health() {
        if(client == null) {
            return Health.outOfService().withDetail("reason", "Not started").build();
        } else {
            try {
                UserList self = client.getHelix().getUsers(null, null, null).execute();
                if(self.getUsers().isEmpty()) {
                    return Health.unknown().withDetail("reason", "No Self?").build();
                }
                User user = self.getUsers().get(0);
                return Health.up()
                        .withDetail("id", user.getId())
                        .withDetail("name", user.getDisplayName())
                        .build();
            } catch (RuntimeException ex) {
                return Health.down()
                        .withDetail("reason", ex.getCause().getClass().getCanonicalName())
                        .withDetail("message", ex.getCause().getMessage())
                        .build();
            }
        }
    }

    @Override
    public void contribute(Info.Builder builder) {
        if(client != null) {
            try {
                UserList self = client.getHelix().getUsers(null, null, null).execute();
                if(!self.getUsers().isEmpty()) {
                    User user = self.getUsers().get(0);
                    Map<String, Object> twitchObj = new HashMap<>(3);
                    twitchObj.put("id", user.getId());
                    twitchObj.put("name", user.getDisplayName());
                    twitchObj.put("channels", client.getChat().getChannels());
                    builder.withDetail("twitch", twitchObj);
                }
            } catch (RuntimeException ex) { }
        }
    }
}
