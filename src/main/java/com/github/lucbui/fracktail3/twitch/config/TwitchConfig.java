package com.github.lucbui.fracktail3.twitch.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class TwitchConfig {
    private final String clientId;
    private final String clientSecret;
    private final String oauth;
    @Builder.Default private final String prefix = "!";
    @Singular private final List<String> autojoinChannels;
}
