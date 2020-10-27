package com.github.lucbui.fracktail3;

import com.github.lucbui.fracktail3.discord.guard.DiscordChannelset;
import com.github.lucbui.fracktail3.discord.hook.action.channel.VoiceStateUpdateAction;
import com.github.lucbui.fracktail3.discord.hook.action.guild.GuildCreateAction;
import com.github.lucbui.fracktail3.discord.hook.context.channel.VoiceStateUpdateContext;
import com.github.lucbui.fracktail3.discord.hook.context.guild.GuildCreateContext;
import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import discord4j.common.util.Snowflake;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.channel.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hook that prints a message whenever a Dragon joins the VC
 */
public class RerHook implements VoiceStateUpdateAction, GuildCreateAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(RerHook.class);

    private static final Snowflake LUCBUILAND = Snowflake.of(423976318082744321L);
    private static final Snowflake DRAGON = Snowflake.of(560500269419331585L);
    private static final DiscordChannelset BOT_TIME = DiscordChannelset.forChannel("bot_time", Snowflake.of(744390997429059595L));

    private final AtomicInteger dragonCounter = new AtomicInteger(0);

    @Override
    public Mono<Void> doAction(VoiceStateUpdateContext ctx) {
        if(ctx.isJoin()) {
            int count = dragonCounter.incrementAndGet();
            if(count == 1) {
                return ctx.getPlatform().message(BOT_TIME, FormatUtils.multilineCodeBlock("There's a rer in voice!"));
            }
        } else if(ctx.isLeave()) {
            dragonCounter.decrementAndGet();
        }
        return Mono.empty();
    }

    @Override
    public Mono<Boolean> guard(VoiceStateUpdateContext ctx) {
        if(!ctx.isJoin() && !ctx.isLeave()) {
            return Mono.just(false);
        }
        VoiceState state = ctx.getPayload().getCurrent();
        if(!state.getGuildId().equals(LUCBUILAND)) {
            return Mono.just(false);
        }
        return state.getMember()
                .filter(member -> member.getRoleIds().contains(DRAGON))
                .hasElement();
    }

    @Override
    public Mono<Void> doAction(GuildCreateContext ctx) {
        return ctx.getPayload().getGuild()
                .getChannels()
                .filter(gc -> gc instanceof VoiceChannel)
                .cast(VoiceChannel.class)
                .flatMap(VoiceChannel::getVoiceStates)
                .flatMap(VoiceState::getMember)
                .filter(member -> member.getRoleIds().contains(DRAGON))
                .count()
                .doOnNext(l -> dragonCounter.set(l.intValue()))
                .filter(l -> l == 1)
                .flatMap(l -> ctx.getPlatform().message(BOT_TIME, FormatUtils.multilineCodeBlock("There's a rer in voice!")))
                .then();
    }

    @Override
    public Mono<Boolean> guard(GuildCreateContext ctx) {
        return Mono.just(ctx.getPayload().getGuild().getId().equals(LUCBUILAND));
    }
}
