package com.github.lucbui.fracktail3.spring.configurer;

import com.github.lucbui.fracktail3.TestUtils;
import com.github.lucbui.fracktail3.magic.command.CommandList;
import com.github.lucbui.fracktail3.magic.command.action.CommandAction;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import com.github.lucbui.fracktail3.spring.command.annotation.Command;
import com.github.lucbui.fracktail3.spring.command.factory.ReflectiveCommandActionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommandConfigurerTest {
    @Mock
    private ReflectiveCommandActionFactory factory;

    @Mock
    private CommandList commandList;

    @InjectMocks
    private CommandConfigurer configurer;

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
        Method method = TestUtils.getMethod(getClass(), "testWithCommand");
        when(factory.createAction(any(), any(Method.class))).thenReturn(context -> Mono.empty());

        configurer.handleMethod(this, method);

        verify(commandList).add(ArgumentMatchers.argThat(command -> command.getId().equals("testWithCommand")));
    }

    @Test
    void testHandleField() {
        Field field = TestUtils.getField(getClass(), "testWithCommand");
        when(factory.createAction(any(), any(Method.class))).thenReturn(context -> Mono.empty());

        configurer.handleField(this, field);

        verify(commandList).add(ArgumentMatchers.argThat(command -> command.getId().equals("testWithCommand")));
    }

    @Test
    void testConfigureCommandAction() {
        configurer.configure(new TestCommandAction(), "test");

        verify(commandList).add(ArgumentMatchers.argThat(command -> command.getId().equals("test")));
    }

    @Test
    void testConfigureCommand() {
        com.github.lucbui.fracktail3.magic.command.Command c = new com.github.lucbui.fracktail3.magic.command.Command.Builder("test")
                .withAction(new TestCommandAction())
                .build();

        configurer.configure(c, "test");

        verify(commandList).add(ArgumentMatchers.argThat(command -> command.getId().equals("test")));
    }

    // Test
    @Command
    public void testWithCommand() {}

    @Command
    public String testWithCommand = "";

    public static class TestCommandAction implements CommandAction {
        @Override
        public Mono<Void> doAction(CommandUseContext<?> context) {
            return Mono.empty();
        }
    }
}