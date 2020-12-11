package com.github.lucbui.fracktail3.modules.dnd.harrowbot;

import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Representation of a Harrow Deck
 */
public class StandardHarrowDeck implements HarrowDeck<HarrowCard> {
    private final List<HarrowCard> cards;
    /**
     * Initialize the deck
     */
    public StandardHarrowDeck() {
        this.cards = new LinkedList<>(EnumSet.allOf(HarrowCard.class));
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    @Override
    public HarrowCard draw() {
        if(cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty!");
        }
        return cards.remove(0);
    }
}
