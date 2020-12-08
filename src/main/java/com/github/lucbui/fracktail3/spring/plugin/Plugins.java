package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import com.github.lucbui.fracktail3.spring.command.model.ParameterComponent;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import org.apache.commons.lang3.ClassUtils;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.lang.reflect.Field;
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

    public Optional<ParameterComponent> createCompiledParameter(Object obj, Method method, Parameter parameter) {
        return plugins.stream()
                .flatMap(cast(CompiledMethodPlugin.class))
                .flatMap(p -> {
                    Result<ParameterComponent> component = p.createParameterComponent(obj, method, parameter);
                    if(component.isResult()) {
                        return Stream.of(Tuples.of(p, component.getResult()));
                    } else {
                        return Stream.empty();
                    }
                })
                .max(Comparator.comparing(t -> t.getT1().getPluginPriority()))
                .map(Tuple2::getT2);
    }

    public ParameterComponent enhanceCompiledParameter(Object obj, Method method, Parameter parameter, ParameterComponent component) {
        ParameterComponent current = component;
        for(Plugin plugin : plugins) {
            if(plugin instanceof CompiledMethodPlugin) {
                current = ((CompiledMethodPlugin) plugin).decorateParameterComponent(obj, method, parameter, current);
            }
        }
        return current;
    }

    public Optional<ReturnComponent> createCompiledReturn(Object obj, Method method) {
        return plugins.stream()
                .flatMap(cast(CompiledMethodPlugin.class))
                .flatMap(p -> {
                    Result<ReturnComponent> component = p.createReturnComponent(obj, method);
                    if(component.isResult()) {
                        return Stream.of(Tuples.of(p, component.getResult()));
                    } else {
                        return Stream.empty();
                    }
                })
                .max(Comparator.comparing(t -> t.getT1().getPluginPriority()))
                .map(Tuple2::getT2);
    }

    public Optional<ReturnComponent> createCompiledFieldReturn(Object obj, Field field) {
        return plugins.stream()
                .flatMap(cast(CompiledMethodPlugin.class))
                .flatMap(p -> {
                    Result<ReturnComponent> component = p.createFieldReturnComponent(obj, field);
                    if(component.isResult()) {
                        return Stream.of(Tuples.of(p, component.getResult()));
                    } else {
                        return Stream.empty();
                    }
                })
                .max(Comparator.comparing(t -> t.getT1().getPluginPriority()))
                .map(Tuple2::getT2);
    }

    public ReturnComponent enhanceCompiledReturn(Object obj, Method method, ReturnComponent component) {
        ReturnComponent current = component;
        for(Plugin plugin : plugins) {
            if(plugin instanceof CompiledMethodPlugin) {
                current = ((CompiledMethodPlugin) plugin).decorateReturnComponent(obj, method, current);
            }
        }
        return current;
    }

    public ReturnComponent enhanceCompiledFieldReturn(Object obj, Field field, ReturnComponent component) {
        ReturnComponent current = component;
        for(Plugin plugin : plugins) {
            if(plugin instanceof CompiledMethodPlugin) {
                current = ((CompiledMethodPlugin) plugin).decorateFieldReturnComponent(obj, field, current);
            }
        }
        return current;
    }

    public ExceptionComponent enhanceCompiledException(Object obj, Method method, ExceptionComponent component) {
        ExceptionComponent current = component;
        for(Plugin plugin : plugins) {
            if(plugin instanceof CompiledMethodPlugin) {
                current = ((CompiledMethodPlugin) plugin).decorateExceptionComponent(obj, method, current);
            }
        }
        return current;
    }

    private <P> Function<? super Plugin, Stream<P>> cast(Class<P> clazz) {
        return obj -> clazz.isInstance(obj) ? Stream.of(clazz.cast(obj)) : Stream.empty();
    }
}
