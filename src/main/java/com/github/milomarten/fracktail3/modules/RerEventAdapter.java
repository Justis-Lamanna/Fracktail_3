package com.github.milomarten.fracktail3.modules;

import discord4j.common.util.Snowflake;
import discord4j.core.event.ReactiveEventAdapter;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.channel.TextChannel;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RerEventAdapter extends ReactiveEventAdapter {
    private static final Snowflake GUILD_ID = Snowflake.of(423976318082744321L);
    private static final Snowflake DRAGON_ROLE = Snowflake.of(560500269419331585L);
    private static final Snowflake BOT_TIME_ID = Snowflake.of(744390997429059595L);

    private final AtomicInteger dragonCounter = new AtomicInteger(0);

    @Override
    public Publisher<?> onVoiceStateUpdate(VoiceStateUpdateEvent event) {
        VoiceState current = event.getCurrent();
        if(!current.getGuildId().equals(GUILD_ID)) {
            return Mono.empty();
        }

        if(event.isJoinEvent()) {
            return current.getMember()
                    .filter(member -> member.getRoleIds().contains(DRAGON_ROLE))
                    .map(member -> dragonCounter.incrementAndGet())
                    .filter(i -> i == 1)
                    .flatMap(i -> event.getClient().getChannelById(BOT_TIME_ID)
                                .cast(TextChannel.class)
                                .flatMap(tc -> tc.createMessage("```There's a rer in voice!```")));
        } else if(event.isLeaveEvent()) {
            return current.getMember()
                    .filter(member -> member.getRoleIds().contains(DRAGON_ROLE))
                    .map(member -> dragonCounter.getAndUpdate(i -> Math.max(i - 1, 0))); //Make sure we don't go negative!
        }

        return Mono.empty();
    }
}
