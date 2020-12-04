package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.platform.Platform;

public interface PlatformBaseContext<T> extends BaseContext<T> {
    Platform getPlatform();
}
