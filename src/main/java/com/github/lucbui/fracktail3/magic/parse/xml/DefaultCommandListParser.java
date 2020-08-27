package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.Behavior;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDCommandList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultCommandListParser extends AbstractParser<CommandList> implements CommandListParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommandListParser.class);

    private final CommandParser commandParser;

    public DefaultCommandListParser(CommandParser commandParser) {
        super(CommandList.class);
        this.commandParser = commandParser;
    }

    @Override
    public CommandList fromXml(Bot bot, DTDBot xml) {
        DTDCommandList commandList = xml.getCommands();
        if(commandList.getCustom() != null) {
            return getFromCustom(commandList.getCustom());
        } else {
            return getCommandListFromXml(bot, xml);
        }
    }

    protected CommandList getCommandListFromXml(Bot bot, DTDBot xml) {
        LOGGER.debug("Parsing command list");
        List<Command> commands = xml.getCommands().getCommand().stream()
                .map(dtdCommand -> commandParser.fromXml(xml, dtdCommand))
                .collect(Collectors.toList());
        commands.forEach(c -> validateCommand(bot, c));
        if (xml.getCommands().getOrElse() != null) {
            return new CommandList(commands, commandParser.getActionParser()
                    .fromXml(xml, null, null, xml.getCommands().getOrElse().getAction()));
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
}