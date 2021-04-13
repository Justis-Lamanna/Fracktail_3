package com.github.lucbui.fracktail3.spring.schedule.model;

import com.github.lucbui.fracktail3.magic.schedule.context.ScheduleUseContext;
import com.github.lucbui.fracktail3.spring.command.model.ParameterBaseComponent;

/**
 * A piece of a MethodCallingScheduledAction which resolves the context into a parameter to be injected into the method
 */
public class ParameterScheduledComponent extends ParameterBaseComponent<ScheduleUseContext> {
    public ParameterScheduledComponent(ParameterConverterFunction<? super ScheduleUseContext> func) {
        super(func);
    }
}
