package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Mono;

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

    @Bean
    public Platform empty() {
        return new Platform() {
            @Override
            public Mono<Boolean> start(Bot bot) {
                return Mono.never();
            }

            @Override
            public Mono<Boolean> stop(Bot bot) {
                return Mono.never();
            }

            @Override
            public Mono<Person> getPerson(String id) {
                return Mono.just(NonePerson.INSTANCE);
            }

            @Override
            public Mono<Place> getPlace(String id) {
                return Mono.just(NonePlace.INSTANCE);
            }

            @Override
            public String getId() {
                return "empty";
            }
        };
    }
}
