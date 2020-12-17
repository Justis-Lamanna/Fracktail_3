package com.github.lucbui.fracktail3.spring.command.factory;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FactoryUtilsTest {
    @Test
    void testCreateAndDecorateJustCreate() {
        List<Parent> creator = Collections.singletonList(new Creator("hello"));

        Optional<String> test = FactoryUtils.createAndDecorate(creator, Parent::get, Parent::decorate);
        assertTrue(test.isPresent());
        assertEquals("hello", test.get());
    }

    @Test
    void testCreateAndDecorateCreateWithDecorators() {
        List<Parent> creator = Arrays.asList(new Decorator(", world"), new Creator("hello"));

        Optional<String> test = FactoryUtils.createAndDecorate(creator, Parent::get, Parent::decorate);
        assertTrue(test.isPresent());
        assertEquals("hello, world", test.get());
    }

    @Test
    void testCreateAndDecorateNoCreate() {
        List<Parent> creator = Arrays.asList(new Decorator("hello"), new Decorator(" world"));

        Optional<String> test = FactoryUtils.createAndDecorate(creator, Parent::get, Parent::decorate);
        assertFalse(test.isPresent());
    }

    @Test
    void testDecorateWithDecorators() {
        List<Parent> decorators = Collections.singletonList(new Decorator(", world"));

        String test = FactoryUtils.decorate(decorators, Parent::decorate, "hello");
        assertEquals("hello, world", test);
    }

    @Test
    void testDecorateWithNoDecorators() {
        List<Parent> decorators = Collections.emptyList();

        String test = FactoryUtils.decorate(decorators, Parent::decorate, "hello");
        assertEquals("hello", test);
    }

    private interface Parent {
        default Optional<String> get() {
            return Optional.empty();
        }

        default String decorate(String base) {
            return base;
        }
    }

    private static class Creator implements Parent {
        private final String init;

        private Creator(String init) {
            this.init = init;
        }

        public Optional<String> get() {
            return Optional.of(init);
        }
    }

    private static class NonCreator implements Parent {
    }

    private static class Decorator implements Parent {
        private final String addendum;

        private Decorator(String addendum) {
            this.addendum = addendum;
        }

        public String decorate(String base) {
            return base + addendum;
        }
    }
}