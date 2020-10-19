package com.github.lucbui.fracktail3.magic.guard.user;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.util.IdStore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A mapping of all usersets in the bot.
 */
public class Usersets<T extends Userset> extends IdStore<T> implements Validated {
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
    public static <T extends Userset> Usersets<T> empty() {
        return new Usersets<>(Collections.emptyMap());
    }

    @Override
    public void validate(BotSpec spec) throws BotConfigurationException {
        getAll().forEach(userset -> Validated.validate(userset, spec));
    }
}
