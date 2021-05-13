package com.github.lucbui.fracktail3.discord.util;

import reactor.core.Disposable;

import java.util.HashMap;

/**
 * Utility map that ensures removed Disposables are disposed of properly
 * @param <K>
 */
public class Disposables<K> extends HashMap<K, Disposable> {
    @Override
    public Disposable put(K key, Disposable value) {
        if(containsKey(key)) {
            return remove(key);
        }
        return super.put(key, value);
    }

    @Override
    public Disposable remove(Object key) {
        Disposable d = super.remove(key);
        if(d != null && !d.isDisposed()) {
            d.dispose();
        }
        return d;
    }

}
