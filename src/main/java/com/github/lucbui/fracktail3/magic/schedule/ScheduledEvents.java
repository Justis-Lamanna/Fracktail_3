package com.github.lucbui.fracktail3.magic.schedule;

import com.github.lucbui.fracktail3.magic.BotSpec;
import com.github.lucbui.fracktail3.magic.Validated;
import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.util.IdStore;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Store which maintains an ID-separated list of events
 */
public class ScheduledEvents extends IdStore<ScheduledEvent> implements Validated {
    /**
     * Initialize this store
     * @param store The store contents
     */
    public ScheduledEvents(Map<String, ScheduledEvent> store) {
        super(store);
    }

    /**
     * Initialize this store
     * @param store The store contents
     */
    public ScheduledEvents(List<ScheduledEvent> store) {
        super(store);
    }

    /**
     * Creates an empty store of scheduled actions
     * @return An empty store of scheduled actions
     */
    public static ScheduledEvents empty() {
        return new ScheduledEvents(Collections.emptyMap());
    }

    @Override
    public void validate(BotSpec spec) throws BotConfigurationException {
        getAll().forEach(e -> e.validate(spec));
    }
}
