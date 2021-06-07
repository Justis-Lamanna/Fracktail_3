package com.github.lucbui.fracktail3.magic.platform.formatting;

import org.junit.jupiter.api.Test;

class SemanticMessageTest {
    @Test
    public void testCreateWithObjectArray() {
        SemanticMessage message = SemanticMessage.create(
                "Hello, my name is ", Intent.CITE, "Fracktail", Intent.NONE, ". It's nice to meet you!");

        assertEquals("Hello, my name is Fracktail. It's nice to meet you!", message.format());
    }

    @Test
    public void testCreateWithObjectArrayWithSemantic() {
        SemanticMessage message = SemanticMessage.create(
                "Hello, my name is ", Intent.CITE, "Fracktail", Intent.NONE, ". It's nice to meet you!");

        Semantic semantic = intent -> {
            if(intent == Intent.CITE) {
                return Formatting.wrapped("~~");
            } else {
                return Formatting.NONE;
            }
        };

        assertEquals("Hello, my name is ~~Fracktail~~. It's nice to meet you!", message.format(semantic));
    }
}