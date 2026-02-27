package com.jrakus.game_state.components;

import org.junit.jupiter.api.*;

import java.util.List;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static org.junit.jupiter.api.Assertions.*;

class DiscardPileTest {

    private DiscardPile discardPile;

    @BeforeEach
    void setUp() {
        discardPile = new DiscardPile();
    }

    @Test
    void newDiscardPileShouldBeEmpty() {
        assertTrue(discardPile.showCardsOnPile().isEmpty());
    }

    @Test
    void addCardsToPileShouldAddAllCards() {
        Card c1 = new Card(HEARTS, NINE);
        Card c2 = new Card(SPADES, JACK);

        List<Card> newCards = List.of(c1, c2);

        discardPile.addCardsToPile(newCards);

        List<Card> cardsOnPile = discardPile.showCardsOnPile();

        assertEquals(2, cardsOnPile.size());

        assertTrue(cardsOnPile.contains(c1));
        assertTrue(cardsOnPile.contains(c2));
    }

    @Test
    void addingMultipleTimesShouldAccumulateCards() {
        Card c1 = new Card(HEARTS, NINE);
        Card c2 = new Card(Card.Suit.SPADES, Card.Rank.JACK);
        Card c3 = new Card(Card.Suit.CLUBS, Card.Rank.ACE);

        discardPile.addCardsToPile(List.of(c1, c2));
        discardPile.addCardsToPile(List.of(c3));

        List<Card> cardsOnPile = discardPile.showCardsOnPile();

        assertEquals(3, cardsOnPile.size());
        assertTrue(cardsOnPile.containsAll(List.of(c1, c2, c3)));
    }

    @Test
    void addingEmptyListDoesNothing() {
        discardPile.addCardsToPile(List.of());

        assertTrue(discardPile.showCardsOnPile().isEmpty());
    }
}