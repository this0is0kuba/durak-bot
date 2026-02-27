package com.jrakus.game_state.components;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile {
    private final List<Card> discardedCards = new ArrayList<>();

    public DiscardPile() {}

    public void addCardsToPile(List<Card> newDiscardedCards) {
        discardedCards.addAll(newDiscardedCards);
    }

    public List<Card> showCardsOnPile() {
        return discardedCards;
    }

    @Override
    public String toString() {
        return "DiscardPile{" +
                "discardedCards=" + discardedCards +
                '}';
    }
}
