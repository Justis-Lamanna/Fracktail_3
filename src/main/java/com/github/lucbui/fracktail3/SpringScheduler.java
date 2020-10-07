package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class SpringScheduler implements Scheduler {

    private final TaskScheduler taskScheduler;

    @Autowired
    public SpringScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public Mono<Instant> wait(Duration duration) {
        return Mono.create(sink -> {
            ScheduledFuture<?> future = taskScheduler.scheduleWithFixedDelay(() -> {
                sink.success(Instant.now());
            }, duration);
            sink.onCancel(() -> future.cancel(false));
        });
    }

    @Override
    public Mono<Instant> at(Instant instant) {
        return Mono.create(sink -> {
            ScheduledFuture<?> future = taskScheduler.schedule(() -> {
                sink.success(Instant.now());
            }, instant);
            sink.onCancel(() -> future.cancel(false));
        });
    }

    @Override
    public Flux<Instant> every(Duration duration) {
        return getForTrigger(new PeriodicTrigger(duration.getSeconds(), TimeUnit.SECONDS));
    }

    @Override
    public Flux<Instant> cron(String cronString) {
        return getForTrigger(new CronTrigger(cronString));
    }

    @Override
    public Flux<Instant> cron(String cronString, TimeZone timeZone) {
        return getForTrigger(new CronTrigger(cronString, timeZone));
    }

    private Flux<Instant> getForTrigger(Trigger trigger) {
        return Flux.create(sink -> {
            ScheduledFuture<?> future = taskScheduler.schedule(() -> {
                sink.next(Instant.now());
            }, trigger);

            sink.onCancel(() -> {
                if(future != null) {
                    future.cancel(false);
                }
            });
        });
    }
}
