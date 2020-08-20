package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.role.DefaultDiscordRolesetValidator;
import com.github.lucbui.fracktail3.magic.role.DiscordRolesetValidator;
import com.github.lucbui.fracktail3.magic.role.Roleset;
import com.github.lucbui.fracktail3.magic.role.Rolesets;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDDiscordRoleset;
import com.github.lucbui.fracktail3.xsd.DTDRoleset;
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

public class RolesetParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(RolesetParser.class);
    private static final ExpressionParser parser = new SpelExpressionParser();

    public Rolesets fromXml(DTDBot xml) {
        Map<String, Roleset> roles = new HashMap<>();
        LOGGER.debug("Parsing Roleset List");
        for (DTDRoleset set : xml.getRolesets().getRoleset()) {
            Roleset roleset = new Roleset(set.getName(), BooleanUtils.isTrue(set.isBlacklist()), StringUtils.defaultIfBlank(set.getExtends(), null));

            if(LOGGER.isDebugEnabled()){
                if(roleset.getExtends() == null) {
                    LOGGER.debug("Creating {} Roleset {}", roleset.isBlacklist() ? "blacklist" : "whitelist", roleset.getName());
                } else {
                    LOGGER.debug("Creating {} Roleset {} (extends {})", roleset.isBlacklist() ? "blacklist" : "whitelist", roleset.getName(), roleset.getExtends());
                }
            }

            if(set.getDiscord() != null) {
                roleset.setDiscordRolesetValidator(fromXml(xml, set, set.getDiscord()));
            }

            roles.put(roleset.getName(), roleset);
            validateNonRecursiveExtends(roles, Collections.singleton(roleset.getName()), roleset);
        }

        validateRoleExtensionsExist(roles);

        return new Rolesets(roles);
    }

    private DiscordRolesetValidator fromXml(DTDBot xml, DTDRoleset set, DTDDiscordRoleset discord) {
        DefaultDiscordRolesetValidator validator = new DefaultDiscordRolesetValidator();
        if(CollectionUtils.isNotEmpty(discord.getSnowflakes())) {
            validator.setLegalSnowflakes(
                    discord.getSnowflakes().stream()
                        .map(DTDDiscordRoleset.Snowflakes::getSnowflake)
                        .map(Snowflake::of)
                        .collect(Collectors.toSet()));
        }
        if(CollectionUtils.isNotEmpty(discord.getRoles())) {
            validator.setLegalRoles(
                    discord.getRoles().stream()
                            .map(DTDDiscordRoleset.Roles::getRole)
                            .map(Snowflake::of)
                            .collect(Collectors.toSet()));
        }

        return validator;
    }

    private void validateRoleExtensionsExist(Map<String, Roleset> roles) {
        for(Roleset set : roles.values()) {
            if(StringUtils.isNotBlank(set.getExtends()) && !roles.containsKey(set.getExtends())) {
                throw new BotConfigurationException("Role " + set.getName() + " extends unknown role " + set.getExtends());
            }
        }
    }

    private void validateNonRecursiveExtends(Map<String, Roleset> roles, Set<String> encounteredRolesets, Roleset roleset) {
        if(StringUtils.isNotBlank(roleset.getExtends())) {
            Roleset extension = roles.get(roleset.getExtends());
            if(extension != null){
                if(encounteredRolesets.contains(extension.getName())) {
                    String chain = String.join("->", encounteredRolesets);
                    throw new BotConfigurationException("Circular dependency detected for roleset: " + chain + "->" + roleset.getExtends());
                } else {
                    HashSet<String> newSet = new LinkedHashSet<>(encounteredRolesets);
                    newSet.add(extension.getName());
                    validateNonRecursiveExtends(roles, newSet, extension);
                }
            }
        }
    }
}
