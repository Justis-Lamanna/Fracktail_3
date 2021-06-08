package com.github.milomarten.fracktail3.magic.platform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.milomarten.fracktail3.magic.Bot;
import com.github.milomarten.fracktail3.magic.schedule.ScheduleSubscriber;
import com.github.milomarten.fracktail3.magic.schedule.ScheduledEvent;
import com.github.milomarten.fracktail3.magic.schedule.ScheduledEvents;
import com.github.milomarten.fracktail3.magic.schedule.Scheduler;
import com.github.milomarten.fracktail3.magic.schedule.context.BasicScheduleUseContext;
import com.github.milomarten.fracktail3.magic.util.CreateAction;
import com.github.milomarten.fracktail3.magic.util.DeleteAction;
import com.github.milomarten.fracktail3.magic.util.MassCreateAction;
import com.github.milomarten.fracktail3.magic.util.UpdateAction;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Data
public class SchedulePlatform implements Platform, DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulePlatform.class);

    @JsonIgnore
    private final Scheduler scheduler;
    private final ScheduledEvents events;
    @JsonIgnore
    private Disposable disposable;

    @Override
    public String getId() {
        return "scheduler";
    }

    @Override
    public ScheduledEvents getConfiguration() {
        return events;
    }

    @Override
    public Mono<Boolean> start(Bot bot) {
        LOGGER.info("Starting scheduled events");
        getConfiguration().forEach(se -> schedule(bot, se));
        disposable = getConfiguration().eventStream()
                .doOnNext(action -> {
                    if(action instanceof CreateAction) {
                        schedule(bot, ((CreateAction<ScheduledEvent>)action).getItem());
                    } else if(action instanceof MassCreateAction) {
                        ((MassCreateAction<ScheduledEvent>)action).getItems().forEach(item -> schedule(bot, item));
                    } else if(action instanceof UpdateAction) {
                        unschedule(((UpdateAction<ScheduledEvent>)action).getOldItem());
                        schedule(bot, ((UpdateAction<ScheduledEvent>)action).getNewItem());
                    } else if(action instanceof DeleteAction) {
                        unschedule(((DeleteAction<ScheduledEvent>)action).getItem());
                    }
                })
                .subscribe();
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> stop(Bot bot) {
        if(disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        getConfiguration().forEach(this::unschedule);
        return Mono.just(true);
    }

    protected void schedule(Bot bot, ScheduledEvent event) {
        LOGGER.info("Scheduling event " + event.getId());
        event.getTrigger()
                .schedule(scheduler)
                .subscribe(new ScheduleSubscriber(event, instant -> new BasicScheduleUseContext(bot, instant, event)));
    }

    protected void unschedule(ScheduledEvent event) {
        LOGGER.info("Unscheduling event " + event.getId());
        event.cancel();
    }

    @Override
    public Mono<Person> getPerson(String id) {
        return Mono.just(NonePerson.INSTANCE);
    }

    @Override
    public Mono<Place> getPlace(String id) {
        return Mono.just(NonePlace.INSTANCE);
    }

    @Override
    public void destroy() throws Exception {
        if(disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }
}
