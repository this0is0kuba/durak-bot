package com.jrakus.game.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DiscardPile {
    private final List<Card> discardedCards = new ArrayList<>();

    public DiscardPile() {}

    public void addCardsToPile(List<Card> newDiscardedCards) {
        discardedCards.addAll(newDiscardedCards);
    }

    public List<Card> showCardsOnPile() {
        return discardedCards;
    }
}
