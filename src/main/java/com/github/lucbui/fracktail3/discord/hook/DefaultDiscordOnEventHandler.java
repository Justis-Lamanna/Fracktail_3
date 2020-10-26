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
import com.github.lucbui.fracktail3.magic.util.IdStore;
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
import java.util.function.Function;

/**
 * Default handler which executes all sub-handlers
 */
public class DefaultDiscordOnEventHandler implements DiscordOnEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDiscordOnEventHandler.class);
    private static final Map<Class<?>, Function3<Bot, DiscordPlatform, Event, Mono<Void>>> MAPPER = new HashMap<>();

    static {
        MAPPER.put(ConnectEvent.class, getFunc(ConnectEvent.class, ConnectContext::new, DiscordEventHookStore::getConnectActionStore, ConnectAction::guard, ConnectAction::doAction));
        MAPPER.put(DisconnectEvent.class, getFunc(DisconnectEvent.class, DisconnectContext::new, DiscordEventHookStore::getDisconnectActionStore, DisconnectAction::guard, DisconnectAction::doAction));
        MAPPER.put(ReconnectEvent.class, getFunc(ReconnectEvent.class, ReconnectContext::new, DiscordEventHookStore::getReconnectActionStore, ReconnectAction::guard, ReconnectAction::doAction));
        MAPPER.put(ReconnectStartEvent.class, getFunc(ReconnectStartEvent.class, ReconnectStartContext::new, DiscordEventHookStore::getReconnectStartActionStore, ReconnectStartAction::guard, ReconnectStartAction::doAction));
        MAPPER.put(ReconnectFailEvent.class, getFunc(ReconnectFailEvent.class, ReconnectFailContext::new, DiscordEventHookStore::getReconnectFailActionStore, ReconnectFailAction::guard, ReconnectFailAction::doAction));
        MAPPER.put(ReadyEvent.class, getFunc(ReadyEvent.class, ReadyContext::new, DiscordEventHookStore::getReadyActionStore, ReadyAction::guard, ReadyAction::doAction));
        MAPPER.put(ResumeEvent.class, getFunc(ResumeEvent.class, ResumeContext::new, DiscordEventHookStore::getResumeActionStore, ResumeAction::guard, ResumeAction::doAction));
        MAPPER.put(GuildCreateEvent.class, getFunc(GuildCreateEvent.class, GuildCreateContext::new, DiscordEventHookStore::getGuildCreateActionStore, GuildCreateAction::guard, GuildCreateAction::doAction));
        MAPPER.put(GuildUpdateEvent.class, getFunc(GuildUpdateEvent.class, GuildUpdateContext::new, DiscordEventHookStore::getGuildUpdateActionStore, GuildUpdateAction::guard, GuildUpdateAction::doAction));
        MAPPER.put(GuildDeleteEvent.class, getFunc(GuildDeleteEvent.class, GuildDeleteContext::new, DiscordEventHookStore::getGuildDeleteActionStore, GuildDeleteAction::guard, GuildDeleteAction::doAction));
        MAPPER.put(MemberJoinEvent.class, getFunc(MemberJoinEvent.class, MemberJoinContext::new, DiscordEventHookStore::getMemberJoinActionStore, MemberJoinAction::guard, MemberJoinAction::doAction));
        MAPPER.put(MemberUpdateEvent.class, getFunc(MemberUpdateEvent.class, MemberUpdateContext::new, DiscordEventHookStore::getMemberUpdateActionStore, MemberUpdateAction::guard, MemberUpdateAction::doAction));
        MAPPER.put(MemberLeaveEvent.class, getFunc(MemberLeaveEvent.class, MemberLeaveContext::new, DiscordEventHookStore::getMemberLeaveActionStore, MemberLeaveAction::guard, MemberLeaveAction::doAction));
        MAPPER.put(MemberChunkEvent.class, getFunc(MemberChunkEvent.class, MemberChunkContext::new, DiscordEventHookStore::getMemberChunkActionStore, MemberChunkAction::guard, MemberChunkAction::doAction));
        MAPPER.put(EmojisUpdateEvent.class, getFunc(EmojisUpdateEvent.class, EmojiUpdateContext::new, DiscordEventHookStore::getEmojisUpdateActionStore, EmojisUpdateAction::guard, EmojisUpdateAction::doAction));
        MAPPER.put(IntegrationsUpdateEvent.class, getFunc(IntegrationsUpdateEvent.class, IntegrationsUpdateContext::new, DiscordEventHookStore::getIntegrationsUpdateActionStore, IntegrationsUpdateAction::guard, IntegrationsUpdateAction::doAction));
        MAPPER.put(BanEvent.class, getFunc(BanEvent.class, BanContext::new, DiscordEventHookStore::getBanActionStore, BanAction::guard, BanAction::doAction));
        MAPPER.put(UnbanEvent.class, getFunc(UnbanEvent.class, UnbanContext::new, DiscordEventHookStore::getUnbanActionStore, UnbanAction::guard, UnbanAction::doAction));
        MAPPER.put(InviteCreateEvent.class, getFunc(InviteCreateEvent.class, InviteCreateContext::new, DiscordEventHookStore::getInviteCreateActionStore, InviteCreateAction::guard, InviteCreateAction::doAction));
        MAPPER.put(InviteDeleteEvent.class, getFunc(InviteDeleteEvent.class, InviteDeleteContext::new, DiscordEventHookStore::getInviteDeleteActionStore, InviteDeleteAction::guard, InviteDeleteAction::doAction));
        MAPPER.put(VoiceServerUpdateEvent.class, getFunc(VoiceServerUpdateEvent.class, VoiceServerUpdateContext::new, DiscordEventHookStore::getVoiceServerUpdateActionStore, VoiceServerUpdateAction::guard, VoiceServerUpdateAction::doAction));
        MAPPER.put(WebhooksUpdateEvent.class, getFunc(WebhooksUpdateEvent.class, WebhooksUpdateContext::new, DiscordEventHookStore::getWebhooksUpdateActionStore, WebhooksUpdateAction::guard, WebhooksUpdateAction::doAction));
        MAPPER.put(RoleCreateEvent.class, getFunc(RoleCreateEvent.class, RoleCreateContext::new, DiscordEventHookStore::getRoleCreateActionStore, RoleCreateAction::guard, RoleCreateAction::doAction));
        MAPPER.put(RoleUpdateEvent.class, getFunc(RoleUpdateEvent.class, RoleUpdateContext::new, DiscordEventHookStore::getRoleUpdateActionStore, RoleUpdateAction::guard, RoleUpdateAction::doAction));
        MAPPER.put(RoleDeleteEvent.class, getFunc(RoleDeleteEvent.class, RoleDeleteContext::new, DiscordEventHookStore::getRoleDeleteActionStore, RoleDeleteAction::guard, RoleDeleteAction::doAction));
        MAPPER.put(TextChannelCreateEvent.class, getFunc(TextChannelCreateEvent.class, TextChannelCreateContext::new, DiscordEventHookStore::getTextChannelCreateActionStore, TextChannelCreateAction::guard, TextChannelCreateAction::doAction));
        MAPPER.put(TextChannelUpdateEvent.class, getFunc(TextChannelUpdateEvent.class, TextChannelUpdateContext::new, DiscordEventHookStore::getTextChannelUpdateActionStore, TextChannelUpdateAction::guard, TextChannelUpdateAction::doAction));
        MAPPER.put(TextChannelDeleteEvent.class, getFunc(TextChannelDeleteEvent.class, TextChannelDeleteContext::new, DiscordEventHookStore::getTextChannelDeleteActionStore, TextChannelDeleteAction::guard, TextChannelDeleteAction::doAction));
        MAPPER.put(VoiceChannelCreateEvent.class, getFunc(VoiceChannelCreateEvent.class, VoiceChannelCreateContext::new, DiscordEventHookStore::getVoiceChannelCreateActionStore, VoiceChannelCreateAction::guard, VoiceChannelCreateAction::doAction));
        MAPPER.put(VoiceChannelUpdateEvent.class, getFunc(VoiceChannelUpdateEvent.class, VoiceChannelUpdateContext::new, DiscordEventHookStore::getVoiceChannelUpdateActionStore, VoiceChannelUpdateAction::guard, VoiceChannelUpdateAction::doAction));
        MAPPER.put(VoiceChannelDeleteEvent.class, getFunc(VoiceChannelDeleteEvent.class, VoiceChannelDeleteContext::new, DiscordEventHookStore::getVoiceChannelDeleteActionStore, VoiceChannelDeleteAction::guard, VoiceChannelDeleteAction::doAction));
        MAPPER.put(PrivateChannelCreateEvent.class, getFunc(PrivateChannelCreateEvent.class, PrivateChannelCreateContext::new, DiscordEventHookStore::getPrivateChannelCreateActionStore, PrivateChannelCreateAction::guard, PrivateChannelCreateAction::doAction));
        MAPPER.put(PrivateChannelDeleteEvent.class, getFunc(PrivateChannelDeleteEvent.class, PrivateChannelDeleteContext::new, DiscordEventHookStore::getPrivateChannelDeleteActionStore, PrivateChannelDeleteAction::guard, PrivateChannelDeleteAction::doAction));
        MAPPER.put(NewsChannelCreateEvent.class, getFunc(NewsChannelCreateEvent.class, NewsChannelCreateContext::new, DiscordEventHookStore::getNewsChannelCreateActionStore, NewsChannelCreateAction::guard, NewsChannelCreateAction::doAction));
        MAPPER.put(NewsChannelUpdateEvent.class, getFunc(NewsChannelUpdateEvent.class, NewsChannelUpdateContext::new, DiscordEventHookStore::getNewsChannelUpdateActionStore, NewsChannelUpdateAction::guard, NewsChannelUpdateAction::doAction));
        MAPPER.put(NewsChannelDeleteEvent.class, getFunc(NewsChannelDeleteEvent.class, NewsChannelDeleteContext::new, DiscordEventHookStore::getNewsChannelDeleteActionStore, NewsChannelDeleteAction::guard, NewsChannelDeleteAction::doAction));
        MAPPER.put(StoreChannelCreateEvent.class, getFunc(StoreChannelCreateEvent.class, StoreChannelCreateContext::new, DiscordEventHookStore::getStoreChannelCreateActionStore, StoreChannelCreateAction::guard, StoreChannelCreateAction::doAction));
        MAPPER.put(StoreChannelUpdateEvent.class, getFunc(StoreChannelUpdateEvent.class, StoreChannelUpdateContext::new, DiscordEventHookStore::getStoreChannelUpdateActionStore, StoreChannelUpdateAction::guard, StoreChannelUpdateAction::doAction));
        MAPPER.put(StoreChannelDeleteEvent.class, getFunc(StoreChannelDeleteEvent.class, StoreChannelDeleteContext::new, DiscordEventHookStore::getStoreChannelDeleteActionStore, StoreChannelDeleteAction::guard, StoreChannelDeleteAction::doAction));
        MAPPER.put(CategoryCreateEvent.class, getFunc(CategoryCreateEvent.class, CategoryCreateContext::new, DiscordEventHookStore::getCategoryCreateActionStore, CategoryCreateAction::guard, CategoryCreateAction::doAction));
        MAPPER.put(CategoryUpdateEvent.class, getFunc(CategoryUpdateEvent.class, CategoryUpdateContext::new, DiscordEventHookStore::getCategoryUpdateActionStore, CategoryUpdateAction::guard, CategoryUpdateAction::doAction));
        MAPPER.put(CategoryDeleteEvent.class, getFunc(CategoryDeleteEvent.class, CategoryDeleteContext::new, DiscordEventHookStore::getCategoryDeleteActionStore, CategoryDeleteAction::guard, CategoryDeleteAction::doAction));
        MAPPER.put(PinsUpdateEvent.class, getFunc(PinsUpdateEvent.class, PinsUpdateContext::new, DiscordEventHookStore::getPinsUpdateActionStore, PinsUpdateAction::guard, PinsUpdateAction::doAction));
        MAPPER.put(TypingStartEvent.class, getFunc(TypingStartEvent.class, TypingStartContext::new, DiscordEventHookStore::getTypingStartActionStore, TypingStartAction::guard, TypingStartAction::doAction));
        MAPPER.put(VoiceStateUpdateEvent.class, getFunc(VoiceStateUpdateEvent.class, VoiceStateUpdateContext::new, DiscordEventHookStore::getVoiceStateUpdateActionStore, VoiceStateUpdateAction::guard, VoiceStateUpdateAction::doAction));
        MAPPER.put(MessageCreateEvent.class, getFunc(MessageCreateEvent.class, MessageCreateContext::new, DiscordEventHookStore::getMessageCreateActionStore, MessageCreateAction::guard, MessageCreateAction::doAction));
        MAPPER.put(MessageUpdateEvent.class, getFunc(MessageUpdateEvent.class, MessageUpdateContext::new, DiscordEventHookStore::getMessageUpdateActionStore, MessageUpdateAction::guard, MessageUpdateAction::doAction));
        MAPPER.put(MessageDeleteEvent.class, getFunc(MessageDeleteEvent.class, MessageDeleteContext::new, DiscordEventHookStore::getMessageDeleteActionStore, MessageDeleteAction::guard, MessageDeleteAction::doAction));
        MAPPER.put(MessageBulkDeleteEvent.class, getFunc(MessageBulkDeleteEvent.class, MessageBulkDeleteContext::new, DiscordEventHookStore::getMessageBulkDeleteActionStore, MessageBulkDeleteAction::guard, MessageBulkDeleteAction::doAction));
        MAPPER.put(ReactionAddEvent.class, getFunc(ReactionAddEvent.class, ReactionAddContext::new, DiscordEventHookStore::getReactionAddActionStore, ReactionAddAction::guard, ReactionAddAction::doAction));
        MAPPER.put(ReactionRemoveEvent.class, getFunc(ReactionRemoveEvent.class, ReactionRemoveContext::new, DiscordEventHookStore::getReactionRemoveActionStore, ReactionRemoveAction::guard, ReactionRemoveAction::doAction));
        MAPPER.put(ReactionRemoveAllEvent.class, getFunc(ReactionRemoveAllEvent.class, ReactionRemoveAllContext::new, DiscordEventHookStore::getReactionRemoveAllActionStore, ReactionRemoveAllAction::guard, ReactionRemoveAllAction::doAction));
        MAPPER.put(ReactionRemoveEmojiEvent.class, getFunc(ReactionRemoveEmojiEvent.class, ReactionRemoveEmojiContext::new, DiscordEventHookStore::getReactionRemoveEmojiActionStore, ReactionRemoveEmojiAction::guard, ReactionRemoveEmojiAction::doAction));
        MAPPER.put(PresenceUpdateEvent.class, getFunc(PresenceUpdateEvent.class, PresenceUpdateContext::new, DiscordEventHookStore::getPresenceUpdateActionStore, PresenceUpdateAction::guard, PresenceUpdateAction::doAction));
        MAPPER.put(UserUpdateEvent.class, getFunc(UserUpdateEvent.class, UserUpdateContext::new, DiscordEventHookStore::getUserUpdateActionStore, UserUpdateAction::guard, UserUpdateAction::doAction));
    }

    @Override
    public Mono<Void> execute(Bot bot, DiscordPlatform platform, Event event) {
        return MAPPER.getOrDefault(event.getClass(), (a, b, c) -> Mono.empty())
                .apply(bot, platform, event);
    }

    private static <RAW, CTX, ACTION> Function3<Bot, DiscordPlatform, Event, Mono<Void>> getFunc(
            Class<RAW> rawClass,
            Function3<Bot, DiscordPlatform, RAW, CTX> ctxFunc,
            Function<DiscordEventHookStore, IdStore<DiscordEventHook<ACTION>>> storeFunc,
            BiFunction<ACTION, CTX, Mono<Boolean>> guardFunc,
            BiFunction<ACTION, CTX, Mono<Void>> func
            ){
        return (bot, platform, event) -> {
            CTX context = ctxFunc.apply(bot, platform, rawClass.cast(event));
            return Flux.fromIterable(storeFunc.apply(platform.getConfig().getHandlers()).getAll())
                    .map(DiscordEventHook::getHook)
                    .filterWhen(hook -> guardFunc.apply(hook, context))
                    .flatMap(hook -> func.apply(hook, context))
                    .then();
        };
    }
}
