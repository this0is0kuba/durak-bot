package com.jrakus.unit_tests.game_state.components;

import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.DiscardPile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    static Stream<Arguments> cardProvider() {
        return Stream.of(
                Arguments.of(
                        List.of()
                ),
                Arguments.of(
                        List.of(
                                new Card(SPADES, TEN)
                        )
                ),
                Arguments.of(
                        List.of(
                                new Card(DIAMONDS, ACE),
                                new Card(CLUBS, ACE)
                        )
                ),
                Arguments.of(
                        List.of(
                                new Card(DIAMONDS, ACE),
                                new Card(CLUBS, ACE),
                                new Card(SPADES, TEN)
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("cardProvider")
    void addCardsToPileShouldAddAllCards(List<Card> newCards) {
        discardPile.addCardsToPile(newCards);

        List<Card> cardsOnPile = discardPile.showCardsOnPile();

        assertEquals(newCards.size(), cardsOnPile.size());

        for(Card card: newCards) {
            assertTrue(cardsOnPile.contains(card));
        }
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