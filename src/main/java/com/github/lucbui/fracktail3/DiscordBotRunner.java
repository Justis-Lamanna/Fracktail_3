package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.BotCreator;
import com.github.lucbui.fracktail3.magic.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.filterset.user.DiscordUserset;
import com.github.lucbui.fracktail3.magic.filterset.user.Userset;
import com.github.lucbui.fracktail3.magic.handlers.action.ActionOptions;
import com.github.lucbui.fracktail3.magic.handlers.action.RespondAction;
import com.github.lucbui.fracktail3.magic.handlers.command.Command;
import com.github.lucbui.fracktail3.magic.handlers.command.HelpCommand;
import com.github.lucbui.fracktail3.magic.platform.discord.DiscordPlatform;
import discord4j.common.util.Snowflake;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DiscordBotRunner implements CommandLineRunner {
    @Autowired
    private Environment environment;

    @Value("${token}")
    private String token;

    @Override
    public void run(String... args) throws Exception {
        Userset steven = DiscordUserset.forUser("steven", Snowflake.of(112005555178000384L));
        Bot bot = new BotCreator()
                .withConfig(DiscordPlatform.INSTANCE,
                    new DiscordConfiguration.Builder(token)
                            .withPrefix("!")
                            .withOwner(Snowflake.of(248612704019808258L))
                            .withPresence(Presence.doNotDisturb(Activity.playing("Beta v3~!")))
                )
                .withUserset(steven)
                .withCommand(new HelpCommand())
                .withCommand(
                    new Command.Builder("hello")
                    .withAction(new ActionOptions.Builder()
                            .with(DiscordConfiguration.OWNER_FILTER, new RespondAction("Hello, beautiful owner."))
                            .with(steven.byId(), new RespondAction("Hello, stinky."))
                            .orElseDo(new RespondAction("Hello, {at_user}!")))
                )
                .build();
        bot.start().block();
    }
}
