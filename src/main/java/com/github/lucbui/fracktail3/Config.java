package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.twitch.config.TwitchConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Config {
    @Bean
    public TwitchConfig twitchConfig(@Value("${twitch.clientId}") String clientId,
                                     @Value("${twitch.secret}") String secret,
                                     @Value("${twitch.oauth}") String token) {
        return TwitchConfig.builder()
                .clientId(clientId)
                .clientSecret(secret)
                .oauth(token)
                .build();
    }
}
