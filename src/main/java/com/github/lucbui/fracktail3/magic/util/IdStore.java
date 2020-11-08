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
        this.store = new HashMap<>(store);
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

    /**
     * Add item to the store. Throws an error if already exists.
     * @param item The item to add
     */
    public void add(ITEM item) {
        Objects.requireNonNull(item);
        if(store.containsKey(item.getId())) {
            throw new IllegalArgumentException("Item with ID " + item.getId() + " already exists in-store");
        }
        store.put(item.getId(), item);
    }

    /**
     * Adds item to the store, replacing if necessary
     * @param item The item to add
     * @return The old item, if there was any
     */
    public Optional<ITEM> replace(ITEM item) {
        Objects.requireNonNull(item);
        ITEM old = store.put(item.getId(), item);
        return Optional.ofNullable(old);
    }

    /**
     * Create an empty IdStore
     * @param <T> The type of store
     * @return An empty ID
     */
    public static <T extends Id> IdStore<T> emptyIdStore() {
        return new IdStore<T>(Collections.emptyMap());
    }
}
