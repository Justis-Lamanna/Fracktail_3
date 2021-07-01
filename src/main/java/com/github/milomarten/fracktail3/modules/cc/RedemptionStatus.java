package com.github.milomarten.fracktail3.modules.cc;

public enum RedemptionStatus {
    OK {
        public RedemptionStatus combine(RedemptionStatus other) {
            switch (other) {
                case NONE:
                case OK:
                    return OK;
                case FAIL:
                case PARTIAL_OK:
                    return PARTIAL_OK;
            }
            return NONE;
        }
    },
    PARTIAL_OK {
        public RedemptionStatus combine(RedemptionStatus other) {
            return PARTIAL_OK;
        }
    },
    FAIL {
        public RedemptionStatus combine(RedemptionStatus other) {
            switch (other) {
                case NONE:
                case FAIL:
                    return FAIL;
                case OK:
                case PARTIAL_OK:
                    return PARTIAL_OK;
            }
            return NONE;
        }
    },
    NONE {
        public RedemptionStatus combine(RedemptionStatus other) {
            return other;
        }
    };

    public abstract RedemptionStatus combine(RedemptionStatus other);
}
