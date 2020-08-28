package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.config.GlobalConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.utils.PresenceUtils;
import com.github.lucbui.fracktail3.xsd.DTDConfiguration;
import com.github.lucbui.fracktail3.xsd.DTDDiscordConfiguration;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultConfigParser implements ConfigParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigParser.class);

    private final ExpressionParser expressionParser;

    public DefaultConfigParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    public GlobalConfiguration globalFromXml(BotSpec botSpec, DTDConfiguration global) {
        if(StringUtils.isNotEmpty(global.getI18N())) {
            LOGGER.debug("I18N: " + global.getI18N());
        }
        return new GlobalConfiguration(global.getI18N());
    }

    @Override
    public DiscordConfiguration discordFromXml(BotSpec botSpec, DTDDiscordConfiguration discord) {
        String token = expressionParser.parseExpression(discord.getToken());
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

        return new DiscordConfiguration(
                botSpec.getGlobalConfiguration().orElse(null),
                token,
                discord.getPrefix(),
                discord.getI18N(),
                presence);
    }
}
