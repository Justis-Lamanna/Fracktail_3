package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.hook.action.channel.*;
import com.github.lucbui.fracktail3.discord.hook.action.guild.*;
import com.github.lucbui.fracktail3.discord.hook.action.lifecycle.*;
import com.github.lucbui.fracktail3.discord.hook.action.message.*;
import com.github.lucbui.fracktail3.discord.hook.action.role.RoleCreateAction;
import com.github.lucbui.fracktail3.discord.hook.action.role.RoleDeleteAction;
import com.github.lucbui.fracktail3.discord.hook.action.role.RoleUpdateAction;
import com.github.lucbui.fracktail3.discord.hook.action.user.PresenceUpdateAction;
import com.github.lucbui.fracktail3.discord.hook.action.user.UserUpdateAction;
import com.github.lucbui.fracktail3.discord.hook.context.channel.*;
import com.github.lucbui.fracktail3.discord.hook.context.guild.*;
import com.github.lucbui.fracktail3.discord.hook.context.lifecycle.*;
import com.github.lucbui.fracktail3.discord.hook.context.message.*;
import com.github.lucbui.fracktail3.discord.hook.context.role.RoleCreateContext;
import com.github.lucbui.fracktail3.discord.hook.context.role.RoleDeleteContext;
import com.github.lucbui.fracktail3.discord.hook.context.role.RoleUpdateContext;
import com.github.lucbui.fracktail3.discord.hook.context.user.PresenceUpdateContext;
import com.github.lucbui.fracktail3.discord.hook.context.user.UserUpdateContext;
import com.github.lucbui.fracktail3.discord.platform.DiscordPlatform;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.RoleCreateEvent;
import discord4j.core.event.domain.role.RoleDeleteEvent;
import discord4j.core.event.domain.role.RoleUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.function.Function3;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Default handler which executes all sub-handlers
 */
