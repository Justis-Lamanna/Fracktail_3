package com.github.lucbui.fracktail3.modules.moon;

import lombok.Data;

@Data
public class MoonPhase {
    private final double angle;

    public boolean isFull() {
        return getCoverage() > 99.5;
    }

    public boolean isNew() {
        return getCoverage() < 0.5;
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
