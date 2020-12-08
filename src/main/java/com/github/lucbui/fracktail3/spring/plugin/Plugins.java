package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.magic.command.Command;
import org.apache.commons.lang3.ClassUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public void onCommandAdd(Command c) {
        plugins.stream().flatMap(cast(CommandPlugin.class)).forEach(p -> p.onCommandAdd(c));
    }

    public void onCommandMerge(Command old, Command nuu) {
        plugins.stream().flatMap(cast(CommandPlugin.class)).forEach(p -> p.onCommandMerge(old, nuu));
    }

    private <P> Function<? super Plugin, Stream<P>> cast(Class<P> clazz) {
        return obj -> clazz.isInstance(obj) ? Stream.of(clazz.cast(obj)) : Stream.empty();
    }
}
