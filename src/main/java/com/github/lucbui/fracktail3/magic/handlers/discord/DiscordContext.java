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
    private MessageCreateEvent message;
    private Locale locale;

    public MessageCreateEvent getMessage() {
        return message;
    }

    public Locale getLocale() {
        return locale;
    }

    public boolean isDm() {
        return !message.getMember().isPresent();
    }

    @Override
    public Map<String, String> getVariableMap() {
        Map<String, String> map = super.getVariableMap();
        map.put("username", message.getMessage().getAuthor().map(User::getUsername).orElse(StringUtils.EMPTY));
        map.put("nickname", message.getMember().map(Member::getDisplayName).orElse(StringUtils.EMPTY));
        map.put("name", message.getMember().map(Member::getDisplayName).orElse(map.get("username")));
        map.put("locale", locale.getDisplayName());

        return map;
    }

    public Mono<Map<String, String>> getExtendedVariableMap() {
        Map<String, String> map = this.getVariableMap();
        return Mono.just(map)
                .zipWith(message.getClient().getSelf().map(User::getUsername).defaultIfEmpty(""), mapCombinator("self"))
                .zipWith(message.getGuild().map(Guild::getName).defaultIfEmpty(""), mapCombinator("guild"));
    }

    private <T> BiFunction<Map<String, String>, String, Map<String, String>> mapCombinator(String name) {
        return (map, input) -> {
            if(StringUtils.isNotEmpty(input)) {
                map.put(name, input);
            }
            return map;
        };
    }
}
