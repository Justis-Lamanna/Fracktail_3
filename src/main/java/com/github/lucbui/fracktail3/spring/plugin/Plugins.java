package com.github.lucbui.fracktail3.spring.plugin;

import org.apache.commons.lang3.ClassUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Plugins {
    private final List<Plugin> plugins;

    public Plugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    public Plugins() {
        this(new ArrayList<>());
    }

    public void addPlugin(Plugin plugin) {
        this.plugins.add(plugin);
    }

    public List<Plugin> getPlugins() {
        return Collections.unmodifiableList(plugins);
    }

    private <P extends Plugin> List<P> getPlugins(Class<P> clazz) {
        return plugins.stream()
                .filter(p -> ClassUtils.isAssignable(p.getClass(), clazz))
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    private <P extends Plugin, T> T iterateUntilResult(Class<P> clazz, Function<P, Result<T>> func, T defValue) {
        for(P p : getPlugins(clazz)) {
            Result<T> res = func.apply(p);
            if(res.isResult()) {
                return res.getResult();
            }
        }
        return defValue;
    }

    private <O, P extends Plugin, T> T iterateUntilResult(O obj, Class<P> clazz, BiFunction<P, O, Result<T>> func, T defValue) {
        for(P p : getPlugins(clazz)) {
            Result<T> res = func.apply(p, obj);
            if(res.isResult()) {
                return res.getResult();
            }
        }
        return defValue;
    }
}
