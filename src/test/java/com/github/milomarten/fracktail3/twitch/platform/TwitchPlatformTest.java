package com.github.milomarten.fracktail3.twitch.platform;

import com.github.milomarten.fracktail3.magic.platform.context.ParameterParser;
import com.github.milomarten.fracktail3.magic.platform.formatting.Intent;
import com.github.milomarten.fracktail3.magic.platform.formatting.SemanticMessage;
import com.github.milomarten.fracktail3.twitch.config.TwitchConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TwitchPlatformTest {
    @Mock
    private TwitchConfig config;

    @Mock
    private ParameterParser parameterParser;

    @InjectMocks
    private TwitchPlatform twitchPlatform;

    @Test
    public void testRoleplayIntent() {
        SemanticMessage sm = SemanticMessage.create(Intent.ROLEPLAY, "gives you an egg");
        String message = sm.toString(twitchPlatform);
        assertEquals("/me gives you an egg", message);
    }
}