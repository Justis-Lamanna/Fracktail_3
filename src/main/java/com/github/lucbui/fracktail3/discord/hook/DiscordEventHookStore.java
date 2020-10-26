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
import com.github.lucbui.fracktail3.magic.util.IdStore;

/**
 * Store of all Event Hooks.
 */
public class DiscordEventHookStore {
    private IdStore<DiscordEventHook<ConnectAction>> connectActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<DisconnectAction>> disconnectActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ReconnectAction>> reconnectActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ReconnectStartAction>> reconnectStartActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ReconnectFailAction>> reconnectFailActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ReadyAction>> readyActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ResumeAction>> resumeActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<GuildCreateAction>> guildCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<GuildUpdateAction>> guildUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<GuildDeleteAction>> guildDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<MemberJoinAction>> memberJoinActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<MemberUpdateAction>> memberUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<MemberLeaveAction>> memberLeaveActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<MemberChunkAction>> memberChunkActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<EmojisUpdateAction>> emojisUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<IntegrationsUpdateAction>> integrationsUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<BanAction>> banActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<UnbanAction>> unbanActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<InviteCreateAction>> inviteCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<InviteDeleteAction>> inviteDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<VoiceServerUpdateAction>> voiceServerUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<WebhooksUpdateAction>> webhooksUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<RoleCreateAction>> roleCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<RoleUpdateAction>> roleUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<RoleDeleteAction>> roleDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<TextChannelCreateAction>> textChannelCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<TextChannelUpdateAction>> textChannelUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<TextChannelDeleteAction>> textChannelDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<VoiceChannelCreateAction>> voiceChannelCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<VoiceChannelUpdateAction>> voiceChannelUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<VoiceChannelDeleteAction>> voiceChannelDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<PrivateChannelCreateAction>> privateChannelCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<PrivateChannelDeleteAction>> privateChannelDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<NewsChannelCreateAction>> newsChannelCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<NewsChannelUpdateAction>> newsChannelUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<NewsChannelDeleteAction>> newsChannelDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<StoreChannelCreateAction>> storeChannelCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<StoreChannelUpdateAction>> storeChannelUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<StoreChannelDeleteAction>> storeChannelDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<CategoryCreateAction>> categoryCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<CategoryUpdateAction>> categoryUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<CategoryDeleteAction>> categoryDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<PinsUpdateAction>> pinsUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<TypingStartAction>> typingStartActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<VoiceStateUpdateAction>> voiceStateUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<MessageCreateAction>> messageCreateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<MessageUpdateAction>> messageUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<MessageDeleteAction>> messageDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<MessageBulkDeleteAction>> messageBulkDeleteActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ReactionAddAction>> reactionAddActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ReactionRemoveAction>> reactionRemoveActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ReactionRemoveAllAction>> reactionRemoveAllActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<ReactionRemoveEmojiAction>> reactionRemoveEmojiActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<PresenceUpdateAction>> presenceUpdateActionStore = IdStore.emptyIdStore();
    private IdStore<DiscordEventHook<UserUpdateAction>> userUpdateActionStore = IdStore.emptyIdStore();

    void setConnectActionStore(IdStore<DiscordEventHook<ConnectAction>> connectActionStore) {
        this.connectActionStore = connectActionStore;
    }

    void setDisconnectActionStore(IdStore<DiscordEventHook<DisconnectAction>> disconnectActionStore) {
        this.disconnectActionStore = disconnectActionStore;
    }

    void setReconnectActionStore(IdStore<DiscordEventHook<ReconnectAction>> reconnectActionStore) {
        this.reconnectActionStore = reconnectActionStore;
    }

    void setReconnectStartActionStore(IdStore<DiscordEventHook<ReconnectStartAction>> reconnectStartActionStore) {
        this.reconnectStartActionStore = reconnectStartActionStore;
    }

