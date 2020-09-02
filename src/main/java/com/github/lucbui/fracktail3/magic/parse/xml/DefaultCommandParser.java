package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.ActionOption;
import com.github.lucbui.fracktail3.magic.handlers.ActionOptions;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.filter.CommandFilter;
import com.github.lucbui.fracktail3.magic.resolver.CompositeResolver;
import com.github.lucbui.fracktail3.magic.resolver.ListFromI18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommand;
import com.github.lucbui.fracktail3.xsd.DTDCommandAction;
import com.github.lucbui.fracktail3.xsd.DTDCommandFilter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultCommandParser extends AbstractParser<Command> implements CommandParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandParser.class);

    private final ActionParser actionParser;

    public DefaultCommandParser(ActionParser actionParser) {
        super(Command.class);
        this.actionParser = actionParser;
    }

    @Override
    public ActionParser getActionParser() {
        return actionParser;
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
        LOGGER.debug("Parsing Command {}", command.getId());

        Resolver<List<String>> names = getNamesResolver(command);

        CommandFilter filter;
        if(command.getFilter() == null) {
            filter = CommandFilter.DEFAULT;
        } else {
            DTDCommandFilter xmlTrigger = command.getFilter();

            boolean enabled = BooleanUtils.isNotFalse(xmlTrigger.isEnabled());

            filter = new CommandFilter(enabled);
        }

        LOGGER.debug("\tEnabled: {}", filter.isEnabled());

        ActionOptions actionOptions;
        if(command.getAction() != null) {
            actionOptions = getActionFromXml(xml, command, command.getAction());
        } else if(command.getActions() != null) {
            actionOptions = null;
        } else {
            throw new BotConfigurationException("No Action or Actions defined for command " + command.getId());
        }

        return new Command(command.getId(), names, filter, actionOptions);
    }

    protected ActionOptions getActionFromXml(DTDBot xml, DTDCommand command, DTDCommandAction action) {
        Action a = actionParser.fromXml(xml, command, action);
        return new ActionOptions(Collections.singletonList(new ActionOption(null, a)), null);
    }

    protected Resolver<List<String>> getNamesResolver(DTDCommand command) {
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
