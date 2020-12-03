package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import com.github.lucbui.fracktail3.spring.command.handler.ReturnHandlers;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.convert.ConversionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

class ReturnComponentFactoryTest {
    @Mock
    private ConversionService conversionService;

    @Spy
    private Plugins plugins = new Plugins();

    @InjectMocks
    private ReturnComponentFactory factory;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void voidReturningMethodShouldHandleVoid() {
        ReturnComponent component = factory.compileReturn(this, getMethod("returnVoid"));
        assertTrue(component.func instanceof ReturnHandlers.Voids);
    }

    @Test
    void monoReturningMethodShouldHandleMono() {
        ReturnComponent component = factory.compileReturn(this, getMethod("returnMono"));
        assertTrue(component.func instanceof ReturnHandlers.Monos);
    }

    @Test
    void fluxReturningMethodShouldHandleFlux() {
        ReturnComponent component = factory.compileReturn(this, getMethod("returnFlux"));
        assertTrue(component.func instanceof ReturnHandlers.Fluxs);
    }

    @Test
    void stringReturningMethodShouldHandleString() {
        ReturnComponent component = factory.compileReturn(this, getMethod("returnString"));
        assertTrue(component.func instanceof ReturnHandlers.Strings);
    }

    @Test
    void formattedStringReturningMethodShouldHandleFormattedString() {
        ReturnComponent component = factory.compileReturn(this, getMethod("returnFString"));
        assertTrue(component.func instanceof ReturnHandlers.FStrings);
    }

    @Test
    void botResponseReturningMethodShouldHandleBotResponse() {
        ReturnComponent component = factory.compileReturn(this, getMethod("returnBotResponse"));
        assertTrue(component.func instanceof ReturnHandlers.BotResponses);
    }

    @Test
    void somethingElseReturningMethodShouldCallPlugins() {
        assertThrows(BotConfigurationException.class, () -> {
            ReturnComponent component = factory.compileReturn(this, getMethod("returnObject"));
        });
        Mockito.verify(plugins).createCompiledReturn(any(), any());
    }

    @Test
    void monoReturningFieldShouldHandleMono() {
        ReturnComponent component = factory.compileReturn(this, getField("mono"));
        assertTrue(component.func instanceof ReturnHandlers.Monos);
    }

    @Test
    void fluxReturningFieldShouldHandleFlux() {
        ReturnComponent component = factory.compileReturn(this, getField("flux"));
        assertTrue(component.func instanceof ReturnHandlers.Fluxs);
    }

    @Test
    void stringReturningFieldShouldHandleString() {
        ReturnComponent component = factory.compileReturn(this, getField("string"));
        assertTrue(component.func instanceof ReturnHandlers.Strings);
    }

    @Test
    void formattedStringReturningFieldShouldHandleFormattedString() {
        ReturnComponent component = factory.compileReturn(this, getField("fString"));
        assertTrue(component.func instanceof ReturnHandlers.FStrings);
    }

    @Test
    void botResponseReturningFieldShouldHandleBotResponse() {
        ReturnComponent component = factory.compileReturn(this, getField("botResponse"));
        assertTrue(component.func instanceof ReturnHandlers.BotResponses);
    }

    @Test
    void somethingElseReturningFieldShouldCallPlugins() {
        assertThrows(BotConfigurationException.class, () -> {
            ReturnComponent component = factory.compileReturn(this, getField("object"));
        });
        Mockito.verify(plugins).createCompiledFieldReturn(any(), any());
    }

    //------------------------------Test Methods------------------------------------------------------------------------
    private Method getMethod(String name) {
        return Arrays.stream(getClass().getMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private Field getField(String name) {
        return Arrays.stream(getClass().getFields())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public Mono<Void> mono = Mono.empty();

    public Flux<Void> flux = Flux.empty();

    public String string = "hello, world";

    public FormattedString fString = FormattedString.from("hello, world");

    public BotResponse botResponse = () -> FormattedString.from("hello, world");

    public Object object = new Object();

    public void returnVoid() { }

    public Mono<Void> returnMono() {return Mono.empty(); }

    public Flux<Void> returnFlux() {return Flux.empty(); }

    public String returnString() {return "hello, world"; }

    public FormattedString returnFString() {return FormattedString.from("hello, world"); }

    public BotResponse returnBotResponse() {
        return () -> FormattedString.from("hello, world");
    }

    public Object returnObject() {
        return new Object();
    }
}