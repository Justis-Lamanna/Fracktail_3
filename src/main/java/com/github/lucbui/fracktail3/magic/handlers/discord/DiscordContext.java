package com.github.lucbui.fracktail3.magic.handlers.discord;

import com.github.lucbui.fracktail3.magic.handlers.CommandContext;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;

public class DiscordContext extends CommandContext {
    public static final String USERNAME = "username";
    public static final String NICKNAME = "nickname";
    public static final String NAME = "name";
    public static final String LOCALE = "locale";
    public static final String SELF = "self";
    public static final String GUILD = "guild";
    private MessageCreateEvent message;
    private Locale locale;

    public MessageCreateEvent getMessage() {
        return message;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setMessage(MessageCreateEvent message) {
        this.message = message;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean isDm() {
        return !message.getMember().isPresent();
    }

    @Override
    public Map<String, Object> getVariableMap() {
        Map<String, Object> map = super.getVariableMap();
        String username = message.getMessage().getAuthor().map(User::getUsername).orElse(StringUtils.EMPTY);
        map.put(USERNAME, username);
        map.put(NICKNAME, message.getMember().map(Member::getDisplayName).orElse(StringUtils.EMPTY));
        map.put(NAME, message.getMember().map(Member::getDisplayName).orElse(username));
        map.put(LOCALE, locale.getDisplayName());

        return map;
    }

    public Mono<Map<String, Object>> getExtendedVariableMap() {
        Map<String, Object> map = this.getVariableMap();
        return Mono.just(map)
                .zipWith(message.getClient().getSelf().map(User::getUsername).defaultIfEmpty(""), mapCombinator(SELF))
                .zipWith(message.getGuild().map(Guild::getName).defaultIfEmpty(""), mapCombinator(GUILD));
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
