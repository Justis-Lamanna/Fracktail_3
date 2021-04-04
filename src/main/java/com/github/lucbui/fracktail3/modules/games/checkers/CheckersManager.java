package com.github.lucbui.fracktail3.modules.games.checkers;

import com.github.lucbui.fracktail3.modules.games.GameSession;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CheckersManager {
    private final Map<String, Checkers> games = new HashMap<>();

    public synchronized GameSession<Checkers> startGame() {
        String id = generateId();
        Checkers checkers = new Checkers();
        games.put(id, checkers);
        return new GameSession<>(id, checkers);
    }

    private String generateId() {
        String rid;
        do {
            rid = RandomStringUtils.randomAlphanumeric(6);
        } while (games.containsKey(rid));
        return rid;
    }

    public boolean endGame(String id) {
        return games.remove(id) != null;
    }

    public Optional<Checkers> getGame(String id) {
        return Optional.ofNullable(games.get(id));
    }
}
