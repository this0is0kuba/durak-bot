package com.jrakus.game_state.components;

import java.util.*;

public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final Random random = new Random();

    public Deck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card drawOneCard() {
        if (cards.isEmpty()) {
            throw new NoSuchElementException("Deck is empty!");
        }
        return cards.removeLast();
    }

    public List<Card> drawCards(int numberOfCards) {
        List<Card> cards = new ArrayList<>(numberOfCards);

        for(int i = 0; i < numberOfCards; i++)
            cards.add(this.drawOneCard());

        return cards;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                ", random=" + random +
                '}';
    }
}
