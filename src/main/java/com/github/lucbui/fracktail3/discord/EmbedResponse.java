package com.github.lucbui.fracktail3.discord;

import com.github.lucbui.fracktail3.discord.context.DiscordPlace;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.annotation.strategy.PlatformModel;
import discord4j.core.spec.EmbedCreateSpec;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Data
@PlatformModel(DiscordPlatform.class)
public class EmbedResponse implements CommandAction {
    private final Consumer<? super EmbedCreateSpec> spec;

    @Override
    public Mono<Void> doAction(CommandUseContext context) {
        return context.getTriggerPlace()
                .filter(p -> p instanceof DiscordPlace)
                .cast(DiscordPlace.class)
                .flatMap(dp -> dp.sendEmbed(spec))
                .doOnError(Throwable::printStackTrace)
                .then();
    }
}
