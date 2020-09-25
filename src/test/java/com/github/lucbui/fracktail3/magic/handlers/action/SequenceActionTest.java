package com.github.lucbui.fracktail3.magic.handlers.action;

import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.platform.CommandContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.publisher.PublisherProbe;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SequenceActionTest {
    @Mock
    private Bot bot;

    @Mock
    private CommandContext commandContext;

    @Mock
    private Action first;

    @Mock
    private Action second;

    private SequenceAction action;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        action = new SequenceAction.Builder(first).then(second).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testDoAction() {
        PublisherProbe<Void> firstProbe = PublisherProbe.empty();
        PublisherProbe<Void> secondProbe = PublisherProbe.empty();
        when(first.doAction(any(), any())).thenReturn(firstProbe.mono());
        when(second.doAction(any(), any())).thenReturn(secondProbe.mono());

        action.doAction(bot, commandContext).block();

        firstProbe.assertWasSubscribed();
        firstProbe.assertWasRequested();
        firstProbe.assertWasNotCancelled();

        secondProbe.assertWasSubscribed();
        secondProbe.assertWasRequested();
        secondProbe.assertWasNotCancelled();
    }
}