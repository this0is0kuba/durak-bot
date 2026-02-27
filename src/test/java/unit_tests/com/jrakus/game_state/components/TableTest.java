package unit_tests.com.jrakus.game_state.components;

import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.Table;
import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    private Table table;

    private Card nineHearts;
    private Card jackHearts;
    private Card queenHearts;
    private Card nineSpades;
    private Card aceSpades;

    @BeforeEach
    void setUp() {
        table = new Table();

        nineHearts = new Card(Card.Suit.HEARTS, Card.Rank.NINE);
        jackHearts = new Card(Card.Suit.HEARTS, Card.Rank.JACK);
        queenHearts = new Card(Card.Suit.HEARTS, Card.Rank.QUEEN);

        nineSpades = new Card(Card.Suit.SPADES, Card.Rank.NINE);
        aceSpades = new Card(Card.Suit.SPADES, Card.Rank.ACE);
    }

    // ============================
    // Attacking cards
    // ============================

    @Test
    void shouldAddAttackingCards() {
        table.addAttackingCards(List.of(nineHearts));

        assertEquals(List.of(nineHearts), table.showAttackingCards());
    }

    @Test
    void shouldAllowSecondAttackWithSameRank() {
        table.addAttackingCards(List.of(nineHearts));
        table.addAttackingCards(List.of(nineSpades)); // same rank (NINE)

        assertEquals(2, table.showAttackingCards().size());
    }

    @Test
    void shouldThrowErrorWhenAddingAttackWithNewRank() {
        table.addAttackingCards(List.of(nineHearts));

        assertThrows(
                DurakGameInvalidStateException.class,
                () -> table.addAttackingCards(List.of(jackHearts))
        );
    }

    @Test
    void shouldThrowErrorWhenExceedingFiveAttackingCards() {
        table.addAttackingCards(List.of(nineHearts, jackHearts, queenHearts, nineSpades, aceSpades));

        assertThrows(
                DurakGameInvalidStateException.class,
                () -> table.addAttackingCards(List.of(new Card(Card.Suit.SPADES, Card.Rank.NINE)))
        );
    }

    // ============================
    // Defending cards
    // ============================

    @Test
    void shouldAllowValidDefendWithHigherSameSuitCard() {
        table.addAttackingCards(List.of(nineHearts));
        table.addDefendingCards(List.of(jackHearts), Card.Suit.SPADES);

        assertEquals(List.of(jackHearts), table.showDefendingCards());
    }

    @Test
    void shouldAllowDefendWithTrump() {
        table.addAttackingCards(List.of(queenHearts));
        table.addDefendingCards(List.of(aceSpades), Card.Suit.SPADES);

        assertEquals(1, table.showDefendingCards().size());
    }

    @Test
    void shouldThrowErrorWhenDefendingCardIsWeaker() {
        table.addAttackingCards(List.of(queenHearts));

        assertThrows(
                DurakGameInvalidStateException.class,
                () -> table.addDefendingCards(List.of(nineHearts), Card.Suit.SPADES)
        );
    }

    @Test
    void shouldThrowWhenDefendingCardCountDoesNotMatch() {
        table.addAttackingCards(List.of(nineHearts, nineSpades));

        assertThrows(
                DurakGameInvalidStateException.class,
                () -> table.addDefendingCards(List.of(jackHearts), Card.Suit.SPADES)
        );
    }

    // ============================
    // Clearing table
    // ============================

    @Test
    void clearTableShouldReturnAllCardsAndResetState() {
        table.addAttackingCards(List.of(nineHearts));
        table.addDefendingCards(List.of(jackHearts), Card.Suit.CLUBS);

        List<Card> allCards = table.clearTable();

        assertEquals(2, allCards.size());
        assertTrue(table.showAttackingCards().isEmpty());
        assertTrue(table.showDefendingCards().isEmpty());
    }

    @Test
    void clearEmptyTableShouldReturnEmptyList() {
        List<Card> allCards = table.clearTable();

        assertTrue(allCards.isEmpty());
    }
}