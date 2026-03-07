package com.jrakus.game_state;

import com.jrakus.game_state.components.*;
import com.jrakus.game_state.validators.DurakGameValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DurakGame {
    private final Deck deck;
    private final DiscardPile discardPile;
    private final Table table;

    private final DurakGamePlayer player1;
    private final DurakGamePlayer player2;
    private final DurakGamePlayer startingPlayer;

    private DurakGamePlayer activePlayer;
    private DurakGamePlayer currentAttackingPlayer;
    private DurakGamePlayer currentDefendingPlayer;

    private final GameState state;
    DurakGameValidator durakGameValidator;

    public DurakGame() {
        this.deck = new Deck();
        this.discardPile = new DiscardPile();
        this.table = new Table();
        this.state = new GameState();
        this.durakGameValidator = new DurakGameValidator();

        player1 = createPlayer();
        player2 = createPlayer();

        activePlayer = chooseWhoStartsTheGame();
        startingPlayer = activePlayer;
        currentAttackingPlayer = activePlayer;
        currentDefendingPlayer = player1 == activePlayer ? player2 : player1;
    }

    public DurakGame(
            Deck deck,
            DiscardPile discardPile,
            Table table,
            GameState gameState,
            DurakGameValidator durakGameValidator,
            DurakGamePlayer durakGamePlayer1,
            DurakGamePlayer durakGamePlayer2,
            DurakGamePlayer activePlayer
    ) {
        this.deck = deck;
        this.discardPile = discardPile;
        this.table = table;
        this.state = gameState;
        this.durakGameValidator = durakGameValidator;

        player1 = durakGamePlayer1;
        player2 = durakGamePlayer2;

        this.activePlayer = activePlayer;
        startingPlayer = activePlayer;
        currentAttackingPlayer = activePlayer;
        currentDefendingPlayer = player1 == activePlayer ? player2 : player1;
    }

    private DurakGamePlayer createPlayer() {
        List<Card> cardsOnHand = deck.drawCards(6);
        return new DurakGamePlayer(cardsOnHand);
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

        table.addDefendingCards(cards, deck.showTrumpCard().suit());
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

    public List<Card> showCurrentAttackingCards() {
        return table.showAttackingCards();
    }

    public List<Card> showCurrentDefendingCards() {
        return table.showDefendingCards();
    }

    public List<Card> showActivePlayerHand() {
        return activePlayer.showCardsOnHand();
    }

    public List<Card> showPlayer1Hand() {
        return player1.showCardsOnHand();
    }

    public List<Card> showPlayer2Hand() {
        return player2.showCardsOnHand();
    }

    public List<Card> getDiscardPile() {
        return discardPile.showCardsOnPile();
    }

    public List<Card> showVisibleCardsForActivePlayer() {
        return new ArrayList<>(activePlayer.getOpponentCardsVisibleToPlayer());
    }

    public Card showTrumpCard() {
        return deck.showTrumpCard();
    }

    public int getNumberOfCardsOfInactivePlayer() {
        if(player1 == activePlayer)
            return player2.showCardsOnHand().size();

        return player1.showCardsOnHand().size();
    }

    public int getNumberOfCardsOnDeck() {
        return deck.size();
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
        durakGameValidator.checkIfAttackHasLessCardsThanOpponentHand(currentDefendingPlayer, cards);
    }

    private void drawCards() {

        final int maxNumberOfCardsOnHand = 6;

        int numberOfCards1 = currentAttackingPlayer.countCardsOnHand();
        int numberOfCards2 = currentDefendingPlayer.countCardsOnHand();

        int numberOfCardsForAttacker = Math.max(maxNumberOfCardsOnHand - numberOfCards1, 0);
        int numberOfCardsForDefender = Math.max(maxNumberOfCardsOnHand - numberOfCards2, 0);

       while (numberOfCardsForAttacker > 0 && !deck.isEmpty()) {
           currentAttackingPlayer.addCardToHand(deck.drawOneCard());
           numberOfCardsForAttacker--;
       }

       while (numberOfCardsForDefender > 0 && !deck.isEmpty()) {
           currentDefendingPlayer.addCardToHand(deck.drawOneCard());
           numberOfCardsForDefender--;
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
