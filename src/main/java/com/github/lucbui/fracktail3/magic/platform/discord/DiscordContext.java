package com.github.lucbui.fracktail3.magic.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Context used when a command is used via Discord
 */
public class DiscordContext extends CommandContext {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordContext.class);

    public static final String USERNAME = "username";
    public static final String NICKNAME = "nickname";
    public static final String NAME = "name";
    public static final String LOCALE = "locale";
    public static final String SELF = "self";
    public static final String GUILD = "guild";
    public static final String GUILD_ID = "guildId";
    public static final String USER_AT = "at_user";
    public static final String OWNER_AT = "at_owner";

    private static final int DISCORD_MAX_MESSAGE = 2000;

    private final MessageCreateEvent event;
    private final DiscordConfiguration config;

    /**
     * Initialize the context with basic values
     * @param config The discord-related configuration of the bot
     * @param event The event which triggered the command usage
     */
    public DiscordContext(
            DiscordConfiguration config,
            MessageCreateEvent event) {
        super(config, event.getMessage().getContent());
        this.config = config;
        this.event = event;
    }

    /**
     * Get the triggering event
     * @return The triggering event
     */
    public MessageCreateEvent getEvent() {
        return event;
    }

    @Override
    public DiscordConfiguration getConfiguration() {
        return config;
    }

    /**
     * Check if this command was triggered in DMs
     * @return True, if this command came from DMs
     */
    public boolean isDm() {
        return !event.getMember().isPresent();
    }

    @Override
    public Mono<Boolean> alert(String message) {
        return Mono.justOrEmpty(config.getOwner())
                .flatMap(owner -> dm(owner, message));
    }

    @Override
    public Mono<Boolean> respond(String message) {
        return respond(event.getMessage().getChannelId(), message);
    }

    /**
     * Send a message in a specific channel
     * @param channel The channel to send the message
     * @param message The message to send
     * @return Asynchronous boolean indicating message was sent
     */
    public Mono<Boolean> respond(Snowflake channel, String message) {
        return this.event.getClient()
                .getChannelById(channel)
                .cast(MessageChannel.class)
                .flatMap(tc -> tc.createMessage(normalize(message)))
                .thenReturn(true);
    }

    /**
     * Send a DM to the user of the command
     * @param message The message to send
     * @return Asynchronous boolean indicating message was sent
     */
    public Mono<Boolean> dm(String message) {
        return event.getMessage().getAuthor()
                .map(usr -> dm(usr.getId(), normalize(message)))
                .orElse(Mono.empty());
    }

    /**
     * Send a DM to a specific user.
     * Note that this user must be in a guild where the bot lives
     * @param user The user to DM
     * @param message The message to send
     * @return Asynchronous boolean indicating message was sent
     */
    public Mono<Boolean> dm(Snowflake user, String message) {
        return this.event.getClient()
                .getUserById(user)
                .flatMap(User::getPrivateChannel)
                .flatMap(pc -> pc.createMessage(message))
                .thenReturn(true);
    }

    @Override
    protected Map<String, Object> getVariableMapConstants() {
        Map<String, Object> map = super.getVariableMapConstants();
        String username = event.getMessage().getAuthor().map(User::getUsername).orElse(StringUtils.EMPTY);
        map.put(USERNAME, username);
        map.put(NICKNAME, event.getMember().map(Member::getDisplayName).orElse(StringUtils.EMPTY));
        map.put(NAME, event.getMember().map(Member::getDisplayName).orElse(username));
        map.put(GUILD_ID, event.getGuildId().map(Snowflake::asString).orElse(StringUtils.EMPTY));
        map.put(USER_AT, event.getMessage().getAuthor().map(User::getMention).orElse(StringUtils.EMPTY));
        config.getOwner().ifPresent(owner -> map.put(OWNER_AT, "<@" + owner.asString() + ">"));
        return map;
    }

    private String normalize(String message) {
        if(message.length() >= DISCORD_MAX_MESSAGE) {
            //Log the abbreviated portion, so it can be obtained.
            LOGGER.info("Chopped off: " + message.substring(DISCORD_MAX_MESSAGE));
        }
        return StringUtils.abbreviate(message, DISCORD_MAX_MESSAGE);
    }
}
