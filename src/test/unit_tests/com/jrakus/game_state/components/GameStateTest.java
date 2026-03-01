package com.jrakus.game_state.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameStateTest {

    private GameState gameState;
    private DurakGamePlayer player1;
    private DurakGamePlayer player2;

    @BeforeEach
    void setUp() {
        gameState = new GameState();

        Card c1 = new Card(Card.Suit.HEARTS, Card.Rank.NINE);
        Card c2 = new Card(Card.Suit.SPADES, Card.Rank.JACK);

        player1 = new DurakGamePlayer(new ArrayList<>(List.of(c1)));
        player2 = new DurakGamePlayer(new ArrayList<>(List.of(c2)));
    }

    @Test
    void initialStateShouldBeActiveGame() {
        assertEquals(GameState.GameStateEnum.ACTIVE_GAME, gameState.getInternalState());
    }

    // ============================
    // Tests for checkGameStateAfterTakingCards
    // ============================

    @Test
    void checkGameStateAfterTakingCards_Player1HandEmpty_Player1Wins() {
        player1.playCards(player1.showCardsOnHand());
        gameState.checkGameStateAfterTakingCards(player1, player1);

        assertEquals(GameState.GameStateEnum.PLAYER_1_WON, gameState.getInternalState());
    }

    @Test
    void checkGameStateAfterTakingCards_Player2HandEmpty_Player2Wins() {
        player2.playCards(player2.showCardsOnHand());
        gameState.checkGameStateAfterTakingCards(player2, player1);

        assertEquals(GameState.GameStateEnum.PLAYER_2_WON, gameState.getInternalState());
    }

    @Test
    void checkGameStateAfterTakingCards_HandNotEmpty_StateRemainsActive() {
        gameState.checkGameStateAfterTakingCards(player1, player1);

        assertEquals(GameState.GameStateEnum.ACTIVE_GAME, gameState.getInternalState());
    }

    // ============================
    // Tests for checkGameStateAfterAttack
    // ============================

    @Test
    void checkGameStateAfterAttack_AttackingPlayerNotStarterAndHandEmpty_Player1Wins() {
        player1.playCards(player1.showCardsOnHand());
        gameState.checkGameStateAfterAttack(player1, player2, player1);

        assertEquals(GameState.GameStateEnum.PLAYER_1_WON, gameState.getInternalState());
    }

    @Test
    void checkGameStateAfterAttack_AttackingPlayerNotStarterAndHandEmpty_Player2Wins() {
        player2.playCards(player2.showCardsOnHand());
        gameState.checkGameStateAfterAttack(player2, player1, player1);

        assertEquals(GameState.GameStateEnum.PLAYER_2_WON, gameState.getInternalState());
    }

    @Test
    void checkGameStateAfterAttack_AttackingPlayerIsStarter_HandEmpty_StateRemainsActive() {
        player1.playCards(player1.showCardsOnHand());
        gameState.checkGameStateAfterAttack(player1, player1, player1);

        assertEquals(GameState.GameStateEnum.ACTIVE_GAME, gameState.getInternalState());
    }

    // ============================
    // Tests for checkGameStateAfterDefend
    // ============================

    @Test
    void checkGameStateAfterDefend_BothHandsEmpty_Draw() {
        player1.playCards(player1.showCardsOnHand());
        player2.playCards(player2.showCardsOnHand());

        gameState.checkGameStateAfterDefend(player2, player1, player1);

        assertEquals(GameState.GameStateEnum.DRAW, gameState.getInternalState());
    }

    @Test
    void checkGameStateAfterDefend_AttackingPlayerEmpty_Player1Wins() {
        player1.playCards(player1.showCardsOnHand());
        gameState.checkGameStateAfterDefend(player2, player1, player1);

        assertEquals(GameState.GameStateEnum.PLAYER_1_WON, gameState.getInternalState());
    }

    @Test
    void checkGameStateAfterDefend_AttackingPlayerEmpty_Player2Wins() {
        player2.playCards(player2.showCardsOnHand());
        gameState.checkGameStateAfterDefend(player1, player2, player1);

        assertEquals(GameState.GameStateEnum.PLAYER_2_WON, gameState.getInternalState());
    }

    @Test
    void checkGameStateAfterDefend_NoEmptyHands_StateRemainsActive() {
        gameState.checkGameStateAfterDefend(player1, player2, player1);

        assertEquals(GameState.GameStateEnum.ACTIVE_GAME, gameState.getInternalState());
    }
}