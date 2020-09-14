package com.github.lucbui.fracktail3.magic.handlers.platform;

import com.github.lucbui.fracktail3.magic.config.Config;

public interface Platform<CONFIG extends Config> {
    String id();

    PlatformHandler platformHandler();
}
