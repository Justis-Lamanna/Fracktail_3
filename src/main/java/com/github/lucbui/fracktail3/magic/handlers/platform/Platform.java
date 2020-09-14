package com.github.lucbui.fracktail3.magic.handlers.platform;

import com.github.lucbui.fracktail3.magic.config.Config;

/**
 * An object which encapsulates a particular platform.
 * This acts as a further layer of abstracting than a PlatformHandler.
 * Platforms are now being used to identify where an event comes from,
 * rather than using the CommandContext's class.
 * @param <CONFIG> The type of configuration this platform uses.
 */
public interface Platform<CONFIG extends Config> {
    /**
     * The ID of this Platform
     * This ID should be unique between instances (although in most cases, this is
     * implemented as a singleton or enum). Platforms with the same ID are assumed to be the same.
     * @return The ID of the Platform
     */
    String id();

    /**
     * The class of the configuration object
     * @return The class of the configuration object
     */
    Class<CONFIG> getConfigClass();

    /**
     * Get the platform handler associated with this platform
     * @return The platform handler associated with this platform
     */
    PlatformHandler platformHandler();
}
