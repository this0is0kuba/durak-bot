package com.jrakus.game_state;

import com.jrakus.game_state.components.*;
import com.jrakus.game_state.validators.DurakGameValidator;

import java.util.List;
import java.util.Random;

public class DurakGame {
    private final Deck deck = new Deck();
    private final DiscardPile discardPile = new DiscardPile();
    private final Table table = new Table();
    private final Card.Suit trump;

    private final DurakGamePlayer durakGamePlayer1;
    private final DurakGamePlayer durakGamePlayer2;
    private final DurakGamePlayer startingPlayer;

    private DurakGamePlayer activePlayer;
    private DurakGamePlayer currentAttackingPlayer;
    private DurakGamePlayer currentDefendingPlayer;

    private GameState state;
    DurakGameValidator durakGameValidator = new DurakGameValidator();

    public DurakGame() {
        List<DurakGamePlayer> bothDurakGamePlayers = createBothPlayers();

        durakGamePlayer1 = bothDurakGamePlayers.getFirst();
        durakGamePlayer2 = bothDurakGamePlayers.get(1);

        activePlayer = chooseWhoStartsTheGame();
        startingPlayer = activePlayer;
        trump = deck.drawOneCard().suit();
    }

    private List<DurakGamePlayer> createBothPlayers() {
        List<Card> cardsOnHand1 = deck.drawCards(6);
        List<Card> cardsOnHand2 = deck.drawCards(6);

        DurakGamePlayer durakGamePlayer1 = new DurakGamePlayer(cardsOnHand1);
        DurakGamePlayer durakGamePlayer2 = new DurakGamePlayer(cardsOnHand2);

        return List.of(durakGamePlayer1, durakGamePlayer2);
    }

    private DurakGamePlayer chooseWhoStartsTheGame() {
        // TODO: choose based on cards instead of choosing randomly
        Random random = new Random();
        return random.nextBoolean() ? durakGamePlayer1 : durakGamePlayer2;
    }

    public void attack(List<Card> cards) {
        checkPlayerBeforeMove(currentAttackingPlayer, cards);

        table.addAttackingCards(cards);
        currentAttackingPlayer.playCards(cards);

        state.checkGameStateAfterAttack(currentAttackingPlayer, startingPlayer, durakGamePlayer1);

        changeActivePlayer();
    }

    public void defend(List<Card> cards) {
        checkPlayerBeforeMove(currentDefendingPlayer, cards);

        table.addDefendingCards(cards, trump);
        currentDefendingPlayer.playCards(cards);

        state.checkGameStateAfterDefend(currentDefendingPlayer, currentAttackingPlayer, durakGamePlayer1);

        changeActivePlayer();
    }

    public void stopAttack() {
        checkPlayerBeforeMove(currentAttackingPlayer, List.of());

        List<Card> discardedCards = table.clearTable();
        discardPile.addCardsToPile(discardedCards);

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    public void takeCardsFromTable() {
        checkPlayerBeforeMove(currentDefendingPlayer, List.of());

        List<Card> cardsFromTable = table.clearTable();
        currentDefendingPlayer.addCardToHand(cardsFromTable);

        state.checkGameStateAfterTakingCards(currentAttackingPlayer, durakGamePlayer1);

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    public GameState.GameStateEnum getGameState() {
        return state.getInternalState();
    }

    public DurakGamePlayer getActivePlayer() {
        return activePlayer;
    }

    public DurakGamePlayer getStartingPlayer() {
        return startingPlayer;
    }

    public DurakGamePlayer getCurrentAttackingPlayer() {
        return currentAttackingPlayer;
    }

    private void checkPlayerBeforeMove(DurakGamePlayer playerToCheck, List<Card> cards) {
        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkIfPlayerCanPlay(playerToCheck, activePlayer);
        durakGameValidator.checkIfPlayerHasCardsThatHePlays(playerToCheck, cards);
    }

    private void changeActivePlayer () {
        boolean isPlayer1Active = activePlayer == durakGamePlayer1;
        activePlayer = isPlayer1Active ? durakGamePlayer2 : durakGamePlayer1;
    }

    private void switchAttackerWithDefender() {
        DurakGamePlayer temp = currentAttackingPlayer;
        currentAttackingPlayer = currentDefendingPlayer;
        currentDefendingPlayer = temp;
    }
}
