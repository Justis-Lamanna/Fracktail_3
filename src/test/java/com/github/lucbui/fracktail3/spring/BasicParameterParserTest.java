package com.github.lucbui.fracktail3.spring;

import com.github.lucbui.fracktail3.magic.command.Command;
import com.github.lucbui.fracktail3.magic.platform.context.BasicParameterParser;
import com.github.lucbui.fracktail3.magic.platform.context.Parameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicParameterParserTest {
    private BasicParameterParser parser;

    @BeforeEach
    void beforeEach() {
        this.parser = new BasicParameterParser();
    }

    @Test
    void parseSingleArgumentParameter() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "first", "", String.class, false))
                .build();
        String message = "parameter";

        Parameters p = parser.parseParametersFromMessage(command, message);
        assertEquals(1, p.getNumberOfParameters());
        assertEquals("parameter", p.getParameter(0).orElseGet(Assertions::fail));
    }

    @Test
    void parseSingleOptionalArgumentParameter_WithoutParameter() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "first", "", String.class, true))
                .build();

        Parameters p = parser.parseParametersFromMessage(command, "");
        assertEquals(1, p.getNumberOfParameters());
        assertFalse(p.getParameter(0).isPresent());
    }

    @Test
    void parseSingleOptionalArgumentParameter_WithParameter() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "first", "", String.class, true))
                .build();

        Parameters p = parser.parseParametersFromMessage(command, "parameter");
        assertEquals(1, p.getNumberOfParameters());
        assertTrue(p.getParameter(0).isPresent());
    }

    @Test
    void passingToFewArgumentsFails() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "first", "", String.class, false))
                .build();
        String message = "";

        assertThrows(IllegalArgumentException.class, () -> {
            parser.parseParametersFromMessage(command, message);
        });
    }

    @Test
    void parseMultiArgument() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "first", "", String.class, false))
                .withParameter(new Command.Parameter(1, "second", "", String.class, false))
                .build();

        Parameters p = parser.parseParametersFromMessage(command, "one two");
        assertEquals(2, p.getNumberOfParameters());
        assertEquals("one", p.getParameter(0).orElseGet(Assertions::fail));
        assertEquals("two", p.getParameter(1).orElseGet(Assertions::fail));
    }

    @Test
    void parseMultiArgument_OneOptional_TwoPassed() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "first", "", String.class, false))
                .withParameter(new Command.Parameter(1, "second", "", String.class, true))
                .build();

        Parameters p = parser.parseParametersFromMessage(command, "one two");
        assertEquals(2, p.getNumberOfParameters());
        assertEquals("one", p.getParameter(0).orElseGet(Assertions::fail));
        assertEquals("two", p.getParameter(1).orElseGet(Assertions::fail));
    }

    @Test
    void parseMultiArgument_OneOptional_OnePassed() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "first", "", String.class, false))
                .withParameter(new Command.Parameter(1, "second", "", String.class, true))
                .build();

        Parameters p = parser.parseParametersFromMessage(command, "one");
        assertEquals(2, p.getNumberOfParameters());
        assertEquals("one", p.getParameter(0).orElseGet(Assertions::fail));
        assertFalse(p.getParameter(1).isPresent());
    }

    @Test
    void parseMultiArgument_OneOptional_OnePassed_Reversed() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "first", "", String.class, true))
                .withParameter(new Command.Parameter(1, "second", "", String.class, false))
                .build();

        Parameters p = parser.parseParametersFromMessage(command, "one");
        assertEquals(2, p.getNumberOfParameters());
        assertEquals("one", p.getParameter(1).orElseGet(Assertions::fail));
        assertFalse(p.getParameter(0).isPresent());
    }

    @Test
    void parseComplex() {
        Command command = new Command.Builder("test")
                .withParameter(new Command.Parameter(0, "a", "", String.class, false))
                .withParameter(new Command.Parameter(1, "b", "", String.class, true))
                .withParameter(new Command.Parameter(2, "c", "", String.class, false))
                .withParameter(new Command.Parameter(3, "d", "", String.class, true))
                .withParameter(new Command.Parameter(4, "e", "", String.class, false))
                .build();

        Parameters p = parser.parseParametersFromMessage(command, "a b c d");
        assertEquals(5, p.getNumberOfParameters());
        assertEquals("a", p.getParameter(0).orElseGet(Assertions::fail));
        assertEquals("b", p.getParameter(1).orElseGet(Assertions::fail));
        assertEquals("c", p.getParameter(2).orElseGet(Assertions::fail));
        assertFalse(p.getParameter(3).isPresent());
        assertEquals("d", p.getParameter(4).orElseGet(Assertions::fail));
    }
}