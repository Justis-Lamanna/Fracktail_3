package com.github.lucbui.fracktail3.spring.plugin;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Plugins {
    private final List<Plugin> plugins;

    public Plugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public Optional<Plugin> getPlugin(Predicate<Plugin> predicate) {
        return plugins.stream()
                .filter(predicate)
                .findFirst();
    }
}
