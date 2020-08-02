package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.resolver.*;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommand;
import com.github.lucbui.fracktail3.xsd.I18NString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommandListParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListParser.class);

    public CommandList fromXml(DTDBot xml) {
        LOGGER.debug("Parsing command list");
        List<Command> commands = xml.getCommands().getCommand().stream()
                .map(dtdCommand -> fromXml(xml, dtdCommand))
                .collect(Collectors.toList());

        return new CommandList(commands);
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
        //TODO: Parse behavior...
        return new Command(name, aliases);
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
