package com.github.lucbui.fracktail3.magic.guard.user;

import com.github.lucbui.fracktail3.magic.Id;
import com.github.lucbui.fracktail3.magic.util.IdStore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A mapping of all usersets in the bot.
 */
public class Usersets<T extends Userset & Id> extends IdStore<T> {
    public Usersets(Map<String, T> store) {
        super(store);
    }
    public Usersets(List<T> store) {
        super(store);
    }

    /**
     * Create an empty userset store
     * @param <T> The type in the store
     * @return An empty userset.
     */
    public static <T extends Userset & Id> Usersets<T> empty() {
        return new Usersets<>(Collections.emptyMap());
    }
}
