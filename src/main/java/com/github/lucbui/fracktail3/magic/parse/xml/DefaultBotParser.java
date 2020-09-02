package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.BehaviorList;
import com.github.lucbui.fracktail3.magic.role.Usersets;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DefaultBotParser implements BotParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBotParser.class);

    protected ExpressionParser expressionParser;
    protected BehaviorListParser commandListParser;
    protected DefaultUsersetParser rolesetParser;
    protected ConfigParser configParser;

    public DefaultBotParser() {
        expressionParser = new IdentityExpressionResolver();
        configParser = new DefaultConfigParser(expressionParser);
        commandListParser = new DefaultBehaviorListParser(
                new DefaultCommandParser(new DefaultActionParser()));
        rolesetParser = new DefaultUsersetParser();
    }

    public DefaultBotParser(
            ExpressionParser expressionParser,
            ConfigParser configParser,
            BehaviorListParser commandListParser,
            DefaultUsersetParser rolesetParser) {
        this.expressionParser = expressionParser;
        this.configParser = configParser;
        this.commandListParser = commandListParser;
        this.rolesetParser = rolesetParser;
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public BehaviorListParser getCommandListParser() {
        return commandListParser;
    }

    public DefaultUsersetParser getRolesetParser() {
        return rolesetParser;
    }

    public ConfigParser getConfigParser() {
        return configParser;
    }

    @Override
    public BotSpec fromXml(DTDBot xml) {
        Objects.requireNonNull(xml);
        BotSpec botSpec = new BotSpec();

        if(xml.getConfiguration() != null) {
            if(xml.getConfiguration().getDiscord() != null) {
                LOGGER.debug("Initializing Discord config");
                botSpec.setDiscordConfig(
                        configParser.discordFromXml(botSpec, xml.getConfiguration().getDiscord()));
            }
        }

        Usersets usersets = rolesetParser.fromXml(xml);
        botSpec.setUsersets(usersets);

        BehaviorList commandList = commandListParser.fromXml(botSpec, xml);
        botSpec.setBehaviorList(commandList);

        botSpec.validate();
        return botSpec;
    }
}
