//package com.github.lucbui.fracktail3.magic.handlers.command;
//
//import com.github.lucbui.fracktail3.magic.Bot;
//import com.github.lucbui.fracktail3.magic.BotSpec;
//import com.github.lucbui.fracktail3.magic.guards.Guard;
//import com.github.lucbui.fracktail3.magic.handlers.Command;
//import com.github.lucbui.fracktail3.magic.handlers.CommandList;
//import com.github.lucbui.fracktail3.magic.handlers.action.Action;
//import com.github.lucbui.fracktail3.magic.platform.CommandContext;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import reactor.core.publisher.Mono;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class HelpCommandTest {
//    @Mock private CommandContext context;
//
//    @Mock private Bot bot;
//    @Mock private BotSpec spec;
//    @Mock private CommandList commandList;
//
//    private Command command;
//    private AutoCloseable mocks;
//
//    private final Command c = new Command("one", "one", Guard.identity(true), Action.NOOP);
//    private final Command cCopy = new Command("one-copy", "one", Guard.identity(true), Action.NOOP);
//
//    @BeforeEach
//    void setUp() {
//        command = new Command("help", new HelpAction());
//        mocks = MockitoAnnotations.openMocks(this);
//        when(bot.getSpec()).thenReturn(spec);
//        when(spec.getCommandList()).thenReturn(commandList);
//        when(context.getCommand()).thenReturn(command);
//        when(context.getExtendedVariableMap()).thenReturn(Mono.just(Collections.emptyMap()));
//        when(context.respond(anyString())).thenReturn(Mono.just(true));
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        mocks.close();
//    }
//
//    @Test
//    void testRetrieveHelp_OneParameterValidHit() {
//        when(commandList.getCommands()).thenReturn(Collections.singletonList(c));
//        when(context.getNormalizedParameter(eq(0))).thenReturn(Optional.of("one"));
//
//        command.doAction(bot, context).block();
//
//        verify(context).respond("one.help");
//    }
//
//    @Test
//    void testRetrieveHelp_OneParameterInvalidHit() {
//        when(commandList.getCommands()).thenReturn(Collections.singletonList(c));
//        when(context.getNormalizedParameter(eq(0))).thenReturn(Optional.of("none"));
//
//        command.doAction(bot, context).block();
//
//        verify(context).respond(contains("Unable to find command"));
//    }
//
//    @Test
//    void testRetrieveHelp_MultipleMatches() {
//        when(commandList.getCommands()).thenReturn(Arrays.asList(c, cCopy));
//        when(context.getNormalizedParameter(eq(0))).thenReturn(Optional.of("one"));
//
//        command.doAction(bot, context).block();
//
//        verify(context).respond("one.help");
//    }
//
//    @Test
//    void testRetrieveHelp_NoParameter() {
//        when(commandList.getCommands()).thenReturn(Collections.singletonList(c));
//        when(context.getNormalizedParameter(eq(0))).thenReturn(Optional.empty());
//
//        command.doAction(bot, context).block();
//
//        verify(context).respond("help.help");
//    }
//}