    void setReconnectFailActionStore(IdStore<DiscordEventHook<ReconnectFailAction>> reconnectFailActionStore) {
        this.reconnectFailActionStore = reconnectFailActionStore;
    }

    void setReadyActionStore(IdStore<DiscordEventHook<ReadyAction>> readyActionStore) {
        this.readyActionStore = readyActionStore;
    }

    void setResumeActionStore(IdStore<DiscordEventHook<ResumeAction>> resumeActionStore) {
        this.resumeActionStore = resumeActionStore;
    }

    void setGuildCreateActionStore(IdStore<DiscordEventHook<GuildCreateAction>> guildCreateActionStore) {
        this.guildCreateActionStore = guildCreateActionStore;
    }

    void setGuildUpdateActionStore(IdStore<DiscordEventHook<GuildUpdateAction>> guildUpdateActionStore) {
        this.guildUpdateActionStore = guildUpdateActionStore;
    }

    void setGuildDeleteActionStore(IdStore<DiscordEventHook<GuildDeleteAction>> guildDeleteActionStore) {
        this.guildDeleteActionStore = guildDeleteActionStore;
    }

    void setMemberJoinActionStore(IdStore<DiscordEventHook<MemberJoinAction>> memberJoinActionStore) {
        this.memberJoinActionStore = memberJoinActionStore;
    }

    void setMemberUpdateActionStore(IdStore<DiscordEventHook<MemberUpdateAction>> memberUpdateActionStore) {
        this.memberUpdateActionStore = memberUpdateActionStore;
    }

    void setMemberLeaveActionStore(IdStore<DiscordEventHook<MemberLeaveAction>> memberLeaveActionStore) {
        this.memberLeaveActionStore = memberLeaveActionStore;
    }

    void setMemberChunkActionStore(IdStore<DiscordEventHook<MemberChunkAction>> memberChunkActionStore) {
        this.memberChunkActionStore = memberChunkActionStore;
    }

    void setEmojisUpdateActionStore(IdStore<DiscordEventHook<EmojisUpdateAction>> emojisUpdateActionStore) {
        this.emojisUpdateActionStore = emojisUpdateActionStore;
    }

    void setIntegrationsUpdateActionStore(IdStore<DiscordEventHook<IntegrationsUpdateAction>> integrationsUpdateActionStore) {
        this.integrationsUpdateActionStore = integrationsUpdateActionStore;
    }

    void setBanActionStore(IdStore<DiscordEventHook<BanAction>> banActionStore) {
        this.banActionStore = banActionStore;
    }

    void setUnbanActionStore(IdStore<DiscordEventHook<UnbanAction>> unbanActionStore) {
        this.unbanActionStore = unbanActionStore;
    }

    void setInviteCreateActionStore(IdStore<DiscordEventHook<InviteCreateAction>> inviteCreateActionStore) {
        this.inviteCreateActionStore = inviteCreateActionStore;
    }

    void setInviteDeleteActionStore(IdStore<DiscordEventHook<InviteDeleteAction>> inviteDeleteActionStore) {
        this.inviteDeleteActionStore = inviteDeleteActionStore;
    }

    void setVoiceServerUpdateActionStore(IdStore<DiscordEventHook<VoiceServerUpdateAction>> voiceServerUpdateActionStore) {
        this.voiceServerUpdateActionStore = voiceServerUpdateActionStore;
    }

    void setWebhooksUpdateActionStore(IdStore<DiscordEventHook<WebhooksUpdateAction>> webhooksUpdateActionStore) {
        this.webhooksUpdateActionStore = webhooksUpdateActionStore;
    }

    void setRoleCreateActionStore(IdStore<DiscordEventHook<RoleCreateAction>> roleCreateActionStore) {
        this.roleCreateActionStore = roleCreateActionStore;
    }

    void setRoleUpdateActionStore(IdStore<DiscordEventHook<RoleUpdateAction>> roleUpdateActionStore) {
        this.roleUpdateActionStore = roleUpdateActionStore;
    }

