package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandList {
    private final List<Command> commands;
    private final Action orElse;

    private boolean caseSensitive;

    public CommandList(List<Command> commands, Action orElse) {
        this.commands = Collections.unmodifiableList(commands);
        this.orElse = Objects.requireNonNull(orElse);
        this.caseSensitive = true;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public Optional<Command> getCommandById(String id) {
        return commands.stream()
                .filter(c -> getEqualityPredicate().test(id, c.getId()))
                .findFirst();
    }

    public Map<String, Command> getCommandsById() {
        return commands.stream()
                .collect(Collectors.toMap(Command::getId, Function.identity()));
    }

    public Map<String, List<Command>> getCommandsByName(Config config, Locale locale) {
        Map<String, List<Command>> returned = new HashMap<>();
        for(Command command : commands) {
            List<String> names = command.getNames().resolve(config, locale);
            for(String name : names) {
                returned.computeIfAbsent(name, s -> new ArrayList<>()).add(command);
            }
        }
        return returned;
    }

    private BiPredicate<String, String> getEqualityPredicate() {
        if(caseSensitive) {
            return StringUtils::equals;
        } else {
            return StringUtils::equalsIgnoreCase;
        }
    }

    public Action getOrElse() {
        return orElse;
    }

    public Mono<Void> doOrElse(Bot bot, CommandContext<?> ctx) {
        if(orElse == null) {
            return Mono.empty();
        }
        return orElse.doAction(bot, ctx);
    }

    public void validate(BotSpec botSpec) {
        commands.forEach(c -> c.validate(botSpec));
        orElse.validate(botSpec);
    }
}
