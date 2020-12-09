package com.github.lucbui.fracktail3.dnd.harrowbot;

/**
 * Some type of Harrow deck of cards
 * @param <CARD>
 */
public interface HarrowDeck<CARD> {
    /**
     * Shuffle the deck
     */
    void shuffle();

    /**
     * Draw a card from the deck
     * @return The drawn card
     */
    CARD draw();
}
