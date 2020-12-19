package com.github.lucbui.fracktail3.discord.context;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.rest.util.AllowedMentions;
import reactor.core.publisher.Mono;

import java.util.Locale;

/**
 * Basic context for MessageCreateEvent
 */
public class DiscordCommandSearchContext extends DiscordBasePlatformContext<MessageCreateEvent> {
    private ResponseType responseType = ResponseType.INLINE;

    private Mono<Locale> cached;

    /**
     * Initialize
     * @param bot The bot
     * @param platform The platform
     * @param payload The payload
     */
    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, MessageCreateEvent payload) {
        super(bot, platform, payload);
        cached = payload.getGuild()
                .map(Guild::getPreferredLocale)
                .defaultIfEmpty(Locale.getDefault())
                .cache();
    }

    /**
     * Initialize with Locale
     * @param bot The bot
     * @param platform The platform
     * @param locale The locale
     * @param payload The payload
     */
    public DiscordCommandSearchContext(Bot bot, DiscordPlatform platform, Locale locale, MessageCreateEvent payload) {
        super(bot, platform, payload);
    }

    /**
     * Get the response type
     * @return The response type
     */
    public ResponseType getResponseType() {
        return responseType;
    }

    /**
     * Set the response type
     * @param responseType The response type
     */
    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    @Override
    public Mono<Void> respond(String message) {
        switch (responseType) {
            case INLINE: return respondInline(message);
            case DM: return respondDm(message);
            default: return respondClassic(message);
        }
    }


    /**
     * Respond in the classic way (as just a plain message)
     * @param message The message to respond with
     * @return Asynchronous indication of completion
     */
    public Mono<Void> respondClassic(String message) {
        return getPayload().getMessage().getChannel()
                .flatMap(mc -> mc.createMessage(message))
                .then();
    }

    /**
     * Respond over DM
     * @param message The message to respond with
     * @return Asynchronous indication of completion
     */
    public Mono<Void> respondDm(String message) {
        return Mono.justOrEmpty(getPayload().getMessage().getAuthor())
                .flatMap(User::getPrivateChannel)
                .flatMap(pc -> pc.createMessage(message))
                .then();
    }

    /**
     * Respond using Discord's inline-reply feature
     * @param message The message to respond with
     * @return Asynchronous indication of completion
     */
    public Mono<Void> respondInline(String message) {
        Snowflake reference = getPayload().getMessage().getId();
        return getPayload().getMessage().getChannel()
                .flatMap(mc -> mc.createMessage(spec -> {
                    spec.setMessageReference(reference);
                    spec.setAllowedMentions(
                            AllowedMentions.builder()
                                    .repliedUser(false)
                                    .build());
                    spec.setContent(message);
                }))
                .then();
    }

    @Override
    public Mono<Locale> getLocale() {
        return cached;
    }
}
