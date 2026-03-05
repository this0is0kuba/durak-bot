package com.jrakus.unit_test.game_state.components;

import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.Table;
import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    private Table table;

    @BeforeEach
    void setUp() {
        table = new Table(); // uses real TableValidator
    }

    // =========================
    // ATTACK TURN VALIDATION
    // =========================

    @Test
    void shouldAllowAttackWithOneCard() {
        Card c1 = new Card(HEARTS, NINE);

        table.addAttackingCards(List.of(c1));

        assertEquals(1, table.showAttackingCards().size());
    }

    @ParameterizedTest
    @CsvSource({
            "HEARTS, NINE, SPADES, NINE",
            "DIAMONDS, TEN, CLUBS, TEN",
            "HEARTS, QUEEN, CLUBS, QUEEN",
    })
    void shouldAllowAttackOnEmptyTable_whenSameRank(
        Suit suit1, Rank rank1,
        Suit suit2, Rank rank2
    ) {
        Card c1 = new Card(suit1, rank1);
        Card c2 = new Card(suit2, rank2);

        table.addAttackingCards(List.of(c1, c2));

        assertEquals(2, table.showAttackingCards().size());
    }

    @Test
    void shouldThrow_whenAttackingWithDifferentRanksOnFirstMove() {
        Card c1 = new Card(HEARTS, NINE);
        Card c2 = new Card(SPADES, JACK);

        assertThrows(DurakGameInvalidStateException.class,
                () -> table.addAttackingCards(List.of(c1, c2)));
    }

    @Test
    void shouldThrow_whenAttackingDuringDefendTurn() {
        Card attack = new Card(HEARTS, NINE);
        table.addAttackingCards(List.of(attack));

        // Now isAttackMove = false

        Card anotherAttack = new Card(SPADES, NINE);

        assertThrows(DurakGameInvalidStateException.class,
                () -> table.addAttackingCards(List.of(anotherAttack)));
    }

    // =========================
    // DEFEND TURN VALIDATION
    // =========================

    @Test
    void shouldAllowDefendWithStrongerCard_sameSuit() {
        Card attack = new Card(HEARTS, NINE);
        table.addAttackingCards(List.of(attack));

        Card defend = new Card(HEARTS, TEN);

        table.addDefendingCards(List.of(defend), SPADES);

        assertEquals(1, table.showDefendingCards().size());
    }

    @ParameterizedTest
    @CsvSource({
            "HEARTS, KING, SPADES, TEN, SPADES",
            "DIAMONDS, QUEEN, DIAMONDS, KING, DIAMONDS"
    })
    void shouldAllowDefendWithTrump(
            Suit attackingSuit, Rank attackingRank,
            Suit defendingSuit, Rank defendingRank,
            Suit trump
    ) {
        Card attack = new Card(attackingSuit, attackingRank);
        table.addAttackingCards(List.of(attack));

        Card defend = new Card(defendingSuit, defendingRank); // trump
        table.addDefendingCards(List.of(defend), trump);

        assertEquals(1, table.showDefendingCards().size());
    }

    @ParameterizedTest
    @CsvSource({
            "HEARTS, KING, HEARTS, TEN, SPADES",
            "HEARTS, KING, HEARTS, TEN, HEARTS",
            "DIAMONDS, ACE, DIAMONDS, KING, DIAMONDS"
    })
    void shouldThrow_whenDefendingWithWeakerCard(
            Suit attackingSuit, Rank attackingRank,
            Suit defendingSuit, Rank defendingRank,
            Suit trump
    ) {
        Card attack = new Card(attackingSuit, attackingRank);
        table.addAttackingCards(List.of(attack));

        Card defend = new Card(defendingSuit, defendingRank);

        assertThrows(DurakGameInvalidStateException.class,
                () -> table.addDefendingCards(List.of(defend), trump));
    }

    @Test
    void shouldThrow_whenDefendingDuringAttackTurn() {
        Card defend = new Card(HEARTS, NINE);

        assertThrows(DurakGameInvalidStateException.class,
                () -> table.addDefendingCards(List.of(defend), SPADES));
    }

    // =========================
    // TURN ALTERNATION
    // =========================

    @Test
    void shouldAlternateTurnsCorrectly() {

        Card attack1 = new Card(HEARTS, NINE);
        table.addAttackingCards(List.of(attack1));

        Card defend1 = new Card(HEARTS, JACK);
        table.addDefendingCards(List.of(defend1), SPADES);

        // attack again allowed
        Card attack2 = new Card(CLUBS, NINE);
        table.addAttackingCards(List.of(attack2));

        assertEquals(2, table.showAttackingCards().size());
    }

    // =========================
    // CLEAR TABLE
    // =========================

    @Test
    void shouldClearTableAndResetTurn() {

        Card attack = new Card(HEARTS, NINE);
        Card defend = new Card(HEARTS, TEN);

        table.addAttackingCards(List.of(attack));
        table.addDefendingCards(List.of(defend), SPADES);

        List<Card> cleared = table.clearTable();

        assertEquals(2, cleared.size());
        assertTrue(table.showAttackingCards().isEmpty());
        assertTrue(table.showDefendingCards().isEmpty());

        // After clear, attack should be allowed again
        Card newAttack = new Card(CLUBS, KING);

        assertDoesNotThrow(() ->
                table.addAttackingCards(List.of(newAttack)));
    }
}