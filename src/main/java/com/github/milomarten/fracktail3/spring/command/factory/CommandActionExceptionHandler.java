package com.github.milomarten.fracktail3.spring.command.factory;

import com.github.milomarten.fracktail3.magic.command.action.CommandAction;
import com.github.milomarten.fracktail3.magic.platform.context.CommandUseContext;
import com.github.milomarten.fracktail3.magic.schedule.action.ScheduledAction;
import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.milomarten.fracktail3.spring.command.model.ExceptionComponent;
import com.github.milomarten.fracktail3.spring.schedule.model.ExceptionScheduledComponent;
import reactor.core.publisher.Mono;

public enum CommandActionExceptionHandler implements ExceptionComponent.ECFunction, ExceptionScheduledComponent.ECSFunction {
    INSTANCE;

    @Override
    public Mono<Void> apply(CommandUseContext context, Throwable t) {
        return ((CommandAction)t).doAction(context);
    }

    @Override
    public Mono<Void> apply(ScheduleUseContext context, Throwable t) {
        return ((ScheduledAction)t).execute(context);
    }
}
