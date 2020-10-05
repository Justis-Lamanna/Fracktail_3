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

public class IdentityDiscordEventHookGuard implements DiscordEventHookGuard {
    private final boolean value;

    public IdentityDiscordEventHookGuard(boolean value) {
        this.value = value;
    }
    
    private Mono<Boolean> value() {
        return Mono.just(value);
    }

    @Override
    public Mono<Boolean> forConnect(Bot bot, DiscordConfiguration config, ConnectEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forDisconnect(Bot bot, DiscordConfiguration config, DisconnectEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forReconnect(Bot bot, DiscordConfiguration config, ReconnectEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forReconnectStart(Bot bot, DiscordConfiguration config, ReconnectStartEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forReconnectFail(Bot bot, DiscordConfiguration config, ReconnectFailEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forReady(Bot bot, DiscordConfiguration config, ReadyEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forResume(Bot bot, DiscordConfiguration config, ResumeEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forGuildCreate(Bot bot, DiscordConfiguration config, GuildCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forGuildUpdate(Bot bot, DiscordConfiguration config, GuildUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forGuildDelete(Bot bot, DiscordConfiguration config, GuildDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forMemberJoin(Bot bot, DiscordConfiguration config, MemberJoinEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forMemberUpdate(Bot bot, DiscordConfiguration config, MemberUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forMemberLeave(Bot bot, DiscordConfiguration config, MemberLeaveEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forMemberChunk(Bot bot, DiscordConfiguration config, MemberChunkEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forEmojisUpdate(Bot bot, DiscordConfiguration config, EmojisUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forIntegrationsUpdate(Bot bot, DiscordConfiguration config, IntegrationsUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forBan(Bot bot, DiscordConfiguration config, BanEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forUnban(Bot bot, DiscordConfiguration config, UnbanEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forInviteCreate(Bot bot, DiscordConfiguration config, InviteCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forInviteDelete(Bot bot, DiscordConfiguration config, InviteDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forVoiceServerUpdate(Bot bot, DiscordConfiguration config, VoiceServerUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forWebhooksUpdate(Bot bot, DiscordConfiguration config, WebhooksUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forRoleCreate(Bot bot, DiscordConfiguration config, RoleCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forRoleUpdate(Bot bot, DiscordConfiguration config, RoleUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forRoleDelete(Bot bot, DiscordConfiguration config, RoleDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forTextChannelCreate(Bot bot, DiscordConfiguration config, TextChannelCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forTextChannelUpdate(Bot bot, DiscordConfiguration config, TextChannelUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forTextChannelDelete(Bot bot, DiscordConfiguration config, TextChannelDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forVoiceChannelCreate(Bot bot, DiscordConfiguration config, VoiceChannelCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forVoiceChannelUpdate(Bot bot, DiscordConfiguration config, VoiceChannelUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forVoiceChannelDelete(Bot bot, DiscordConfiguration config, VoiceChannelDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forPrivateChannelCreate(Bot bot, DiscordConfiguration config, PrivateChannelCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forPrivateChannelDelete(Bot bot, DiscordConfiguration config, PrivateChannelDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forNewsChannelCreate(Bot bot, DiscordConfiguration config, NewsChannelCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forNewsChannelUpdate(Bot bot, DiscordConfiguration config, NewsChannelUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forNewsChannelDelete(Bot bot, DiscordConfiguration config, NewsChannelDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forStoreChannelCreate(Bot bot, DiscordConfiguration config, StoreChannelCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forStoreChannelUpdate(Bot bot, DiscordConfiguration config, StoreChannelUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forStoreChannelDelete(Bot bot, DiscordConfiguration config, StoreChannelDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forCategoryCreate(Bot bot, DiscordConfiguration config, CategoryCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forCategoryUpdate(Bot bot, DiscordConfiguration config, CategoryUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forCategoryDelete(Bot bot, DiscordConfiguration config, CategoryDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forPinsUpdate(Bot bot, DiscordConfiguration config, PinsUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forTypingStart(Bot bot, DiscordConfiguration config, TypingStartEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forVoiceStateUpdate(Bot bot, DiscordConfiguration config, VoiceStateUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forMessageCreate(Bot bot, DiscordConfiguration config, MessageCreateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forMessageUpdate(Bot bot, DiscordConfiguration config, MessageUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forMessageDelete(Bot bot, DiscordConfiguration config, MessageDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forMessageBulkDelete(Bot bot, DiscordConfiguration config, MessageBulkDeleteEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forReactionAdd(Bot bot, DiscordConfiguration config, ReactionAddEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forReactionRemove(Bot bot, DiscordConfiguration config, ReactionRemoveEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forReactionRemoveAll(Bot bot, DiscordConfiguration config, ReactionRemoveAllEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forReactionRemoveEmoji(Bot bot, DiscordConfiguration config, ReactionRemoveEmojiEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forPresenceUpdate(Bot bot, DiscordConfiguration config, PresenceUpdateEvent event) {
        return value();
    }

    @Override
    public Mono<Boolean> forUserUpdate(Bot bot, DiscordConfiguration config, UserUpdateEvent event) {
        return value();
    }
}
