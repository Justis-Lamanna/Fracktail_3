package com.github.milomarten.fracktail3.discord.platform;

import com.github.milomarten.fracktail3.discord.config.DiscordConfiguration;
import com.github.milomarten.fracktail3.magic.platform.context.ParameterParser;
import com.github.milomarten.fracktail3.magic.platform.formatting.SemanticMessage;
import com.github.milomarten.fracktail3.magic.platform.formatting.StdIntent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DiscordPlatformTest {
    @Mock
    private DiscordConfiguration discordConfiguration;

    @Mock
    private ParameterParser parameterParser;

    @InjectMocks
    private DiscordPlatform discordPlatform;

    @Test
    public void testQuoteIntent() {
        SemanticMessage sm = SemanticMessage.create(StdIntent.QUOTE, "Hello\nMy name is\nMilo Marten!");
        String message = sm.toString(discordPlatform);
        assertEquals("> Hello\n> My name is\n> Milo Marten!", message);
    }
}