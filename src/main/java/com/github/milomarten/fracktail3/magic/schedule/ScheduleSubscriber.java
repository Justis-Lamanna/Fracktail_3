package com.github.milomarten.fracktail3.magic.schedule;

import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.function.Function;

public class ScheduleSubscriber implements Subscriber<Instant> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleSubscriber.class);

    private final ScheduledEvent event;
    private final Function<Instant, ScheduleUseContext> contextCreator;

    public ScheduleSubscriber(ScheduledEvent event, Function<Instant, ScheduleUseContext> contextCreator) {
        this.event = event;
        this.contextCreator = contextCreator;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        event.schedule(new Proxy(subscription));
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Instant instant) {
        if(event.isEnabled()) {
            ScheduleUseContext ctx = contextCreator.apply(instant);
            event.execute(ctx).subscribe();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.error("Encountered error on scheduled action " + event.getId() + ": ", throwable);
    }

    @Override
    public void onComplete() {
        event.complete();
        LOGGER.debug("Scheduled action {} completed", event.getId());
    }

    public class Proxy {
        private final Subscription subscription;

        public Proxy(Subscription subscription) {
            this.subscription = subscription;
        }

        public void cancel() {
            ScheduleSubscriber.LOGGER.debug("Cancelled action {}", event.getId());
            this.subscription.cancel();
        }
    }
}
