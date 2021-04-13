package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Mono;

/**
 * A guard which enforces a certain type of platform
 */
public class PlatformValidatorGuard implements Guard {
    private final Class<? extends Platform> platform;

    /**
     * Initialize
     * @param platform The class of platform to allow
     */
    public PlatformValidatorGuard(Class<? extends Platform> platform) {
        this.platform = platform;
    }

    @Override
    public Mono<Boolean> matches(CommandUseContext ctx) {
        return Mono.just(ClassUtils.isAssignable(ctx.getPlatform().getClass(), platform));
    }

    /**
     * Get the allowable platform class
     * @return Platform class
     */
    public Class<? extends Platform> getPlatform() {
        return platform;
    }
}
