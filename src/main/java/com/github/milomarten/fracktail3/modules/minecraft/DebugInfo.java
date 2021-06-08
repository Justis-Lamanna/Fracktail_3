package com.github.milomarten.fracktail3.modules.minecraft;

import lombok.Data;

import java.time.Instant;

@Data
public class DebugInfo {
    private boolean ping;
    private boolean query;
    private boolean srv;
    private boolean querymismatch;
    private boolean ipinsrv;
    private boolean cnameinsrv;
    private boolean animatedmotd;
    private Instant cachetime;
}
