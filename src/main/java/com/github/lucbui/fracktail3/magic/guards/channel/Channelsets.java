package com.github.lucbui.fracktail3.magic.guards.channel;

import com.github.lucbui.fracktail3.magic.utils.model.IdStore;

import java.util.Map;

/**
 * A mapping of all Channelsets in the bot
 */
public class Channelsets extends IdStore<Channelset> {
    public Channelsets(Map<String, Channelset> store) {
        super(store);
    }
}
