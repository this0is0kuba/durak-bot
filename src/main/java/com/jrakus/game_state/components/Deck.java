package com.jrakus.game_state.components;

import java.util.*;

public class Deck {
    private final List<Card> cards = new ArrayList<>();
    private final Random random = new Random();

    private final Card trumpCard;
    private boolean isDeckEmpty = false;

    public Deck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }

        shuffle();

        trumpCard = cards.getLast();
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card drawOneCard() {
        if (cards.isEmpty()) {
            return getTrumpCard();
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
        return isDeckEmpty;
    }

    public int size() {
        return cards.size();
    }

    public Card showTrumpCard() {
        return trumpCard;
    }

    public int getNumberOfCards() {
        return cards.size() + 1;
    }

    private Card getTrumpCard() {
        if(isDeckEmpty) {
            throw new NoSuchElementException("Deck is empty!");
        }
        isDeckEmpty = true;
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
