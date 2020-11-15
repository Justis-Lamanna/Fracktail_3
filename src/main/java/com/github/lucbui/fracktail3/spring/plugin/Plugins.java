package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.spring.command.ParameterComponent;
import org.apache.commons.lang3.ClassUtils;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
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

    public ParameterComponent enhanceCompiledParameter(Object obj, Method method, Parameter parameter, ParameterComponent component) {
        ParameterComponent current = component;
        for(Plugin plugin : plugins) {
            if(plugin instanceof CompiledParameterPlugin) {
                current = ((CompiledParameterPlugin) plugin).enhanceCompiledParameter(obj, method, parameter, current);
            }
        }
        return current;
    }

    public Optional<ParameterComponent> createCompiledParameter(Object obj, Method method, Parameter parameter) {
        return plugins.stream()
                .flatMap(cast(CompiledParameterPlugin.class))
                .flatMap(p -> {
                    Result<ParameterComponent> component = p.createCompiledParameter(obj, method, parameter);
                    if(component.isResult()) {
                        return Stream.of(Tuples.of(p, component.getResult()));
                    } else {
                        return Stream.empty();
                    }
                })
                .max(Comparator.comparing(t -> t.getT1().getCPPPriority()))
                .map(Tuple2::getT2);
    }

    private <P> Function<? super Plugin, Stream<P>> cast(Class<P> clazz) {
        return obj -> clazz.isInstance(obj) ? Stream.of(clazz.cast(obj)) : Stream.empty();
    }
}
