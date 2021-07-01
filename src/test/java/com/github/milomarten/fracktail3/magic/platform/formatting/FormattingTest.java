package com.github.milomarten.fracktail3.magic.platform.formatting;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormattingTest {
    @Test
    public void testWrapped() {
        Formatting f = Formatting.wrapped("~~");
        assertEquals("~~hello~~", f.format("hello"));
    }

    @Test
    public void testWrappedTwoParam() {
        Formatting f = Formatting.wrapped("!", "?");
        assertEquals("!hello?", f.format("hello"));
    }

    @Test
    public void testPrefixed() {
        Formatting f = Formatting.prefixed("~~");
        assertEquals("~~hello", f.format("hello"));
    }

    @Test
    public void testXml() {
        Formatting f = Formatting.xml("b");
        assertEquals("<b>hello</b>", f.format("hello"));
    }

    @Test
    public void testXmlWithAttrs() {
        Formatting f = Formatting.xml("a", Map.of("href", "www.google.com"));
        assertEquals("<a href=\"www.google.com\">hello</a>", f.format("hello"));
    }

    @Test
    public void testXmlWithAttrsHtmlUnfriendly() {
        Formatting f = Formatting.xml("math", Map.of("eq", "10 > 5"));
        assertEquals("<math eq=\"10 &gt; 5\">hello</math>", f.format("hello"));
    }

    @Test
    public void testReplace() {
        Formatting f = Formatting.replace(Pattern.compile("a"), "b");
        assertEquals("bdbm", f.format("adam"));
    }
}