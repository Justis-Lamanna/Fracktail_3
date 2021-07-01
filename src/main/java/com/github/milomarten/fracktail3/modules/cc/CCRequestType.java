package com.github.milomarten.fracktail3.modules.cc;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CCRequestType {
    TEST(0),
    START(1),
    STOP(2);

    private final int id;

    CCRequestType(int id) {
        this.id = id;
    }

    @JsonValue
    public int getId() {
        return id;
    }
}
