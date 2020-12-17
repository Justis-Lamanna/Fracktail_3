package com.github.lucbui.fracktail3.spring.service;

import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.command.handler.StdReturnConverterFunctions;
import com.github.lucbui.fracktail3.spring.command.model.BotResponse;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ReturnConvertersTest {
    private final ReturnConverters returnConverters = new ReturnConverters();

    @Test
    void shouldReturnVoidsWhenVoidType() {
        ReturnComponent.ReturnConverterFunction rcf = returnConverters.getHandlerForType(Void.class)
                .orElseGet(Assertions::fail);

        assertEquals(StdReturnConverterFunctions.Voids.class, rcf.getClass());
    }

    @Test
    void shouldReturnVoidsWhenVoidPrimitiveType() {
        ReturnComponent.ReturnConverterFunction rcf = returnConverters.getHandlerForType(Void.TYPE)
                .orElseGet(Assertions::fail);

        assertEquals(StdReturnConverterFunctions.Voids.class, rcf.getClass());
    }

    @Test
    void shouldReturnMonosWhenMonoType() {
        ReturnComponent.ReturnConverterFunction rcf = returnConverters.getHandlerForType(Mono.class)
                .orElseGet(Assertions::fail);

        assertEquals(StdReturnConverterFunctions.Monos.class, rcf.getClass());
    }

    @Test
    void shouldReturnFluxsWhenFluxType() {
        ReturnComponent.ReturnConverterFunction rcf = returnConverters.getHandlerForType(Flux.class)
                .orElseGet(Assertions::fail);

        assertEquals(StdReturnConverterFunctions.Fluxs.class, rcf.getClass());
    }

    @Test
    void shouldReturnStringsWhenStringType() {
        ReturnComponent.ReturnConverterFunction rcf = returnConverters.getHandlerForType(String.class)
                .orElseGet(Assertions::fail);

        assertEquals(StdReturnConverterFunctions.Strings.class, rcf.getClass());
    }

    @Test
    void shouldReturnFStringsWhenFormattedStringType() {
        ReturnComponent.ReturnConverterFunction rcf = returnConverters.getHandlerForType(FormattedString.class)
                .orElseGet(Assertions::fail);

        assertEquals(StdReturnConverterFunctions.FStrings.class, rcf.getClass());
    }

    @Test
    void shouldReturnBotResponsesWhenBotResponseType() {
        ReturnComponent.ReturnConverterFunction rcf = returnConverters.getHandlerForType(BotResponse.class)
                .orElseGet(Assertions::fail);

        assertEquals(StdReturnConverterFunctions.BotResponses.class, rcf.getClass());
    }

    @Test
    void shouldReturnEmptyWhenUnknownClass() {
        assertFalse(returnConverters.getHandlerForType(Object.class).isPresent());
    }
}