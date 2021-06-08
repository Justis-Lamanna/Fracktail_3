package com.github.milomarten.fracktail3.twitch.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.milomarten.fracktail3.twitch.platform.TwitchHook;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class TwitchConfig {
    private final String clientId;
    @JsonIgnore private final String clientSecret;
    @JsonIgnore private final String oauth;
    @Builder.Default private final String prefix = "!";
    @Singular private final List<String> autojoinChannels;
    @Singular private final List<TwitchHook> hooks;
}
