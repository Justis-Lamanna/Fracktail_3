package com.github.lucbui.fracktail3.discord.event;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

public class MessageReceive extends EventWrapper<MessageCreateEvent> implements HasGuild, HasUser, HasChannel, HasMember {
    public MessageReceive(MessageCreateEvent event) {
        super(event);
    }

    @Override
    public Snowflake getGuildId() {
        return getRawEvent().getGuildId().orElse(null);
    }

    @Override
    public Mono<Guild> getGuild() {
        return getRawEvent().getGuild();
    }

    @Override
    public Snowflake getUserId() {
        return getRawEvent().getMessage().getAuthor().map(User::getId).orElse(null);
    }

    @Override
    public Mono<User> getUser() {
        return Mono.justOrEmpty(getRawEvent().getMessage().getAuthor());
    }

    @Override
    public Snowflake getChannelId() {
        return getRawEvent().getMessage().getChannelId();
    }

    @Override
    public Mono<MessageChannel> getChannel() {
        return getRawEvent().getMessage().getChannel();
    }

    @Override
    public Member getMember() {
        return getRawEvent().getMember().orElse(null);
    }
}
