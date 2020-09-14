package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.filterset.user.DiscordUserset;
import com.github.lucbui.fracktail3.magic.filterset.user.Userset;
import com.github.lucbui.fracktail3.magic.filterset.user.Usersets;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDDiscordUserset;
import com.github.lucbui.fracktail3.xsd.DTDUserset;
import discord4j.core.object.util.Snowflake;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultUsersetParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUsersetParser.class);

    public Usersets fromXml(DTDBot xml) {
        Map<String, Userset> roles = new HashMap<>();
        LOGGER.debug("Parsing Roleset List");

        if(xml.getConfiguration() != null &&
                xml.getConfiguration().getDiscord() != null &&
                xml.getConfiguration().getDiscord().getOwner() != null) {
            Snowflake owner = Snowflake.of(xml.getConfiguration().getDiscord().getOwner().getValue());
            Userset userset = DiscordUserset.forUser("owner", owner);
            LOGGER.debug("Creating Roleset owner");
            roles.put(userset.getName(), userset);
        }

        if(xml.getUsersets() != null && xml.getUsersets().getUserset() != null) {
            for (DTDUserset set : xml.getUsersets().getUserset()) {
                Userset userset;
                if (set.getDiscord() != null) {
                    userset = fromXml(xml, set, set.getDiscord());
                } else {
                    throw new BotConfigurationException("No userset specified");
                }

                Userset oldSet = roles.put(userset.getName(), userset);
                if (LOGGER.isDebugEnabled() && oldSet != null) {
                    LOGGER.debug("Replacing set {}", oldSet);
                    if (userset.getExtends().isPresent()) {
                        LOGGER.debug("Creating {} Roleset {} (extends {})", userset.isBlacklist() ? "blacklist" : "whitelist", userset.getName(), userset.getExtends());
                    } else {
                        LOGGER.debug("Creating {} Roleset {}", userset.isBlacklist() ? "blacklist" : "whitelist", userset.getName());
                    }
                }
            }
        }

        return new Usersets(roles);
    }

    protected DiscordUserset fromXml(DTDBot xml, DTDUserset set, DTDDiscordUserset discord) {
        DiscordUserset drv = new DiscordUserset(
                set.getName(),
                BooleanUtils.isTrue(set.isBlacklist()),
                StringUtils.defaultIfBlank(set.getExtends(), null));
        if(discord.getUsers() != null && CollectionUtils.isNotEmpty(discord.getUsers().getSnowflake())) {
            Set<Snowflake> userIds = discord.getUsers().getSnowflake().stream()
                    .map(dtd -> Snowflake.of(dtd.getValue()))
                    .collect(Collectors.toSet());
            drv.setUserSnowflakes(userIds);
        }

        if(discord.getRoles() != null && CollectionUtils.isNotEmpty(discord.getRoles().getSnowflake())) {
            Set<Snowflake> userIds = discord.getRoles().getSnowflake().stream()
                    .map(dtd -> Snowflake.of(dtd.getValue()))
                    .collect(Collectors.toSet());
            drv.setRoleSnowflakes(userIds);
        }
        return drv;
    }
}
