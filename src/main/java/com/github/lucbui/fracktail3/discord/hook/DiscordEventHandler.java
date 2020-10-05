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
 * A snippet of code which executes when a certain event comes in
 */
public interface DiscordEventHandler {
    //Lifecycle Events
    default Mono<Void> onConnect(Bot bot, DiscordConfiguration config, ConnectEvent event) { return Mono.empty(); }
    default Mono<Void> onDisconnect(Bot bot, DiscordConfiguration config, DisconnectEvent event) { return Mono.empty(); }
    default Mono<Void> onReconnect(Bot bot, DiscordConfiguration config, ReconnectEvent event) { return Mono.empty(); }
    default Mono<Void> onReconnectStart(Bot bot, DiscordConfiguration config, ReconnectStartEvent event) { return Mono.empty(); }
    default Mono<Void> onReconnectFail(Bot bot, DiscordConfiguration config, ReconnectFailEvent event) { return Mono.empty(); }
    default Mono<Void> onReady(Bot bot, DiscordConfiguration config, ReadyEvent event) { return Mono.empty(); }
    default Mono<Void> onResume(Bot bot, DiscordConfiguration config, ResumeEvent event) { return Mono.empty(); }
    //Guild Events
    default Mono<Void> onGuildCreate(Bot bot, DiscordConfiguration config, GuildCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onGuildUpdate(Bot bot, DiscordConfiguration config, GuildUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onGuildDelete(Bot bot, DiscordConfiguration config, GuildDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onMemberJoin(Bot bot, DiscordConfiguration config, MemberJoinEvent event) { return Mono.empty(); }
    default Mono<Void> onMemberUpdate(Bot bot, DiscordConfiguration config, MemberUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onMemberLeave(Bot bot, DiscordConfiguration config, MemberLeaveEvent event) { return Mono.empty(); }
    default Mono<Void> onMemberChunk(Bot bot, DiscordConfiguration config, MemberChunkEvent event) { return Mono.empty(); }
    default Mono<Void> onEmojisUpdate(Bot bot, DiscordConfiguration config, EmojisUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onIntegrationsUpdate(Bot bot, DiscordConfiguration config, IntegrationsUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onBan(Bot bot, DiscordConfiguration config, BanEvent event) { return Mono.empty(); }
    default Mono<Void> onUnban(Bot bot, DiscordConfiguration config, UnbanEvent event) { return Mono.empty(); }
    default Mono<Void> onInviteCreate(Bot bot, DiscordConfiguration config, InviteCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onInviteDelete(Bot bot, DiscordConfiguration config, InviteDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onVoiceServerUpdate(Bot bot, DiscordConfiguration config, VoiceServerUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onWebhooksUpdate(Bot bot, DiscordConfiguration config, WebhooksUpdateEvent event) { return Mono.empty(); }
    //Role Events
    default Mono<Void> onRoleCreate(Bot bot, DiscordConfiguration config, RoleCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onRoleUpdate(Bot bot, DiscordConfiguration config, RoleUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onRoleDelete(Bot bot, DiscordConfiguration config, RoleDeleteEvent event) { return Mono.empty(); }
    //Channel Events
    default Mono<Void> onTextChannelCreate(Bot bot, DiscordConfiguration config, TextChannelCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onTextChannelUpdate(Bot bot, DiscordConfiguration config, TextChannelUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onTextChannelDelete(Bot bot, DiscordConfiguration config, TextChannelDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onVoiceChannelCreate(Bot bot, DiscordConfiguration config, VoiceChannelCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onVoiceChannelUpdate(Bot bot, DiscordConfiguration config, VoiceChannelUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onVoiceChannelDelete(Bot bot, DiscordConfiguration config, VoiceChannelDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onPrivateChannelCreate(Bot bot, DiscordConfiguration config, PrivateChannelCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onPrivateChannelDelete(Bot bot, DiscordConfiguration config, PrivateChannelDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onNewsChannelCreate(Bot bot, DiscordConfiguration config, NewsChannelCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onNewsChannelUpdate(Bot bot, DiscordConfiguration config, NewsChannelUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onNewsChannelDelete(Bot bot, DiscordConfiguration config, NewsChannelDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onStoreChannelCreate(Bot bot, DiscordConfiguration config, StoreChannelCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onStoreChannelUpdate(Bot bot, DiscordConfiguration config, StoreChannelUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onStoreChannelDelete(Bot bot, DiscordConfiguration config, StoreChannelDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onCategoryCreate(Bot bot, DiscordConfiguration config, CategoryCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onCategoryUpdate(Bot bot, DiscordConfiguration config, CategoryUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onCategoryDelete(Bot bot, DiscordConfiguration config, CategoryDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onPinsUpdate(Bot bot, DiscordConfiguration config, PinsUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onTypingStart(Bot bot, DiscordConfiguration config, TypingStartEvent event) { return Mono.empty(); }
    default Mono<Void> onVoiceStateUpdate(Bot bot, DiscordConfiguration config, VoiceStateUpdateEvent event) { return Mono.empty(); }
    //Message Events
    default Mono<Void> onMessageCreate(Bot bot, DiscordConfiguration config, MessageCreateEvent event) { return Mono.empty(); }
    default Mono<Void> onMessageUpdate(Bot bot, DiscordConfiguration config, MessageUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onMessageDelete(Bot bot, DiscordConfiguration config, MessageDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onMessageBulkDelete(Bot bot, DiscordConfiguration config, MessageBulkDeleteEvent event) { return Mono.empty(); }
    default Mono<Void> onReactionAdd(Bot bot, DiscordConfiguration config, ReactionAddEvent event) { return Mono.empty(); }
    default Mono<Void> onReactionRemove(Bot bot, DiscordConfiguration config, ReactionRemoveEvent event) { return Mono.empty(); }
    default Mono<Void> onReactionRemoveAll(Bot bot, DiscordConfiguration config, ReactionRemoveAllEvent event) { return Mono.empty(); }
    default Mono<Void> onReactionRemoveEmoji(Bot bot, DiscordConfiguration config, ReactionRemoveEmojiEvent event) { return Mono.empty(); }
    //User Events
    default Mono<Void> onPresenceUpdate(Bot bot, DiscordConfiguration config, PresenceUpdateEvent event) { return Mono.empty(); }
    default Mono<Void> onUserUpdate(Bot bot, DiscordConfiguration config, UserUpdateEvent event) { return Mono.empty(); }

    static DiscordEventHandler noop() {
        return new DiscordEventHandler(){};
    }
}
