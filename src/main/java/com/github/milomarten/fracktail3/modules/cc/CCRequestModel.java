package com.github.milomarten.fracktail3.modules.cc;

import lombok.Data;

@Data
public class CCRequestModel {
    private final CCRedemption code;
    private final String viewer;
    private final CCRequestType type;
}
