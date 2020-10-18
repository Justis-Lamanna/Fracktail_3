package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.magic.handlers.Command;
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
                .withCommand(
                    new Command.Builder("hello")
                    .withAction(new RespondAction(null, FormattedString.literal("Hey!")))
                )
                .build();
        bot.start().block();
    }
}
