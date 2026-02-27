package com.jrakus.game_state.components;

import java.util.*;

public class DurakGamePlayer {
    private final Set<Card> visibleCardsOnOpponentHand = new HashSet<>();
    private final List<Card> cardsOnHand;

    public DurakGamePlayer(List<Card> hand) {
        this.cardsOnHand = hand;
    }

    public List<Card> showCardsOnHand() {
        return cardsOnHand;
    }

    public int countCardsOnHand() {
        return cardsOnHand.size();
    }

    public void playCards(List<Card> cardsToPlay) {
        if(!new HashSet<>(cardsOnHand).containsAll(cardsToPlay))
            throw new NoSuchElementException("There is no such card on hand");

        cardsOnHand.removeAll(cardsToPlay);
    }

    public void addCardsToHand(List<Card> cards) {
        cardsOnHand.addAll(cards);
    }

    public void addCardToHand(Card card) {
        cardsOnHand.add(card);
    }

    public void addOpponentVisibleCards(List<Card> cards) {
        for (Card card: cards)
            addOpponentVisibleCard(card);
    }

    public void removeOpponentVisibleCards(List<Card> cards) {
        for (Card card: cards)
            removeOpponentVisibleCard(card);
    }

    public void addOpponentVisibleCard(Card card) {
        visibleCardsOnOpponentHand.add(card);
    }

    public void removeOpponentVisibleCard(Card card) {
        visibleCardsOnOpponentHand.remove(card);
    }

    public Set<Card> getOpponentCardsVisibleToPlayer() {
        return visibleCardsOnOpponentHand;
    }

    @Override
    public String toString() {
        return "DurakGamePlayer{" +
                ", cardsOnHand=" + cardsOnHand +
                '}';
    }
}
