package com.github.lucbui.fracktail3.magic.schedule;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.TimeZone;

/**
 * Abstraction used to schedule 'ticks' of a clock
 */
public interface Scheduler {
    /**
     * Tick after duration has passed
     * @param duration The duration to wait for
     * @return A Mono which ticks once the duration expires
     */
    default Mono<Instant> wait(Duration duration) {
        return Mono.delay(duration).map(l -> Instant.now());
    }

    /**
     * Tick at an exact instant of time
     * @param instant The instant to tick at
     * @return A mono which ticks once the instant occurs
     */
    Mono<Instant> at(Instant instant);

    /**
     * Tick at an exact instant of time
     * @param time The instant to tick at
     * @return A mono which ticks once the instant occurs
     */
    default Mono<Instant> at(ZonedDateTime time) {
        return at(time.toInstant());
    }

    /**
     * Ticks every duration, forever
     * @param duration The duration to wait for
     * @return A flux which ticks once every duration
     */
    default Flux<Instant> every(Duration duration) {
        return Flux.interval(duration).map(l -> Instant.now());
    }

    /**
     * Emit a tick at each time matching the CRON string
     * @param cronString The cron string to use
     * @return A flux which ticks with regard to the provided CRON string
     */
    Flux<Instant> cron(String cronString);

    /**
     * Emit a tick at each time matching the CRON string, using a specific timezone
     * @param cronString The cron string to use
     * @param timeZone The timezone to use as reference
     * @return A flux which ticks with regard to the provided CRON string
     */
    Flux<Instant> cron(String cronString, TimeZone timeZone);
}

