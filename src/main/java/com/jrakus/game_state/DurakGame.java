package com.jrakus.game_state;

import com.jrakus.game_state.components.*;
import com.jrakus.game_state.validators.DurakGameValidator;

import java.util.List;
import java.util.Random;

public class DurakGame {
    private final Deck deck = new Deck();
    private final DiscardPile discardPile = new DiscardPile();
    private final Table table = new Table();
    private final Card trumpCard;

    private final DurakGamePlayer player1;
    private final DurakGamePlayer player2;
    private final DurakGamePlayer startingPlayer;

    private DurakGamePlayer activePlayer;
    private DurakGamePlayer currentAttackingPlayer;
    private DurakGamePlayer currentDefendingPlayer;

    private final GameState state = new GameState();
    DurakGameValidator durakGameValidator = new DurakGameValidator();

    public DurakGame() {
        List<DurakGamePlayer> bothDurakGamePlayers = createBothPlayers();

        player1 = bothDurakGamePlayers.getFirst();
        player2 = bothDurakGamePlayers.get(1);

        activePlayer = chooseWhoStartsTheGame();
        startingPlayer = activePlayer;
        trumpCard = deck.showTrumpCard();
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
        return random.nextBoolean() ? player1 : player2;
    }

    public void doAttack(List<Card> cards) {
        validateAttackMove(currentAttackingPlayer, cards);

        table.addAttackingCards(cards);
        currentAttackingPlayer.playCards(cards);

        changeVisibleCardsAfterAttack(cards);
        state.checkGameStateAfterAttack(currentAttackingPlayer, startingPlayer, player1);

        changeActivePlayer();
    }

    public void doDefend(List<Card> cards) {
        validateMove(currentDefendingPlayer, cards);

        table.addDefendingCards(cards, trumpCard.suit());
        currentDefendingPlayer.playCards(cards);

        changeVisibleCardsAfterDefend(cards);
        state.checkGameStateAfterDefend(currentDefendingPlayer, currentAttackingPlayer, player1);

        changeActivePlayer();
    }

    public void stopAttack() {
        validateMove(currentAttackingPlayer, List.of());

        List<Card> discardedCards = table.clearTable();
        discardPile.addCardsToPile(discardedCards);

        drawCards();

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    public void takeCardsFromTable() {
        validateMove(currentDefendingPlayer, List.of());

        List<Card> cardsFromTable = table.clearTable();
        currentDefendingPlayer.addCardsToHand(cardsFromTable);

        changeVisibleCardsAfterTakingCards(cardsFromTable);
        state.checkGameStateAfterTakingCards(currentAttackingPlayer, player1);

        drawCards();

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    public GameState.GameStateEnum getGameState() {
        return state.getInternalState();
    }

    public boolean isPlayer1Active() {
        return activePlayer == player1;
    }

    public boolean isPlayer1Attacking() {
        return currentAttackingPlayer == player1;
    }

    private void changeVisibleCardsAfterAttack(List<Card> cards) {
        currentDefendingPlayer.removeOpponentVisibleCards(cards);
    }

    private void changeVisibleCardsAfterDefend(List<Card> cards) {
        currentAttackingPlayer.removeOpponentVisibleCards(cards);
    }

    private void changeVisibleCardsAfterTakingCards(List<Card> cards) {
        currentDefendingPlayer.addOpponentVisibleCards(cards);
    }

    private void validateMove(DurakGamePlayer playerToCheck, List<Card> cards) {
        durakGameValidator.checkIfGameIsStillActive(state.getInternalState());
        durakGameValidator.checkIfPlayerCanPlay(playerToCheck, activePlayer);
        durakGameValidator.checkIfPlayerHasCardsThatHePlays(playerToCheck, cards);
    }

    private void validateAttackMove(DurakGamePlayer playerToCheck, List<Card> cards) {
        validateMove(playerToCheck, cards);
        durakGameValidator.checkIfAttackHasCorrectAmountOfCards(currentDefendingPlayer, cards);
    }

    private void drawCards() {

        final int maxNumberOfCardsOnHand = 6;

        int numberOfCards1 = currentAttackingPlayer.countCardsOnHand();
        int numberOfCards2 = currentDefendingPlayer.countCardsOnHand();

        int numberOfCardsForAttacker = Math.max(maxNumberOfCardsOnHand - numberOfCards1, 0);
        int numberOfCardsForDefender = Math.max(maxNumberOfCardsOnHand - numberOfCards2, 0);

       while (numberOfCardsForAttacker < maxNumberOfCardsOnHand && !deck.isEmpty()) {
           currentAttackingPlayer.addCardToHand(deck.drawOneCard());
       }

       while (numberOfCardsForDefender < maxNumberOfCardsOnHand && !deck.isEmpty()) {
           currentDefendingPlayer.addCardToHand(deck.drawOneCard());
       }
    }

    private void changeActivePlayer () {
        boolean isPlayer1Active = activePlayer == player1;
        activePlayer = isPlayer1Active ? player2 : player1;
    }

    private void switchAttackerWithDefender() {
        DurakGamePlayer temp = currentAttackingPlayer;
        currentAttackingPlayer = currentDefendingPlayer;
        currentDefendingPlayer = temp;
    }
}
