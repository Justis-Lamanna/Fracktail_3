package com.github.lucbui.fracktail3.magic.util;

import java.time.Duration;
import java.time.Instant;

/**
 * Utility to help maintain a cooldown timer
 */
public class Cooldown {
    private final Duration cooldown;
    private Instant lastCall = null;

    /**
     * Initialize with a cooldown
     * @param cooldown The cooldown duration
     */
    public Cooldown(Duration cooldown) {
        this.cooldown = cooldown;
    }

    /**
     * Trigger cooldown to begin
     */
    public void triggerCooldown() {
        this.lastCall = Instant.now();
    }

    /**
     * Test if cooldown is complete
     * @return True, if complete
     */
    public boolean isCooldownComplete() {
        Instant now = Instant.now();
        Duration timeSinceLastCall = Duration.between(lastCall, now);
        return timeSinceLastCall.compareTo(cooldown) >= 0;
    }
}
