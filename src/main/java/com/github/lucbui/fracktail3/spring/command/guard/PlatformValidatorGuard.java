package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.PlatformBaseContext;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Mono;

public class PlatformValidatorGuard implements Guard {
    private final Class<? extends Platform> platform;

    public PlatformValidatorGuard(Class<? extends Platform> platform) {
        this.platform = platform;
    }

    @Override
    public Mono<Boolean> matches(PlatformBaseContext<?> ctx) {
        return Mono.just(ClassUtils.isAssignable(ctx.getPlatform().getClass(), platform));
    }

    public Class<? extends Platform> getPlatform() {
        return platform;
    }
}
