//package com.github.lucbui.fracktail3.magic.handlers.action;
//
//import com.github.lucbui.fracktail3.magic.Bot;
//import com.github.lucbui.fracktail3.magic.platform.CommandContext;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import reactor.core.publisher.Mono;
//
//import java.util.Collections;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class RespondActionTest {
//
//    @Mock
//    private Bot bot;
//
//    @Mock
//    private CommandContext commandContext;
//
//    private static final String MSG = "Hello, World";
//
//    private RespondAction action;
//    private AutoCloseable mocks;
//
//    @BeforeEach
//    void setUp() {
//        mocks = MockitoAnnotations.openMocks(this);
//        action = new RespondAction(MSG);
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        mocks.close();
//    }
//
//    @Test
//    void testDoAction() {
//        when(commandContext.respond(anyString())).thenReturn(Mono.just(true));
//        when(commandContext.getExtendedVariableMap()).thenReturn(Mono.just(Collections.emptyMap()));
//        action.doAction(bot, commandContext).block();
//        verify(commandContext).respond("Hello, World");
//    }
//}