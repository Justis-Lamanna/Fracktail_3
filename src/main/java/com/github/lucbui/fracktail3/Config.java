package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfigurationBuilder;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHook;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHookStoreBuilder2;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.github.lucbui.fracktail3.spring.annotation.Name;
import com.github.lucbui.fracktail3.spring.plugin.Plugin;
import com.github.lucbui.fracktail3.spring.plugin.command.CommandLookupPlugin;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import groovy.util.Eval;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.OptionalInt;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
@EnableScheduling
public class Config {
    //Pattern which provides the following:
    //Group 1 - Number of Dice
    //Group 2 - Dice to roll
    //Group 3 - Keep amount
    private final Pattern DICE_ROLL_PATTERN = Pattern.compile("([0-9]+)?d([0-9]+)(k([0-9]+))?");
    private final Pattern VALID_EXPRESSION_REGEX = Pattern.compile("[0-9+\\-*/(). ]+");

    @Bean
    public Platform discord(@Value("${token}") String token) {
        return new DiscordPlatform.Builder()
            .withConfiguration(new DiscordConfigurationBuilder(token)
            .withPrefix("!")
            .withOwner(248612704019808258L)
            .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
            .withHandlers(new DiscordEventHookStoreBuilder2()
                    .withHook(new DiscordEventHook<>("rer", new RerHook()))
            ))
            .build();
    }

    @Command
    @Name("r")
    public Mono<Void> roll(CommandUseContext<?> ctx) {
        //1. Replace all rolls with a calculated number.
        Matcher matcher = DICE_ROLL_PATTERN.matcher(ctx.getRawParameters().trim());
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            int dice = parse(matcher.group(2));
            int numDice = parseOpt(matcher.group(1)).orElse(1);
            int keep = parseOpt(matcher.group(4)).orElse(numDice);
            matcher.appendReplacement(sb, roll(dice, numDice, keep));
        }
        matcher.appendTail(sb);

        String expression = StringUtils.deleteWhitespace(sb.toString());
        if(!VALID_EXPRESSION_REGEX.matcher(expression).matches()) {
            return ctx.respond("Sorry, I didn't understand that expression.");
        }

        Object o;
        try {
            o = Eval.me(expression);
        } catch (Exception ex) {
            return ctx.respond("Something went wrong evaluating that. Check your syntax!");
        }

        String result;
        if(o instanceof Integer) {
            result = Integer.toString((Integer) o);
        } else if(o instanceof BigDecimal) {
            result = ((BigDecimal)o).setScale(2, RoundingMode.FLOOR).toString();
        } else {
            result = o.toString();
        }

        return ctx.respond(expression + " = " + FormatUtils.bold(result));
    }

    private String roll(int dice, int numDice, int keep) {
        Random random = new Random();
        String rolls = IntStream.range(0, numDice)
                .map(x -> random.nextInt(dice) + 1)
                .boxed()
                .sorted(Comparator.reverseOrder()) //Max to Min
                .limit(keep)
                .map(i -> Integer.toString(i))
                .collect(Collectors.joining("+"));
        return "(" + rolls + ")";
    }

    private OptionalInt parseOpt(String s) {
        if(s == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(Integer.parseInt(s));
    }

    private int parse(String s) {
        if(s == null) {
            throw new NumberFormatException("Number cannot be blank");
        }
        return Integer.parseInt(s);
    }

    private String prettifyExpression(String raw) {
        String trimmed = StringUtils.deleteWhitespace(raw);
        return trimmed;
    }

    @Bean
    public Plugin clp() {
        return new CommandLookupPlugin();
    }
}
