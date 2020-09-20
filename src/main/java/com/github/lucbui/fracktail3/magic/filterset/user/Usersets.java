package com.github.lucbui.fracktail3.magic.filterset.user;

import com.github.lucbui.fracktail3.magic.filterset.ComplexFilterStore;

import java.util.Map;

/**
 * A mapping of all usersets in the bot.
 */
public class Usersets extends ComplexFilterStore<Userset> {
    public Usersets(Map<String, Userset> store) {
        super(store);
    }
}
