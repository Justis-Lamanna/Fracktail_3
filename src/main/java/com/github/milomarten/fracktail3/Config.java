package com.github.milomarten.fracktail3;

import com.github.milomarten.fracktail3.magic.platform.Person;
import com.github.milomarten.fracktail3.magic.platform.formatting.Intent;
import com.github.milomarten.fracktail3.magic.platform.formatting.SemanticMessage;
import com.github.milomarten.fracktail3.spring.command.annotation.Command;
import com.github.milomarten.fracktail3.spring.command.annotation.ForPlatform;
import com.github.milomarten.fracktail3.spring.command.annotation.InjectPerson;
import com.github.milomarten.fracktail3.twitch.platform.TwitchPlatform;
import org.springframework.context.annotation.Configuration;

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
        return SemanticMessage.create(Intent.ROLEPLAY, "Hands " + user.getName() + " an egg.");
    }

    @Command
    public String cats = "We now have five cats: Magpie, Oz, August, Voxel, and Peach. Voxel and Peach are our newest kittens, and very big troublemakers.";

    @Command
    public String magpie = "Magpie is our special princess cat who visits stream sometimes. She is a grouchy old lady (11 years old) who very much wants you to pet her.";

    @Command
    public String august = "August is our middle cat, and one of our biggest troublemakers. He is the only one who explores the neighborhood around our house, although he always comes home when called. He is golden and has a very very tiny meow.";

    @Command
    public String oz = "Oz is the second-oldest cat, and terrified of everything. He is part Siamese, and is perfectly happy just napping and hiding under furniture in the house. He has a surprising taste for human food that the other cats don't have.";

    @Command
    public String voxel = "Voxel is one of our newest kittens. He is extremely chill, and loves to sleep.";

    @Command
    public String peach = "Peach is one of our newest kittens. She likes to spend her time on the barstool in our kitchen.";

    @Command
    public String kittens = "Kitten pics -> https://imgur.com/18piqXC";
}
