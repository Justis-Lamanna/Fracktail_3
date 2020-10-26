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
import com.github.lucbui.fracktail3.magic.util.IBuilder;
import com.github.lucbui.fracktail3.magic.util.IdStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Store of all Event Hooks.
 */
public class DiscordEventHookStoreBuilder implements IBuilder<DiscordEventHookStore> {
    private final List<DiscordEventHook<ConnectAction>> connectActionStore = new ArrayList<>();
    private final List<DiscordEventHook<DisconnectAction>> disconnectActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ReconnectAction>> reconnectActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ReconnectStartAction>> reconnectStartActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ReconnectFailAction>> reconnectFailActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ReadyAction>> readyActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ResumeAction>> resumeActionStore = new ArrayList<>();
    private final List<DiscordEventHook<GuildCreateAction>> guildCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<GuildUpdateAction>> guildUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<GuildDeleteAction>> guildDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<MemberJoinAction>> memberJoinActionStore = new ArrayList<>();
    private final List<DiscordEventHook<MemberUpdateAction>> memberUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<MemberLeaveAction>> memberLeaveActionStore = new ArrayList<>();
    private final List<DiscordEventHook<MemberChunkAction>> memberChunkActionStore = new ArrayList<>();
    private final List<DiscordEventHook<EmojisUpdateAction>> emojisUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<IntegrationsUpdateAction>> integrationsUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<BanAction>> banActionStore = new ArrayList<>();
    private final List<DiscordEventHook<UnbanAction>> unbanActionStore = new ArrayList<>();
    private final List<DiscordEventHook<InviteCreateAction>> inviteCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<InviteDeleteAction>> inviteDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<VoiceServerUpdateAction>> voiceServerUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<WebhooksUpdateAction>> webhooksUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<RoleCreateAction>> roleCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<RoleUpdateAction>> roleUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<RoleDeleteAction>> roleDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<TextChannelCreateAction>> textChannelCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<TextChannelUpdateAction>> textChannelUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<TextChannelDeleteAction>> textChannelDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<VoiceChannelCreateAction>> voiceChannelCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<VoiceChannelUpdateAction>> voiceChannelUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<VoiceChannelDeleteAction>> voiceChannelDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<PrivateChannelCreateAction>> privateChannelCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<PrivateChannelDeleteAction>> privateChannelDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<NewsChannelCreateAction>> newsChannelCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<NewsChannelUpdateAction>> newsChannelUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<NewsChannelDeleteAction>> newsChannelDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<StoreChannelCreateAction>> storeChannelCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<StoreChannelUpdateAction>> storeChannelUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<StoreChannelDeleteAction>> storeChannelDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<CategoryCreateAction>> categoryCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<CategoryUpdateAction>> categoryUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<CategoryDeleteAction>> categoryDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<PinsUpdateAction>> pinsUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<TypingStartAction>> typingStartActionStore = new ArrayList<>();
    private final List<DiscordEventHook<VoiceStateUpdateAction>> voiceStateUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<MessageCreateAction>> messageCreateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<MessageUpdateAction>> messageUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<MessageDeleteAction>> messageDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<MessageBulkDeleteAction>> messageBulkDeleteActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ReactionAddAction>> reactionAddActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ReactionRemoveAction>> reactionRemoveActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ReactionRemoveAllAction>> reactionRemoveAllActionStore = new ArrayList<>();
    private final List<DiscordEventHook<ReactionRemoveEmojiAction>> reactionRemoveEmojiActionStore = new ArrayList<>();
    private final List<DiscordEventHook<PresenceUpdateAction>> presenceUpdateActionStore = new ArrayList<>();
    private final List<DiscordEventHook<UserUpdateAction>> userUpdateActionStore = new ArrayList<>();

