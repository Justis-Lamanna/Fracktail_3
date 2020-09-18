package com.github.lucbui.fracktail3.magic.resolver;

import com.github.lucbui.fracktail3.magic.config.Config;

import java.util.*;

/**
 * Resolve, via I18N, into a comma-separated list of strings
 */
public class ListFromI18NResolver implements Resolver<List<String>> {
    private static final String DELIMITER = ",";

    private final String key;

    /**
     * Create a resolver with the given key
     * @param key The key to use
     */
    public ListFromI18NResolver(String key) {
        this.key = key;
    }

    /**
     * The I18N key
     * @return The key
     */
    public String getKey() {
        return key;
    }

    @Override
    public List<String> resolve(Config configuration, Locale locale) {
        Optional<String> base = configuration.getTextForKey(key, locale);
        if(base.isPresent()) {
            String[] values = base.get().split(DELIMITER);
            return Arrays.asList(values);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return "ListFromI18NResolver{" +
                "key='" + key + '\'' +
                '}';
    }
}
