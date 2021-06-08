package com.github.milomarten.fracktail3.modules.games;

import java.util.function.Supplier;

public class ActionLegality {
    private static final ActionLegality LEGAL = new ActionLegality(null);

    private final String failureReason;

    private ActionLegality(String failureReason) {
        this.failureReason = failureReason;
    }

    public static ActionLegality legal() {
        return LEGAL;
    }

    public static ActionLegality illegal(String reason) {
        return new ActionLegality(reason);
    }

    public static ActionLegality test(boolean legality, String reason) {
        return legality ? legal() : illegal(reason);
    }

    public boolean isLegal() {
        return failureReason == null;
    }

    public boolean isIllegal() {
        return !isLegal();
    }

    public String getReason() {
        if(isLegal()) {
            throw new IllegalStateException("Move is legal");
        }
        return failureReason;
    }

    public ActionLegality and(Supplier<ActionLegality> other) {
        if(isIllegal()) {
            return this;
        } else {
            return other.get();
        }
    }

    public ActionLegality doIfLegal(Runnable action) {
        if(isLegal()) {
            action.run();
        }
        return this;
    }
}
