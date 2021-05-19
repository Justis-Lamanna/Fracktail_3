package com.github.lucbui.fracktail3.twitch.util.spring;

import com.github.lucbui.fracktail3.twitch.config.TwitchConfig;
import com.github.lucbui.fracktail3.twitch.platform.TwitchEventAdapter;
import com.github.lucbui.fracktail3.twitch.platform.TwitchHook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class TwitchSpringConfig {
    @Bean
    public TwitchConfig twitchConfig(@Value("${twitch.clientId}") String clientId,
                                     @Value("${twitch.secret}") String secret,
                                     @Value("${twitch.oauth}") String token,
                                     @Value("${twitch.autojoin}") List<String> channels,
                                     Map<String, TwitchEventAdapter> hooks) {
        return TwitchConfig.builder()
                .clientId(clientId)
                .clientSecret(secret)
                .oauth(token)
                .autojoinChannels(channels)
                .hooks(TwitchHook.fromMap(hooks))
                .build();
    }
}
