package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.spring.annotation.FString;
import com.github.lucbui.fracktail3.spring.annotation.OnExceptionRespond;
import com.github.lucbui.fracktail3.spring.command.ExceptionComponent;
import com.github.lucbui.fracktail3.spring.command.handler.ExceptionRespondHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OnExceptionRespondStrategyTest {
    private final OnExceptionRespondStrategy strategy = new OnExceptionRespondStrategy();

    private ExceptionRespondHandler getHandler(ExceptionComponent component, Class<? extends Throwable> clazz) {
        ExceptionRespondHandler handler = component.getBestHandlerFor(clazz)
                .filter(eh -> eh instanceof ExceptionRespondHandler)
                .map(eh -> (ExceptionRespondHandler)eh)
                .orElseGet(Assertions::fail);
        assertNotNull(component.getCandidates().get(clazz), "candidates should have handler for class " + clazz);

        return handler;
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesAllExceptions() {
        Method method = getMethod(getClass(), "exceptionRespondCatchAll");
        ExceptionComponent component = strategy.decorate(this, method, new ExceptionComponent());
        ExceptionRespondHandler handler = getHandler(component, Throwable.class);
        assertEquals("Hello, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesAllExceptions_ClassLevel() {
        Method method = getMethod(OnExceptionRespondStrategyTest.CatchAll.class, "exceptionRespondCatchAll");
        ExceptionComponent component = strategy.decorate(new OnExceptionRespondStrategyTest.CatchAll(), method, new ExceptionComponent());
        ExceptionRespondHandler handler = getHandler(component, Throwable.class);
        assertEquals("Hello, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesAllExceptions_Overwrite() {
        Method method = getMethod(OnExceptionRespondStrategyTest.CatchAll.class, "exceptionRespondOverwrite");
        ExceptionComponent component = strategy.decorate(new OnExceptionRespondStrategyTest.CatchAll(), method, new ExceptionComponent());
        ExceptionRespondHandler handler = getHandler(component, Throwable.class);
        assertEquals("Goodbye, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesAllExceptions_Add() {
        Method method = getMethod(OnExceptionRespondStrategyTest.CatchAll.class, "exceptionRespondAdd");
        ExceptionComponent component = strategy.decorate(new OnExceptionRespondStrategyTest.CatchAll(), method, new ExceptionComponent());
        ExceptionRespondHandler defaultHandler = getHandler(component, Throwable.class);
        ExceptionRespondHandler specificHandler = getHandler(component, NoSuchElementException.class);

        assertEquals("Hello, world", defaultHandler.getfString().getRaw());
        assertEquals("Goodbye, world", specificHandler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesSpecificExceptions() {
        Method method = getMethod(getClass(), "exceptionRespondSpecificException");
        ExceptionComponent component = strategy.decorate(this, method, new ExceptionComponent());
        ExceptionRespondHandler handler = getHandler(component, NoSuchElementException.class);
        assertEquals("Hello, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesSpecificExceptions_ClassLevel() {
        Method method = getMethod(OnExceptionRespondStrategyTest.CatchSpecific.class, "exceptionRespondSpecificException");
        ExceptionComponent component = strategy.decorate(new OnExceptionRespondStrategyTest.CatchSpecific(), method, new ExceptionComponent());
        ExceptionRespondHandler handler = getHandler(component, NoSuchElementException.class);
        assertEquals("Hello, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWithTwoHandlers() {
        Method method = getMethod(getClass(), "exceptionRespondSpecificExceptionTwoAnnotations");
        ExceptionComponent component = strategy.decorate(this, method, new ExceptionComponent());
        assertEquals(2, component.getCandidates().size());
    }

    //------------------------------Test Methods------------------------------------------------------------------------
    private Method getMethod(Class<?> clazz, String name) {
        return Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(name))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @OnExceptionRespond(@FString("Hello, world"))
    public void exceptionRespondCatchAll() {}

    @OnExceptionRespond(exception = NoSuchElementException.class, value = @FString("Hello, world"))
    public void exceptionRespondSpecificException() {}

    @OnExceptionRespond(exception = NoSuchElementException.class, value = @FString("Hello, world"))
    @OnExceptionRespond(exception = NullPointerException.class, value = @FString("Null, world"))
    public void exceptionRespondSpecificExceptionTwoAnnotations() {}

    @OnExceptionRespond(@FString("Hello, world"))
    public static class CatchAll{
        public void exceptionRespondCatchAll() {}

        @OnExceptionRespond(@FString("Goodbye, world"))
        public void exceptionRespondOverwrite() {}

        @OnExceptionRespond(exception = NoSuchElementException.class, value = @FString("Goodbye, world"))
        public void exceptionRespondAdd() {}
    }

    @OnExceptionRespond(exception = NoSuchElementException.class, value = @FString("Hello, world"))
    public static class CatchSpecific{
        public void exceptionRespondSpecificException() {}
    }
}