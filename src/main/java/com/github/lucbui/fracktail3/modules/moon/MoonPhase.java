package com.github.lucbui.fracktail3.modules.moon;

import lombok.Data;

@Data
public class MoonPhase {
    private final double angle;

    public boolean isFull() {
        return angle > 99.9;
    }

    public boolean isNew() {
        return angle < 0 && angle > -0.1;
    }

    public boolean isWaning() {
        return angle < 0;
    }

    public boolean isWaxing() {
        return angle > 0;
    }

    public double getCoverage() {
        return Math.abs(angle);
    }
}
