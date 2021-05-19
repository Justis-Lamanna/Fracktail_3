package com.github.lucbui.fracktail3.spring.command.guard;

import com.github.lucbui.fracktail3.magic.guard.Guard;
import com.github.lucbui.fracktail3.magic.platform.Platform;
import com.github.lucbui.fracktail3.magic.platform.context.CommandUseContext;
import lombok.Data;
import org.apache.commons.lang3.ClassUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * A guard which enforces a certain type of platform
 */
@Data
public class PlatformValidatorGuard implements Guard {
    private final Collection<Class<? extends Platform>> platforms;

    @Override
    public Mono<Boolean> matches(CommandUseContext ctx) {
        return Flux.fromIterable(platforms)
                .all(clazz -> ClassUtils.isAssignable(ctx.getPlatform().getClass(), clazz));
    }
}
