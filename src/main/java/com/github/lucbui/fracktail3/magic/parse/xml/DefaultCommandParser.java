package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.trigger.CommandTrigger;
import com.github.lucbui.fracktail3.magic.resolver.CompositeResolver;
import com.github.lucbui.fracktail3.magic.resolver.ListFromI18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommandTrigger;
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
        if(command.getCustom() != null) {
            return getFromCustom(command.getCustom());
        } else {
            return getCommandFromXml(xml, command);
        }
    }

    protected Command getCommandFromXml(DTDBot xml, DTDCommandWithId command) {
        LOGGER.debug("Parsing Command {}", command.getId());

        Resolver<List<String>> names = getNamesResolver(command);

        CommandTrigger trigger;
        if(command.getTrigger() == null) {
            trigger = CommandTrigger.DEFAULT;
        } else {
            DTDCommandTrigger xmlTrigger = command.getTrigger();

            boolean enabled = BooleanUtils.isNotFalse(xmlTrigger.isEnabled());
            String role = xmlTrigger.getRole() == null ? null : xmlTrigger.getRole().getValue();

            trigger = new CommandTrigger(enabled, role);
        }

        LOGGER.debug("\tEnabled: {} | Role: {}",
                trigger.isEnabled(),
                StringUtils.defaultString(trigger.getRole(), "Any"));

        List<Behavior> behaviors = command.getBehaviors().getBehavior().stream()
                .map(s -> behaviorParser.fromXml(xml, command, s))
                .collect(Collectors.toList());
        if (command.getBehaviors().getOrElse() != null) {
            return new Command(command.getId(), names, trigger, behaviors,
                    getActionParser().fromXml(xml, command, null, command.getBehaviors().getOrElse().getAction()));
        } else {
            return new Command(command.getId(), names, trigger, behaviors, null);
        }
    }

    protected Resolver<List<String>> getNamesResolver(DTDCommandWithId command) {
        Resolver<List<String>> names;
        if (StringUtils.isNotBlank(command.getNamesFrom())) {
            names = new ListFromI18NResolver(command.getNamesFrom());
            LOGGER.debug("\tGetting Names From Key: {}", command.getNamesFrom());
        } else if (command.getNames() != null) {
            names = command.getNames().getName().stream()
                    .map(DefaultCommandParser::fromI18NString)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), CompositeResolver::new));
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(command.getNames().getName().stream()
                        .map(i18n -> getDebugString("Name", i18n))
                        .collect(Collectors.joining(",", "\t", "")));
            }
        } else {
            names = Resolver.identity(Collections.singletonList(command.getId()));
            LOGGER.debug("\tAssuming name as {}", command.getId());
        }
        return names;
    }
}
