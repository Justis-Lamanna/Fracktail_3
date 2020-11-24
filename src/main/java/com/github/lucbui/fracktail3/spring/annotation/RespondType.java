package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import com.github.lucbui.fracktail3.spring.command.ReturnComponent;
import reactor.core.publisher.Mono;
import reactor.function.Function3;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;

public enum RespondType {
    DIRECT_MESSAGE(PlatformBaseContext::privateMessage, PlatformBaseContext::directMessage),
    INLINE(PlatformBaseContext::respond, PlatformBaseContext::respond);

    private final BiFunction<CommandUseContext<?>, String, Mono<Void>> stringHandler;
    private final Function3<CommandUseContext<?>, FormattedString, Map<String, Object>, Mono<Void>> fsHandler;

    RespondType(BiFunction<CommandUseContext<?>, String, Mono<Void>> stringHandler, Function3<CommandUseContext<?>, FormattedString, Map<String, Object>, Mono<Void>> fsHandler) {
        this.stringHandler = stringHandler;
        this.fsHandler = fsHandler;
    }

    public ReturnComponent.ReturnConverterFunction forString() {
        return (context, o) -> stringHandler.apply(context, (String) o);
    }

    public ReturnComponent.ReturnConverterFunction forFString() {
        return forFString(Collections.emptyMap());
    }

    public ReturnComponent.ReturnConverterFunction forFString(Map<String, Object> map) {
        return (context, o) -> fsHandler.apply(context, (FormattedString) o, map);
    }

    public BiFunction<CommandUseContext<?>, String, Mono<Void>> outputString() {
        return stringHandler;
    }

    public Function3<CommandUseContext<?>, FormattedString, Map<String, Object>, Mono<Void>> outputFString() {
        return fsHandler;
    }
}
