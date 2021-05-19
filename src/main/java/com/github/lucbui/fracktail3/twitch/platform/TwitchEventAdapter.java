package com.github.lucbui.fracktail3.twitch.platform;

import com.github.twitch4j.chat.events.CommandEvent;
import com.github.twitch4j.chat.events.channel.*;
import com.github.twitch4j.chat.events.roomstate.*;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

/**
 * Adapter which allows for responding to different Twitch events
 */
public abstract class TwitchEventAdapter {
    public Publisher<?> onBitsBadgeEarnedEvent(BitsBadgeEarnedEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onChannelJoinEvent(ChannelJoinEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onChannelLeaveEvent(ChannelLeaveEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onChannelMessageActionEvent(ChannelMessageActionEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onChannelMessageEvent(ChannelMessageEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onChannelModEvent(ChannelModEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onChannelNoticeEvent(ChannelNoticeEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onChannelStateEvent(ChannelStateEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onCheerEvent(CheerEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onClearChatEvent(ClearChatEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onDonationEvent(DonationEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onExtendSubscriptionEvent(ExtendSubscriptionEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onFollowEvent(FollowEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onGiftSubscriptionsEvent(GiftSubscriptionsEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onGiftSubUpgradeEvent(GiftSubUpgradeEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onGlobalUserStateEvent(GlobalUserStateEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onHostOffEvent(HostOffEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onHostOnEvent(HostOnEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onIRCMessageEvent(IRCMessageEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onListModsEvent(ListModsEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onListVipsEvent(ListVipsEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onMessageDeleteError(MessageDeleteError error) {
        return Mono.empty();
    }

    public Publisher<?> onMessageDeleteSuccess(MessageDeleteSuccess event) {
        return Mono.empty();
    }

    public Publisher<?> onPayForwardEvent(PayForwardEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onPrimeGiftReceivedEvent(PrimeGiftReceivedEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onPrimeSubUpgradeEvent(PrimeSubUpgradeEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onRaidCancellationEvent(RaidCancellationEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onRaidEvent(RaidEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onRewardGiftEvent(RewardGiftEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onRitualEvent(RitualEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onSubscriptionEvent(SubscriptionEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onUserBanEvent(UserBanEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onUserStateEvent(UserStateEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onUserTimeoutEvent(UserTimeoutEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onCommandEvent(CommandEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onChannelStatesEvent(ChannelStatesEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onEmoteOnlyEvent(EmoteOnlyEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onFollowersOnlyEvent(FollowersOnlyEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onRobot9000Event(Robot9000Event event) {
        return Mono.empty();
    }

    public Publisher<?> onSlowModeEvent(SlowModeEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onSubscribersOnlyEvent(SubscribersOnlyEvent event) {
        return Mono.empty();
    }

    public Publisher<?> onUnknownEvent(Object object) {
        return Mono.empty();
    }

    public Publisher<?> onEvent(Object o) {
        if (o instanceof BitsBadgeEarnedEvent) {
            return onBitsBadgeEarnedEvent((BitsBadgeEarnedEvent) o);
        } else if (o instanceof ChannelJoinEvent) {
            return onChannelJoinEvent((ChannelJoinEvent) o);
        } else if (o instanceof ChannelLeaveEvent) {
            return onChannelLeaveEvent((ChannelLeaveEvent) o);
        } else if (o instanceof ChannelMessageActionEvent) {
            return onChannelMessageActionEvent((ChannelMessageActionEvent) o);
        } else if (o instanceof ChannelMessageEvent) {
            return onChannelMessageEvent((ChannelMessageEvent) o);
        } else if (o instanceof ChannelModEvent) {
            return onChannelModEvent((ChannelModEvent) o);
        } else if (o instanceof ChannelNoticeEvent) {
            return onChannelNoticeEvent((ChannelNoticeEvent) o);
        } else if (o instanceof ChannelStateEvent) {
            return onChannelStateEvent((ChannelStateEvent) o);
        } else if (o instanceof CheerEvent) {
            return onCheerEvent((CheerEvent) o);
        } else if (o instanceof ClearChatEvent) {
            return onClearChatEvent((ClearChatEvent) o);
        } else if (o instanceof DonationEvent) {
            return onDonationEvent((DonationEvent) o);
        } else if (o instanceof ExtendSubscriptionEvent) {
            return onExtendSubscriptionEvent((ExtendSubscriptionEvent) o);
        } else if (o instanceof FollowEvent) {
            return onFollowEvent((FollowEvent) o);
        } else if (o instanceof GiftSubscriptionsEvent) {
            return onGiftSubscriptionsEvent((GiftSubscriptionsEvent) o);
        } else if (o instanceof GiftSubUpgradeEvent) {
            return onGiftSubUpgradeEvent((GiftSubUpgradeEvent) o);
        } else if (o instanceof GlobalUserStateEvent) {
            return onGlobalUserStateEvent((GlobalUserStateEvent) o);
        } else if (o instanceof HostOffEvent) {
            return onHostOffEvent((HostOffEvent) o);
        } else if (o instanceof HostOnEvent) {
            return onHostOnEvent((HostOnEvent) o);
        } else if (o instanceof IRCMessageEvent) {
            return onIRCMessageEvent((IRCMessageEvent) o);
        } else if (o instanceof ListModsEvent) {
            return onListModsEvent((ListModsEvent) o);
        } else if (o instanceof ListVipsEvent) {
            return onListVipsEvent((ListVipsEvent) o);
        } else if (o instanceof MessageDeleteError) {
            return onMessageDeleteError((MessageDeleteError) o);
        } else if (o instanceof MessageDeleteSuccess) {
            return onMessageDeleteSuccess((MessageDeleteSuccess) o);
        } else if (o instanceof PayForwardEvent) {
            return onPayForwardEvent((PayForwardEvent) o);
        } else if (o instanceof PrimeGiftReceivedEvent) {
            return onPrimeGiftReceivedEvent((PrimeGiftReceivedEvent) o);
        } else if (o instanceof PrimeSubUpgradeEvent) {
            return onPrimeSubUpgradeEvent((PrimeSubUpgradeEvent) o);
        } else if (o instanceof RaidCancellationEvent) {
            return onRaidCancellationEvent((RaidCancellationEvent) o);
        } else if (o instanceof RaidEvent) {
            return onRaidEvent((RaidEvent) o);
        } else if (o instanceof RewardGiftEvent) {
            return onRewardGiftEvent((RewardGiftEvent) o);
        } else if (o instanceof RitualEvent) {
            return onRitualEvent((RitualEvent) o);
        } else if (o instanceof SubscriptionEvent) {
            return onSubscriptionEvent((SubscriptionEvent) o);
        } else if (o instanceof UserBanEvent) {
            return onUserBanEvent((UserBanEvent) o);
        } else if (o instanceof UserStateEvent) {
            return onUserStateEvent((UserStateEvent) o);
        } else if (o instanceof UserTimeoutEvent) {
            return onUserTimeoutEvent((UserTimeoutEvent) o);
        } else if (o instanceof CommandEvent) {
            return onCommandEvent((CommandEvent) o);
        } else if (o instanceof EmoteOnlyEvent) {
            return onEmoteOnlyEvent((EmoteOnlyEvent) o);
        } else if (o instanceof FollowersOnlyEvent) {
            return onFollowersOnlyEvent((FollowersOnlyEvent) o);
        } else if (o instanceof Robot9000Event) {
            return onRobot9000Event((Robot9000Event) o);
        } else if (o instanceof SlowModeEvent) {
            return onSlowModeEvent((SlowModeEvent) o);
        } else if (o instanceof SubscribersOnlyEvent) {
            return onSubscribersOnlyEvent((SubscribersOnlyEvent) o);
        } else {
            return onUnknownEvent(o);
        }
    }
}
