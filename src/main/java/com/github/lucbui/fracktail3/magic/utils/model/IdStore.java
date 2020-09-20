package com.github.lucbui.fracktail3.magic.utils.model;

import com.github.lucbui.fracktail3.magic.Id;

import java.util.*;
import java.util.stream.Collectors;

public class IdStore<ITEM extends Id>{
    private final Map<String, ITEM> store;

    public IdStore(Map<String, ITEM> store) {
        this.store = Collections.unmodifiableMap(store);
    }

    public IdStore(List<ITEM> items) {
        this(items.stream()
                .collect(Collectors.toMap(Id::getId, i -> i)));
    }

    public Optional<ITEM> getById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<ITEM> getAll() {
        return new ArrayList<>(store.values());
    }

    public int size() {
        return store.size();
    }
}
