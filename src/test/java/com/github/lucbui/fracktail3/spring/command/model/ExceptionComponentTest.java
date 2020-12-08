package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.command.handler.ExceptionRespondHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ExceptionComponentTest {
    private ExceptionRespondHandler makeHandler() {
        return new ExceptionRespondHandler(FormattedString.literal("hello, world"));
    }

    private ExceptionRespondHandler getBestHandler(ExceptionComponent component, Class<? extends Throwable> clazz) {
        return component.getBestHandlerFor(clazz)
                .filter(eh -> eh instanceof ExceptionRespondHandler)
                .map(eh -> (ExceptionRespondHandler)eh)
                .orElseGet(Assertions::fail);
    }

    @Test
    void getBestHandlerForExactClass() {
        ExceptionComponent component = new ExceptionComponent();
        component.addHandler(NullPointerException.class, makeHandler());

        ExceptionRespondHandler best = getBestHandler(component, NullPointerException.class);
        assertEquals("hello, world", best.getfString().getRaw());
    }

    @Test
    void getBestHandlerForSubclass() {
        ExceptionComponent component = new ExceptionComponent();
        component.addHandler(IOException.class, makeHandler());

        ExceptionRespondHandler best = getBestHandler(component, FileNotFoundException.class);
        assertEquals("hello, world", best.getfString().getRaw());
    }

    @Test
    void getBestHandlerNoneApplicable() {
        ExceptionComponent component = new ExceptionComponent();
        component.addHandler(NullPointerException.class, makeHandler());

        assertFalse(component.getBestHandlerFor(IOException.class).isPresent());
    }

    @Test
    void getBestHandlerNonePresent() {
        ExceptionComponent component = new ExceptionComponent();

        assertFalse(component.getBestHandlerFor(IOException.class).isPresent());
    }
}