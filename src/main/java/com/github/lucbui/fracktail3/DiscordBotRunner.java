package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.filterset.user.DiscordUserset;
import com.github.lucbui.fracktail3.magic.filterset.user.UsersetById;
import com.github.lucbui.fracktail3.magic.handlers.action.RespondAction;
import com.github.lucbui.fracktail3.magic.handlers.command.Command;
import com.github.lucbui.fracktail3.magic.handlers.command.HelpCommand;
import com.github.lucbui.fracktail3.magic.handlers.platform.discord.DiscordPlatform;
import discord4j.common.util.Snowflake;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DiscordBotRunner implements CommandLineRunner {
    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        String token = environment.getRequiredProperty("token");
        Bot bot = new BotCreator()
                .withConfig(DiscordPlatform.INSTANCE,
                    new DiscordConfiguration.Builder(token)
                            .withPrefix("!")
                            .withOwner(Snowflake.of(248612704019808258L))
                            .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
                            .build()
                )
                .withUserset(DiscordUserset.forUser("steven", Snowflake.of(0L)))
                .withCommand(
                    new Command.Builder("hello")
                    .withFilter(new UsersetById("owner"))
                    .withAction(new RespondAction("Hello, {at_user}!"))
                    .build()
                )
                .withCommand(new HelpCommand())
                .build();
        bot.start().block();
    }
}
