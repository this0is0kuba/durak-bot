package com.jrakus.game_state.validators;

import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.DurakGamePlayer;
import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static com.jrakus.game_state.components.GameState.GameStateEnum.ACTIVE_GAME;
import static com.jrakus.game_state.components.GameState.GameStateEnum.PLAYER_1_WON;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DurakGameValidatorTest {

    private DurakGameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DurakGameValidator();
    }

    // =========================
    // checkIfGameIsStillActive
    // =========================

    @Test
    void shouldNotThrow_whenGameIsActive() {
        assertDoesNotThrow(() ->
                validator.checkIfGameIsStillActive(ACTIVE_GAME));
    }

    @Test
    void shouldThrow_whenGameIsNotActive() {
        assertThrows(DurakGameInvalidStateException.class,
                () -> validator.checkIfGameIsStillActive(PLAYER_1_WON));
    }

    // =========================
    // checkIfPlayerCanPlay
    // =========================

    @Test
    void shouldNotThrow_whenPlayerIsActivePlayer() {

        DurakGamePlayer player = mock(DurakGamePlayer.class);

        assertDoesNotThrow(() ->
                validator.checkIfPlayerCanPlay(player, player));
    }

    @Test
    void shouldThrow_whenPlayerIsNotActivePlayer() {

        DurakGamePlayer player1 = mock(DurakGamePlayer.class);
        DurakGamePlayer player2 = mock(DurakGamePlayer.class);

        assertThrows(DurakGameInvalidStateException.class,
                () -> validator.checkIfPlayerCanPlay(player1, player2));
    }

    // =========================
    // checkIfPlayerHasCardsThatHePlays
    // =========================

    @Test
    void shouldNotThrow_whenPlayerHasAllCards() {

        DurakGamePlayer player = mock(DurakGamePlayer.class);

        Card card1 = new Card(HEARTS, TEN);
        Card card2 = new Card(SPADES, JACK);

        when(player.showCardsOnHand()).thenReturn(List.of(card1, card2));

        assertDoesNotThrow(() ->
                validator.checkIfPlayerHasCardsThatHePlays(player, List.of(card1)));
    }

    @Test
    void shouldThrow_whenPlayerDoesNotHaveCard() {

        DurakGamePlayer player = mock(DurakGamePlayer.class);

        Card cardOnHand = new Card(HEARTS, NINE);
        Card cardPlayed = new Card(SPADES, KING);

        when(player.showCardsOnHand()).thenReturn(List.of(cardOnHand));

        assertThrows(DurakGameInvalidStateException.class,
                () -> validator.checkIfPlayerHasCardsThatHePlays(player, List.of(cardPlayed)));
    }

    @Test
    void shouldNotThrow_whenCardsListIsEmpty_andPlayerHasCards() {

        DurakGamePlayer player = mock(DurakGamePlayer.class);

        when(player.showCardsOnHand()).thenReturn(List.of(
                new Card(HEARTS, TEN)
        ));

        assertDoesNotThrow(() ->
                validator.checkIfPlayerHasCardsThatHePlays(player, List.of()));
    }

    // =========================
    // checkIfAttackHasLessCardsThanOpponentHand
    // =========================

    @Test
    void shouldNotThrow_whenAttackCardsLessThanOpponentHand() {

        DurakGamePlayer opponent = mock(DurakGamePlayer.class);
        when(opponent.countCardsOnHand()).thenReturn(3);

        List<Card> attackCards = List.of(
                new Card(HEARTS, NINE),
                new Card(SPADES, TEN)
        );

        assertDoesNotThrow(() ->
                validator.checkIfAttackHasLessCardsThanOpponentHand(opponent, attackCards));
    }

    @Test
    void shouldNotThrow_whenAttackCardsEqualToOpponentHand() {

        DurakGamePlayer opponent = mock(DurakGamePlayer.class);
        when(opponent.countCardsOnHand()).thenReturn(2);

        List<Card> attackCards = List.of(
                new Card(HEARTS, NINE),
                new Card(SPADES, NINE)
        );

        assertDoesNotThrow(() ->
                validator.checkIfAttackHasLessCardsThanOpponentHand(opponent, attackCards));
    }

    @Test
    void shouldThrow_whenAttackCardsMoreThanOpponentHand() {

        DurakGamePlayer opponent = mock(DurakGamePlayer.class);
        when(opponent.countCardsOnHand()).thenReturn(1);

        List<Card> attackCards = List.of(
                new Card(HEARTS, NINE),
                new Card(SPADES, NINE)
        );

        assertThrows(DurakGameInvalidStateException.class,
                () -> validator.checkIfAttackHasLessCardsThanOpponentHand(opponent, attackCards));
    }
}