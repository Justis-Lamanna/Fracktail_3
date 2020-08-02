package com.github.lucbui.fracktail3.magic.resolver;

import com.github.lucbui.fracktail3.magic.config.Config;

import java.util.*;

public class ListFromI18NResolver implements Resolver<List<String>> {
    private static final String DELIMITER = ",";
    private final String key;

    public ListFromI18NResolver(String key) {
        this.key = key;
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
