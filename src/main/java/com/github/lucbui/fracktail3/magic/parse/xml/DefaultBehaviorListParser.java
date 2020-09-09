package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.action.Action;
import com.github.lucbui.fracktail3.xsd.DTDBehavior;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DefaultBehaviorListParser implements BehaviorListParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBehaviorListParser.class);

    private final CommandParser commandParser;

    public DefaultBehaviorListParser(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    @Override
    public BehaviorList fromXml(BotSpec botSpec, DTDBot xml) {
        LOGGER.debug("Parsing command list");
        List<Command> commands = new ArrayList<>();
        for(DTDBehavior behavior : xml.getBehaviors().getBehavior()) {
            if(behavior.getCommand() != null) {
                Command command = commandParser.fromXml(xml, behavior.getCommand());
                commands.add(command);
            }
        }
        return new BehaviorList(new CommandList(commands, Action.NOOP));
    }
}
