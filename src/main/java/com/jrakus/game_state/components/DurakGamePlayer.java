package com.jrakus.game_state.components;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public class DurakGamePlayer {
    private final List<Card> cardsOnHand;

    public DurakGamePlayer(List<Card> hand) {
        this.cardsOnHand = hand;
    }

    public List<Card> showCardsOnHand() {
        return cardsOnHand;
    }

    public void playCards(List<Card> cardsToPlay) {
        if(!new HashSet<>(cardsOnHand).containsAll(cardsToPlay))
            throw new NoSuchElementException("There is no such card on hand");

        cardsOnHand.removeAll(cardsToPlay);
    }

    public void addCardToHand(List<Card> cards) {
        cardsOnHand.addAll(cards);
    }


    @Override
    public String toString() {
        return "DurakGamePlayer{" +
                ", cardsOnHand=" + cardsOnHand +
                '}';
    }
}
