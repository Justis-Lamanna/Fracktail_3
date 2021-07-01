package com.github.milomarten.fracktail3.modules.cc;

import lombok.Data;

@Data(staticConstructor = "of")
public class RedemptionResult {
    private final RedemptionStatus status;
    private final String reason;

    public RedemptionResult combine(RedemptionResult other) {
        RedemptionStatus newStatus = status.combine(other.status);
        String newReason = this.status == RedemptionStatus.NONE ? other.reason : this.reason;
        return RedemptionResult.of(newStatus, newReason);
    }
}
