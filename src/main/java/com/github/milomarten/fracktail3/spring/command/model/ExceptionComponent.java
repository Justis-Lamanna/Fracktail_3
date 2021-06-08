package com.github.milomarten.fracktail3.spring.command.model;

import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;
import reactor.core.publisher.Mono;

/**
 * A piece of a MethodCallingAction or FieldCallingAction which handles exceptions thrown in the course of execution
 */
public class ExceptionComponent extends ExceptionBaseComponent<ExceptionComponent.ECFunction> {
    public interface ECFunction {
        Mono<Void> apply(CommandUseContext context, Throwable t);
    }
}
