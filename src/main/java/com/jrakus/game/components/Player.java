package com.jrakus.game.components;

import java.util.List;

public class Player {
    private final String name;
    private final Hand hand;

    public Player(String name, Hand hand) {
        this.name = name;
        this.hand = hand;
    }

    public List<Card> showCardsOnHand() {
        return hand.showCards();
    }

    public void playCards(List<Card> cards) {
        hand.playCards(cards);
    }

    public void addCardToHand(List<Card> card) {
        hand.addCards(card);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", hand=" + hand +
                '}';
    }
}
