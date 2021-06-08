package com.github.milomarten.fracktail3.magic.platform;

import com.github.milomarten.fracktail3.discord.util.Disposables;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;
import java.util.Set;

public abstract class BasePlatform implements Platform, DisposableBean {
    private final Map<String, Disposable> subscriptionMap = new Disposables<>();

    public static final String EVERYTHING_ID = "*";
    public static final String MULTI_ID_DELIMITER = ";";
    public static final String URN_DELIMITER = ":";
    public static final String COMMAND_FEED = "_command-feed";

    protected void registerSubscription(String id, Disposable disposable) {
        this.subscriptionMap.put(id, disposable);
    }

    protected void clearSubscription(String id) {
        this.subscriptionMap.remove(id);
    }

    protected Set<String> getSubscriptions() {
        return subscriptionMap.keySet();
    }

    @Override
    public Mono<Person> getPerson(String id) {
        return Mono.defer(() -> {
            if(id.contains(MULTI_ID_DELIMITER)) {
                return getMultiPerson(id.split(MULTI_ID_DELIMITER));
            } else {
                return getSinglePerson(id);
            }
        }).defaultIfEmpty(NonePerson.INSTANCE).onErrorReturn(NonePerson.INSTANCE);
    }

    protected Mono<Person> getMultiPerson(String[] ids) {
        return Flux.fromArray(ids)
                .flatMap(this::getSinglePerson)
                .collectList()
                .map(MultiPerson::new);
    }

    protected Mono<Person> getSinglePerson(String id) {
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
                return getMultiPlace(id.split(MULTI_ID_DELIMITER));
            } else {
                return getSinglePlace(id);
            }
        }).defaultIfEmpty(NonePlace.INSTANCE).onErrorReturn(NonePlace.INSTANCE);
    }

    private Mono<Place> getMultiPlace(String[] ids) {
        return Flux.fromArray(ids)
                .flatMap(this::getSinglePlace)
                .collectList()
                .map(MultiPlace::new);
    }

    private Mono<Place> getSinglePlace(String id) {
        if(id.contains(URN_DELIMITER)) {
            return getPlaceByUri(URI.create(id));
        } else {
            return getPlaceByNonUri(id);
        }
    }

    protected abstract Mono<Place> getPlaceByNonUri(String id);

    protected abstract Mono<Place> getPlaceByUri(URI place);

    @Override
    public void destroy() throws Exception {
        LoggerFactory.getLogger(getClass()).info("Destroying open subscriptions for platform " + getId());
        this.subscriptionMap.clear();
    }
}