    void setRoleDeleteActionStore(IdStore<DiscordEventHook<RoleDeleteAction>> roleDeleteActionStore) {
        this.roleDeleteActionStore = roleDeleteActionStore;
    }

    void setTextChannelCreateActionStore(IdStore<DiscordEventHook<TextChannelCreateAction>> textChannelCreateActionStore) {
        this.textChannelCreateActionStore = textChannelCreateActionStore;
    }

    void setTextChannelUpdateActionStore(IdStore<DiscordEventHook<TextChannelUpdateAction>> textChannelUpdateActionStore) {
        this.textChannelUpdateActionStore = textChannelUpdateActionStore;
    }

    void setTextChannelDeleteActionStore(IdStore<DiscordEventHook<TextChannelDeleteAction>> textChannelDeleteActionStore) {
        this.textChannelDeleteActionStore = textChannelDeleteActionStore;
    }

    void setVoiceChannelCreateActionStore(IdStore<DiscordEventHook<VoiceChannelCreateAction>> voiceChannelCreateActionStore) {
        this.voiceChannelCreateActionStore = voiceChannelCreateActionStore;
    }

    void setVoiceChannelUpdateActionStore(IdStore<DiscordEventHook<VoiceChannelUpdateAction>> voiceChannelUpdateActionStore) {
        this.voiceChannelUpdateActionStore = voiceChannelUpdateActionStore;
    }

    void setVoiceChannelDeleteActionStore(IdStore<DiscordEventHook<VoiceChannelDeleteAction>> voiceChannelDeleteActionStore) {
        this.voiceChannelDeleteActionStore = voiceChannelDeleteActionStore;
    }

    void setPrivateChannelCreateActionStore(IdStore<DiscordEventHook<PrivateChannelCreateAction>> privateChannelCreateActionStore) {
        this.privateChannelCreateActionStore = privateChannelCreateActionStore;
    }

    void setPrivateChannelDeleteActionStore(IdStore<DiscordEventHook<PrivateChannelDeleteAction>> privateChannelDeleteActionStore) {
        this.privateChannelDeleteActionStore = privateChannelDeleteActionStore;
    }

    void setNewsChannelCreateActionStore(IdStore<DiscordEventHook<NewsChannelCreateAction>> newsChannelCreateActionStore) {
        this.newsChannelCreateActionStore = newsChannelCreateActionStore;
    }

    void setNewsChannelUpdateActionStore(IdStore<DiscordEventHook<NewsChannelUpdateAction>> newsChannelUpdateActionStore) {
        this.newsChannelUpdateActionStore = newsChannelUpdateActionStore;
    }

    void setNewsChannelDeleteActionStore(IdStore<DiscordEventHook<NewsChannelDeleteAction>> newsChannelDeleteActionStore) {
        this.newsChannelDeleteActionStore = newsChannelDeleteActionStore;
    }

    void setStoreChannelCreateActionStore(IdStore<DiscordEventHook<StoreChannelCreateAction>> storeChannelCreateActionStore) {
        this.storeChannelCreateActionStore = storeChannelCreateActionStore;
    }

    void setStoreChannelUpdateActionStore(IdStore<DiscordEventHook<StoreChannelUpdateAction>> storeChannelUpdateActionStore) {
        this.storeChannelUpdateActionStore = storeChannelUpdateActionStore;
    }

    void setStoreChannelDeleteActionStore(IdStore<DiscordEventHook<StoreChannelDeleteAction>> storeChannelDeleteActionStore) {
        this.storeChannelDeleteActionStore = storeChannelDeleteActionStore;
    }

    void setCategoryCreateActionStore(IdStore<DiscordEventHook<CategoryCreateAction>> categoryCreateActionStore) {
        this.categoryCreateActionStore = categoryCreateActionStore;
    }

