package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.*;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.magic.handlers.action.RespondAction;
import com.github.lucbui.fracktail3.magic.resolver.CompositeResolver;
import com.github.lucbui.fracktail3.magic.resolver.I18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.ListFromI18NResolver;
import com.github.lucbui.fracktail3.magic.resolver.Resolver;
import com.github.lucbui.fracktail3.xsd.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandListParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListParser.class);

    public CommandList fromXml(Bot bot, DTDBot xml) {
        LOGGER.debug("Parsing command list");
        List<Command> commands = xml.getCommands().getCommand().stream()
                .map(dtdCommand -> fromXml(xml, dtdCommand))
                .collect(Collectors.toList());
        commands.forEach(c -> validateCommand(bot, c));
        if(xml.getCommands().getOrElse() != null) {
            return new CommandList(commands, fromXml(xml, null, null, xml.getCommands().getOrElse().getAction()));
        } else {
            return new CommandList(commands, null);
        }
    }

    private void validateCommand(Bot bot, Command c) {
        if(c.hasRoleRestriction()) {
            bot.getRolesets().flatMap(r -> r.getRoleset(c.getRole()))
                    .orElseThrow(() -> new BotConfigurationException("Command " + c.getName() + " contains unknown role " + c.getRole()));
        }
        c.getBehaviors().forEach(b -> validateBehavior(bot, c, b));
    }

    private void validateBehavior(Bot bot, Command c, Behavior b) {
        if(b.hasRoleRestriction()) {
            bot.getRolesets().flatMap(r -> r.getRoleset(b.getRole()))
                    .orElseThrow(() -> new BotConfigurationException("Behavior in " + c.getName() + " contains unknown role " + b.getRole()));
        }
    }

    private Command fromXml(DTDBot xml, DTDCommand command) {
        Resolver<String> name = fromI18NString(command.getName());
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug(getDebugString("Name", command.getName()));
        }

        Resolver<List<String>> aliases;
        if(StringUtils.isNotBlank(command.getAliasesFrom())) {
            aliases = new ListFromI18NResolver(command.getAliasesFrom());
            LOGGER.debug("Aliases From Key: {}", command.getAliasesFrom());
        } else if(command.getAliases() != null){
            aliases = command.getAliases().getAlias().stream()
                    .map(this::fromI18NString)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), CompositeResolver::new));
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(command.getAliases().getAlias().stream()
                        .map(i18n -> getDebugString("Alias", i18n))
                        .collect(Collectors.joining(",")));
            }
        } else {
            aliases = Resolver.identity(Collections.emptyList());
        }

        String role = command.getRole() == null ? null : command.getRole().getValue();

        List<Behavior> behaviors = command.getBehaviors().getBehavior().stream()
                .map(s -> fromXml(xml, command, s))
                .collect(Collectors.toList());
        if(command.getBehaviors().getOrElse() != null) {
            return new Command(name, aliases, role, behaviors,
                    fromXml(xml, command, null, command.getBehaviors().getOrElse().getAction()));
        } else {
            return new Command(name, aliases, role, behaviors, null);
        }
    }

    private Behavior fromXml(DTDBot xml, DTDCommand command, DTDBehavior behavior) {
        DTDBehavior.Parameters parameters = behavior.getParameters();
        NamedParametersConfiguration npConfig = new NamedParametersConfiguration();
        int totalParameters;
        if(parameters != null && CollectionUtils.isNotEmpty(parameters.getParameter())) {
            int highestParameterIdx = -1;
            for (DTDParameter param : parameters.getParameter()) {
                String name = param.getValue();
                if (npConfig.hasKey(name)) {
                    throw new BotConfigurationException("Two parameters with same name: " + name);
                }
                int start;
                int end;
                if (param.getIndex() != null) {
                    start = end = param.getIndex().intValue();
                } else {
                    start = param.getStartIndex() == null ? 0 : param.getStartIndex().intValue();
                    end = param.getEndIndex().equalsIgnoreCase("unbounded") ? -1 : Integer.parseInt(param.getEndIndex());
                }
                if (end > highestParameterIdx) {
                    highestParameterIdx = end;
                }
                npConfig.add(name, Range.fromTo(start, end));
            }
            totalParameters = highestParameterIdx + 1;
        } else {
            totalParameters = 0;
        }

        String role = behavior.getRole() == null ? null : behavior.getRole().getValue();

        return new Behavior(totalParameters, fromXml(xml, command, behavior, behavior.getAction()), role, npConfig);
    }

    private Action fromXml(DTDBot xml, DTDCommand command, DTDBehavior behavior, DTDAction action) {
        if(behavior != null) {
            return new RespondAction();
        }
        return null;
    }

    private String getDebugString(String type, I18NString string) {
        return BooleanUtils.isTrue(string.isI18N()) ?
            type + " From Key: " + string.getValue() :
            type + ": " + string.getValue();
    }

    private Resolver<String> fromI18NString(I18NString string) {
        return BooleanUtils.isTrue(string.isI18N()) ?
                new I18NResolver(string.getValue()) :
                Resolver.identity(string.getValue());
    }
}
