package com.github.lucbui.fracktail3.magic.platform;

import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.config.Config;

/**
 * An object which encapsulates a particular platform.
 * This acts as a further layer of abstracting than a PlatformHandler.
 * Platforms are now being used to identify where an event comes from,
 * rather than using the CommandContext's class.
 */
public interface Platform extends Id {
    /**
     * Get the configuration of this platform
     * @return The platform's configuration
     */
    Config getConfig();

    /**
     * Get the platform handler associated with this platform
     * @return The platform handler associated with this platform
     */
    PlatformHandler platformHandler();
}
