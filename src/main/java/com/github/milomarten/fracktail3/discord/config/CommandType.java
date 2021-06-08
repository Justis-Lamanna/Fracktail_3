package com.github.milomarten.fracktail3.discord.config;

public enum CommandType {
    /**
     * Old-style commands with prefix and message parsing.
     */
    LEGACY,

    /**
     * New-style commands that Discord handles with custom UI and other tools.
     */
    SLASH,

    /**
     * Support both legacy-syle and slash-style commands
     */
    ALL
}
