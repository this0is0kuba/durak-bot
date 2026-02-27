package unit_tests.com.jrakus.game_state.components;

import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.Deck;
import com.jrakus.playground.exceptions.DurakGameInvalidMoveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Deck deck;
    private final int totalStandardCards = Card.Suit.values().length * Card.Rank.values().length;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void shouldInitializeDeckWithAllCardsPlusTrump() {
        assertEquals(totalStandardCards, deck.size());
        assertFalse(deck.isEmpty());
        assertNotNull(deck.showTrumpCard());
    }

    @Test
    void drawOneCardShouldReduceDeckSize() {
        int initialSize = deck.size();

        Card drawn = deck.drawOneCard();

        assertNotNull(drawn);
        assertEquals(initialSize - 1, deck.size());
    }

    @Test
    void drawCardsShouldReturnRequestedNumberOfCards() {
        List<Card> drawnCards = deck.drawCards(5);

        assertEquals(5, drawnCards.size());
        assertEquals(totalStandardCards - 5, deck.size());
    }

    @Test
    void drawNegativeNumberOfCardsShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> deck.drawCards(-1));
    }

    @Test
    void drawingAllCardsShouldEventuallyReturnTrumpCard() {

        for (int i = 0; i < totalStandardCards - 1; i++)
            deck.drawOneCard();

        // Last card in deck should be trump card
        Card trumpFromDraw = deck.drawOneCard();

        assertEquals(deck.showTrumpCard(), trumpFromDraw);
        assertTrue(deck.isEmpty());
    }

    @Test
    void drawingAfterTrumpCardShouldThrowException() {

        for (int i = 0; i < totalStandardCards; i++) {
            deck.drawOneCard();
        }

        assertThrows(DurakGameInvalidMoveException.class, deck::drawOneCard);
    }

    @Test
    void deckShouldContain24DifferentCards() {

        List<Card> cardsOnDeck = deck.drawCards(totalStandardCards);
        Set<Card> cardSet = new HashSet<>(cardsOnDeck);

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {

                Card card = new Card(suit, rank);
                boolean isCardOnDeck = cardSet.contains(card);
                assertTrue(isCardOnDeck);
            }
        }

    }

    @Test
    void shuffleShouldBeDeterministicWithSameSeed() {
        Random r1 = new Random(42);
        Random r2 = new Random(42);

        Deck d1 = new Deck(r1);
        Deck d2 = new Deck(r2);

        List<Card> cards1 = d1.drawCards(d1.size());
        List<Card> cards2 = d2.drawCards(d2.size());

        assertEquals(cards1, cards2);
    }


    @Test
    void shuffleShouldProduceDifferentOrderForDifferentSeeds() {
        Deck deck1 = new Deck(new Random(1));
        Deck deck2 = new Deck(new Random(2));

        List<Card> cards1 = deck1.drawCards(deck1.size());
        List<Card> cards2 = deck2.drawCards(deck2.size());

        assertNotEquals(cards1, cards2);
    }

}