    void setCategoryUpdateActionStore(IdStore<DiscordEventHook<CategoryUpdateAction>> categoryUpdateActionStore) {
        this.categoryUpdateActionStore = categoryUpdateActionStore;
    }

    void setCategoryDeleteActionStore(IdStore<DiscordEventHook<CategoryDeleteAction>> categoryDeleteActionStore) {
        this.categoryDeleteActionStore = categoryDeleteActionStore;
    }

    void setPinsUpdateActionStore(IdStore<DiscordEventHook<PinsUpdateAction>> pinsUpdateActionStore) {
        this.pinsUpdateActionStore = pinsUpdateActionStore;
    }

    void setTypingStartActionStore(IdStore<DiscordEventHook<TypingStartAction>> typingStartActionStore) {
        this.typingStartActionStore = typingStartActionStore;
    }

    void setVoiceStateUpdateActionStore(IdStore<DiscordEventHook<VoiceStateUpdateAction>> voiceStateUpdateActionStore) {
        this.voiceStateUpdateActionStore = voiceStateUpdateActionStore;
    }

    void setMessageCreateActionStore(IdStore<DiscordEventHook<MessageCreateAction>> messageCreateActionStore) {
        this.messageCreateActionStore = messageCreateActionStore;
    }

    void setMessageUpdateActionStore(IdStore<DiscordEventHook<MessageUpdateAction>> messageUpdateActionStore) {
        this.messageUpdateActionStore = messageUpdateActionStore;
    }

    void setMessageDeleteActionStore(IdStore<DiscordEventHook<MessageDeleteAction>> messageDeleteActionStore) {
        this.messageDeleteActionStore = messageDeleteActionStore;
    }

    void setMessageBulkDeleteActionStore(IdStore<DiscordEventHook<MessageBulkDeleteAction>> messageBulkDeleteActionStore) {
        this.messageBulkDeleteActionStore = messageBulkDeleteActionStore;
    }

    void setReactionAddActionStore(IdStore<DiscordEventHook<ReactionAddAction>> reactionAddActionStore) {
        this.reactionAddActionStore = reactionAddActionStore;
    }

    void setReactionRemoveActionStore(IdStore<DiscordEventHook<ReactionRemoveAction>> reactionRemoveActionStore) {
        this.reactionRemoveActionStore = reactionRemoveActionStore;
    }

    void setReactionRemoveAllActionStore(IdStore<DiscordEventHook<ReactionRemoveAllAction>> reactionRemoveAllActionStore) {
        this.reactionRemoveAllActionStore = reactionRemoveAllActionStore;
    }

    void setReactionRemoveEmojiActionStore(IdStore<DiscordEventHook<ReactionRemoveEmojiAction>> reactionRemoveEmojiActionStore) {
        this.reactionRemoveEmojiActionStore = reactionRemoveEmojiActionStore;
    }

    void setPresenceUpdateActionStore(IdStore<DiscordEventHook<PresenceUpdateAction>> presenceUpdateActionStore) {
        this.presenceUpdateActionStore = presenceUpdateActionStore;
    }

    void setUserUpdateActionStore(IdStore<DiscordEventHook<UserUpdateAction>> userUpdateActionStore) {
        this.userUpdateActionStore = userUpdateActionStore;
    }

    public IdStore<DiscordEventHook<ConnectAction>> getConnectActionStore() {
        return connectActionStore;
    }

    public IdStore<DiscordEventHook<DisconnectAction>> getDisconnectActionStore() {
        return disconnectActionStore;
    }

    public IdStore<DiscordEventHook<ReconnectAction>> getReconnectActionStore() {
        return reconnectActionStore;
    }

    public IdStore<DiscordEventHook<ReconnectStartAction>> getReconnectStartActionStore() {
        return reconnectStartActionStore;
    }

    public IdStore<DiscordEventHook<ReconnectFailAction>> getReconnectFailActionStore() {
        return reconnectFailActionStore;
    }

