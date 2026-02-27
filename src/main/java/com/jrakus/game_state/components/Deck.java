package com.jrakus.game_state.components;

import com.jrakus.playground.exceptions.DurakGameInvalidMoveException;

import java.util.*;

public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final Random random;

    private final Card trumpCard;

    public Deck() {
       this(new Random());
    }

    public Deck(Random random) {
        this.random = random;

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }

        shuffle();
        trumpCard = cards.getFirst();
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card drawOneCard() {
        if (cards.isEmpty())
            throw new DurakGameInvalidMoveException("Deck is empty!");

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

    public Card showTrumpCard() {
        return trumpCard;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                ", random=" + random +
                '}';
    }
}
