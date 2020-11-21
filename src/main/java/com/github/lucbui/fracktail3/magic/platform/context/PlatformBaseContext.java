package com.github.lucbui.fracktail3.magic.platform.context;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface PlatformBaseContext<T> extends BaseContext<T> {
    Platform getPlatform();

    default Mono<Void> respond(FormattedString fString) {
        return fString.getFor(this)
                .flatMap(this::respond);
    }

    default Mono<Void> respond(FormattedString fString, Map<String, Object> addlVariables) {
        return fString.getFor(this, addlVariables)
                .flatMap(this::respond);
    }
}
