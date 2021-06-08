package com.github.milomarten.fracktail3.modules.minecraft;

import lombok.Data;

import java.util.Map;

@Data
public class ModInfo {
    private String[] names;
    private Map<String, String> raw;
}
