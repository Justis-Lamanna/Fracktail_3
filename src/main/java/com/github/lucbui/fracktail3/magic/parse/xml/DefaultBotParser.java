package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.role.Rolesets;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DefaultBotParser implements BotParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBotParser.class);

    protected ExpressionParser expressionParser;
    protected CommandListParser commandListParser;
    protected RolesetParser rolesetParser;
    protected ConfigParser configParser;

    public DefaultBotParser() {
        expressionParser = new IdentityExpressionResolver();
        configParser = new DefaultConfigParser(expressionParser);
        commandListParser = new DefaultCommandListParser(
                new DefaultCommandParser(
                        new DefaultBehaviorParser(
                                new DefaultActionParser())));
        rolesetParser = new RolesetParser();
    }

    public DefaultBotParser(
            ExpressionParser expressionParser,
            ConfigParser configParser,
            CommandListParser commandListParser,
            RolesetParser rolesetParser) {
        this.expressionParser = expressionParser;
        this.configParser = configParser;
        this.commandListParser = commandListParser;
        this.rolesetParser = rolesetParser;
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public CommandListParser getCommandListParser() {
        return commandListParser;
    }

    public RolesetParser getRolesetParser() {
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
            if(xml.getConfiguration().getGlobal() != null) {
                LOGGER.debug("Initializing Global config");
                botSpec.setGlobalConfig(
                        configParser.globalFromXml(botSpec, xml.getConfiguration().getGlobal()));
            }
            if(xml.getConfiguration().getDiscord() != null) {
                LOGGER.debug("Initializing Discord config");
                botSpec.setDiscordConfig(
                        configParser.discordFromXml(botSpec, xml.getConfiguration().getDiscord()));
            }
        }

        if(xml.getRolesets() != null) {
            Rolesets rolesets = rolesetParser.fromXml(xml);
            botSpec.setRolesets(rolesets);
        }

        CommandList commandList = commandListParser.fromXml(botSpec, xml);
        botSpec.setCommandList(commandList);

        botSpec.validate();
        return botSpec;
    }
}
