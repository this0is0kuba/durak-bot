package com.jrakus.game.components;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public class Hand {
    private final List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> showCards() {
        return cards;
    }

    public void playCards(List<Card> cardsToPlay) {

        if(!new HashSet<>(cards).containsAll(cardsToPlay))
            throw new NoSuchElementException("There is no such card on hand");

        cards.removeAll(cardsToPlay);
    }

    void addCards(List<Card> cardsToAdd) {
        cards.addAll(cardsToAdd);
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                '}';
    }
}
