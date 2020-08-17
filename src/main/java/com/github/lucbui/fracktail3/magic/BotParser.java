package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.config.GlobalConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.handlers.CommandList;
import com.github.lucbui.fracktail3.magic.handlers.discord.CommandListDiscordHandler;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BotParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotParser.class);
    private static final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    private Environment environment;

    @Autowired
    private CommandListParser commandListParser;

    @Autowired
    private RolesetParser rolesetParser;

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
                String token = parseToken(discord.getToken());
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

    private String parseToken(String token) {
        String resolvedToken = environment.resolvePlaceholders(token);
        final TemplateParserContext templateContext = new TemplateParserContext();
        Expression expression = parser.parseExpression(resolvedToken, templateContext);
        return expression.getValue(String.class);
    }
}
