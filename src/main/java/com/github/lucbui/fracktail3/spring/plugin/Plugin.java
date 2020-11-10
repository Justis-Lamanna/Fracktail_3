package com.github.lucbui.fracktail3.spring.plugin;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.command.action.PlatformBasicAction;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.ParameterComponent;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.function.BiFunction;

public interface Plugin {
    default boolean canHandleParameterCompile(Object obj, Method method, Parameter parameter) {
        return false;
    }

    default ParameterComponent handleParameterCompile(Object obj, Method method, Parameter parameter) {
        return null;
    }

    default boolean canHandleReturnCompile(Object obj, Method method) {
        return false;
    }

    default BiFunction<CommandUseContext<?>, Object, Mono<Void>> handleReturnCompile(Object obj, Method method) {
        return null;
    }

    default boolean canProcessCommandBean(Command command) {
        return false;
    }

    default Command processCommandBean(Command command) {
        return null;
    }

    default boolean canProcessActionBean(String id, CommandAction action) {
        return false;
    }

    default Command processActionBean(String id, CommandAction action) {
        return null;
    }

    default boolean canProcessActionBean(String id, PlatformBasicAction action) {
        return false;
    }

    default Command processActionBean(String id, PlatformBasicAction action) {
        return null;
    }

    default boolean canProcessMethodCommand(Command.Builder builder, Object obj, Method method) {
        return false;
    }

    default void processMethodCommand(Command.Builder builder, Object obj, Method method) {

    }
}
