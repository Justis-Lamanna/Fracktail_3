package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;

@Component
public class DiscordBotRunner implements CommandLineRunner {
    @Autowired
    private Bot bot;

    private Instant startTime;

    @Override
    public void run(String... args) throws Exception {
        bot.start().block();
    }

    @PostConstruct
    private void setStartTime() {
        startTime = Instant.now();
    }

    @PreDestroy
    public void destroy() throws Exception {
        bot.stop().block();
    }

    @Command
    public String uptime() {
        long elapsed = Duration.between(startTime, Instant.now()).toMillis();
        return "I have been alive for " + DurationFormatUtils.formatDurationWords(elapsed, true, false) + ".";
    }
}
