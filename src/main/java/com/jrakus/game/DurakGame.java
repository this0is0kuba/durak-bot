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

    private final Player player1;
    private final Player player2;
    private final Player startingPlayer;

    private Player activePlayer;
    private Player currentAttackingPlayer;
    private Player currentDefendingPlayer;

    private GameState state;
    DurakGameValidator durakGameValidator = new DurakGameValidator();

    public DurakGame() {
        List<Player> bothPlayers = createBothPlayers();

        player1 = bothPlayers.getFirst();
        player2 = bothPlayers.get(1);

        activePlayer = chooseWhoStartsTheGame();
        startingPlayer = activePlayer;
        trump = deck.drawOneCard().suit();
    }

    private List<Player> createBothPlayers() {
        List<Card> cardsOnHand1 = deck.drawCards(6);
        List<Card> cardsOnHand2 = deck.drawCards(6);

        Player player1 = new Player(cardsOnHand1);
        Player player2 = new Player(cardsOnHand2);

        return List.of(player1, player2);
    }

    private Player chooseWhoStartsTheGame() {
        // TODO: choose based on cards instead of choosing randomly
        Random random = new Random();
        return random.nextBoolean() ? player1 : player2;
    }

    public void attack(Player attackingPlayer, List<Card> cards) {

        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkAttacker(attackingPlayer, activePlayer, currentAttackingPlayer);
        durakGameValidator.checkIfPlayerHasCardsThatHePlays(attackingPlayer, cards);

        table.addAttackingCards(cards);
        attackingPlayer.playCards(cards);

        state.checkGameStateAfterAttack(currentAttackingPlayer, startingPlayer, player1);

        changeActivePlayer();
    }

    public void defend(Player defendingPlayer, List<Card> cards) {

        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkDefender(defendingPlayer, activePlayer, currentDefendingPlayer);
        durakGameValidator.checkIfPlayerHasCardsThatHePlays(defendingPlayer, cards);

        table.addDefendingCards(cards, trump);
        defendingPlayer.playCards(cards);

        state.checkGameStateAfterDefend(currentDefendingPlayer, currentAttackingPlayer, player1);

        changeActivePlayer();
    }

    public void stopAttack(Player attackingPlayer) {

        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkAttacker(attackingPlayer, activePlayer, currentAttackingPlayer);

        List<Card> discardedCards = table.clearTable();
        discardPile.addCardsToPile(discardedCards);

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    public void takeCardsFromTable(Player defendingPlayer) {

        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkDefender(defendingPlayer, activePlayer, currentAttackingPlayer);

        List<Card> cardsFromTable = table.clearTable();
        defendingPlayer.addCardToHand(cardsFromTable);

        state.checkGameStateAfterTakingCards(currentAttackingPlayer, player1);

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    private void changeActivePlayer () {
        boolean isPlayer1Active = activePlayer == player1;
        activePlayer = isPlayer1Active ? player2 : player1;
    }

    private void switchAttackerWithDefender() {
        Player temp = currentAttackingPlayer;
        currentAttackingPlayer = currentDefendingPlayer;
        currentDefendingPlayer = temp;
    }
}
