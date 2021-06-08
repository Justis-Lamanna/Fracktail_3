package com.github.milomarten.fracktail3.spring.schedule.model;

import com.github.milomarten.fracktail3.magic.schedule.context.ScheduleUseContext;
import lombok.Data;
import org.springframework.core.convert.TypeDescriptor;

/**
 * A piece of a MethodCallingScheduledAction which resolves the context into a parameter to be injected into the method
 */
@Data
public class ParameterScheduledComponent {
    protected final TypeDescriptor type;
    protected PCSFunction func;

    public ParameterScheduledComponent(TypeDescriptor type) {
        this.type = type;
    }

    public interface PCSFunction {
        Object apply(ScheduleUseContext context);
    }
}
