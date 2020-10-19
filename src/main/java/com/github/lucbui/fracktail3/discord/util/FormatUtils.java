package com.github.lucbui.fracktail3.discord.util;

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
}
