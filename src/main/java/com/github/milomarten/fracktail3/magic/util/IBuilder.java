package com.github.milomarten.fracktail3.magic.util;

/**
 * Generic interface for any class following the Builder pattern
 * @param <BUILD> The type built
 */
public interface IBuilder<BUILD> {
    /**
     * Finish constructing the object being built
     * @return The constructed object
     */
    BUILD build();
}
