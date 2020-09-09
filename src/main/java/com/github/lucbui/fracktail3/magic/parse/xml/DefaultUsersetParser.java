package com.github.lucbui.fracktail3.magic.parse.xml;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.role.DefaultDiscordRolesetValidator;
import com.github.lucbui.fracktail3.magic.role.DiscordRolesetValidator;
import com.github.lucbui.fracktail3.magic.role.Userset;
import com.github.lucbui.fracktail3.magic.role.Usersets;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDDiscordUserset;
import com.github.lucbui.fracktail3.xsd.DTDUserset;
import discord4j.core.object.util.Snowflake;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultUsersetParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUsersetParser.class);
    private static final ExpressionParser parser = new SpelExpressionParser();

    public Usersets fromXml(DTDBot xml) {
        Map<String, Userset> roles = new HashMap<>();
        LOGGER.debug("Parsing Roleset List");

        if(xml.getConfiguration() != null &&
                xml.getConfiguration().getDiscord() != null &&
                xml.getConfiguration().getDiscord().getOwner() != null) {
            Snowflake owner = Snowflake.of(xml.getConfiguration().getDiscord().getOwner());
            Userset userset = new Userset("owner", DefaultDiscordRolesetValidator.forUser(owner));
            LOGGER.debug("Creating Roleset owner");
            roles.put(userset.getName(), userset);
        }

        if(xml.getUsersets() != null && xml.getUsersets().getUserset() != null) {
            for (DTDUserset set : xml.getUsersets().getUserset()) {
                Userset userset = new Userset(set.getName(), BooleanUtils.isTrue(set.isBlacklist()), StringUtils.defaultIfBlank(set.getExtends(), null));

                if (LOGGER.isDebugEnabled()) {
                    if (userset.getExtends() == null) {
                        LOGGER.debug("Creating {} Roleset {}", userset.isBlacklist() ? "blacklist" : "whitelist", userset.getName());
                    } else {
                        LOGGER.debug("Creating {} Roleset {} (extends {})", userset.isBlacklist() ? "blacklist" : "whitelist", userset.getName(), userset.getExtends());
                    }
                }

                if (set.getDiscord() != null) {
                    userset.setDiscordRolesetValidator(fromXml(xml, set, set.getDiscord()));
                }

                Userset oldSet = roles.put(userset.getName(), userset);
                if (LOGGER.isDebugEnabled() && oldSet != null) {
                    LOGGER.debug("Replacing set {}", oldSet);
                }
                validateNonRecursiveExtends(roles, Collections.singleton(userset.getName()), userset);
            }
        }

        validateRoleExtensionsExist(roles);

        return new Usersets(roles);
    }

    protected DiscordRolesetValidator fromXml(DTDBot xml, DTDUserset set, DTDDiscordUserset discord) {
        DefaultDiscordRolesetValidator validator = new DefaultDiscordRolesetValidator();
        if(CollectionUtils.isNotEmpty(discord.getSnowflakes())) {
            validator.setLegalSnowflakes(
                    discord.getSnowflakes().stream()
                        .map(DTDDiscordUserset.Snowflakes::getSnowflake)
                        .map(Snowflake::of)
                        .collect(Collectors.toSet()));
        }
        if(CollectionUtils.isNotEmpty(discord.getRoles())) {
            validator.setLegalRoles(
                    discord.getRoles().stream()
                            .map(DTDDiscordUserset.Roles::getRole)
                            .map(Snowflake::of)
                            .collect(Collectors.toSet()));
        }

        return validator;
    }

    protected void validateRoleExtensionsExist(Map<String, Userset> roles) {
        for(Userset set : roles.values()) {
            if(StringUtils.isNotBlank(set.getExtends()) && !roles.containsKey(set.getExtends())) {
                throw new BotConfigurationException("Role " + set.getName() + " extends unknown role " + set.getExtends());
            }
        }
    }

    protected void validateNonRecursiveExtends(Map<String, Userset> roles, Set<String> encounteredRolesets, Userset userset) {
        if(StringUtils.isNotBlank(userset.getExtends())) {
            Userset extension = roles.get(userset.getExtends());
            if(extension != null){
                if(encounteredRolesets.contains(extension.getName())) {
                    String chain = String.join("->", encounteredRolesets);
                    throw new BotConfigurationException("Circular dependency detected for roleset: " + chain + "->" + userset.getExtends());
                } else {
                    HashSet<String> newSet = new LinkedHashSet<>(encounteredRolesets);
                    newSet.add(extension.getName());
                    validateNonRecursiveExtends(roles, newSet, extension);
                }
            }
        }
    }
}
