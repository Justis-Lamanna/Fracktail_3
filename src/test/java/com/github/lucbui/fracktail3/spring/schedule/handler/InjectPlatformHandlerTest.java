package com.github.lucbui.fracktail3.spring.schedule.handler;

import com.github.lucbui.fracktail3.BaseFracktailTest;
import com.github.lucbui.fracktail3.magic.Bot;
import com.github.lucbui.fracktail3.magic.config.Config;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class InjectPlatformHandlerTest extends BaseFracktailTest {
    @BeforeEach
    void setUp() {
        super.parentSetup();
    }

    @Test
    void retrieveByClassOnly() {
        InjectPlatformHandler handler = new InjectPlatformHandler(TestPlatform.class);
        when(bot.getPlatforms()).thenReturn(Collections.singletonList(new TestPlatform()));

        Platform platform = (Platform) handler.apply(context);
        assertTrue(platform instanceof TestPlatform);
    }

    @Test
    void retrieveByIdAndClass() {
        InjectPlatformHandler handler = new InjectPlatformHandler("test", TestPlatform.class);
        when(bot.getPlatform(eq("test"))).thenReturn(Optional.of(new TestPlatform()));

        Platform platform = (Platform) handler.apply(context);
        assertTrue(platform instanceof TestPlatform);
    }

    @Test
    void retrieveByClassNoneFound() {
        InjectPlatformHandler handler = new InjectPlatformHandler(TestPlatform.class);
        when(bot.getPlatforms()).thenReturn(Collections.emptyList());

        Platform platform = (Platform) handler.apply(context);
        assertNull(platform);
    }

    @Test
    void retrieveByIdAndClassNoneFound() {
        InjectPlatformHandler handler = new InjectPlatformHandler("test", TestPlatform.class);
        when(bot.getPlatform(any())).thenReturn(Optional.empty());

        Platform platform = (Platform) handler.apply(context);
        assertNull(platform);
    }

    private static class TestPlatform implements Platform {
        @Override
        public Config getConfig() {
            return new Config() { };
        }

        @Override
        public Mono<Boolean> start(Bot bot) {
            return Mono.just(true);
        }

        @Override
        public Mono<Boolean> stop(Bot bot) {
            return Mono.just(true);
        }

        @Override
        public String getId() {
            return "test";
        }
    }
}