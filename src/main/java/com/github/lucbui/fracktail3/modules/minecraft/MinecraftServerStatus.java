package com.github.lucbui.fracktail3.modules.minecraft;

import lombok.Data;

@Data
public class MinecraftServerStatus {
    private boolean online;
    private String ip;
    private int port;
    private DebugInfo debug;
    private MOTDInfo motd;
    private PlayersInfo players;
    private String version;
    private int protocol;
    private String hostname;
    private String icon;
    private String software;
    private String map;
    private ModInfo plugins;
    private ModInfo mods;
    private MOTDInfo info;
}
