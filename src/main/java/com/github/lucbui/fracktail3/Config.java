package com.github.lucbui.fracktail3;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class Config {
//    @Bean
//    public Platform discord(@Value("${discord.token}") String token, List<ReactiveEventAdapter> hooks) {
//        DiscordConfiguration configuration = DiscordConfiguration.builder()
//                .token(token)
//                .prefix("!")
//                .initialPresence(Presence.doNotDisturb(Activity.watching("you be such a cutie")))
//                .replyStyle(ReplyStyle.PLAIN)
//                .hooks(hooks)
//                .build();
//
//        return new DiscordPlatform(
//                configuration,
//                new BasicParameterParser()
//        );
//    }
}
