package com.github.lucbui.fracktail3.magic.util;

import com.github.lucbui.fracktail3.magic.Id;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.multiset.HashMultiSet;

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
     * Add items to the store.
     * If any items have a non-unique ID, an error is thrown, and none of the batch are added.
     * @param items The items to add
     */
    public void addAll(Collection<ITEM> items) {
        Objects.requireNonNull(items);
        items.forEach(Objects::requireNonNull);
        //Check all items are unique w.r.t each other.
        MultiSet<String> ids = items.stream().map(Id::getId).collect(Collectors.toCollection(HashMultiSet::new));
        Set<String> nonUniqueIds = ids.stream().filter(s -> ids.getCount(s) > 1).collect(Collectors.toSet());
        if(!nonUniqueIds.isEmpty()) {
            throw new IllegalArgumentException("Duplicate IDs: " + String.join(",", nonUniqueIds));
        }
        //Check all items are unique w.r.t the store.
        Set<String> common = SetUtils.intersection(ids.uniqueSet(), store.keySet()).toSet();
        if(!common.isEmpty()) {
            throw new IllegalArgumentException("ID matches one already in store: " + String.join(",", common));
        }

        items.forEach(i -> store.put(i.getId(), i));
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
