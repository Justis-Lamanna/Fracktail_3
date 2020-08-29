package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.resolver.CompositeResolver;
import com.github.lucbui.fracktail3.magic.resolver.ListFromI18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommandWithId;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultCommandParser extends AbstractParser<Command> implements CommandParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandParser.class);

    private final BehaviorParser behaviorParser;

    public DefaultCommandParser(BehaviorParser behaviorParser) {
        super(Command.class);
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
    public Command fromXml(DTDBot xml, DTDCommandWithId command) {
        Command parsedCmd;
        if(command.getCustom() != null) {
            parsedCmd = getFromCustom(command.getCustom());
        } else {
            parsedCmd = getCommandFromXml(xml, command);
        }

        parsedCmd.setEnabled(BooleanUtils.isNotFalse(command.isEnabled()));
        LOGGER.debug("Command is {}", parsedCmd.isEnabled() ? "enabled" : "disabled");

        return parsedCmd;
    }

    protected Command getCommandFromXml(DTDBot xml, DTDCommandWithId command) {
        Resolver<List<String>> names = getNamesResolver(command);

        String role = command.getRole() == null ? null : command.getRole().getValue();
        if(LOGGER.isDebugEnabled() && role != null) {
            LOGGER.debug("Role: {}", role);
        }

        List<Behavior> behaviors = command.getBehaviors().getBehavior().stream()
                .map(s -> behaviorParser.fromXml(xml, command, s))
                .collect(Collectors.toList());
        if (command.getBehaviors().getOrElse() != null) {
            return new Command(command.getId(), names, role, behaviors,
                    getActionParser().fromXml(xml, command, null, command.getBehaviors().getOrElse().getAction()));
        } else {
            return new Command(command.getId(), names, role, behaviors, null);
        }
    }

    protected Resolver<List<String>> getNamesResolver(DTDCommandWithId command) {
        Resolver<List<String>> names;
        if (StringUtils.isNotBlank(command.getNamesFrom())) {
            names = new ListFromI18NResolver(command.getNamesFrom());
            LOGGER.debug("Aliases From Key: {}", command.getNamesFrom());
        } else if (command.getNames() != null) {
            names = command.getNames().getName().stream()
                    .map(DefaultCommandParser::fromI18NString)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), CompositeResolver::new));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(command.getNames().getName().stream()
                        .map(i18n -> getDebugString("Alias", i18n))
                        .collect(Collectors.joining(",")));
            }
        } else {
            names = Resolver.identity(Collections.singletonList(command.getId()));
        }
        return names;
    }
}
