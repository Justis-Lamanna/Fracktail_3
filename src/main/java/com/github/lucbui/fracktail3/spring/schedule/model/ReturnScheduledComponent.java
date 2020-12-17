package com.github.lucbui.fracktail3.spring.schedule.model;

import com.github.lucbui.fracktail3.magic.platform.context.ScheduledUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ReturnBaseComponent;

/**
 * A piece of a MethodCallingScheduledAction or FieldCallingScheduledAction which determines how the method's response should be handled
 */
public class ReturnScheduledComponent extends ReturnBaseComponent<ScheduledUseContext> {
    public ReturnScheduledComponent(ReturnConverterFunction<? super ScheduledUseContext> func) {
        super(func);
    }
}
