package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.config.GlobalConfiguration;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.DTDConfiguration;
import com.github.lucbui.fracktail3.xsd.DTDDiscordConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
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
    private static final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    private Environment environment;

    private BeanFactory beanFactory;

    public Bot fromXml(DTDBot xml) {
        Objects.requireNonNull(xml);
        Bot bot = new Bot();
        if(xml.getConfiguration() != null) {
            if(xml.getConfiguration().getGlobal() != null) {
                DTDConfiguration global = xml.getConfiguration().getGlobal();
                bot.globalConfig = new GlobalConfiguration(global.getI18N());
            }
            if(xml.getConfiguration().getDiscord() != null) {
                DTDDiscordConfiguration discord = xml.getConfiguration().getDiscord();
                String token = parseToken(discord.getToken());
                if(StringUtils.isBlank(token)) {
                    throw new BotConfigurationException("Token must be non-null and non-blank");
                }
                bot.discordConfig = new DiscordConfiguration(
                        bot.globalConfig,
                        token,
                        discord.getPrefix(),
                        discord.getI18N());
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
