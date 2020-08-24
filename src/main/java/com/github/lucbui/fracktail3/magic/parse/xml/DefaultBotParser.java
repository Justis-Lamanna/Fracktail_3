package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.RolesetParser;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandListDiscordHandler;
import com.github.lucbui.fracktail3.magic.resolver.ExpressionResolver;
import com.github.lucbui.fracktail3.magic.resolver.IdentityExpressionResolver;
import com.github.lucbui.fracktail3.magic.role.Rolesets;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DefaultBotParser implements BotParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBotParser.class);

    protected ExpressionResolver expressionResolver;
    protected CommandListParser commandListParser;
    protected RolesetParser rolesetParser;
    protected ConfigParser configParser;

    public DefaultBotParser() {
        expressionResolver = new IdentityExpressionResolver();
        configParser = new DefaultGlobalConfigParser(expressionResolver);
        commandListParser = new DefaultCommandListParser(
                new DefaultCommandParser(
                        new DefaultBehaviorParser(
                                new DefaultActionParser())));
        rolesetParser = new RolesetParser();
    }

    protected DefaultBotParser(
            ExpressionResolver expressionResolver,
            ConfigParser configParser,
            CommandListParser commandListParser,
            RolesetParser rolesetParser) {
        this.expressionResolver = expressionResolver;
        this.configParser = configParser;
        this.commandListParser = commandListParser;
        this.rolesetParser = rolesetParser;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
        this.configParser = new DefaultGlobalConfigParser(expressionResolver);
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
