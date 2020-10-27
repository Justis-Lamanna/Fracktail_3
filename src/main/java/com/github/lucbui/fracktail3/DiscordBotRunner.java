package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.config.DiscordConfigurationBuilder;
import com.github.lucbui.fracktail3.discord.guard.DiscordUserset;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHook;
import com.github.lucbui.fracktail3.discord.hook.DiscordEventHookStoreBuilder;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.command.action.RespondingAction;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
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
        RerHook hook = new RerHook();
        Bot bot = new BotCreator()
                .withPlatform(new DiscordPlatform.Builder()
                    .withConfiguration(new DiscordConfigurationBuilder(token)
                    .withPrefix("!")
                    .withOwner(248612704019808258L)
                    .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
                    .withHandlers(new DiscordEventHookStoreBuilder()
                            .withGuildCreateActionHook(new DiscordEventHook<>("rer", hook))
                            .withVoiceStateUpdateActionHook(new DiscordEventHook<>("rer", hook))
                    )
                    .withUserset(DiscordUserset.forUser("steven", 112005555178000384L))))
                .withScheduler(scheduler)
                .withCommand(new Command.Builder("hello")
                    .withAction(new RespondingAction(FormattedString.literal("Hello!"))))
                .build();
        bot.start().block();
    }
}
