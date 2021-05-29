package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.FailedSpecException;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * A generic spec that describes the creation or editing for an Editable item
 */
public class Spec {
    private final Map<String, Object> map = new HashMap<>();

    /**
     * Get a value from its key
     * @param key The key to get
     * @param clazz The class of the retrieved value
     * @param <T> The type to return
     * @return The value for that key, or null if not present
     */
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(this.map.get(key));
    }

    /**
     * Get a value from its key, throwing an exception if it is not present
     * @param key The key to look up
     * @param clazz The class of the retrieve value
     * @param <T> The type to return
     * @return The value for that key
     * @throws FailedSpecException Key is not present in the spec
     */
    public <T> T getRequired(String key, Class<T> clazz) {
        Object obj = this.map.get(key);
        if(obj == null) {
            throw new FailedSpecException(key + " was required, but not present");
        }
        return clazz.cast(obj);
    }

    /**
     * Get a value from its key, wrapped in an Optional
     * @param key The key to get
     * @param clazz The class of the retrieved value
     * @param <T> The type to return
     * @return The value for that key, or an empty Optional if not present/null
     */
    public <T> Optional<T> getOptional(String key, Class<T> clazz) {
        Object obj = this.map.get(key);
        if(obj == null) {
            return Optional.empty();
        } else {
            return Optional.of(clazz.cast(obj));
        }
    }

    /**
     * Set a key-value pair
     * @param key The key to set
     * @param value The value to set
     * @return The old value, if there, or null otherwise
     */
    public Object put(String key, Object value) {
        return this.map.put(key, value);
    }

    /**
     * Get all the keys in the spec
     * @return The set of keys in the spec
     */
    public Set<String> keys() {
        return this.map.keySet();
    }

    /**
     * Get all the values in the spec
     * @return The list values in the spec
     */
    public Collection<Object> values() {
        return this.map.values();
    }

    /**
     * Iterate through each key-value pair in the map
     * @param consumer The function to execute on each
     */
    public void forEach(BiConsumer<? super String, ? super Object> consumer) {
        this.map.forEach(consumer);
    }
}
