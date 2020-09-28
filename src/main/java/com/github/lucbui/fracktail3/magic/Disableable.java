package com.github.lucbui.fracktail3.magic;

/**
 * Interface that marks an object as being able to be disabled or not
 */
public interface Disableable {
    /**
     * Check if this object is enabled
     * @return True, if enabled
     */
    boolean isEnabled();

    /**
     * Set this object's enabled state
     * @param enabled True if enabled, false if disabled
     */
    void setEnabled(boolean enabled);
}
