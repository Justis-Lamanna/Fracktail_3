package com.github.lucbui.fracktail3.magic.handlers.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.object.util.Snowflake;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;

public class DiscordContext extends CommandContext<DiscordContext> {
    public static final String USERNAME = "username";
    public static final String NICKNAME = "nickname";
    public static final String NAME = "name";
    public static final String LOCALE = "locale";
    public static final String SELF = "self";
    public static final String GUILD = "guild";
    public static final String GUILD_ID = "guildId";
    public static final String USER_AT = "at_user";
    public static final String OWNER_AT = "at_owner";

    private DiscordConfiguration configuration;
    private MessageCreateEvent event;
    private Locale locale;

    public MessageCreateEvent getEvent() {
        return event;
    }

    public Locale getLocale() {
        return locale;
    }

    public DiscordContext setEvent(MessageCreateEvent message) {
        this.event = message;
        return this;
    }

    public DiscordContext setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public DiscordContext setConfiguration(DiscordConfiguration discordConfiguration) {
        this.configuration = discordConfiguration;
        return this;
    }

    public boolean isDm() {
        return !event.getMember().isPresent();
    }

    @Override
    public Map<String, Object> getVariableMap() {
        Map<String, Object> map = super.getVariableMap();
        String username = event.getMessage().getAuthor().map(User::getUsername).orElse(StringUtils.EMPTY);
        map.put(USERNAME, username);
        map.put(NICKNAME, event.getMember().map(Member::getDisplayName).orElse(StringUtils.EMPTY));
        map.put(NAME, event.getMember().map(Member::getDisplayName).orElse(username));
        map.put(LOCALE, locale.getDisplayName());
        map.put(GUILD_ID, event.getGuildId().map(Snowflake::asString).orElse(StringUtils.EMPTY));
        map.put(USER_AT, event.getMessage().getAuthor().map(User::getMention).orElse(StringUtils.EMPTY));
        configuration.getOwner().ifPresent(owner -> map.put(OWNER_AT, "<@" + owner.asString() + ">"));
        return map;
    }

    @Override
    public Mono<Boolean> respond(String message) {
        return respond(event.getMessage().getChannelId(), message);
    }

    public Mono<Boolean> respond(Snowflake channel, String message) {
        return this.event.getClient()
                .getChannelById(channel)
                .cast(MessageChannel.class)
                .flatMap(tc -> tc.createMessage(message))
                .thenReturn(true);
    }

    public Mono<Boolean> dm(String message) {
        return event.getMessage().getAuthor()
                .map(usr -> dm(usr.getId(), message))
                .orElse(Mono.empty());
    }

    public Mono<Boolean> dm(Snowflake user, String message) {
        return this.event.getClient()
                .getUserById(user)
                .flatMap(User::getPrivateChannel)
                .flatMap(pc -> pc.createMessage(message))
                .thenReturn(true);
    }

    public Mono<Boolean> react(String emoji) {
        return this.event.getMessage()
                .addReaction(ReactionEmoji.unicode(emoji))
                .thenReturn(true);
    }

    public Mono<Boolean> react(Snowflake id, String name) {
        return react(id, name, false);
    }

    public Mono<Boolean> react(Snowflake id, String name, boolean animated) {
        return this.event.getMessage()
                .addReaction(ReactionEmoji.custom(id, name, animated))
                .thenReturn(true);
    }

    public Mono<Boolean> alert(String message) {
        return Mono.justOrEmpty(configuration.getOwner())
                .flatMap(owner -> dm(owner, message));
    }

    public Mono<Map<String, Object>> getExtendedVariableMap() {
        Map<String, Object> map = this.getVariableMap();
        return Mono.just(map)
                .zipWith(event.getClient().getSelf().map(User::getUsername).defaultIfEmpty(""), mapCombinator(SELF))
                .zipWith(event.getGuild().map(Guild::getName).defaultIfEmpty(""), mapCombinator(GUILD));
    }

    public DiscordConfiguration getConfiguration() {
        return configuration;
    }

    public static boolean containsExtendedVariable(String msg) {
        return StringUtils.containsAny(msg, "{" + SELF, "{" + GUILD);
    }

    private <T> BiFunction<Map<String, Object>, String, Map<String, Object>> mapCombinator(String name) {
        return (map, input) -> {
            if(StringUtils.isNotEmpty(input)) {
                map.put(name, input);
            }
            return map;
        };
    }
}
