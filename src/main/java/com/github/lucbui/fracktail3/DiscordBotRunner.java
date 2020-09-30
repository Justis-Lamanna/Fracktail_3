package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.guards.DiscordUserset;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.guards.user.Userset;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.action.ActionOptions;
import com.github.lucbui.fracktail3.magic.handlers.action.CommandsAction;
import com.github.lucbui.fracktail3.magic.handlers.action.HelpAction;
import com.github.lucbui.fracktail3.magic.handlers.action.RespondAction;
import discord4j.common.util.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DiscordBotRunner implements CommandLineRunner {
    @Autowired
    private DiscordPlatform platform;

    @Override
    public void run(String... args) throws Exception {
        Userset steven = DiscordUserset.forUser("steven", Snowflake.of(112005555178000384L));
        Bot bot = new BotCreator()
                .withPlatform(platform)
                .withUserset(steven)
                .withCommand(new Command("help", new HelpAction()))
                .withCommand(new Command("commands", new CommandsAction()))
                .withCommand(
                    new Command.Builder("hello")
                    .withAction(new ActionOptions.Builder()
                            .with(DiscordConfiguration.OWNER_GUARD, new RespondAction("Hello, beautiful owner."))
                            .with(steven.byId(), new RespondAction("Hello, stinky."))
                            .orElseDo(new RespondAction("Hello, {at_user}!")))
                )
                .build();
        bot.start().block();
    }
}
