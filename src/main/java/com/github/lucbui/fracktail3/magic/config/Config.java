package com.github.lucbui.fracktail3.magic.config;

import com.github.lucbui.fracktail3.magic.guards.channel.Channelset;
import com.github.lucbui.fracktail3.magic.guards.user.Userset;

import java.util.Optional;

/**
 * Describes common behavior for a Config
 */
public interface Config {
    Optional<? extends Userset> getUserset(String id);
    Optional<? extends Channelset> getChannelset(String id);
}
