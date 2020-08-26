package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class Command {
    @Bean
    public Action sleep() {
        return (bot, context) -> bot.restart(Duration.of(30, ChronoUnit.SECONDS)).then();
    }
}
