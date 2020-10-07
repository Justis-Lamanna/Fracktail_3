package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.guards.user.InUsersetGuard;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.action.ActionOptions;
import com.github.lucbui.fracktail3.magic.handlers.action.CommandsAction;
import com.github.lucbui.fracktail3.magic.handlers.action.HelpAction;
import com.github.lucbui.fracktail3.magic.handlers.action.RespondAction;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DiscordBotRunner implements CommandLineRunner {
    @Autowired
    private DiscordPlatform platform;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(String... args) throws Exception {
        Bot bot = new BotCreator()
                .withPlatform(platform)
                .withScheduler(scheduler)
                .withCommand(new Command("help", new HelpAction()))
                .withCommand(new Command("commands", new CommandsAction()))
                .withCommand(
                    new Command.Builder("hello")
                    .withAction(new ActionOptions.Builder()
                            .with(DiscordConfiguration.OWNER_GUARD, new RespondAction("Hello, beautiful owner."))
                            .with(new InUsersetGuard("steven"), new RespondAction("Hello, stinky."))
                            .orElseDo(new RespondAction("Hello, {at_user}!")))
                )
                .build();
        bot.start().block();
    }
}
