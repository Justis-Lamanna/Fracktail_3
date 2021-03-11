package com.github.lucbui.fracktail3.modules.games;

public class ActionLegality {
    private final String failureReason;

    private ActionLegality(String failureReason) {
        this.failureReason = failureReason;
    }

    public static ActionLegality legal() {
        return new ActionLegality(null);
    }

    public static ActionLegality illegal(String reason) {
        return new ActionLegality(reason);
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
}
