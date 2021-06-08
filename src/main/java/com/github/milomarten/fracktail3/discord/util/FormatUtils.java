package com.github.milomarten.fracktail3.discord.util;

import discord4j.common.util.Snowflake;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for formatting text
 */
public class FormatUtils {
    private static final String MARKDOWN_BOLD = "**";
    private static final String MARKDOWN_ITALICS = "*";
    private static final String MARKDOWN_UNDERLINE = "__";
    private static final String MARKDOWN_STRIKETHROUGH = "~~";
    private static final String MARKDOWN_CODE_BLOCK = "`";
    private static final String MARKDOWN_MULTILINE_CODE_BLOCK = "```";
    private static final String MARKDOWN_QUOTE_BLOCK = "> ";
    private static final String MARKDOWN_MULTILINE_QUOTE_BLOCK = ">>> ";
    private static final String MARKDOWN_SPOILER = "||";

    private static final String AT_USER = "<@";
    private static final String AT_ROLE = "<@&";
    private static final String END_AT = ">";

    private static final String EMOTE_PREFIX = "<:";
    private static final String ANIMATED_EMOTE_PREFIX = "<a:";
    private static final String EMOTE_SEPARATOR = ":";
    private static final String EMOTE_END = ">";

    private FormatUtils() {
    }

    /**
     * Markdown a text as bold
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String bold(String text) {
        return MARKDOWN_BOLD + text + MARKDOWN_BOLD;
    }

    /**
     * Markdown a text as italics
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String italics(String text) {
        return MARKDOWN_ITALICS + text + MARKDOWN_ITALICS;
    }

    /**
     * Markdown a text as underlined
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String underline(String text) {
        return MARKDOWN_UNDERLINE + text + MARKDOWN_UNDERLINE;
    }

    /**
     * Markdown a text as bold and italics
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String boldItalics(String text) {
        return bold(italics(text));
    }

    /**
     * Markdown a text as underline and italics
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String underlineItalics(String text) {
        return underline(italics(text));
    }

    /**
     * Markdown a text as underline and bold
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String underlineBold(String text) {
        return underline(bold(text));
    }

    /**
     * Markdown a text as bold, underline, and italics
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String underlineBoldItalics(String text) {
        return underline(bold(italics(text)));
    }

    /**
     * Markdown a text as struck-through
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String strikethrough(String text) {
        return MARKDOWN_STRIKETHROUGH + text + MARKDOWN_STRIKETHROUGH;
    }

    /**
     * Markdown a text as a single-line code block
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String codeBlock(String text) {
        return MARKDOWN_CODE_BLOCK + text + MARKDOWN_CODE_BLOCK;
    }

    /**
     * Markdown a text as a multiline code block
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String multilineCodeBlock(String text) {
        return MARKDOWN_MULTILINE_CODE_BLOCK + text + MARKDOWN_MULTILINE_CODE_BLOCK;
    }

    /**
     * Markdown a text as a multiline code block
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String multilineCodeBlock(String text, String language) {
        return MARKDOWN_MULTILINE_CODE_BLOCK + language + "\n" + text + MARKDOWN_MULTILINE_CODE_BLOCK;
    }

    /**
     * Markdown a text as a quote block.
     * A newline is automatically applied to the end
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String quote(String text) {
        return MARKDOWN_QUOTE_BLOCK + text + "\n";
    }

    /**
     * Markdown a text as a multiline quote block.
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String multilineQuote(String text) {
        return MARKDOWN_MULTILINE_QUOTE_BLOCK + text;
    }

    /**
     * Markdown a text as a spoiler.
     * @param text The text to markdown
     * @return The marked-down text
     */
    public static String spoiler(String text) {
        return MARKDOWN_SPOILER + text + MARKDOWN_SPOILER;
    }

    /**
     * Ping a user
     * @param user The ID of the user
     * @return A string representing a ping
     */
    public static String mentionUser(Snowflake user) {
        return AT_USER + user.asString() + END_AT;
    }

    /**
     * From a user Ping, extract a user
     * @param mention The string mention
     * @return The parsed Snowflake
     */
    public static Snowflake fromUserMention(String mention) {
        String trimmed = StringUtils.removeEnd(StringUtils.removeStart(mention, AT_USER), END_AT);
        trimmed = StringUtils.removeStart(trimmed, "!");
        return Snowflake.of(trimmed);
    }

    /**
     * Ping a role
     * Note that this doesn't verify if the role can actually be pinged
     * @param role The ID of the role
     * @return A string representing a ping
     */
    public static String mentionRole(Snowflake role) {
        return AT_ROLE + role.asString() + END_AT;
    }

    /**
     * Display an non-animated emote
     * @param name The emoji name
     * @param id The emoji ID
     * @return A string representing an emote
     */
    public static String emoji(String name, Snowflake id) {
        return EMOTE_PREFIX + name + EMOTE_SEPARATOR + id.asString() + EMOTE_END;
    }

    /**
     * Display an animated emote
     * @param name The emoji name
     * @param id The emoji ID
     * @return A string representing an emote
     */
    public static String animatedEmoji(String name, Snowflake id) {
        return ANIMATED_EMOTE_PREFIX + name + EMOTE_SEPARATOR + id.asString() + EMOTE_END;
    }
}
