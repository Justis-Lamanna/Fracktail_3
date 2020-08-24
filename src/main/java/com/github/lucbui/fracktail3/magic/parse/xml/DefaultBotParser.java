package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandListDiscordHandler;
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

    protected DefaultBotParser(
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

    public void setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
        this.configParser = new DefaultConfigParser(expressionParser);
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
    public Bot fromXml(DTDBot xml) {
        Objects.requireNonNull(xml);
        Bot bot = new Bot();

        if(xml.getRolesets() != null) {
            Rolesets rolesets = rolesetParser.fromXml(xml);
            bot.setRolesets(rolesets);
        }

        CommandList commandList = commandListParser.fromXml(bot, xml);
        if(xml.getConfiguration() != null) {
            if(xml.getConfiguration().getGlobal() != null) {
                LOGGER.debug("Initializing Global config");
                bot.setGlobalConfig(
                        configParser.globalFromXml(bot, xml.getConfiguration().getGlobal()));
            }
            if(xml.getConfiguration().getDiscord() != null) {
                LOGGER.debug("Initializing Discord config");
                bot.setDiscordConfig(
                        configParser.discordFromXml(bot, xml.getConfiguration().getDiscord()));

                bot.setDiscordHandler(new CommandListDiscordHandler(commandList));
            }
        }
        return bot;
    }
}
