package com.github.lucbui.fracktail3.magic.handlers.platform.discord;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;

public class DiscordContext extends CommandContext {
    public static final String USERNAME = "username";
    public static final String NICKNAME = "nickname";
    public static final String NAME = "name";
    public static final String LOCALE = "locale";
    public static final String SELF = "self";
    public static final String GUILD = "guild";
    public static final String GUILD_ID = "guildId";
    public static final String USER_AT = "at_user";
    public static final String OWNER_AT = "at_owner";

    private final MessageCreateEvent event;

    public DiscordContext(
            DiscordPlatform platform,
            DiscordConfiguration config,
            MessageCreateEvent event) {
        super(platform, config, event.getMessage().getContent().orElse(StringUtils.EMPTY));
        this.event = event;
    }

    public MessageCreateEvent getEvent() {
        return event;
    }

    public boolean isDm() {
        return !event.getMember().isPresent();
    }

    @Override
    public Mono<Boolean> respond(String message) {
        return respond(event.getMessage().getChannelId(), message);
    }

    @Override
    public Mono<Boolean> alert(String message) {
        return Mono.justOrEmpty(config.getOwner())
                .flatMap(owner -> dm(owner, message));
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

    @Override
    public Mono<Map<String, Object>> getExtendedVariableMap() {
        if(containsExtendedVariable(getContents())) {
            return super.getExtendedVariableMap()
                    .zipWith(event.getClient().getSelf().map(User::getUsername).defaultIfEmpty(""), mapCombinator(SELF))
                    .zipWith(event.getGuild().map(Guild::getName).defaultIfEmpty(""), mapCombinator(GUILD));
        } else {
            return Mono.just(this.getVariableMapConstants());
        }
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

    private static boolean containsExtendedVariable(String msg) {
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
