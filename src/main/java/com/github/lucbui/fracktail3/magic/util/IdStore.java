package com.github.lucbui.fracktail3.magic.util;

import com.github.lucbui.fracktail3.magic.Id;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Collection of objects that can be retrieved by their ID
 * @param <ITEM> The item contained in the store
 */
public class IdStore<ITEM extends Id>{
    private final Map<String, ITEM> store;

    /**
     * Initialize a store using a Map of ID-ITEM pairs
     * @param store The store state
     */
    public IdStore(Map<String, ITEM> store) {
        this.store = Collections.unmodifiableMap(store);
    }

    /**
     * Initialize a store using a list of ITEMs
     * @param items The items to retrieve
     */
    public IdStore(List<ITEM> items) {
        this(items.stream()
                .collect(Collectors.toMap(Id::getId, i -> i)));
    }

    /**
     * Get an ITEM by its ID
     * @param id The ID to lookup
     * @return The ITEM found, or empty() if none were found
     */
    public Optional<ITEM> getById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Get all ITEMs in the store
     * @return All ITEMs, in no particular order
     */
    public List<ITEM> getAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * Get the size of the store
     * @return The store size
     */
    public int size() {
        return store.size();
    }
}
