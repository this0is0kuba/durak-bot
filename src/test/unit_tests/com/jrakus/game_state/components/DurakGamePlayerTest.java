package com.jrakus.game_state.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DurakGamePlayerTest {

    private DurakGamePlayer player;
    private Card c1, c2, c3, c4;

    @BeforeEach
    void setUp() {
        c1 = new Card(Card.Suit.HEARTS, Card.Rank.NINE);
        c2 = new Card(Card.Suit.SPADES, Card.Rank.JACK);
        c3 = new Card(Card.Suit.CLUBS, Card.Rank.ACE);
        c4 = new Card(Card.Suit.SPADES, Card.Rank.KING);

        List<Card> initialHand = new ArrayList<>(List.of(c1, c2));

        player = new DurakGamePlayer(initialHand);
    }

    // ============================
    // Tests for cards on hand
    // ============================

    @Test
    void showCardsOnHandShouldReturnAllCardsOnHand() {
        List<Card> hand = player.showCardsOnHand();

        assertEquals(2, hand.size());

        assertTrue(hand.contains(c1));
        assertTrue(hand.contains(c2));
    }

    @Test
    void playCardsShouldRemoveCardsFromHand() {
        player.playCards(List.of(c1));

        List<Card> hand = player.showCardsOnHand();

        assertEquals(1, hand.size());
        assertFalse(hand.contains(c1));
        assertTrue(hand.contains(c2));
    }

    @Test
    void playCardsShouldThrowIfCardNotOnHand() {
        assertThrows(
                NoSuchElementException.class,
                () -> player.playCards(List.of(c3))
        );
    }

    @Test
    void addCardsToHandShouldAddAllCards() {
        player.addCardsToHand(List.of(c3, c4));

        List<Card> hand = player.showCardsOnHand();

        assertEquals(4, hand.size());
        assertTrue(hand.containsAll(List.of(c1, c2, c3, c4)));
    }

    @Test
    void addCardToHandShouldAddSingleCard() {
        player.addCardToHand(c3);

        List<Card> hand = player.showCardsOnHand();
        assertEquals(3, hand.size());
        assertTrue(hand.contains(c3));
    }

    // ============================
    // Tests for opponent visible cards
    // ============================

    @Test
    void addOpponentVisibleCardShouldAddCard() {
        player.addOpponentVisibleCard(c3);

        Set<Card> visible = player.getOpponentCardsVisibleToPlayer();

        assertEquals(1, visible.size());
        assertTrue(visible.contains(c3));
    }

    @Test
    void addOpponentVisibleCardsShouldAddMultipleCards() {
        player.addOpponentVisibleCards(List.of(c1, c2));

        Set<Card> visible = player.getOpponentCardsVisibleToPlayer();

        assertEquals(2, visible.size());
        assertTrue(visible.containsAll(List.of(c1, c2)));
    }

    @Test
    void removeOpponentVisibleCardShouldRemoveCard() {
        player.addOpponentVisibleCard(c1);
        player.removeOpponentVisibleCard(c1);

        Set<Card> visible = player.getOpponentCardsVisibleToPlayer();
        assertFalse(visible.contains(c1));
    }

    @Test
    void removeOpponentVisibleCardsShouldRemoveMultipleCards() {
        player.addOpponentVisibleCards(List.of(c1, c2));
        player.removeOpponentVisibleCards(List.of(c1, c2));

        Set<Card> visible = player.getOpponentCardsVisibleToPlayer();
        assertTrue(visible.isEmpty());
    }

    @Test
    void visibleCardsSetShouldNotDuplicateCards() {
        player.addOpponentVisibleCard(c1);
        player.addOpponentVisibleCard(c1);

        Set<Card> visible = player.getOpponentCardsVisibleToPlayer();
        assertEquals(1, visible.size());
    }

    @Test
    void removeCardsShouldDoNothingIfCardsAreInvisible() {
        player.addOpponentVisibleCard(c1);
        player.removeOpponentVisibleCards(List.of(c2, c3));

        Set<Card> visible = player.getOpponentCardsVisibleToPlayer();
        assertEquals(1, visible.size());
    }

    // ============================
    // Edge case tests
    // ============================

    @Test
    void playCardsWithEmptyListShouldDoNothing() {
        List<Card> before = new ArrayList<>(player.showCardsOnHand());
        player.playCards(List.of());
        assertEquals(before, player.showCardsOnHand());
    }

    @Test
    void addEmptyListToHandShouldDoNothing() {
        List<Card> before = new ArrayList<>(player.showCardsOnHand());
        player.addCardsToHand(List.of());
        assertEquals(before, player.showCardsOnHand());
    }

    @Test
    void addEmptyListToOpponentVisibleShouldDoNothing() {
        Set<Card> before = new HashSet<>(player.getOpponentCardsVisibleToPlayer());
        player.addOpponentVisibleCards(List.of());
        assertEquals(before, player.getOpponentCardsVisibleToPlayer());
    }
}