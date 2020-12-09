package com.github.lucbui.fracktail3.dnd.harrowbot;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.dnd.AbilityScore;
import com.github.lucbui.fracktail3.spring.annotation.Command;
import com.github.lucbui.fracktail3.spring.annotation.ForPlatform;
import com.github.lucbui.fracktail3.spring.annotation.Payload;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.rest.util.Color;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
public class HarrowBot {
    public Harrowing<HarrowCard> perform(AbilityScore choosing) {
        return Harrowing.perform(choosing, new StandardHarrowDeck());
    }

    public Harrowing<HarrowCard> perform() {
        return Harrowing.perform(null, new StandardHarrowDeck());
    }

    @Command
    @ForPlatform(DiscordPlatform.class)
    public Mono<Void> harrow(@Payload MessageCreateEvent event) {
        Harrowing<HarrowCard> harrowing = perform();

        return event.getMessage().getChannel()
                .flatMap(mc -> mc.createEmbed(embed -> {
                    embed.setTitle("A harrowing, eh? Here's your fortune...");
                    embed.setDescription("First three values represent the past. Second three represent the present, and the last represent the future.");
                    embed.setColor(Color.SEA_GREEN);
                    embed.setUrl("https://pathfinderwiki.com/wiki/Harrow");
                    embed.setTimestamp(Instant.now());
                    embed.setFooter("\uD83D\uDD28 - Strength | \uD83D\uDD11 - Dexterity | \uD83D\uDEE1 - Constitution | \uD83D\uDCDA - Intelligence | \uD83C\uDF1F - Wisdom | \uD83D\uDC51 - Charisma", null);
                    harrowing.getPulls().forEach(card -> {
                        String title = card.getName() + " (" + normalizeScore(card.getScore()) + card.getAlignment().getShortName() + ")";
                        embed.addField(title,  card.getDescription(), true);
                    });
                }))
                .then();
    }

    private String normalizeScore(AbilityScore score) {
        switch(score) {
            case STRENGTH: return "\uD83D\uDD28";
            case DEXTERITY: return "\uD83D\uDD11";
            case CONSTITUTION: return "\uD83D\uDEE1";
            case INTELLIGENCE: return "\uD83D\uDCDA";
            case WISDOM: return "\uD83C\uDF1F";
            case CHARISMA: return "\uD83D\uDC51";
            default: return "";
        }
    }
}
