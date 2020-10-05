package com.github.lucbui.fracktail3.discord.hook;

import com.github.lucbui.fracktail3.discord.config.DiscordConfiguration;
import com.github.lucbui.fracktail3.magic.Bot;
import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.RoleCreateEvent;
import discord4j.core.event.domain.role.RoleDeleteEvent;
import discord4j.core.event.domain.role.RoleUpdateEvent;
import reactor.core.publisher.Mono;
import reactor.function.Function4;

public enum DiscordSupportedEvents {
    CONNECT(ConnectEvent.class, DiscordEventHookGuard::forConnect, DiscordEventHandler::onConnect),
    DISCONNECT(DisconnectEvent.class, DiscordEventHookGuard::forDisconnect, DiscordEventHandler::onDisconnect),
    RECONNECT(ReconnectEvent.class, DiscordEventHookGuard::forReconnect, DiscordEventHandler::onReconnect),
    RECONNECT_START(ReconnectStartEvent.class, DiscordEventHookGuard::forReconnectStart, DiscordEventHandler::onReconnectStart),
    RECONNECT_FAIL(ReconnectFailEvent.class, DiscordEventHookGuard::forReconnectFail, DiscordEventHandler::onReconnectFail),
    READY(ReadyEvent.class, DiscordEventHookGuard::forReady, DiscordEventHandler::onReady),
    RESUME(ResumeEvent.class, DiscordEventHookGuard::forResume, DiscordEventHandler::onResume),
    GUILD_CREATE(GuildCreateEvent.class, DiscordEventHookGuard::forGuildCreate, DiscordEventHandler::onGuildCreate),
    GUILD_UPDATE(GuildUpdateEvent.class, DiscordEventHookGuard::forGuildUpdate, DiscordEventHandler::onGuildUpdate),
    GUILD_DELETE(GuildDeleteEvent.class, DiscordEventHookGuard::forGuildDelete, DiscordEventHandler::onGuildDelete),
    MEMBER_JOIN(MemberJoinEvent.class, DiscordEventHookGuard::forMemberJoin, DiscordEventHandler::onMemberJoin),
    MEMBER_UPDATE(MemberUpdateEvent.class, DiscordEventHookGuard::forMemberUpdate, DiscordEventHandler::onMemberUpdate),
    MEMBER_LEAVE(MemberLeaveEvent.class, DiscordEventHookGuard::forMemberLeave, DiscordEventHandler::onMemberLeave),
    MEMBER_CHUNK(MemberChunkEvent.class, DiscordEventHookGuard::forMemberChunk, DiscordEventHandler::onMemberChunk),
    EMOJIS_UPDATE(EmojisUpdateEvent.class, DiscordEventHookGuard::forEmojisUpdate, DiscordEventHandler::onEmojisUpdate),
    INTEGRATIONS_UPDATE(IntegrationsUpdateEvent.class, DiscordEventHookGuard::forIntegrationsUpdate, DiscordEventHandler::onIntegrationsUpdate),
    BAN(BanEvent.class, DiscordEventHookGuard::forBan, DiscordEventHandler::onBan),
    UNBAN(UnbanEvent.class, DiscordEventHookGuard::forUnban, DiscordEventHandler::onUnban),
    INVITE_CREATE(InviteCreateEvent.class, DiscordEventHookGuard::forInviteCreate, DiscordEventHandler::onInviteCreate),
    INVITE_DELETE(InviteDeleteEvent.class, DiscordEventHookGuard::forInviteDelete, DiscordEventHandler::onInviteDelete),
    VOICE_SERVER_UPDATE(VoiceServerUpdateEvent.class, DiscordEventHookGuard::forVoiceServerUpdate, DiscordEventHandler::onVoiceServerUpdate),
    WEBHOOKS_UPDATE(WebhooksUpdateEvent.class, DiscordEventHookGuard::forWebhooksUpdate, DiscordEventHandler::onWebhooksUpdate),
    ROLE_CREATE(RoleCreateEvent.class, DiscordEventHookGuard::forRoleCreate, DiscordEventHandler::onRoleCreate),
    ROLE_UPDATE(RoleUpdateEvent.class, DiscordEventHookGuard::forRoleUpdate, DiscordEventHandler::onRoleUpdate),
    ROLE_DELETE(RoleDeleteEvent.class, DiscordEventHookGuard::forRoleDelete, DiscordEventHandler::onRoleDelete),
    TEXT_CHANNEL_CREATE(TextChannelCreateEvent.class, DiscordEventHookGuard::forTextChannelCreate, DiscordEventHandler::onTextChannelCreate),
    TEXT_CHANNEL_UPDATE(TextChannelUpdateEvent.class, DiscordEventHookGuard::forTextChannelUpdate, DiscordEventHandler::onTextChannelUpdate),
    TEXT_CHANNEL_DELETE(TextChannelDeleteEvent.class, DiscordEventHookGuard::forTextChannelDelete, DiscordEventHandler::onTextChannelDelete),
    VOICE_CHANNEL_CREATE(VoiceChannelCreateEvent.class, DiscordEventHookGuard::forVoiceChannelCreate, DiscordEventHandler::onVoiceChannelCreate),
    VOICE_CHANNEL_UPDATE(VoiceChannelUpdateEvent.class, DiscordEventHookGuard::forVoiceChannelUpdate, DiscordEventHandler::onVoiceChannelUpdate),
    VOICE_CHANNEL_DELETE(VoiceChannelDeleteEvent.class, DiscordEventHookGuard::forVoiceChannelDelete, DiscordEventHandler::onVoiceChannelDelete),
    PRIVATE_CHANNEL_CREATE(PrivateChannelCreateEvent.class, DiscordEventHookGuard::forPrivateChannelCreate, DiscordEventHandler::onPrivateChannelCreate),
    PRIVATE_CHANNEL_DELETE(PrivateChannelDeleteEvent.class, DiscordEventHookGuard::forPrivateChannelDelete, DiscordEventHandler::onPrivateChannelDelete),
    NEWS_CHANNEL_CREATE(NewsChannelCreateEvent.class, DiscordEventHookGuard::forNewsChannelCreate, DiscordEventHandler::onNewsChannelCreate),
    NEWS_CHANNEL_UPDATE(NewsChannelUpdateEvent.class, DiscordEventHookGuard::forNewsChannelUpdate, DiscordEventHandler::onNewsChannelUpdate),
    NEWS_CHANNEL_DELETE(NewsChannelDeleteEvent.class, DiscordEventHookGuard::forNewsChannelDelete, DiscordEventHandler::onNewsChannelDelete),
    STORE_CHANNEL_CREATE(StoreChannelCreateEvent.class, DiscordEventHookGuard::forStoreChannelCreate, DiscordEventHandler::onStoreChannelCreate),
    STORE_CHANNEL_UPDATE(StoreChannelUpdateEvent.class, DiscordEventHookGuard::forStoreChannelUpdate, DiscordEventHandler::onStoreChannelUpdate),
    STORE_CHANNEL_DELETE(StoreChannelDeleteEvent.class, DiscordEventHookGuard::forStoreChannelDelete, DiscordEventHandler::onStoreChannelDelete),
    CATEGORY_CREATE(CategoryCreateEvent.class, DiscordEventHookGuard::forCategoryCreate, DiscordEventHandler::onCategoryCreate),
    CATEGORY_UPDATE(CategoryUpdateEvent.class, DiscordEventHookGuard::forCategoryUpdate, DiscordEventHandler::onCategoryUpdate),
    CATEGORY_DELETE(CategoryDeleteEvent.class, DiscordEventHookGuard::forCategoryDelete, DiscordEventHandler::onCategoryDelete),
    PINS_UPDATE(PinsUpdateEvent.class, DiscordEventHookGuard::forPinsUpdate, DiscordEventHandler::onPinsUpdate),
    TYPING_START(TypingStartEvent.class, DiscordEventHookGuard::forTypingStart, DiscordEventHandler::onTypingStart),
    VOICE_STATE_UPDATE(VoiceStateUpdateEvent.class, DiscordEventHookGuard::forVoiceStateUpdate, DiscordEventHandler::onVoiceStateUpdate),
    MESSAGE_CREATE(MessageCreateEvent.class, DiscordEventHookGuard::forMessageCreate, DiscordEventHandler::onMessageCreate),
    MESSAGE_UPDATE(MessageUpdateEvent.class, DiscordEventHookGuard::forMessageUpdate, DiscordEventHandler::onMessageUpdate),
    MESSAGE_DELETE(MessageDeleteEvent.class, DiscordEventHookGuard::forMessageDelete, DiscordEventHandler::onMessageDelete),
    MESSAGE_BULK_DELETE(MessageBulkDeleteEvent.class, DiscordEventHookGuard::forMessageBulkDelete, DiscordEventHandler::onMessageBulkDelete),
    REACTION_ADD(ReactionAddEvent.class, DiscordEventHookGuard::forReactionAdd, DiscordEventHandler::onReactionAdd),
    REACTION_REMOVE(ReactionRemoveEvent.class, DiscordEventHookGuard::forReactionRemove, DiscordEventHandler::onReactionRemove),
    REACTION_REMOVE_ALL(ReactionRemoveAllEvent.class, DiscordEventHookGuard::forReactionRemoveAll, DiscordEventHandler::onReactionRemoveAll),
    REACTION_REMOVE_EMOJI(ReactionRemoveEmojiEvent.class, DiscordEventHookGuard::forReactionRemoveEmoji, DiscordEventHandler::onReactionRemoveEmoji),
    PRESENCE_UPDATE(PresenceUpdateEvent.class, DiscordEventHookGuard::forPresenceUpdate, DiscordEventHandler::onPresenceUpdate),
    USER_UPDATE(UserUpdateEvent.class, DiscordEventHookGuard::forUserUpdate, DiscordEventHandler::onUserUpdate);

