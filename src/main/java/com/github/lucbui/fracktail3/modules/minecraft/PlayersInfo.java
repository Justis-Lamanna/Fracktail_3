package com.github.lucbui.fracktail3.modules.minecraft;

import lombok.Data;

import java.util.Map;

@Data
public class PlayersInfo {
    private int online;
    private int max;
    private String[] list;
    private Map<String, String> uuid;
}
