package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.discord.annotation.DiscordReply;
import com.github.lucbui.fracktail3.discord.context.ReplyStyle;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import com.github.lucbui.fracktail3.spring.command.annotation.Command;
import com.github.lucbui.fracktail3.spring.command.annotation.InjectPlatform;
import com.github.lucbui.fracktail3.spring.command.annotation.Usage;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Cron;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Schedule;
import discord4j.common.util.Snowflake;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;

@Component
public class UptimeModule {
    private static final String BOT_TIME = "744390997429059595";
    private static final Snowflake ME = Snowflake.of(248612704019808258L);

    private Instant startTime;

    @PostConstruct
    private void setStartTime() {
        startTime = Instant.now();
    }

    @Command
    public String uptime() {
        long elapsed = Duration.between(startTime, Instant.now()).toMillis();
        return "I have been alive for " + DurationFormatUtils.formatDurationWords(elapsed, true, false) + ".";
    }

    @Schedule
    @Cron(hour = "22", dayOfWeek = "SUN-THU", timezone = "America/Chicago")
    public Mono<Void> sleepTimer(@InjectPlatform DiscordPlatform platform) {
        return platform.sendMessage(BOT_TIME, FormatUtils.mentionUser(ME) + ", GO THE HECK TO SLEEP!!")
                .then();
    }

    @Command
    @Usage("Evaluate an arbitrary math expression")
    @DiscordReply(ReplyStyle.REPLY)
    public String math = "The answer is 3.";
}