    private final Class<?> clazz;
    private final Function4<Bot, DiscordConfiguration, Event, DiscordEventHook, Mono<Void>> executor;

    <T> DiscordSupportedEvents(Class<T> type,
                               Function4<DiscordEventHookGuard, Bot, DiscordConfiguration, T, Mono<Boolean>> hookFn,
                               Function4<DiscordEventHandler, Bot, DiscordConfiguration, T, Mono<Void>> handlerFn) {
        this.clazz = type;
        executor = (bot, config, event, hook) -> {
            if (event.getClass().equals(type)) {
                return Mono.just(event)
                        .cast(type)
                        .filterWhen(e -> hookFn.apply(hook.getGuard(), bot, config, e))
                        .flatMap(e -> handlerFn.apply(hook.getHandler(), bot, config, e));
            }
            return null;
        };
    }

    /**
     * Get the raw type
     * @return The raw type
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * Execute the appropriate flow for the event type
     * @param bot The bot running
     * @param configuration The configuration
     * @param event The event running
     * @param hook The hook running
     * @return Asynchronous indication of completion, or null if the event is not proper.
     */
    public Mono<Void> execute(Bot bot, DiscordConfiguration configuration, Event event, DiscordEventHook hook) {
        return executor.apply(bot, configuration, event, hook);
    }
}
