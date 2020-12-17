package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.discord.guard.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.spring.command.annotation.Command;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Cron;
import com.github.lucbui.fracktail3.spring.schedule.annotation.InjectPlatform;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Schedule;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;

@Component
public class UptimeModule {
    private static final DiscordChannelset BOT_TIME = DiscordChannelset.forChannel(744390997429059595L);

    private Instant startTime;

    @Schedule
    @Cron(hour = "22", dayOfWeek = "SUN-THU", timezone = "America/Chicago")
    public Mono<Void> sleepTimer(@InjectPlatform DiscordPlatform platform) {
        return platform.message(BOT_TIME, "<@!248612704019808258>, go the heck to sleep!!");
    }

    @PostConstruct
    private void setStartTime() {
        startTime = Instant.now();
    }

    @Command
    public String uptime() {
        long elapsed = Duration.between(startTime, Instant.now()).toMillis();
        return "I have been alive for " + DurationFormatUtils.formatDurationWords(elapsed, true, false) + ".";
    }
}
