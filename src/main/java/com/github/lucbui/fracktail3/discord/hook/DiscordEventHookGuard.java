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

/**
 * A guard which can filter out Discord events
 */
public interface DiscordEventHookGuard {
    //Lifecycle Events
    default Mono<Boolean> forConnect(Bot bot, DiscordConfiguration config, ConnectEvent event) { return Mono.just(false); }
    default Mono<Boolean> forDisconnect(Bot bot, DiscordConfiguration config, DisconnectEvent event) { return Mono.just(false); }
    default Mono<Boolean> forReconnect(Bot bot, DiscordConfiguration config, ReconnectEvent event) { return Mono.just(false); }
    default Mono<Boolean> forReconnectStart(Bot bot, DiscordConfiguration config, ReconnectStartEvent event) { return Mono.just(false); }
    default Mono<Boolean> forReconnectFail(Bot bot, DiscordConfiguration config, ReconnectFailEvent event) { return Mono.just(false); }
    default Mono<Boolean> forReady(Bot bot, DiscordConfiguration config, ReadyEvent event) { return Mono.just(false); }
    default Mono<Boolean> forResume(Bot bot, DiscordConfiguration config, ResumeEvent event) { return Mono.just(false); }
    //Guild Events
    default Mono<Boolean> forGuildCreate(Bot bot, DiscordConfiguration config, GuildCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forGuildUpdate(Bot bot, DiscordConfiguration config, GuildUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forGuildDelete(Bot bot, DiscordConfiguration config, GuildDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forMemberJoin(Bot bot, DiscordConfiguration config, MemberJoinEvent event) { return Mono.just(false); }
    default Mono<Boolean> forMemberUpdate(Bot bot, DiscordConfiguration config, MemberUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forMemberLeave(Bot bot, DiscordConfiguration config, MemberLeaveEvent event) { return Mono.just(false); }
    default Mono<Boolean> forMemberChunk(Bot bot, DiscordConfiguration config, MemberChunkEvent event) { return Mono.just(false); }
    default Mono<Boolean> forEmojisUpdate(Bot bot, DiscordConfiguration config, EmojisUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forIntegrationsUpdate(Bot bot, DiscordConfiguration config, IntegrationsUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forBan(Bot bot, DiscordConfiguration config, BanEvent event) { return Mono.just(false); }
    default Mono<Boolean> forUnban(Bot bot, DiscordConfiguration config, UnbanEvent event) { return Mono.just(false); }
    default Mono<Boolean> forInviteCreate(Bot bot, DiscordConfiguration config, InviteCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forInviteDelete(Bot bot, DiscordConfiguration config, InviteDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forVoiceServerUpdate(Bot bot, DiscordConfiguration config, VoiceServerUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forWebhooksUpdate(Bot bot, DiscordConfiguration config, WebhooksUpdateEvent event) { return Mono.just(false); }
    //Role Events
    default Mono<Boolean> forRoleCreate(Bot bot, DiscordConfiguration config, RoleCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forRoleUpdate(Bot bot, DiscordConfiguration config, RoleUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forRoleDelete(Bot bot, DiscordConfiguration config, RoleDeleteEvent event) { return Mono.just(false); }
    //Channel Events
    default Mono<Boolean> forTextChannelCreate(Bot bot, DiscordConfiguration config, TextChannelCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forTextChannelUpdate(Bot bot, DiscordConfiguration config, TextChannelUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forTextChannelDelete(Bot bot, DiscordConfiguration config, TextChannelDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forVoiceChannelCreate(Bot bot, DiscordConfiguration config, VoiceChannelCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forVoiceChannelUpdate(Bot bot, DiscordConfiguration config, VoiceChannelUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forVoiceChannelDelete(Bot bot, DiscordConfiguration config, VoiceChannelDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forPrivateChannelCreate(Bot bot, DiscordConfiguration config, PrivateChannelCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forPrivateChannelDelete(Bot bot, DiscordConfiguration config, PrivateChannelDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forNewsChannelCreate(Bot bot, DiscordConfiguration config, NewsChannelCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forNewsChannelUpdate(Bot bot, DiscordConfiguration config, NewsChannelUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forNewsChannelDelete(Bot bot, DiscordConfiguration config, NewsChannelDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forStoreChannelCreate(Bot bot, DiscordConfiguration config, StoreChannelCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forStoreChannelUpdate(Bot bot, DiscordConfiguration config, StoreChannelUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forStoreChannelDelete(Bot bot, DiscordConfiguration config, StoreChannelDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forCategoryCreate(Bot bot, DiscordConfiguration config, CategoryCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forCategoryUpdate(Bot bot, DiscordConfiguration config, CategoryUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forCategoryDelete(Bot bot, DiscordConfiguration config, CategoryDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forPinsUpdate(Bot bot, DiscordConfiguration config, PinsUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forTypingStart(Bot bot, DiscordConfiguration config, TypingStartEvent event) { return Mono.just(false); }
    default Mono<Boolean> forVoiceStateUpdate(Bot bot, DiscordConfiguration config, VoiceStateUpdateEvent event) { return Mono.just(false); }
    //Message Events
    default Mono<Boolean> forMessageCreate(Bot bot, DiscordConfiguration config, MessageCreateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forMessageUpdate(Bot bot, DiscordConfiguration config, MessageUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forMessageDelete(Bot bot, DiscordConfiguration config, MessageDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forMessageBulkDelete(Bot bot, DiscordConfiguration config, MessageBulkDeleteEvent event) { return Mono.just(false); }
    default Mono<Boolean> forReactionAdd(Bot bot, DiscordConfiguration config, ReactionAddEvent event) { return Mono.just(false); }
    default Mono<Boolean> forReactionRemove(Bot bot, DiscordConfiguration config, ReactionRemoveEvent event) { return Mono.just(false); }
    default Mono<Boolean> forReactionRemoveAll(Bot bot, DiscordConfiguration config, ReactionRemoveAllEvent event) { return Mono.just(false); }
    default Mono<Boolean> forReactionRemoveEmoji(Bot bot, DiscordConfiguration config, ReactionRemoveEmojiEvent event) { return Mono.just(false); }
    //User Events
    default Mono<Boolean> forPresenceUpdate(Bot bot, DiscordConfiguration config, PresenceUpdateEvent event) { return Mono.just(false); }
    default Mono<Boolean> forUserUpdate(Bot bot, DiscordConfiguration config, UserUpdateEvent event) { return Mono.just(false); }

    static DiscordEventHookGuard identity(boolean value) {
        return new IdentityDiscordEventHookGuard(value);
    }
}
