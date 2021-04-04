package com.github.lucbui.fracktail3.modules.games;

import lombok.Data;

@Data
public class GameSession<G extends Game<?>> {
    private final String id;
    private final G game;
}
