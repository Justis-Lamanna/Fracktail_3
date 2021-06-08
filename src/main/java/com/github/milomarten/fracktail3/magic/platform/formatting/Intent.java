package com.github.milomarten.fracktail3.magic.platform.formatting;

/**
 * Describes the intent of a token.
 * This list roughly corresponds to the semantic HTML introduced in HTML5. This allows for messages to have its semantic
 * meaning encoded in it, rather than using platform-dependent structures such as Markdown.
 */
public enum Intent {
    /**
     * This token is a snippet of code
     */
    CODE,
    /**
     * This token should be emphasized (typically, italicized)
     */
    EMPHASIS,
    /**
     * This token should have a strong importance (typically, bold)
     */
    STRONG,
    /**
     * This token represents a quote of some sort
     */
    QUOTE,
    /**
     * This token represents a spoiler of some sort
     * Text should be obfuscated such that readers who want to avoid being spoiled cannot read the message, while
     * readers that do want to be spoiled can read the message.
     */
    SPOILER,
    /**
     * This token represents a roleplay action
     * While not exactly formal, this comes up reasonably often in chatrooms. Describes an action the bot is taking,
     * rather than words the bot is saying.
     */
    ROLEPLAY,
    /**
     * This token represents text that is retconned (typically, strikethrough)
     */
    RETCON,
    /**
     * This token represents a name of a book or movie (typically, italicized)
     */
    CITE,
    /**
     * Indicates that no specific intent is provided
     */
    NONE
}
