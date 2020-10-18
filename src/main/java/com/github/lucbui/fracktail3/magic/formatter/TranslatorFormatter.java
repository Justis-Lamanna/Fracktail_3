package com.github.lucbui.fracktail3.magic.formatter;

import com.github.lucbui.fracktail3.magic.Localizable;
import com.github.lucbui.fracktail3.magic.platform.context.BaseContext;
import reactor.core.publisher.Mono;

/**
 * Formatter which treats the input as a key to a ResourceBundle
 */
public class TranslatorFormatter implements ContextFormatter {
    private final String _default;

    /**
     * Initialize this formatter with a default string
     * The default string is returned when there is no ResourceBundle, or the key is not present in a bundle.
     * @param _default The default string
     */
    public TranslatorFormatter(String _default) {
        this._default = _default;
    }

    /**
     * Initialize this formatter
     * If no ResourceBundle is present, or the input key doesn't exist, the key is returned back.
     */
    public TranslatorFormatter() {
        this._default = null;
    }

    @Override
    public Mono<String> format(String raw, BaseContext<?> ctx) {
        if(ctx.getPlatform().getConfig() instanceof Localizable) {
            Localizable config = (Localizable) ctx.getPlatform().getConfig();
            return Mono.justOrEmpty(config.getBundleIfEnabled(ctx.getLocale()))
                    .filter(bundle -> bundle.containsKey(raw))
                    .map(bundle -> bundle.getString(raw))
                    .defaultIfEmpty(_default == null ? raw : _default);
        }
        return Mono.just(_default == null ? raw : _default);
    }
}
