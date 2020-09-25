package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.filterset.Filter;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.publisher.PublisherProbe;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ActionOptionsTest {
    @Mock
    private Bot bot;

    @Mock
    private CommandContext commandContext;

    @Mock private Action firstAction;
    @Mock private Filter firstFilter;

    @Mock private Action defaultAction;

    private ActionOptions action;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        action = new ActionOptions(Collections.singletonList(new ActionOption(firstFilter, firstAction)), defaultAction);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testFilterBranch_FilterMatches() {
        PublisherProbe<Void> firstProbe = PublisherProbe.empty();
        PublisherProbe<Void> defaultProbe = PublisherProbe.empty();
        when(firstFilter.matches(any(), any())).thenReturn(Mono.just(true));
        when(firstAction.doAction(any(), any())).thenReturn(firstProbe.mono());
        when(defaultAction.doAction(any(), any())).thenReturn(defaultProbe.mono());

        action.doAction(bot, commandContext).block();

        firstProbe.assertWasSubscribed();
        firstProbe.assertWasRequested();
        firstProbe.assertWasNotCancelled();
        defaultProbe.assertWasNotSubscribed();
    }

    @Test
    void testFilterBranch_FilterDoesNotMatches() {
        PublisherProbe<Void> firstProbe = PublisherProbe.empty();
        PublisherProbe<Void> defaultProbe = PublisherProbe.empty();
        when(firstFilter.matches(any(), any())).thenReturn(Mono.just(false));
        when(firstAction.doAction(any(), any())).thenReturn(firstProbe.mono());
        when(defaultAction.doAction(any(), any())).thenReturn(defaultProbe.mono());

        action.doAction(bot, commandContext).block();

        firstProbe.assertWasNotSubscribed();
        defaultProbe.assertWasSubscribed();
        defaultProbe.assertWasRequested();
        defaultProbe.assertWasNotCancelled();
    }
}