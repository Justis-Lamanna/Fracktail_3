package com.github.lucbui.fracktail3.discord.context;

/**
 * Describe the style of response the bot should use
 */
public enum ReplyStyle {
    /**
     * Sent as a normal message
     */
    PLAIN,

    /**
     * Sent as a normal message, with the original command replied to
     */
    REPLY,

    /**
     * DM the response directly to the user
     */
    DM
}