    public DiscordEventHookStoreBuilder withConnectActionHook(DiscordEventHook<ConnectAction> hook) {
        this.connectActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withDisconnectActionHook(DiscordEventHook<DisconnectAction> hook) {
        this.disconnectActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withReconnectActionHook(DiscordEventHook<ReconnectAction> hook) {
        this.reconnectActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withReconnectStartActionHook(DiscordEventHook<ReconnectStartAction> hook) {
        this.reconnectStartActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withReconnectFailActionHook(DiscordEventHook<ReconnectFailAction> hook) {
        this.reconnectFailActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withReadyActionHook(DiscordEventHook<ReadyAction> hook) {
        this.readyActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withResumeActionHook(DiscordEventHook<ResumeAction> hook) {
        this.resumeActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withGuildCreateActionHook(DiscordEventHook<GuildCreateAction> hook) {
        this.guildCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withGuildUpdateActionHook(DiscordEventHook<GuildUpdateAction> hook) {
        this.guildUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withGuildDeleteActionHook(DiscordEventHook<GuildDeleteAction> hook) {
        this.guildDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withMemberJoinActionHook(DiscordEventHook<MemberJoinAction> hook) {
        this.memberJoinActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withMemberUpdateActionHook(DiscordEventHook<MemberUpdateAction> hook) {
        this.memberUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withMemberLeaveActionHook(DiscordEventHook<MemberLeaveAction> hook) {
        this.memberLeaveActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withMemberChunkActionHook(DiscordEventHook<MemberChunkAction> hook) {
        this.memberChunkActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withEmojisUpdateActionHook(DiscordEventHook<EmojisUpdateAction> hook) {
        this.emojisUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withIntegrationsUpdateActionHook(DiscordEventHook<IntegrationsUpdateAction> hook) {
        this.integrationsUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withBanActionHook(DiscordEventHook<BanAction> hook) {
        this.banActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withUnbanActionHook(DiscordEventHook<UnbanAction> hook) {
        this.unbanActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withInviteCreateActionHook(DiscordEventHook<InviteCreateAction> hook) {
        this.inviteCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withInviteDeleteActionHook(DiscordEventHook<InviteDeleteAction> hook) {
        this.inviteDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withVoiceServerUpdateActionHook(DiscordEventHook<VoiceServerUpdateAction> hook) {
        this.voiceServerUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withWebhooksUpdateActionHook(DiscordEventHook<WebhooksUpdateAction> hook) {
        this.webhooksUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withRoleCreateActionHook(DiscordEventHook<RoleCreateAction> hook) {
        this.roleCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withRoleUpdateActionHook(DiscordEventHook<RoleUpdateAction> hook) {
        this.roleUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withRoleDeleteActionHook(DiscordEventHook<RoleDeleteAction> hook) {
        this.roleDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withTextChannelCreateActionHook(DiscordEventHook<TextChannelCreateAction> hook) {
        this.textChannelCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withTextChannelUpdateActionHook(DiscordEventHook<TextChannelUpdateAction> hook) {
        this.textChannelUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withTextChannelDeleteActionHook(DiscordEventHook<TextChannelDeleteAction> hook) {
        this.textChannelDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withVoiceChannelCreateActionHook(DiscordEventHook<VoiceChannelCreateAction> hook) {
        this.voiceChannelCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withVoiceChannelUpdateActionHook(DiscordEventHook<VoiceChannelUpdateAction> hook) {
        this.voiceChannelUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withVoiceChannelDeleteActionHook(DiscordEventHook<VoiceChannelDeleteAction> hook) {
        this.voiceChannelDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withPrivateChannelCreateActionHook(DiscordEventHook<PrivateChannelCreateAction> hook) {
        this.privateChannelCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withPrivateChannelDeleteActionHook(DiscordEventHook<PrivateChannelDeleteAction> hook) {
        this.privateChannelDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withNewsChannelCreateActionHook(DiscordEventHook<NewsChannelCreateAction> hook) {
        this.newsChannelCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withNewsChannelUpdateActionHook(DiscordEventHook<NewsChannelUpdateAction> hook) {
        this.newsChannelUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withNewsChannelDeleteActionHook(DiscordEventHook<NewsChannelDeleteAction> hook) {
        this.newsChannelDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withStoreChannelCreateActionHook(DiscordEventHook<StoreChannelCreateAction> hook) {
        this.storeChannelCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withStoreChannelUpdateActionHook(DiscordEventHook<StoreChannelUpdateAction> hook) {
        this.storeChannelUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withStoreChannelDeleteActionHook(DiscordEventHook<StoreChannelDeleteAction> hook) {
        this.storeChannelDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withCategoryCreateActionHook(DiscordEventHook<CategoryCreateAction> hook) {
        this.categoryCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withCategoryUpdateActionHook(DiscordEventHook<CategoryUpdateAction> hook) {
        this.categoryUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withCategoryDeleteActionHook(DiscordEventHook<CategoryDeleteAction> hook) {
        this.categoryDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withPinsUpdateActionHook(DiscordEventHook<PinsUpdateAction> hook) {
        this.pinsUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withTypingStartActionHook(DiscordEventHook<TypingStartAction> hook) {
        this.typingStartActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withVoiceStateUpdateActionHook(DiscordEventHook<VoiceStateUpdateAction> hook) {
        this.voiceStateUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withMessageCreateActionHook(DiscordEventHook<MessageCreateAction> hook) {
        this.messageCreateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withMessageUpdateActionHook(DiscordEventHook<MessageUpdateAction> hook) {
        this.messageUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withMessageDeleteActionHook(DiscordEventHook<MessageDeleteAction> hook) {
        this.messageDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withMessageBulkDeleteActionHook(DiscordEventHook<MessageBulkDeleteAction> hook) {
        this.messageBulkDeleteActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withReactionAddActionHook(DiscordEventHook<ReactionAddAction> hook) {
        this.reactionAddActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withReactionRemoveActionHook(DiscordEventHook<ReactionRemoveAction> hook) {
        this.reactionRemoveActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withReactionRemoveAllActionHook(DiscordEventHook<ReactionRemoveAllAction> hook) {
        this.reactionRemoveAllActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withReactionRemoveEmojiActionHook(DiscordEventHook<ReactionRemoveEmojiAction> hook) {
        this.reactionRemoveEmojiActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withPresenceUpdateActionHook(DiscordEventHook<PresenceUpdateAction> hook) {
        this.presenceUpdateActionStore.add(hook);
		return this;
    }

    public DiscordEventHookStoreBuilder withUserUpdateActionHook(DiscordEventHook<UserUpdateAction> hook) {
        this.userUpdateActionStore.add(hook);
		return this;
    }

    @Override
    public DiscordEventHookStore build() {
        DiscordEventHookStore store = new DiscordEventHookStore();
        store.setConnectActionStore(new IdStore<>(connectActionStore));
        store.setDisconnectActionStore(new IdStore<>(disconnectActionStore));
        store.setReconnectActionStore(new IdStore<>(reconnectActionStore));
        store.setReconnectStartActionStore(new IdStore<>(reconnectStartActionStore));
        store.setReconnectFailActionStore(new IdStore<>(reconnectFailActionStore));
        store.setReadyActionStore(new IdStore<>(readyActionStore));
        store.setResumeActionStore(new IdStore<>(resumeActionStore));
        store.setGuildCreateActionStore(new IdStore<>(guildCreateActionStore));
        store.setGuildUpdateActionStore(new IdStore<>(guildUpdateActionStore));
        store.setGuildDeleteActionStore(new IdStore<>(guildDeleteActionStore));
        store.setMemberJoinActionStore(new IdStore<>(memberJoinActionStore));
        store.setMemberUpdateActionStore(new IdStore<>(memberUpdateActionStore));
        store.setMemberLeaveActionStore(new IdStore<>(memberLeaveActionStore));
        store.setMemberChunkActionStore(new IdStore<>(memberChunkActionStore));
        store.setEmojisUpdateActionStore(new IdStore<>(emojisUpdateActionStore));
        store.setIntegrationsUpdateActionStore(new IdStore<>(integrationsUpdateActionStore));
        store.setBanActionStore(new IdStore<>(banActionStore));
        store.setUnbanActionStore(new IdStore<>(unbanActionStore));
        store.setInviteCreateActionStore(new IdStore<>(inviteCreateActionStore));
        store.setInviteDeleteActionStore(new IdStore<>(inviteDeleteActionStore));
        store.setVoiceServerUpdateActionStore(new IdStore<>(voiceServerUpdateActionStore));
        store.setWebhooksUpdateActionStore(new IdStore<>(webhooksUpdateActionStore));
        store.setRoleCreateActionStore(new IdStore<>(roleCreateActionStore));
        store.setRoleUpdateActionStore(new IdStore<>(roleUpdateActionStore));
        store.setRoleDeleteActionStore(new IdStore<>(roleDeleteActionStore));
        store.setTextChannelCreateActionStore(new IdStore<>(textChannelCreateActionStore));
        store.setTextChannelUpdateActionStore(new IdStore<>(textChannelUpdateActionStore));
        store.setTextChannelDeleteActionStore(new IdStore<>(textChannelDeleteActionStore));
        store.setVoiceChannelCreateActionStore(new IdStore<>(voiceChannelCreateActionStore));
        store.setVoiceChannelUpdateActionStore(new IdStore<>(voiceChannelUpdateActionStore));
        store.setVoiceChannelDeleteActionStore(new IdStore<>(voiceChannelDeleteActionStore));
        store.setPrivateChannelCreateActionStore(new IdStore<>(privateChannelCreateActionStore));
        store.setPrivateChannelDeleteActionStore(new IdStore<>(privateChannelDeleteActionStore));
        store.setNewsChannelCreateActionStore(new IdStore<>(newsChannelCreateActionStore));
        store.setNewsChannelUpdateActionStore(new IdStore<>(newsChannelUpdateActionStore));
        store.setNewsChannelDeleteActionStore(new IdStore<>(newsChannelDeleteActionStore));
        store.setStoreChannelCreateActionStore(new IdStore<>(storeChannelCreateActionStore));
        store.setStoreChannelUpdateActionStore(new IdStore<>(storeChannelUpdateActionStore));
        store.setStoreChannelDeleteActionStore(new IdStore<>(storeChannelDeleteActionStore));
        store.setCategoryCreateActionStore(new IdStore<>(categoryCreateActionStore));
        store.setCategoryUpdateActionStore(new IdStore<>(categoryUpdateActionStore));
        store.setCategoryDeleteActionStore(new IdStore<>(categoryDeleteActionStore));
        store.setPinsUpdateActionStore(new IdStore<>(pinsUpdateActionStore));
        store.setTypingStartActionStore(new IdStore<>(typingStartActionStore));
        store.setVoiceStateUpdateActionStore(new IdStore<>(voiceStateUpdateActionStore));
        store.setMessageCreateActionStore(new IdStore<>(messageCreateActionStore));
        store.setMessageUpdateActionStore(new IdStore<>(messageUpdateActionStore));
        store.setMessageDeleteActionStore(new IdStore<>(messageDeleteActionStore));
        store.setMessageBulkDeleteActionStore(new IdStore<>(messageBulkDeleteActionStore));
        store.setReactionAddActionStore(new IdStore<>(reactionAddActionStore));
        store.setReactionRemoveActionStore(new IdStore<>(reactionRemoveActionStore));
        store.setReactionRemoveAllActionStore(new IdStore<>(reactionRemoveAllActionStore));
        store.setReactionRemoveEmojiActionStore(new IdStore<>(reactionRemoveEmojiActionStore));
        store.setPresenceUpdateActionStore(new IdStore<>(presenceUpdateActionStore));
        store.setUserUpdateActionStore(new IdStore<>(userUpdateActionStore));
        return store;
    }
}
