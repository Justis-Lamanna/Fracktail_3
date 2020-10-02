package com.github.lucbui.fracktail3.discord.event;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

public interface HasChannel {
    Snowflake getChannelId();
    Mono<MessageChannel> getChannel();
}
