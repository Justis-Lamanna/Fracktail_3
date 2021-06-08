package com.github.milomarten.fracktail3.modules.minecraft;

import lombok.Data;

@Data
public class MOTDInfo {
    private String[] raw;
    private String[] clean;
    private String[] html;
}
