package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.Bot;

import java.util.Locale;

public interface BaseContext<T> {
    Bot getBot();
    Locale getLocale();
    T getPayload();
}
