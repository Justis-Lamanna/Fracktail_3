package com.github.lucbui.fracktail3.modules.games;

public interface Action<T, B extends Board<T>> {
    void performAction(Game<T, B> previousState);
}
