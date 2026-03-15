package com.jrakus.game_state.components;

import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;

import java.util.*;

public class Deck {
    private final List<Card> cards;
    private final Random random;

    private final Card trumpCard;

    public Deck() {
       this(new Random());
    }

    public Deck(Random random) {
        this.random = random;

        int nCards = Card.Suit.values().length * Card.Rank.values().length;
        this.cards = new ArrayList<>(nCards);

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }

        shuffle();
        trumpCard = cards.getFirst();
    }

    public Deck(List<Card> cards) {
        this.random = new Random();

        this.cards = cards;
        trumpCard = cards.getFirst();
    }

    public Deck(Card trumpCard) {
        this.cards = new ArrayList<>(0);
        this.random = new Random();

        this.trumpCard = trumpCard;
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card drawOneCard() {
        if (cards.isEmpty())
            throw new DurakGameInvalidStateException("Deck is empty!");

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
