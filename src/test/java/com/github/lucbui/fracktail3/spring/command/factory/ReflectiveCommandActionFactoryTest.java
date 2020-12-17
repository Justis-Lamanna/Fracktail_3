package com.github.lucbui.fracktail3.spring.command.factory;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.spring.command.model.ExceptionComponent;
import com.github.lucbui.fracktail3.spring.command.model.MethodComponent;
import com.github.lucbui.fracktail3.spring.command.model.ReturnComponent;
import com.github.lucbui.fracktail3.spring.schedule.model.ExceptionScheduledComponent;
import com.github.lucbui.fracktail3.spring.schedule.model.ReturnScheduledComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReflectiveCommandActionFactoryTest {
    @Mock
    private MethodComponentFactory methodComponentFactory;

    @Mock
    private ParameterComponentFactory parameterComponentFactory;

    @Mock
    private ReturnComponentFactory returnComponentFactory;

    @Mock
    private ExceptionComponentFactory exceptionComponentFactory;

    @InjectMocks
    private ReflectiveCommandActionFactory factory;

    private AutoCloseable mocks;

    @BeforeEach
    private void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    private void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testCreateActionFromMethod() {
        when(methodComponentFactory.compileMethod(any(), any())).thenReturn(new MethodComponent());
        when(parameterComponentFactory.compileParameters(any(), any())).thenReturn(Collections.emptyList());
        when(returnComponentFactory.compileReturn(any(), any(Method.class))).thenReturn(new ReturnComponent((ctx, obj) -> Mono.empty()));
        when(exceptionComponentFactory.compileException(any(), any(Method.class))).thenReturn(new ExceptionComponent());

        Method method = TestUtils.getMethod(getClass(), "test");
        factory.createAction(this, method);

        verify(methodComponentFactory).compileMethod(eq(this), eq(method));
        verify(parameterComponentFactory).compileParameters(eq(this), eq(method));
        verify(returnComponentFactory).compileReturn(eq(this), eq(method));
        verify(exceptionComponentFactory).compileException(eq(this), eq(method));
    }

    @Test
    void testCreateActionFromField() {
        when(methodComponentFactory.compileMethod(any(), any())).thenReturn(new MethodComponent());
        when(returnComponentFactory.compileReturn(any(), any(Field.class))).thenReturn(new ReturnComponent((ctx, obj) -> Mono.empty()));
        when(exceptionComponentFactory.compileException(any(), any(Field.class))).thenReturn(new ExceptionComponent());

        Field field = TestUtils.getField(getClass(), "test");
        factory.createAction(this, field);

        verify(methodComponentFactory).compileField(eq(this), eq(field));
        verify(returnComponentFactory).compileReturn(eq(this), eq(field));
        verify(exceptionComponentFactory).compileException(eq(this), eq(field));
    }

    @Test
    void testCreateScheduleActionFromMethod() {
        when(parameterComponentFactory.compileScheduleParameters(any(), any())).thenReturn(Collections.emptyList());
        when(returnComponentFactory.compileScheduledReturn(any(), any(Method.class))).thenReturn(new ReturnScheduledComponent((ctx, obj) -> Mono.empty()));
        when(exceptionComponentFactory.compileScheduleException(any(), any(Method.class))).thenReturn(new ExceptionScheduledComponent());

        Method method = TestUtils.getMethod(getClass(), "test");
        factory.createScheduledAction(this, method);

        verify(parameterComponentFactory).compileScheduleParameters(eq(this), eq(method));
        verify(returnComponentFactory).compileScheduledReturn(eq(this), eq(method));
        verify(exceptionComponentFactory).compileScheduleException(eq(this), eq(method));
    }

    @Test
    void testCreateScheduleActionFromField() {
        when(returnComponentFactory.compileScheduledReturn(any(), any(Method.class))).thenReturn(new ReturnScheduledComponent((ctx, obj) -> Mono.empty()));
        when(exceptionComponentFactory.compileScheduleException(any(), any(Method.class))).thenReturn(new ExceptionScheduledComponent());

        Field field = TestUtils.getField(getClass(), "test");
        factory.createScheduledAction(this, field);

        verify(returnComponentFactory).compileScheduledReturn(eq(this), eq(field));
        verify(exceptionComponentFactory).compileScheduleException(eq(this), eq(field));
    }

    public void test() {}

    public String test = "";
}