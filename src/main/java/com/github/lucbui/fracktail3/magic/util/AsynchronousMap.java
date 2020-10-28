package com.github.lucbui.fracktail3.magic.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Wrapper to allow an asynchronous map to be used in Map contexts.
 * This "solves" the issue of allowing formatting to draw from asynchronous sources, without itself supporting it. Penalty
 * of resolving complex statements (like Guild Name) is only incurred when attempting to use them.
 * When a value is called using get(), the internal Mono is called and waited for (blocked).
 * @param <K> The type of key
 * @param <V> The type of value
 */
public class AsynchronousMap<K, V> implements Map<K, V> {
    Map<K, Mono<? extends V>> internal;

    public AsynchronousMap() {
        this.internal = new HashMap<>();
    }

    public AsynchronousMap(int size) {
        this.internal = new HashMap<>(size);
    }

    public AsynchronousMap(Map<K, V> map) {
        if(map instanceof AsynchronousMap) {
            this.internal = new HashMap<>(((AsynchronousMap<K, V>) map).internal);
        } else {
            this.internal = new HashMap<>(map.size());
            map.forEach((k, v) -> this.internal.put(k, Mono.just(v)));
        }
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return Flux.fromIterable(internal.values())
                .flatMap(m -> m)
                .cast(Object.class)
                .hasElement(value)
                .blockOptional()
                .orElse(false);
    }

    @Override
    public V get(Object key) {
        Mono<? extends V> value = internal.get(key);
        return value == null ? null : value.block();
    }

    public Mono<? extends V> getAsync(Object key) {
        return internal.get(key);
    }

    @Override
    public V put(K key, V value) {
        Mono<? extends V> old = internal.put(key, Mono.just(value));
        return old == null ? null : old.block();
    }

    public Mono<? extends V> putAsync(K key, Mono<? extends V> value) {
        return internal.put(key, value);
    }

    @Override
    public V remove(Object key) {
        Mono<? extends V> old = internal.remove(key);
        return old == null ? null : old.block();
    }

    public Mono<? extends V> removeAsync(Object key) {
        return internal.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        if(map instanceof AsynchronousMap) {
            internal.putAll(((AsynchronousMap<K, V>) map).internal);
        } else {
            map.forEach((k, v) -> this.internal.put(k, Mono.just(v)));
        }
    }

    @Override
    public void clear() {
        internal.clear();
    }

    @Override
    public Set<K> keySet() {
        return internal.keySet();
    }

    @Override
    public Collection<V> values() {
        return Flux.concat(internal.values())
                .collectList()
                .blockOptional()
                .orElse(Collections.emptyList());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Flux.fromIterable(internal.entrySet())
                .map(entry -> new IEntry<>(entry.getKey(), entry.getValue()))
                .map(e -> (Entry<K, V>)e)
                .collect(Collectors.toSet())
                .blockOptional().orElse(Collections.emptySet());
    }

    private static class IEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private final Mono<V> value;

        public IEntry(K key, Mono<V> value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value.block();
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }
}
