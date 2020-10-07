package com.github.lucbui.fracktail3.magic.schedule;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DefaultScheduler implements Scheduler {
    @Override
    public Mono<Instant> wait(Duration duration) {
        return Mono.create(sink -> {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    sink.success(Instant.now());
                }
            };
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            ScheduledFuture<?> future = executor.schedule(task, duration.toMillis(), TimeUnit.MILLISECONDS);

            sink.onCancel(() -> future.cancel(false));
        });
    }

    @Override
    public Mono<Instant> at(Instant instant) {
        return Mono.create(sink -> {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    sink.success(Instant.now());
                }
            };
            //We use a timer here, rather than an ExecutorService, since it is less optimized for executing on specific
            //dates.
            Timer timer = new Timer();
            timer.schedule(task, Date.from(instant));

            sink.onCancel(timer::cancel);
        });
    }

    @Override
    public Flux<Instant> every(Duration duration) {
        return Flux.create(sink -> {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    sink.next(Instant.now());
                }
            };
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            ScheduledFuture<?> future = executor.schedule(task, duration.toMillis(), TimeUnit.MILLISECONDS);

            sink.onCancel(() -> future.cancel(false));
        });
    }

    @Override
    public Flux<Instant> cron(String cronString) {
        throw new BotConfigurationException("Default scheduler does not support CRON");
    }

    @Override
    public Flux<Instant> cron(String cronString, TimeZone timeZone) {
        throw new BotConfigurationException("Default scheduler does not support CRON");
    }
}
