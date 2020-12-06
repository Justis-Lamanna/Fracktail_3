package com.github.lucbui.fracktail3.spring.annotation.strategy;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.spring.command.handler.ReturnConverters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AutoReturnStrategyTest {
    @Mock
    private ReturnConverters returnConverters;

    @InjectMocks
    private AutoReturnStrategy autoReturnStrategy;

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
    void shouldCallConvertersWithMethodType() {
        when(returnConverters.getHandlerForType(any()))
                .thenReturn(Optional.empty());

        autoReturnStrategy.create(this, TestUtils.getMethod(getClass(), "method"));

        verify(returnConverters).getHandlerForType(eq(String.class));
    }

    @Test
    void shouldCallConvertersWithFieldType() {
        when(returnConverters.getHandlerForType(any()))
                .thenReturn(Optional.empty());

        autoReturnStrategy.create(this, TestUtils.getField(getClass(), "field"));

        verify(returnConverters).getHandlerForType(eq(String.class));
    }

    // --- Test Method ---

    public String field = "";

    public String method() { return ""; }
}