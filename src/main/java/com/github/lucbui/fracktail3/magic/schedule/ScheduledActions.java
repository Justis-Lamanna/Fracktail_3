package com.github.lucbui.fracktail3.magic.schedule;

import com.github.lucbui.fracktail3.magic.utils.model.IdStore;

import java.util.List;
import java.util.Map;

/**
 * Store which maintains an ID-separated list of events
 */
public class ScheduledActions extends IdStore<ScheduledEvent> {
    /**
     * Initialize this store
     * @param store The store contents
     */
    public ScheduledActions(Map<String, ScheduledEvent> store) {
        super(store);
    }

    /**
     * Initialize this store
     * @param store The store contents
     */
    public ScheduledActions(List<ScheduledEvent> scheduledEvents) {
        super(scheduledEvents);
    }
}
