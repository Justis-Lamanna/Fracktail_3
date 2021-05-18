package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.discord.util.Disposables;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Set;

public abstract class BasePlatform implements Platform {
    private final Map<String, Disposable> subscriptionMap = new Disposables<>();

    public static final String MULTI_ID_DELIMITER = ";";
    public static final String URN_DELIMITER = ":";

    protected void registerSubscription(String id, Disposable disposable) {
        this.subscriptionMap.put(id, disposable);
    }

    protected void clearSubscription(String id) {
        this.subscriptionMap.remove(id);
    }

    protected void clearAllSubscriptions() {
        this.subscriptionMap.clear();
    }

    protected Set<String> getSubscriptions() {
        return subscriptionMap.keySet();
    }

    @Override
    public Mono<Person> getPerson(String id) {
        return Mono.defer(() -> {
            if(id.contains(MULTI_ID_DELIMITER)) {
                return Flux.fromArray(id.split(MULTI_ID_DELIMITER))
                        .flatMap(this::getInidividualPerson)
                        .collectList()
                        .map(MultiPerson::new);
            } else {
                return getInidividualPerson(id);
            }
        }).defaultIfEmpty(NonePerson.INSTANCE).onErrorReturn(NonePerson.INSTANCE);
    }

    private Mono<Person> getInidividualPerson(String id) {
        if(id.contains(URN_DELIMITER)) {
            return getPersonByUri(URI.create(id));
        } else {
            return getPersonByNonUri(id);
        }
    }

    protected abstract Mono<Person> getPersonByNonUri(String id);

    protected abstract Mono<Person> getPersonByUri(URI person);

    @Override
    public Mono<Place> getPlace(String id) {
        return Mono.defer(() -> {
            if(id.contains(MULTI_ID_DELIMITER)) {
                return Flux.fromArray(id.split(MULTI_ID_DELIMITER))
                        .flatMap(this::getInidividualPlace)
                        .collectList()
                        .map(MultiPlace::new);
            } else {
                return getInidividualPlace(id);
            }
        }).defaultIfEmpty(NonePlace.INSTANCE).onErrorReturn(NonePlace.INSTANCE);
    }

    private Mono<Place> getInidividualPlace(String id) {
        if(id.contains(URN_DELIMITER)) {
            return getPlaceByUri(URI.create(id));
        } else {
            return getPlaceByNonUri(id);
        }
    }

    protected abstract Mono<Place> getPlaceByNonUri(String id);

    protected abstract Mono<Place> getPlaceByUri(URI place);
}
