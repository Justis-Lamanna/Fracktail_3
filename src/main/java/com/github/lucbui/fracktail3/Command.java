package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.handlers.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Command {
    @Bean
    public Behavior sleep() {
        return new Behavior(
                Range.unbounded(),
                (bot, context) -> bot.stop().delayElement(Duration.ofSeconds(30)).then(bot.start()).then(),
                null);
    }
}
