package com.github.lucbui.fracktail3.magic.guards.channel;

import com.github.lucbui.fracktail3.magic.utils.model.IdStore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A mapping of all Channelsets in the bot
 */
public class Channelsets<T extends Channelset> extends IdStore<T> {
    public Channelsets(Map<String, T> store) {
        super(store);
    }
    public Channelsets(List<T> store) { super(store); }

    /**
     * Create an empty channelset store
     * @param <T> The type of channelset
     * @return An empty channelsets
     */
    public static <T extends Channelset> Channelsets<T> empty() {
        return new Channelsets<>(Collections.emptyMap());
    }
}
