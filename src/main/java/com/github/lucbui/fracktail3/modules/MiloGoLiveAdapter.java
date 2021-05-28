package com.github.lucbui.fracktail3.modules;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import com.github.lucbui.fracktail3.spring.service.PlatformService;
import com.github.lucbui.fracktail3.twitch.platform.TwitchEventAdapter;
import com.github.twitch4j.events.ChannelChangeGameEvent;
import com.github.twitch4j.events.ChannelChangeTitleEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import com.github.twitch4j.helix.domain.Stream;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.core.spec.MessageEditSpec;
import discord4j.rest.util.Color;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class MiloGoLiveAdapter extends TwitchEventAdapter {
    private static final Snowflake ANNOUNCEMENTS = Snowflake.of(746898862098087977L);
    private static final String AT_STREAM_ALERTS = FormatUtils.mentionRole(Snowflake.of(535277349340708864L));

    @Autowired
    private PlatformService platformService;

    private AtomicReference<Message> goLiveMessage = new AtomicReference<>();

    @Override
    public Publisher<?> onChannelGoLiveEvent(ChannelGoLiveEvent event) {
        goLiveMessage.updateAndGet(oldM -> {
            if(oldM != null) {
                return oldM;
            }
            return createMessage(event.getStream());
        });
        return Mono.empty();
    }

    @Override
    public Publisher<?> onChannelGoOfflineEvent(ChannelGoOfflineEvent event) {
        goLiveMessage.updateAndGet(m -> {
            if(m != null) {
                m.delete("Stream ended").block();
            }
            return null;
        });
        return Mono.empty();
    }

    @Override
    public Publisher<?> onChannelChangeGameEvent(ChannelChangeGameEvent event) {
        goLiveMessage.updateAndGet(m -> {
            if(m == null) {
                return createMessage(event.getStream());
            } else {
                return m.edit(spec -> matchSpecToStream(spec, event.getStream())).block();
            }
        });
        return Mono.empty();
    }

    @Override
    public Publisher<?> onChannelChangeTitleEvent(ChannelChangeTitleEvent event) {
        goLiveMessage.updateAndGet(m -> {
            if(m == null) {
                return createMessage(event.getStream());
            } else {
                return m.edit(spec -> matchSpecToStream(spec, event.getStream())).block();
            }
        });
        return Mono.empty();
    }

    private Message createMessage(Stream stream) {
        DiscordPlatform platform = platformService.getPlatform(DiscordPlatform.class);
        return platform.getClient().getChannelById(ANNOUNCEMENTS)
                .cast(TextChannel.class)
                .flatMap(tc -> tc.createMessage(spec -> matchSpecToStream(spec, stream)))
                .block();
    }

    private void matchSpecToStream(MessageCreateSpec spec, Stream stream) {
        spec.setContent(AT_STREAM_ALERTS);
        spec.setEmbed(embed -> embed
                .setTitle(stream.getUserName() + " is now Streaming!")
                .setDescription(stream.getTitle())
                .setTimestamp(stream.getStartedAtInstant())
                .setThumbnail(stream.getThumbnailUrl())
                .setUrl("https://twitch.tv/" + stream.getUserLogin())
                .setColor(Color.of(100, 65, 165))
                .addField("Playing", stream.getGameName(), true));
    }

    private void matchSpecToStream(MessageEditSpec spec, Stream stream) {
        spec.setContent(AT_STREAM_ALERTS);
        spec.setEmbed(embed -> embed
                .setTitle(stream.getUserName() + " is now Streaming!")
                .setDescription(stream.getTitle())
                .setTimestamp(stream.getStartedAtInstant())
                .setThumbnail(stream.getThumbnailUrl())
                .setUrl("https://twitch.tv/" + stream.getUserLogin())
                .setColor(Color.of(100, 65, 165))
                .addField("Playing", stream.getGameName(), true));
    }
}
