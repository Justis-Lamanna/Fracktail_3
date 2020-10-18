package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.Platform;

import java.util.Locale;

public interface BaseContext<T> {
    Bot getBot();
    Platform getPlatform();
    T getPayload();
    Locale getLocale();
}
