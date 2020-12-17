package com.github.lucbui.fracktail3.spring.service;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StrategyExtractorTest {
    @Mock
    private ApplicationContext context;

    @InjectMocks
    private StrategyExtractor extractor;

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
    void testGetStrategiesReturnsBean() {
        Object probe = new Object();
        when(context.getBean(Object.class)).thenReturn(probe);

        Method method = TestUtils.getMethod(getClass(), "test");
        List<Object> objs = extractor.getStrategies(method, TestMetaAnnotation.class, TestMetaAnnotation::value);

        assertEquals(1, objs.size());
        assertSame(probe, objs.get(0));
    }

    @Test
    void testGetStrategiesReturnsCreatedInstance() {
        when(context.getBean(Object.class)).thenThrow(new NoSuchBeanDefinitionException(""));

        Method method = TestUtils.getMethod(getClass(), "test");
        List<Object> objs = extractor.getStrategies(method, TestMetaAnnotation.class, TestMetaAnnotation::value);

        assertEquals(1, objs.size());
    }

    @Test
    void testGetStrategiesMultipleClassBeansThrowException() {
        assertThrows(BotConfigurationException.class, () -> {
            when(context.getBean(Object.class)).thenThrow(new NoUniqueBeanDefinitionException(Object.class, "one", "two", "three"));

            Method method = TestUtils.getMethod(getClass(), "test");
            List<Object> objs = extractor.getStrategies(method, TestMetaAnnotation.class, TestMetaAnnotation::value);
        });
    }

    @Test
    void testGetStrategiesBeanErrorThrowException() {
        assertThrows(BotConfigurationException.class, () -> {
            when(context.getBean(Object.class)).thenThrow(new BeansException("") {
                @Override
                public String getMessage() {
                    return super.getMessage();
                }
            });

            Method method = TestUtils.getMethod(getClass(), "test");
            List<Object> objs = extractor.getStrategies(method, TestMetaAnnotation.class, TestMetaAnnotation::value);
        });
    }

    @Test
    void testGetStrategiesInstantiationException() {
        assertThrows(BotConfigurationException.class, () -> {
            when(context.getBean(Integer.class)).thenThrow(new NoSuchBeanDefinitionException(""));

            Method method = TestUtils.getMethod(getClass(), "test");
            List<Object> objs = extractor.getStrategies(method, TestBadMetaAnnotation.class, TestBadMetaAnnotation::value);
        });
    }

    //-----Test Methods-----

    @TestAnnotation
    public void test() {}

    @TestMetaAnnotation
    @TestBadMetaAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestAnnotation { }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestMetaAnnotation {
        Class<Object>[] value() default { Object.class };
    }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestBadMetaAnnotation {
        Class<Integer>[] value() default { Integer.class };
    }
}