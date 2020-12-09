package com.github.lucbui.fracktail3.dnd.dicebot;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.springframework.stereotype.Component;


@Component
public class DefaultDiceFactory implements DiceFactory {
    private final RandomGenerator generator = new MersenneTwister();

    @Override
    public Dice createDice(int faces) {
        return new FaceDice(generator, faces);
    }
}
