package com.github.lucbui.fracktail3.modules;

import com.github.lucbui.fracktail3.spring.annotation.Command;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;

@Component
public class UptimeModule {
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
}
