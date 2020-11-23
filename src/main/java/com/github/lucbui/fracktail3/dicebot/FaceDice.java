package com.github.lucbui.fracktail3.dicebot;

import org.apache.commons.math3.random.RandomGenerator;

public class FaceDice implements Dice {
    private final RandomGenerator random;
    private final int faces;

    public FaceDice(RandomGenerator random, int faces) {
        this.random = random;
        this.faces = faces;
    }

    public int roll() {
        return random.nextInt(faces) + 1;
    }

    public int getFaces() {
        return faces;
    }
}
