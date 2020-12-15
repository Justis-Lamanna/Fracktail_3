package com.github.lucbui.fracktail3.modules.meta;

import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.github.lucbui.fracktail3.spring.annotation.schedule.RunEvery;
import com.github.lucbui.fracktail3.spring.annotation.schedule.Schedule;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;

@Component
public class UptimeModule {
    private Instant startTime;

    @Schedule
    //@Cron(hour = "22", dayOfWeek = "SUN-THU", timezone = "America/Chicago")
    @RunEvery("PT5S")
    public void test() {
        System.out.println("Hi!");
        //throw new CancelTaskException();
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
