package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.resolver.CompositeResolver;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.ListFromI18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommand;
import com.github.lucbui.fracktail3.xsd.I18NString;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultCommandParser implements CommandParser, SupportsCustom<Command> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandParser.class);

    private final BehaviorParser behaviorParser;

    public DefaultCommandParser(BehaviorParser behaviorParser) {
        this.behaviorParser = behaviorParser;
    }

    @Override
    public BehaviorParser getBehaviorParser() {
        return behaviorParser;
    }

    @Override
    public ActionParser getActionParser() {
        return behaviorParser.getActionParser();
    }

    @Override
    public Command fromXml(DTDBot xml, DTDCommand command) {
        if(command.getCustom() != null) {
            return getFromCustom(command.getCustom());
        } else {
            return getCommandFromXml(xml, command);
        }
    }

    protected Command getCommandFromXml(DTDBot xml, DTDCommand command) {
        Resolver<String> name = fromI18NString(command.getName());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getDebugString("Name", command.getName()));
        }

        Resolver<List<String>> aliases = getAliasResolver(command);

        String role = command.getRole() == null ? null : command.getRole().getValue();

        List<Behavior> behaviors = command.getBehaviors().getBehavior().stream()
                .map(s -> behaviorParser.fromXml(xml, command, s))
                .collect(Collectors.toList());
        if (command.getBehaviors().getOrElse() != null) {
            return new Command(name, aliases, role, behaviors,
                    getActionParser().fromXml(xml, command, null, command.getBehaviors().getOrElse().getAction()));
        } else {
            return new Command(name, aliases, role, behaviors, null);
        }
    }

    protected Resolver<List<String>> getAliasResolver(DTDCommand command) {
        Resolver<List<String>> aliases;
        if (StringUtils.isNotBlank(command.getAliasesFrom())) {
            aliases = new ListFromI18NResolver(command.getAliasesFrom());
            LOGGER.debug("Aliases From Key: {}", command.getAliasesFrom());
        } else if (command.getAliases() != null) {
            aliases = command.getAliases().getAlias().stream()
                    .map(DefaultCommandParser::fromI18NString)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), CompositeResolver::new));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(command.getAliases().getAlias().stream()
                        .map(i18n -> getDebugString("Alias", i18n))
                        .collect(Collectors.joining(",")));
            }
        } else {
            aliases = Resolver.identity(Collections.emptyList());
        }
        return aliases;
    }

    private static String getDebugString(String type, I18NString string) {
        return BooleanUtils.isTrue(string.isI18N()) ?
                type + " From Key: " + string.getValue() :
                type + ": " + string.getValue();
    }

    private static Resolver<String> fromI18NString(I18NString string) {
        return BooleanUtils.isTrue(string.isI18N()) ?
                new I18NResolver(string.getValue()) :
                Resolver.identity(string.getValue());
    }

    @Override
    public Class<Command> getParsedClass() {
        return Command.class;
    }
}
