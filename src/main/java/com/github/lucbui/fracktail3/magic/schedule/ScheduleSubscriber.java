package com.github.lucbui.fracktail3.magic.schedule;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.Instant;

public class ScheduleSubscriber implements Subscriber<Instant> {
    private Proxy proxy;

    public Proxy getProxy() {
        return this.proxy;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.proxy = new Proxy(subscription);
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Instant instant) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {}

    public static class Proxy {
        private final Subscription subscription;

        public Proxy(Subscription subscription) {
            this.subscription = subscription;
        }

        public void cancel() {
            this.subscription.cancel();
        }
    }
}
