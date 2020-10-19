package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfigurationBuilder;
import com.github.lucbui.fracktail3.discord.guards.DiscordUserset;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.handlers.Command;
import com.github.lucbui.fracktail3.magic.handlers.action.LoggingAction;
import com.github.lucbui.fracktail3.magic.schedule.Scheduler;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DiscordBotRunner implements CommandLineRunner {
    @Value("${token}")
    private String token;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void run(String... args) throws Exception {
        Bot bot = new BotCreator()
                .withPlatform(new DiscordPlatform.Builder()
                .withConfiguration(new DiscordConfigurationBuilder(token)
                .withPrefix("!")
                .withOwner(248612704019808258L)
                .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
                .withUserset(DiscordUserset.forUser("steven", 112005555178000384L))))
                .withScheduler(scheduler)
                .withCommand(new Command.Builder("hello").withAction(new LoggingAction("Hey!")))
                .build();
        bot.start().block();
    }
}
