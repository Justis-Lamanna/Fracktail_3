package com.github.lucbui.fracktail3.discord.event;

import com.github.lucbui.fracktail3.magic.hook.SupportedEvent;
import discord4j.core.event.domain.*;
import discord4j.core.event.domain.channel.*;
import discord4j.core.event.domain.guild.*;
import discord4j.core.event.domain.lifecycle.*;
import discord4j.core.event.domain.message.*;
import discord4j.core.event.domain.role.RoleCreateEvent;
import discord4j.core.event.domain.role.RoleDeleteEvent;
import discord4j.core.event.domain.role.RoleUpdateEvent;

/**
 * Marker interface which marks an event as supported
 */
public enum DiscordSupportedEvent implements SupportedEvent {
    //--Lifecycle Events
    CONNECT(ConnectEvent.class),
    DISCONNECT(DisconnectEvent.class),
    READY(ReadyEvent.class),
    RECONNECT(ReconnectEvent.class),
    RECONNECT_START(ReconnectStartEvent.class),
    RECONNECT_FAIL(ReconnectFailEvent.class),
    RESUME(ResumeEvent.class),
    //--Guild Events
    GUILD_CREATE(GuildCreateEvent.class),
    GUILD_UPDATE(GuildUpdateEvent.class),
    GUILD_DELETE(GuildDeleteEvent.class),
    MEMBER_JOIN(MemberJoinEvent.class),
    MEMBER_UPDATE(MemberUpdateEvent.class),
    MEMBER_LEAVE(MemberLeaveEvent.class),
    MEMBER_CHUNK(MemberChunkEvent.class),
    BAN(BanEvent.class),
    UNBAN(UnbanEvent.class),
    EMOJI_UPDATE(EmojisUpdateEvent.class),
    INTEGRATIONS_UPDATE(IntegrationsUpdateEvent.class),
    //--Channel Events
    TEXT_CHANNEL_CREATE(TextChannelCreateEvent.class),
    TEXT_CHANNEL_UPDATE(TextChannelUpdateEvent.class),
    TEXT_CHANNEL_DELETE(TextChannelDeleteEvent.class),
    VOICE_CHANNEL_CREATE(VoiceChannelCreateEvent.class),
    VOICE_CHANNEL_UPDATE(VoiceChannelUpdateEvent.class),
    VOICE_CHANNEL_DELETE(VoiceChannelDeleteEvent.class),
    STORE_CHANNEL_CREATE(StoreChannelCreateEvent.class),
    STORE_CHANNEL_UPDATE(StoreChannelUpdateEvent.class),
    STORE_CHANNEL_DELETE(StoreChannelDeleteEvent.class),
    NEWS_CHANNEL_CREATE(NewsChannelCreateEvent.class),
    NEWS_CHANNEL_UPDATE(NewsChannelUpdateEvent.class),
    NEWS_CHANNEL_DELETE(NewsChannelDeleteEvent.class),
    PRIVATE_CHANNEL_CREATE(PrivateChannelCreateEvent.class),
    PRIVATE_CHANNEL_DELETE(PrivateChannelDeleteEvent.class),
    CATEGORY_CREATE(CategoryCreateEvent.class),
    CATEGORY_UPDATE(CategoryUpdateEvent.class),
    CATEGORY_DELETE(CategoryDeleteEvent.class),
    PINS_UPDATE(PinsUpdateEvent.class),
    TYPING_START(TypingStartEvent.class),
    //--Message Events
    MESSAGE_CREATE(MessageCreateEvent.class),
    MESSAGE_UPDATE(MessageUpdateEvent.class),
    MESSAGE_DELETE(MessageDeleteEvent.class),
    MESSAGE_BULK_DELETE(MessageBulkDeleteEvent.class),
    REACTION_ADD(ReactionAddEvent.class),
    REACTION_REMOVE(ReactionRemoveEvent.class),
    REACTION_REMOVE_ALL(ReactionRemoveAllEvent.class),
    REACTION_REMOVE_EMOJI(ReactionRemoveEmojiEvent.class),
    //--Role Events
    ROLE_CREATE(RoleCreateEvent.class),
    ROLE_UPDATE(RoleUpdateEvent.class),
    ROLE_DELETE(RoleDeleteEvent.class),
    //--Everything Else!
    INVITE_CREATE(InviteCreateEvent.class),
    INVITE_DELETE(InviteDeleteEvent.class),
    PRESENCE_UPDATE(PresenceUpdateEvent.class),
    USER_UPDATE(UserUpdateEvent.class),
    VOICE_SERVER_UPDATE(VoiceServerUpdateEvent.class),
    VOICE_STATE_UPDATE(VoiceStateUpdateEvent.class),
    WEBHOOKS_UPDATE(WebhooksUpdateEvent.class);

    private final Class<? extends Event> eventClass;

    DiscordSupportedEvent(Class<? extends Event> eventClass) {
        this.eventClass = eventClass;
    }

    public Class<? extends Event> getRawEventClass() {
        return eventClass;
    }
}
