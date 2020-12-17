package com.github.lucbui.fracktail3.spring.configurer;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.magic.schedule.ScheduledEvents;
import com.github.lucbui.fracktail3.spring.command.factory.ReflectiveCommandActionFactory;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Cron;
import com.github.lucbui.fracktail3.spring.schedule.annotation.Schedule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduleConfigurerTest {
    @Mock
    private ReflectiveCommandActionFactory factory;

    @Mock
    private ScheduledEvents scheduledEvents;

    @InjectMocks
    private ScheduleConfigurer configurer;

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
    void testHandleMethod() {
        Method method = TestUtils.getMethod(getClass(), "testWithCron");
        when(factory.createScheduledAction(any(), any(Method.class))).thenReturn(context -> Mono.empty());
        configurer.handleMethod(this, method);

        verify(scheduledEvents).add(any());
    }

    @Test
    void testHandleField() {
        Field field = TestUtils.getField(getClass(), "testWithCron");
        when(factory.createScheduledAction(any(), any(Field.class))).thenReturn(context -> Mono.empty());
        configurer.handleField(this, field);

        verify(scheduledEvents).add(any());
    }

    // Test
    @Schedule
    @Cron(hour = "*")
    public void testWithCron() {}

    @Schedule
    @Cron(hour = "*")
    public String testWithCron = "";
}