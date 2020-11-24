package com.github.lucbui.fracktail3.spring.annotation;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import com.github.lucbui.fracktail3.spring.command.ReturnComponent;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

public enum RespondType {
    DIRECT_MESSAGE(PlatformBaseContext::directMessage, PlatformBaseContext::directMessage),
    INLINE(PlatformBaseContext::respond, PlatformBaseContext::respond);

    private final BiFunction<CommandUseContext<?>, String, Mono<Void>> stringHandler;
    private final BiFunction<CommandUseContext<?>, FormattedString, Mono<Void>> fsHandler;

    RespondType(BiFunction<CommandUseContext<?>, String, Mono<Void>> stringHandler, BiFunction<CommandUseContext<?>, FormattedString, Mono<Void>> fsHandler) {
        this.stringHandler = stringHandler;
        this.fsHandler = fsHandler;
    }

    public ReturnComponent.ReturnConverterFunction forString() {
        return (context, o) -> stringHandler.apply(context, (String) o);
    }

    public ReturnComponent.ReturnConverterFunction forFString() {
        return (context, o) -> fsHandler.apply(context, (FormattedString) o);
    }
}
