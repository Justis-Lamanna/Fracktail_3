package com.github.lucbui.fracktail3.twitch.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TwitchConfig {
    private final String clientId;
    private final String clientSecret;
    private final String oauth;
}
