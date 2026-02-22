package com.jrakus.game;

import com.jrakus.game.components.*;
import com.jrakus.game.validators.DurakGameValidator;

import java.util.List;
import java.util.Random;

public class DurakGame {
    private final Deck deck = new Deck();
    private final DiscardPile discardPile = new DiscardPile();
    private final Table table = new Table();
    private final Card.Suit trump;

    private final DurakGamePlayer durakGamePlayer1;
    private final DurakGamePlayer durakGamePlayer2;
    private final DurakGamePlayer startingDurakGamePlayer;

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
        startingDurakGamePlayer = activePlayer;
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

    public void attack(DurakGamePlayer attackingDurakGamePlayer, List<Card> cards) {

        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkAttacker(attackingDurakGamePlayer, activePlayer, currentAttackingPlayer);
        durakGameValidator.checkIfPlayerHasCardsThatHePlays(attackingDurakGamePlayer, cards);

        table.addAttackingCards(cards);
        attackingDurakGamePlayer.playCards(cards);

        state.checkGameStateAfterAttack(currentAttackingPlayer, startingDurakGamePlayer, durakGamePlayer1);

        changeActivePlayer();
    }

    public void defend(DurakGamePlayer defendingDurakGamePlayer, List<Card> cards) {

        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkDefender(defendingDurakGamePlayer, activePlayer, currentDefendingPlayer);
        durakGameValidator.checkIfPlayerHasCardsThatHePlays(defendingDurakGamePlayer, cards);

        table.addDefendingCards(cards, trump);
        defendingDurakGamePlayer.playCards(cards);

        state.checkGameStateAfterDefend(currentDefendingPlayer, currentAttackingPlayer, durakGamePlayer1);

        changeActivePlayer();
    }

    public void stopAttack(DurakGamePlayer attackingDurakGamePlayer) {

        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkAttacker(attackingDurakGamePlayer, activePlayer, currentAttackingPlayer);

        List<Card> discardedCards = table.clearTable();
        discardPile.addCardsToPile(discardedCards);

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    public void takeCardsFromTable(DurakGamePlayer defendingDurakGamePlayer) {

        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkDefender(defendingDurakGamePlayer, activePlayer, currentAttackingPlayer);

        List<Card> cardsFromTable = table.clearTable();
        defendingDurakGamePlayer.addCardToHand(cardsFromTable);

        state.checkGameStateAfterTakingCards(currentAttackingPlayer, durakGamePlayer1);

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    public GameState.GameStateEnum getGameState() {
        return state.getInternalState();
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