    public IdStore<DiscordEventHook<ReadyAction>> getReadyActionStore() {
        return readyActionStore;
    }

    public IdStore<DiscordEventHook<ResumeAction>> getResumeActionStore() {
        return resumeActionStore;
    }

    public IdStore<DiscordEventHook<GuildCreateAction>> getGuildCreateActionStore() {
        return guildCreateActionStore;
    }

    public IdStore<DiscordEventHook<GuildUpdateAction>> getGuildUpdateActionStore() {
        return guildUpdateActionStore;
    }

    public IdStore<DiscordEventHook<GuildDeleteAction>> getGuildDeleteActionStore() {
        return guildDeleteActionStore;
    }

    public IdStore<DiscordEventHook<MemberJoinAction>> getMemberJoinActionStore() {
        return memberJoinActionStore;
    }

    public IdStore<DiscordEventHook<MemberUpdateAction>> getMemberUpdateActionStore() {
        return memberUpdateActionStore;
    }

    public IdStore<DiscordEventHook<MemberLeaveAction>> getMemberLeaveActionStore() {
        return memberLeaveActionStore;
    }

    public IdStore<DiscordEventHook<MemberChunkAction>> getMemberChunkActionStore() {
        return memberChunkActionStore;
    }

    public IdStore<DiscordEventHook<EmojisUpdateAction>> getEmojisUpdateActionStore() {
        return emojisUpdateActionStore;
    }

    public IdStore<DiscordEventHook<IntegrationsUpdateAction>> getIntegrationsUpdateActionStore() {
        return integrationsUpdateActionStore;
    }

    public IdStore<DiscordEventHook<BanAction>> getBanActionStore() {
        return banActionStore;
    }

    public IdStore<DiscordEventHook<UnbanAction>> getUnbanActionStore() {
        return unbanActionStore;
    }

    public IdStore<DiscordEventHook<InviteCreateAction>> getInviteCreateActionStore() {
        return inviteCreateActionStore;
    }

    public IdStore<DiscordEventHook<InviteDeleteAction>> getInviteDeleteActionStore() {
        return inviteDeleteActionStore;
    }

    public IdStore<DiscordEventHook<VoiceServerUpdateAction>> getVoiceServerUpdateActionStore() {
        return voiceServerUpdateActionStore;
    }

    public IdStore<DiscordEventHook<WebhooksUpdateAction>> getWebhooksUpdateActionStore() {
        return webhooksUpdateActionStore;
    }

    public IdStore<DiscordEventHook<RoleCreateAction>> getRoleCreateActionStore() {
        return roleCreateActionStore;
    }

    public IdStore<DiscordEventHook<RoleUpdateAction>> getRoleUpdateActionStore() {
        return roleUpdateActionStore;
    }

    public IdStore<DiscordEventHook<RoleDeleteAction>> getRoleDeleteActionStore() {
        return roleDeleteActionStore;
    }

    public IdStore<DiscordEventHook<TextChannelCreateAction>> getTextChannelCreateActionStore() {
        return textChannelCreateActionStore;
    }

    public IdStore<DiscordEventHook<TextChannelUpdateAction>> getTextChannelUpdateActionStore() {
        return textChannelUpdateActionStore;
    }

    public IdStore<DiscordEventHook<TextChannelDeleteAction>> getTextChannelDeleteActionStore() {
        return textChannelDeleteActionStore;
    }

    public IdStore<DiscordEventHook<VoiceChannelCreateAction>> getVoiceChannelCreateActionStore() {
        return voiceChannelCreateActionStore;
    }

    public IdStore<DiscordEventHook<VoiceChannelUpdateAction>> getVoiceChannelUpdateActionStore() {
        return voiceChannelUpdateActionStore;
    }

    public IdStore<DiscordEventHook<VoiceChannelDeleteAction>> getVoiceChannelDeleteActionStore() {
        return voiceChannelDeleteActionStore;
    }

