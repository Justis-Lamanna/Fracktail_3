package com.github.lucbui.fracktail3.spring.command.model;

import com.github.lucbui.fracktail3.magic.platform.context.ScheduledUseContext;

/**
 * A piece of a MethodCallingScheduledAction or FieldCallingScheduledAction which determines how the method's response should be handled
 */
public class ReturnScheduledComponent extends ReturnBaseComponent<ScheduledUseContext> {
    protected ReturnScheduledComponent(ReturnConverterFunction<? super ScheduledUseContext> func) {
        super(func);
    }
}