public class DefaultDiscordOnEventHandler implements DiscordOnEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiscordOnEventHandler.class);
    private static final Map<Class<?>, Function3<Bot, DiscordPlatform, Event, Mono<Void>>> MAPPER = new HashMap<>();

    static {
        MAPPER.put(ConnectEvent.class, getFunc(ConnectEvent.class, ConnectAction.class, ConnectContext::new, ConnectAction::guard, ConnectAction::doAction));
        MAPPER.put(DisconnectEvent.class, getFunc(DisconnectEvent.class, DisconnectAction.class, DisconnectContext::new, DisconnectAction::guard, DisconnectAction::doAction));
        MAPPER.put(ReconnectEvent.class, getFunc(ReconnectEvent.class, ReconnectAction.class, ReconnectContext::new, ReconnectAction::guard, ReconnectAction::doAction));
        MAPPER.put(ReconnectStartEvent.class, getFunc(ReconnectStartEvent.class, ReconnectStartAction.class, ReconnectStartContext::new, ReconnectStartAction::guard, ReconnectStartAction::doAction));
        MAPPER.put(ReconnectFailEvent.class, getFunc(ReconnectFailEvent.class, ReconnectFailAction.class, ReconnectFailContext::new, ReconnectFailAction::guard, ReconnectFailAction::doAction));
        MAPPER.put(ReadyEvent.class, getFunc(ReadyEvent.class, ReadyAction.class, ReadyContext::new, ReadyAction::guard, ReadyAction::doAction));
        MAPPER.put(ResumeEvent.class, getFunc(ResumeEvent.class, ResumeAction.class, ResumeContext::new, ResumeAction::guard, ResumeAction::doAction));
        MAPPER.put(GuildCreateEvent.class, getFunc(GuildCreateEvent.class, GuildCreateAction.class, GuildCreateContext::new, GuildCreateAction::guard, GuildCreateAction::doAction));
        MAPPER.put(GuildUpdateEvent.class, getFunc(GuildUpdateEvent.class, GuildUpdateAction.class, GuildUpdateContext::new, GuildUpdateAction::guard, GuildUpdateAction::doAction));
        MAPPER.put(GuildDeleteEvent.class, getFunc(GuildDeleteEvent.class, GuildDeleteAction.class, GuildDeleteContext::new, GuildDeleteAction::guard, GuildDeleteAction::doAction));
        MAPPER.put(MemberJoinEvent.class, getFunc(MemberJoinEvent.class, MemberJoinAction.class, MemberJoinContext::new, MemberJoinAction::guard, MemberJoinAction::doAction));
        MAPPER.put(MemberUpdateEvent.class, getFunc(MemberUpdateEvent.class, MemberUpdateAction.class, MemberUpdateContext::new, MemberUpdateAction::guard, MemberUpdateAction::doAction));
        MAPPER.put(MemberLeaveEvent.class, getFunc(MemberLeaveEvent.class, MemberLeaveAction.class, MemberLeaveContext::new, MemberLeaveAction::guard, MemberLeaveAction::doAction));
        MAPPER.put(MemberChunkEvent.class, getFunc(MemberChunkEvent.class, MemberChunkAction.class, MemberChunkContext::new, MemberChunkAction::guard, MemberChunkAction::doAction));
        MAPPER.put(EmojisUpdateEvent.class, getFunc(EmojisUpdateEvent.class, EmojisUpdateAction.class, EmojiUpdateContext::new, EmojisUpdateAction::guard, EmojisUpdateAction::doAction));
        MAPPER.put(IntegrationsUpdateEvent.class, getFunc(IntegrationsUpdateEvent.class, IntegrationsUpdateAction.class, IntegrationsUpdateContext::new, IntegrationsUpdateAction::guard, IntegrationsUpdateAction::doAction));
        MAPPER.put(BanEvent.class, getFunc(BanEvent.class, BanAction.class, BanContext::new, BanAction::guard, BanAction::doAction));
        MAPPER.put(UnbanEvent.class, getFunc(UnbanEvent.class, UnbanAction.class, UnbanContext::new, UnbanAction::guard, UnbanAction::doAction));
        MAPPER.put(InviteCreateEvent.class, getFunc(InviteCreateEvent.class, InviteCreateAction.class, InviteCreateContext::new, InviteCreateAction::guard, InviteCreateAction::doAction));
        MAPPER.put(InviteDeleteEvent.class, getFunc(InviteDeleteEvent.class, InviteDeleteAction.class, InviteDeleteContext::new, InviteDeleteAction::guard, InviteDeleteAction::doAction));
        MAPPER.put(VoiceServerUpdateEvent.class, getFunc(VoiceServerUpdateEvent.class, VoiceServerUpdateAction.class, VoiceServerUpdateContext::new, VoiceServerUpdateAction::guard, VoiceServerUpdateAction::doAction));
        MAPPER.put(WebhooksUpdateEvent.class, getFunc(WebhooksUpdateEvent.class, WebhooksUpdateAction.class, WebhooksUpdateContext::new, WebhooksUpdateAction::guard, WebhooksUpdateAction::doAction));
        MAPPER.put(RoleCreateEvent.class, getFunc(RoleCreateEvent.class, RoleCreateAction.class, RoleCreateContext::new, RoleCreateAction::guard, RoleCreateAction::doAction));
        MAPPER.put(RoleUpdateEvent.class, getFunc(RoleUpdateEvent.class, RoleUpdateAction.class, RoleUpdateContext::new, RoleUpdateAction::guard, RoleUpdateAction::doAction));
        MAPPER.put(RoleDeleteEvent.class, getFunc(RoleDeleteEvent.class, RoleDeleteAction.class, RoleDeleteContext::new, RoleDeleteAction::guard, RoleDeleteAction::doAction));
        MAPPER.put(TextChannelCreateEvent.class, getFunc(TextChannelCreateEvent.class, TextChannelCreateAction.class, TextChannelCreateContext::new, TextChannelCreateAction::guard, TextChannelCreateAction::doAction));
        MAPPER.put(TextChannelUpdateEvent.class, getFunc(TextChannelUpdateEvent.class, TextChannelUpdateAction.class, TextChannelUpdateContext::new, TextChannelUpdateAction::guard, TextChannelUpdateAction::doAction));
        MAPPER.put(TextChannelDeleteEvent.class, getFunc(TextChannelDeleteEvent.class, TextChannelDeleteAction.class, TextChannelDeleteContext::new, TextChannelDeleteAction::guard, TextChannelDeleteAction::doAction));
        MAPPER.put(VoiceChannelCreateEvent.class, getFunc(VoiceChannelCreateEvent.class, VoiceChannelCreateAction.class, VoiceChannelCreateContext::new, VoiceChannelCreateAction::guard, VoiceChannelCreateAction::doAction));
        MAPPER.put(VoiceChannelUpdateEvent.class, getFunc(VoiceChannelUpdateEvent.class, VoiceChannelUpdateAction.class, VoiceChannelUpdateContext::new, VoiceChannelUpdateAction::guard, VoiceChannelUpdateAction::doAction));
        MAPPER.put(VoiceChannelDeleteEvent.class, getFunc(VoiceChannelDeleteEvent.class, VoiceChannelDeleteAction.class, VoiceChannelDeleteContext::new, VoiceChannelDeleteAction::guard, VoiceChannelDeleteAction::doAction));
        MAPPER.put(PrivateChannelCreateEvent.class, getFunc(PrivateChannelCreateEvent.class, PrivateChannelCreateAction.class, PrivateChannelCreateContext::new, PrivateChannelCreateAction::guard, PrivateChannelCreateAction::doAction));
        MAPPER.put(PrivateChannelDeleteEvent.class, getFunc(PrivateChannelDeleteEvent.class, PrivateChannelDeleteAction.class, PrivateChannelDeleteContext::new, PrivateChannelDeleteAction::guard, PrivateChannelDeleteAction::doAction));
        MAPPER.put(NewsChannelCreateEvent.class, getFunc(NewsChannelCreateEvent.class, NewsChannelCreateAction.class, NewsChannelCreateContext::new, NewsChannelCreateAction::guard, NewsChannelCreateAction::doAction));
        MAPPER.put(NewsChannelUpdateEvent.class, getFunc(NewsChannelUpdateEvent.class, NewsChannelUpdateAction.class, NewsChannelUpdateContext::new, NewsChannelUpdateAction::guard, NewsChannelUpdateAction::doAction));
        MAPPER.put(NewsChannelDeleteEvent.class, getFunc(NewsChannelDeleteEvent.class, NewsChannelDeleteAction.class, NewsChannelDeleteContext::new, NewsChannelDeleteAction::guard, NewsChannelDeleteAction::doAction));
        MAPPER.put(StoreChannelCreateEvent.class, getFunc(StoreChannelCreateEvent.class, StoreChannelCreateAction.class, StoreChannelCreateContext::new, StoreChannelCreateAction::guard, StoreChannelCreateAction::doAction));
        MAPPER.put(StoreChannelUpdateEvent.class, getFunc(StoreChannelUpdateEvent.class, StoreChannelUpdateAction.class, StoreChannelUpdateContext::new, StoreChannelUpdateAction::guard, StoreChannelUpdateAction::doAction));
        MAPPER.put(StoreChannelDeleteEvent.class, getFunc(StoreChannelDeleteEvent.class, StoreChannelDeleteAction.class, StoreChannelDeleteContext::new, StoreChannelDeleteAction::guard, StoreChannelDeleteAction::doAction));
        MAPPER.put(CategoryCreateEvent.class, getFunc(CategoryCreateEvent.class, CategoryCreateAction.class, CategoryCreateContext::new, CategoryCreateAction::guard, CategoryCreateAction::doAction));
        MAPPER.put(CategoryUpdateEvent.class, getFunc(CategoryUpdateEvent.class, CategoryUpdateAction.class, CategoryUpdateContext::new, CategoryUpdateAction::guard, CategoryUpdateAction::doAction));
        MAPPER.put(CategoryDeleteEvent.class, getFunc(CategoryDeleteEvent.class, CategoryDeleteAction.class, CategoryDeleteContext::new, CategoryDeleteAction::guard, CategoryDeleteAction::doAction));
        MAPPER.put(PinsUpdateEvent.class, getFunc(PinsUpdateEvent.class, PinsUpdateAction.class, PinsUpdateContext::new, PinsUpdateAction::guard, PinsUpdateAction::doAction));
        MAPPER.put(TypingStartEvent.class, getFunc(TypingStartEvent.class, TypingStartAction.class, TypingStartContext::new, TypingStartAction::guard, TypingStartAction::doAction));
        MAPPER.put(VoiceStateUpdateEvent.class, getFunc(VoiceStateUpdateEvent.class, VoiceStateUpdateAction.class, VoiceStateUpdateContext::new, VoiceStateUpdateAction::guard, VoiceStateUpdateAction::doAction));
        MAPPER.put(MessageCreateEvent.class, getFunc(MessageCreateEvent.class, MessageCreateAction.class, MessageCreateContext::new, MessageCreateAction::guard, MessageCreateAction::doAction));
        MAPPER.put(MessageUpdateEvent.class, getFunc(MessageUpdateEvent.class, MessageUpdateAction.class, MessageUpdateContext::new, MessageUpdateAction::guard, MessageUpdateAction::doAction));
        MAPPER.put(MessageDeleteEvent.class, getFunc(MessageDeleteEvent.class, MessageDeleteAction.class, MessageDeleteContext::new, MessageDeleteAction::guard, MessageDeleteAction::doAction));
        MAPPER.put(MessageBulkDeleteEvent.class, getFunc(MessageBulkDeleteEvent.class, MessageBulkDeleteAction.class, MessageBulkDeleteContext::new, MessageBulkDeleteAction::guard, MessageBulkDeleteAction::doAction));
        MAPPER.put(ReactionAddEvent.class, getFunc(ReactionAddEvent.class, ReactionAddAction.class, ReactionAddContext::new, ReactionAddAction::guard, ReactionAddAction::doAction));
        MAPPER.put(ReactionRemoveEvent.class, getFunc(ReactionRemoveEvent.class, ReactionRemoveAction.class, ReactionRemoveContext::new, ReactionRemoveAction::guard, ReactionRemoveAction::doAction));
        MAPPER.put(ReactionRemoveAllEvent.class, getFunc(ReactionRemoveAllEvent.class, ReactionRemoveAllAction.class, ReactionRemoveAllContext::new, ReactionRemoveAllAction::guard, ReactionRemoveAllAction::doAction));
        MAPPER.put(ReactionRemoveEmojiEvent.class, getFunc(ReactionRemoveEmojiEvent.class, ReactionRemoveEmojiAction.class, ReactionRemoveEmojiContext::new, ReactionRemoveEmojiAction::guard, ReactionRemoveEmojiAction::doAction));
        MAPPER.put(PresenceUpdateEvent.class, getFunc(PresenceUpdateEvent.class, PresenceUpdateAction.class, PresenceUpdateContext::new, PresenceUpdateAction::guard, PresenceUpdateAction::doAction));
        MAPPER.put(UserUpdateEvent.class, getFunc(UserUpdateEvent.class, UserUpdateAction.class, UserUpdateContext::new, UserUpdateAction::guard, UserUpdateAction::doAction));
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordPlatform platform, Event event) {
        return MAPPER.getOrDefault(event.getClass(), (a, b, c) -> Mono.empty())
                .apply(bot, platform, event);
    }

    private static <RAW, CTX, ACTION> Function3<Bot, DiscordPlatform, Event, Mono<Void>> getFunc(
            Class<RAW> rawClass,
            Class<ACTION> actionClass,
            Function3<Bot, DiscordPlatform, RAW, CTX> ctxFunc,
            BiFunction<ACTION, CTX, Mono<Boolean>> guardFunc,
            BiFunction<ACTION, CTX, Mono<Void>> func
            ){
        return (bot, platform, event) -> {
            RAW cEvent = rawClass.cast(event);
            CTX context = ctxFunc.apply(bot, platform, cEvent);
            return Flux.fromIterable(platform.getConfig().getHandlers().getAllFor(actionClass))
                    .map(DiscordEventHook::getHook)
                    .filterWhen(hook -> guardFunc.apply(hook, context))
                    .flatMap(hook -> func.apply(hook, context))
                    .then();
        };
    }
}
