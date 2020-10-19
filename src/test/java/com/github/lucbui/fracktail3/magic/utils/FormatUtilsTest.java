package com.github.lucbui.fracktail3.magic.utils;

import com.github.lucbui.fracktail3.discord.util.FormatUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatUtilsTest {
    @Test
    void testBold() {
        String text = "hello, world";
        assertEquals("**hello, world**", FormatUtils.bold(text));
    }

    @Test
    void testItalics() {
        String text = "hello, world";
        assertEquals("*hello, world*", FormatUtils.italics(text));
    }

    @Test
    void testUnderline() {
        String text = "hello, world";
        assertEquals("__hello, world__", FormatUtils.underline(text));
    }

    @Test
    void testBoldItalics() {
        String text = "hello, world";
        assertEquals("***hello, world***", FormatUtils.boldItalics(text));
    }

    @Test
    void testUnderlineItalics() {
        String text = "hello, world";
        assertEquals("__*hello, world*__", FormatUtils.underlineItalics(text));
    }

    @Test
    void testUnderlineBold() {
        String text = "hello, world";
        assertEquals("__**hello, world**__", FormatUtils.underlineBold(text));
    }

    @Test
    void testUnderlineBoldItalics() {
        String text = "hello, world";
        assertEquals("__***hello, world***__", FormatUtils.underlineBoldItalics(text));
    }

    @Test
    void testStrikethrough() {
        String text = "hello, world";
        assertEquals("~~hello, world~~", FormatUtils.strikethrough(text));
    }

    @Test
    void testCodeBlock() {
        String text = "hello, world";
        assertEquals("`hello, world`", FormatUtils.codeBlock(text));
    }

    @Test
    void testMultilineCodeBlock() {
        String text = "hello, world";
        assertEquals("```hello, world```", FormatUtils.multilineCodeBlock(text));
    }

    @Test
    void testMultilineCodeBlockWithSyntaxHighlighting() {
        String text = "hello, world";
        assertEquals("```java\nhello, world```", FormatUtils.multilineCodeBlock(text, "java"));
    }

    @Test
    void testQuoteBlock() {
        String text = "hello, world";
        assertEquals("> hello, world\n", FormatUtils.quote(text));
    }

    @Test
    void testMultilineQuoteBlock() {
        String text = "hello, world";
        assertEquals(">>> hello, world", FormatUtils.multilineQuote(text));
    }

    @Test
    void testSpoiler() {
        String text = "hello, world";
        assertEquals("||hello, world||", FormatUtils.spoiler(text));
    }
}