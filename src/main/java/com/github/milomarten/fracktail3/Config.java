package com.github.milomarten.fracktail3;

import com.github.milomarten.fracktail3.magic.platform.Person;
import com.github.milomarten.fracktail3.magic.platform.formatting.SemanticMessage;
import com.github.milomarten.fracktail3.magic.platform.formatting.StdIntent;
import com.github.milomarten.fracktail3.magic.platform.formatting.TimestampIntent;
import com.github.milomarten.fracktail3.spring.command.annotation.Command;
import com.github.milomarten.fracktail3.spring.command.annotation.ForPlatform;
import com.github.milomarten.fracktail3.spring.command.annotation.InjectPerson;
import com.github.milomarten.fracktail3.spring.command.annotation.Parameter;
import com.github.milomarten.fracktail3.twitch.platform.TwitchPlatform;
import org.springframework.context.annotation.Configuration;

import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

@Configuration
public class Config {
    @Command
    public String donate = "Don't donate here: https://streamlabs.com/milo_marten/tip";

    @Command
    public String twitter = "Get stream alerts here! https://twitter.com/marten_streams";

    @Command
    @ForPlatform(TwitchPlatform.class)
    public String discord = "We have a Discord server, if you'd like to hang! https://discord.gg/FUSEEcqPFD";

    @Command
    public String lego = "My birthday is soon, so we won't be buying any Lego sets until after my birthday!";

    @Command
    public String rafo = "Rafo!";

    @Command
    public String dook = "Rafo?";

    @Command
    public SemanticMessage egg(@InjectPerson Person user) {
        return SemanticMessage.create(StdIntent.ROLEPLAY, "Hands " + user.getName() + " an egg.");
    }

    @Command
    public String cats = "We now have five cats: Magpie, Oz, August, Voxel, and Peach. Voxel and Peach are our newest kittens, and very big troublemakers. Spot is not technically our cat, but he has adopted us as his food givers.";

    @Command
    public String magpie = "Magpie is our special princess cat who visits stream sometimes. She is a grouchy old lady (11 years old) who very much wants you to pet her.";

    @Command
    public String august = "August is our middle cat, and one of our biggest troublemakers. He has recently been diagnosed with FLUTD, which requires a special diet and a lot of vet trips. He is golden and has a very very tiny meow.";

    @Command
    public String oz = "Oz is the second-oldest cat, and terrified of everything. He is part Siamese, and is perfectly happy just napping and hiding under furniture in the house. He has a surprising taste for human food that the other cats don't have.";

    @Command
    public String voxel = "Voxel is one of our newest kittens. He is extremely chill, and loves to sleep. Often, we will catch him on the roof of my office. We are not sure how he gets up there.";

    @Command
    public String peach = "Peach is one of our newest kittens. She tends to always get into mischief. She likes to catch cicadas and bring them into the house (still alive).";

    @Command
    public String spot = "Spot is a stray cat who hangs out at our place. He is Peach and Voxel's father, we are almost certain. Every evening he will come to our back porch and scream for food whenever he sees us.";

    @Command
    public String kittens = "Kitten pics -> https://imgur.com/18piqXC";

    @Command
    public String luna = "Luna is Andi's precious princess lady with a russian accent. She is 3 and a half years old and perfect.";

    @Command
    public SemanticMessage timestamp(@Parameter(0) String str, @Parameter(value = 1, optional = true) Optional<String> formatChar) {
        TimestampIntent.Format format = formatChar
                .map(String::toUpperCase)
                .map(TimestampIntent.Format::valueOf)
                .orElse(TimestampIntent.Format.DEFAULT);

        return TimeFormat.parseBest(str, TimeFormat.FORMATS)
                .map(zdt -> SemanticMessage.create(new TimestampIntent(format), zdt.toInstant().toEpochMilli()))
                .orElseGet(() -> SemanticMessage.create("Unexpected format. Should be ISO Format, either as\n" +
                        "a date (YYYY-MM-DD), assuming midnight\n" +
                        "a time (HH:MM<:SS>), assuming today\n" +
                        "a date and time (YYYY-MM-DDTHH:MM:SS), assuming the default timezone, or\n" +
                        "a date, time, and offset (YYYY-MM-DDTHH:MM:SSZ+OO:OO).\n\nTimezone is assumed to be " +
                        TimeFormat.DEFAULT.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "."));
    }

    @Command
    public SemanticMessage countdown(@Parameter(0) String str) {
        return TimeFormat.parseBest(str, TimeFormat.FORMATS)
                .map(zdt -> SemanticMessage.create(new TimestampIntent(TimestampIntent.Format.RELATIVE), zdt.toInstant().toEpochMilli()))
                .orElseGet(() -> SemanticMessage.create("Unexpected format. Should be ISO Format, either as\n" +
                        "a date (YYYY-MM-DD), assuming midnight\n" +
                        "a time (HH:MM<:SS>), assuming today\n" +
                        "a date and time (YYYY-MM-DDTHH:MM:SS), assuming the default timezone, or\n" +
                        "a date, time, and offset (YYYY-MM-DDTHH:MM:SSZ+OO:OO).\n\nTimezone is assumed to be " +
                        TimeFormat.DEFAULT.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "."));
    }
}
