package com.github.milomarten.fracktail3.modules.cc;

import lombok.Data;

@Data
public class CCResponse {
    private long id;
    private int status;
    private String message;
}
