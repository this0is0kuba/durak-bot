package com.jrakus.game;

import com.jrakus.game.components.*;

import java.util.HashSet;
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

    private GameState state = GameState.ACTIVE_GAME;

    public enum GameState {
        ACTIVE_GAME,
        PLAYER_1_WON,
        PLAYER_2_WON,
        DRAW
    }

    public DurakGame() {
        List<Player> bothPlayers = createBothPlayers();

        player1 = bothPlayers.getFirst();
        player2 = bothPlayers.get(1);

        activePlayer = chooseWhoStartsTheGame();
        startingPlayer = activePlayer;
        trump = deck.drawOneCard().suit();
    }

    private List<Player> createBothPlayers() {
        List<Card> handCards1 = deck.drawCards(6);
        List<Card> handCards2 = deck.drawCards(6);

        Hand hand1 = new Hand(handCards1);
        Hand hand2 = new Hand(handCards2);

        Player player1 = new Player("player1", hand1);
        Player player2 = new Player("player2", hand2);

        return List.of(player1, player2);
    }

    private Player chooseWhoStartsTheGame() {
        // TODO: choose based on cards instead of choosing randomly
        Random random = new Random();
        return random.nextBoolean() ? player1 : player2;
    }

    public void attack(Player attackingPlayer, List<Card> cards) {

        checkAttacker(attackingPlayer);
        checkIfPlayerHasCardsThatHePlays(attackingPlayer, cards);

        table.addAttackingCards(cards);
        attackingPlayer.playCards(cards);

        checkGameStateAfterAttack();

        changeActivePlayer();
    }

    public void defend(Player defendingPlayer, List<Card> cards) {

        checkDefender(defendingPlayer);
        checkIfPlayerHasCardsThatHePlays(defendingPlayer, cards);

        table.addDefendingCards(cards, trump);
        defendingPlayer.playCards(cards);

        checkGameStateAfterDefend();

        changeActivePlayer();
    }

    public void stopAttack(Player attackingPlayer) {

        checkAttacker(attackingPlayer);

        List<Card> discardedCards = table.clearTable();
        discardPile.addCardsToPile(discardedCards);

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    public void takeCardsFromTable(Player defendingPlayer) {

        checkDefender(defendingPlayer);

        List<Card> cardsFromTable = table.clearTable();
        defendingPlayer.addCardToHand(cardsFromTable);

        checkGameStateAfterTakingCards();

        changeActivePlayer();
        switchAttackerWithDefender();
    }

    private void checkAttacker(Player attackingPlayer) {
        if(attackingPlayer != activePlayer)
            throw new RuntimeException(String.format("This is not turn for player: %s", attackingPlayer));

        if(attackingPlayer != currentAttackingPlayer)
            throw new RuntimeException(String.format("Player %s does not attack now", attackingPlayer));
    }

    private void checkDefender(Player defendingPlayer) {
        if(defendingPlayer != activePlayer)
            throw new RuntimeException(String.format("This is not turn for player: %s", defendingPlayer));

        if(defendingPlayer != currentDefendingPlayer)
            throw new RuntimeException(String.format("Player %s does not defend now", defendingPlayer));
    }

    private void checkIfPlayerHasCardsThatHePlays(Player player, List<Card> cards) {
        List<Card> cardsOnHand = player.showCardsOnHand();

        if(!new HashSet<>(cardsOnHand).containsAll(cards)) {
            throw new RuntimeException(String.format("Player %s does not have cards that he wants to play", player));
        }
    }

    private void changeActivePlayer () {
        boolean isPlayer1Active = activePlayer == player1;
        activePlayer = isPlayer1Active ? player2 : player1;
    }

    private void checkGameStateAfterTakingCards() {
        boolean isTheEndOfTheGame = currentAttackingPlayer.showCardsOnHand().isEmpty();

        if(isTheEndOfTheGame) {
            if (currentAttackingPlayer == player1) {
                state = GameState.PLAYER_1_WON;
            } else {
                state = GameState.PLAYER_2_WON;
            }
        }
    }

    private void checkGameStateAfterAttack() {
        boolean attackerStartedTheGame = (currentAttackingPlayer == startingPlayer);
        boolean attackingPlayerHasNoCards = currentAttackingPlayer.showCardsOnHand().isEmpty();

        if(attackingPlayerHasNoCards && !attackerStartedTheGame) {
            if (currentAttackingPlayer == player1) {
                state = GameState.PLAYER_1_WON;
            } else {
                state = GameState.PLAYER_2_WON;
            }
        }
    }

    private void checkGameStateAfterDefend() {
        boolean attackingPlayerHasNoCards = currentDefendingPlayer.showCardsOnHand().isEmpty();
        boolean defendingPlayerHasNoCards = currentDefendingPlayer.showCardsOnHand().isEmpty();

        if (attackingPlayerHasNoCards && defendingPlayerHasNoCards) {
            state = GameState.DRAW;
        }

        if(attackingPlayerHasNoCards) {
            if (currentAttackingPlayer == player1) {
                state = GameState.PLAYER_1_WON;
            } else {
                state = GameState.PLAYER_2_WON;
            }
        }
    }

    private void switchAttackerWithDefender() {
        Player temp = currentAttackingPlayer;
        currentAttackingPlayer = currentDefendingPlayer;
        currentDefendingPlayer = temp;
    }
}
