package com.github.lucbui.fracktail3.spring.plugin.command;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.formatter.FormattedString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CommandHelpActionTest extends BaseFracktailTest {
    private CommandHelpAction commandHelpAction;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        commandHelpAction = new CommandHelpAction();
    }

    @Test
    void shouldReturnHelpIfValidCommandProvided() {
        when(parameters.getParameter(eq(0))).thenReturn(Optional.of("test"));
        when(command.getNames()).thenReturn(Collections.singleton("test"));
        when(command.getHelp()).thenReturn(FormattedString.literal("test hello"));

        //TODO: ??????
    }
}