    public IdStore<DiscordEventHook<PrivateChannelCreateAction>> getPrivateChannelCreateActionStore() {
        return privateChannelCreateActionStore;
    }

    public IdStore<DiscordEventHook<PrivateChannelDeleteAction>> getPrivateChannelDeleteActionStore() {
        return privateChannelDeleteActionStore;
    }

    public IdStore<DiscordEventHook<NewsChannelCreateAction>> getNewsChannelCreateActionStore() {
        return newsChannelCreateActionStore;
    }

    public IdStore<DiscordEventHook<NewsChannelUpdateAction>> getNewsChannelUpdateActionStore() {
        return newsChannelUpdateActionStore;
    }

    public IdStore<DiscordEventHook<NewsChannelDeleteAction>> getNewsChannelDeleteActionStore() {
        return newsChannelDeleteActionStore;
    }

    public IdStore<DiscordEventHook<StoreChannelCreateAction>> getStoreChannelCreateActionStore() {
        return storeChannelCreateActionStore;
    }

    public IdStore<DiscordEventHook<StoreChannelUpdateAction>> getStoreChannelUpdateActionStore() {
        return storeChannelUpdateActionStore;
    }

    public IdStore<DiscordEventHook<StoreChannelDeleteAction>> getStoreChannelDeleteActionStore() {
        return storeChannelDeleteActionStore;
    }

    public IdStore<DiscordEventHook<CategoryCreateAction>> getCategoryCreateActionStore() {
        return categoryCreateActionStore;
    }

    public IdStore<DiscordEventHook<CategoryUpdateAction>> getCategoryUpdateActionStore() {
        return categoryUpdateActionStore;
    }

    public IdStore<DiscordEventHook<CategoryDeleteAction>> getCategoryDeleteActionStore() {
        return categoryDeleteActionStore;
    }

    public IdStore<DiscordEventHook<PinsUpdateAction>> getPinsUpdateActionStore() {
        return pinsUpdateActionStore;
    }

    public IdStore<DiscordEventHook<TypingStartAction>> getTypingStartActionStore() {
        return typingStartActionStore;
    }

    public IdStore<DiscordEventHook<VoiceStateUpdateAction>> getVoiceStateUpdateActionStore() {
        return voiceStateUpdateActionStore;
    }

    public IdStore<DiscordEventHook<MessageCreateAction>> getMessageCreateActionStore() {
        return messageCreateActionStore;
    }

    public IdStore<DiscordEventHook<MessageUpdateAction>> getMessageUpdateActionStore() {
        return messageUpdateActionStore;
    }

    public IdStore<DiscordEventHook<MessageDeleteAction>> getMessageDeleteActionStore() {
        return messageDeleteActionStore;
    }

    public IdStore<DiscordEventHook<MessageBulkDeleteAction>> getMessageBulkDeleteActionStore() {
        return messageBulkDeleteActionStore;
    }

    public IdStore<DiscordEventHook<ReactionAddAction>> getReactionAddActionStore() {
        return reactionAddActionStore;
    }

    public IdStore<DiscordEventHook<ReactionRemoveAction>> getReactionRemoveActionStore() {
        return reactionRemoveActionStore;
    }

    public IdStore<DiscordEventHook<ReactionRemoveAllAction>> getReactionRemoveAllActionStore() {
        return reactionRemoveAllActionStore;
    }

    public IdStore<DiscordEventHook<ReactionRemoveEmojiAction>> getReactionRemoveEmojiActionStore() {
        return reactionRemoveEmojiActionStore;
    }

    public IdStore<DiscordEventHook<PresenceUpdateAction>> getPresenceUpdateActionStore() {
        return presenceUpdateActionStore;
    }

    public IdStore<DiscordEventHook<UserUpdateAction>> getUserUpdateActionStore() {
        return userUpdateActionStore;
    }
}
