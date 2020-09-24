package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import reactor.core.publisher.Mono;

public class TranslatorFormatter implements ContextFormatter {
    private final String _default;

    public TranslatorFormatter(String _default) {
        this._default = _default;
    }

    public TranslatorFormatter() {
        this._default = null;
    }

    @Override
    public Mono<String> format(String raw, CommandContext ctx) {
        return Mono.just(ctx.translate(raw, _default == null ? raw : _default));
    }
}
