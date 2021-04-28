package com.github.lucbui.fracktail3.spring.schedule.model;

import com.github.lucbui.fracktail3.magic.schedule.context.ScheduleUseContext;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A piece of a MethodCallingScheduledAction or FieldCallingScheduledAction which determines how the method's response should be handled
 */
@Data
public class ReturnScheduledComponent {
    protected RCSFunction func;
    protected final List<Consumer<Object>> consumers;

    public ReturnScheduledComponent() {
        this.consumers = new ArrayList<>();
    }

    public interface RCSFunction {
        Mono<Void> apply(ScheduleUseContext context, Object returnVal);
    }
}
