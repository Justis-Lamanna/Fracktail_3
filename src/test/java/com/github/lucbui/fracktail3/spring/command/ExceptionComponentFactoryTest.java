package com.github.lucbui.fracktail3.spring.command;

import com.github.lucbui.fracktail3.spring.annotation.FString;
import com.github.lucbui.fracktail3.spring.annotation.OnExceptionRespond;
import com.github.lucbui.fracktail3.spring.annotation.RespondType;
import com.github.lucbui.fracktail3.spring.command.handler.ExceptionRespondHandler;
import com.github.lucbui.fracktail3.spring.plugin.Plugins;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.core.convert.ConversionService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExceptionComponentFactoryTest {
    @Mock
    private ConversionService conversionService;

    @Spy
    private Plugins plugins = new Plugins();

    @InjectMocks
    private ExceptionComponentFactory factory;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    private ExceptionRespondHandler getHandler(ExceptionComponent component, Class<? extends Throwable> clazz) {
        ExceptionRespondHandler handler = component.getBestHandlerFor(clazz)
                .filter(eh -> eh instanceof ExceptionRespondHandler)
                .map(eh -> (ExceptionRespondHandler)eh)
                .orElseGet(Assertions::fail);
        assertNotNull(component.candidates.get(clazz), "candidates should have handler for class " + clazz);

        return handler;
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesAllExceptions() {
        Method method = getMethod(getClass(), "exceptionRespondCatchAll");
        ExceptionComponent component = factory.compileException(this, method);
        ExceptionRespondHandler handler = getHandler(component, Throwable.class);
        assertEquals(RespondType.INLINE, handler.getType());
        assertEquals("Hello, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesAllExceptions_ClassLevel() {
        Method method = getMethod(CatchAll.class, "exceptionRespondCatchAll");
        ExceptionComponent component = factory.compileException(new CatchAll(), method);
        ExceptionRespondHandler handler = getHandler(component, Throwable.class);
        assertEquals(RespondType.INLINE, handler.getType());
        assertEquals("Hello, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesAllExceptions_Overwrite() {
        Method method = getMethod(CatchAll.class, "exceptionRespondOverwrite");
        ExceptionComponent component = factory.compileException(new CatchAll(), method);
        ExceptionRespondHandler handler = getHandler(component, Throwable.class);
        assertEquals(RespondType.INLINE, handler.getType());
        assertEquals("Goodbye, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesAllExceptions_Add() {
        Method method = getMethod(CatchAll.class, "exceptionRespondAdd");
        ExceptionComponent component = factory.compileException(new CatchAll(), method);
        ExceptionRespondHandler defaultHandler = getHandler(component, Throwable.class);
        ExceptionRespondHandler specificHandler = getHandler(component, NoSuchElementException.class);

        assertEquals(RespondType.INLINE, defaultHandler.getType());
        assertEquals("Hello, world", defaultHandler.getfString().getRaw());
        assertEquals(RespondType.INLINE, specificHandler.getType());
        assertEquals("Goodbye, world", specificHandler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesSpecificExceptions() {
        Method method = getMethod(getClass(), "exceptionRespondSpecificException");
        ExceptionComponent component = factory.compileException(this, method);
        ExceptionRespondHandler handler = getHandler(component, NoSuchElementException.class);
        assertEquals(RespondType.INLINE, handler.getType());
        assertEquals("Hello, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWhichHandlesSpecificExceptions_ClassLevel() {
        Method method = getMethod(CatchSpecific.class, "exceptionRespondSpecificException");
        ExceptionComponent component = factory.compileException(new CatchSpecific(), method);
        ExceptionRespondHandler handler = getHandler(component, NoSuchElementException.class);
        assertEquals(RespondType.INLINE, handler.getType());
        assertEquals("Hello, world", handler.getfString().getRaw());
    }

    @Test
    void shouldCreateExceptionComponentWithTwoHandlers() {
        Method method = getMethod(getClass(), "exceptionRespondSpecificExceptionTwoAnnotations");
        ExceptionComponent component = factory.compileException(this, method);
        assertEquals(2, component.candidates.size());
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