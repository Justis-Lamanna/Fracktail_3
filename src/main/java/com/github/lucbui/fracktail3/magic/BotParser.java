package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.config.GlobalConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandListDiscordHandler;
import com.github.lucbui.fracktail3.magic.resolver.ExpressionResolver;
import com.github.lucbui.fracktail3.magic.resolver.IdentityExpressionResolver;
import com.github.lucbui.fracktail3.magic.role.Rolesets;
import com.github.lucbui.fracktail3.magic.utils.PresenceUtils;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDConfiguration;
import com.github.lucbui.fracktail3.xsd.DTDDiscordConfiguration;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class BotParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotParser.class);

    private ExpressionResolver expressionResolver;

    private CommandListParser commandListParser;

    private RolesetParser rolesetParser;

    public BotParser() {
        expressionResolver = new IdentityExpressionResolver();
        commandListParser = new CommandListParser();
        rolesetParser = new RolesetParser();
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }

    public void setCommandListParser(CommandListParser commandListParser) {
        this.commandListParser = commandListParser;
    }

    public void setRolesetParser(RolesetParser rolesetParser) {
        this.rolesetParser = rolesetParser;
    }

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
                DTDConfiguration global = xml.getConfiguration().getGlobal();
                if(StringUtils.isNotEmpty(global.getI18N())) {
                    LOGGER.debug("I18N: " + global.getI18N());
                }
                bot.setGlobalConfig(new GlobalConfiguration(global.getI18N()));
            }
            if(xml.getConfiguration().getDiscord() != null) {
                LOGGER.debug("Initializing Discord config");
                DTDDiscordConfiguration discord = xml.getConfiguration().getDiscord();
                String token = expressionResolver.parseExpression(discord.getToken());
                if(StringUtils.isBlank(token)) {
                    throw new BotConfigurationException("Token must be non-null and non-blank");
                }

                Presence presence = PresenceUtils.getPresence(discord.getPresence());
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Token: {}", token);
                    LOGGER.debug("Prefix: {}", discord.getPrefix());
                    if(StringUtils.isNotEmpty(discord.getI18N())) {
                        LOGGER.debug("I18N: " + discord.getI18N());
                    }
                    LOGGER.debug("Setting Presence to {}, Activity Type to {}, Activity Text to {}",
                            presence.getStatus(),
                            presence.getActivity()
                                    .map(Activity::getType)
                                    .map(Enum::name)
                                    .orElse("None"),
                            presence.getActivity()
                                    .map(a -> a.getStreamingUrl()
                                            .map(url -> a.getName() + "(url=" + url + ")")
                                            .orElseGet(a::getName))
                                    .orElse("None"));
                }

                bot.setDiscordConfig(new DiscordConfiguration(
                        bot.getGlobalConfiguration().orElse(null),
                        token,
                        discord.getPrefix(),
                        discord.getI18N(),
                        presence));

                bot.setDiscordHandler(new CommandListDiscordHandler(commandList));
            }
        }
        return bot;
    }
}
