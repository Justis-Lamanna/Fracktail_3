package com.github.lucbui.fracktail3.magic.handlers;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

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
        this.orElse = orElse;
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

    public List<Command.Resolved> getCommandsByName(String name, Config config, Locale locale) {
        return commands.stream()
                .map(c -> c.resolve(config, locale))
                .filter(c -> getAnyEqualityPredicate().test(name, c.getNames().toArray(new String[0])))
                .collect(Collectors.toList());
    }

    public Map<String, Command> getCommandsById() {
        return commands.stream()
                .collect(Collectors.toMap(Command::getId, Function.identity()));
    }

    public Map<String, List<Command.Resolved>> getCommandsByName(Config config, Locale locale) {
        return commands.stream()
                .map(c -> c.resolve(config, locale))
                .flatMap(c -> c.getNames().stream().map(name -> Tuples.of(name, c)))
                .collect(Collectors.groupingBy(Tuple2::getT1,
                        caseSensitive ? HashMap::new : CaseInsensitiveMap::new,
                        Collectors.mapping(Tuple2::getT2, Collectors.toList())));
    }

    private BiPredicate<String, String> getEqualityPredicate() {
        if(caseSensitive) {
            return StringUtils::equals;
        } else {
            return StringUtils::equalsIgnoreCase;
        }
    }

    private BiPredicate<String, String[]> getAnyEqualityPredicate() {
        if(caseSensitive) {
            return StringUtils::equalsAny;
        } else {
            return StringUtils::equalsAnyIgnoreCase;
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

    public void validate(BotSpec spec) throws BotConfigurationException {
        commands.forEach(c -> c.validate(spec));
    }
}
