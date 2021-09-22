package com.github.milomarten.fracktail3.magic.platform.formatting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SemanticSupportMessageTest {
    @Test
    public void testCreateWithObjectArray() {
        SemanticMessage message = SemanticMessage.create(
                "Hello, my name is ", StdIntent.CITE, "Fracktail", StdIntent.NONE, ". It's nice to meet you!");

        assertEquals("Hello, my name is Fracktail. It's nice to meet you!", message.toString());
    }

    @Test
    public void testCreateWithObjectArrayWithSemantic() {
        SemanticMessage message = SemanticMessage.create(
                "Hello, my name is ", StdIntent.CITE, "Fracktail", StdIntent.NONE, ". It's nice to meet you!");

        SemanticSupport semantic = intent -> {
            if(intent == StdIntent.CITE) {
                return Formatting.wrapped("~~");
            } else {
                return Formatting.NONE;
            }
        };

        assertEquals("Hello, my name is ~~Fracktail~~. It's nice to meet you!", message.toString(semantic));
    }

    @Test
    public void testCreateWithObjectArrayWithSemanticWithTransformer() {
        SemanticMessage message = SemanticMessage.create(
                "Hello, my name is ", StdIntent.CITE, "Fracktail", StdIntent.NONE, ". It's nice to meet you!");

        SemanticSupport semantic = intent -> {
            if(intent == StdIntent.CITE) {
                return Formatting.transforming(String::toUpperCase);
            } else {
                return Formatting.NONE;
            }
        };

        assertEquals("Hello, my name is FRACKTAIL. It's nice to meet you!", message.toString(semantic));
    }
}