package com.github.lucbui.fracktail3.magic.schedule;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.schedule.DiscordScheduleContext;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.GatewayDiscordClient;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class ScheduleSubscriber implements Subscriber<Instant> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleSubscriber.class);

    private final Bot bot;
    private final DiscordConfiguration configuration;
    private final GatewayDiscordClient client;
    private final ScheduledEvent event;

    public ScheduleSubscriber(Bot bot, DiscordConfiguration configuration, GatewayDiscordClient client, ScheduledEvent event) {
        this.bot = bot;
        this.configuration = configuration;
        this.client = client;
        this.event = event;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        event.schedule(new Proxy(subscription));
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Instant instant) {
        if(event.isEnabled()) {
            DiscordScheduleContext ctx = new DiscordScheduleContext(configuration, event, instant, client);
            event.execute(bot, ctx).subscribe();
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
