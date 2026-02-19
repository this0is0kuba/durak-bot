package com.jrakus.game;

import com.jrakus.game.components.*;

import java.util.HashSet;
import java.util.List;

public class DurakGame {
    private final Deck deck = new Deck();
    private final DiscardPile discardPile = new DiscardPile();
    private final Table table = new Table();

    private final Player player1;
    private final Player player2;

    private final PlayerIdentifier activePlayer;
    private final GameState state;
    private final Card.Suit trump;

    public enum PlayerIdentifier {PLAYER_1, PLAYER_2}

    public enum GameState {
        PLAYER_1_ATTACKS,
        PLAYER_2_ATTACKS,
        PLAYER_1_WON,
        PLAYER_2_WON,
    }

    public DurakGame() {
        List<Player> bothPlayers = createBothPlayers();

        player1 = bothPlayers.getFirst();
        player2 = bothPlayers.get(1);

        activePlayer = chooseWhoStartTheGame();
        state = activePlayer == PlayerIdentifier.PLAYER_1 ? GameState.PLAYER_1_ATTACKS : GameState.PLAYER_2_ATTACKS;
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

    private PlayerIdentifier chooseWhoStartTheGame() {
        // TODO: choose based on cards
        return PlayerIdentifier.PLAYER_1;
    }

    public void attack(Player attackingPlayer, List<Card> cards) {

        checkAttacker(attackingPlayer);
        checkIfPlayerHasCardsThatHePlays(attackingPlayer, cards);

        table.addAttackingCards(cards);
        attackingPlayer.playCards(cards);
    }

    public void defend(Player defendingPlayer, List<Card> cards) {

        checkDefender(defendingPlayer);
        checkIfPlayerHasCardsThatHePlays(defendingPlayer, cards);

        table.addDefendingCards(cards, trump);
        defendingPlayer.playCards(cards);
    }

    public void stopAttack(Player attackingPlayer) {
        // TODO
    }

    public void takeCardsFromTable(Player defendingPlayer) {
        // TODO
    }

    private void checkAttacker(Player attackingPlayer) {
        PlayerIdentifier playerIdentifier = findPlayerIdentifier(attackingPlayer);
        PlayerIdentifier activeAttacker = findPlayerWhoAttacks();

        if(playerIdentifier != activePlayer)
            throw new RuntimeException(String.format("This is not turn for player: %s", attackingPlayer));

        if(playerIdentifier != activeAttacker)
            throw new RuntimeException(String.format("Player %s does not attack now", attackingPlayer));
    }

    private void checkDefender(Player defendingPlayer) {
        PlayerIdentifier playerIdentifier = findPlayerIdentifier(defendingPlayer);
        PlayerIdentifier activeDefender = findPlayerWhoDefends();

        if(playerIdentifier != activePlayer)
            throw new RuntimeException(String.format("This is not turn for player: %s", defendingPlayer));

        if(playerIdentifier != activeDefender)
            throw new RuntimeException(String.format("Player %s does not defend now", defendingPlayer));
    }

    private void checkIfPlayerHasCardsThatHePlays(Player player, List<Card> cards) {
        List<Card> cardsOnHand = player.showCardsOnHand();

        if(!new HashSet<>(cardsOnHand).containsAll(cards)) {
            throw new RuntimeException(String.format("Player %s does not have cards that he wants to play", player));
        }
    }

    private PlayerIdentifier findPlayerIdentifier(Player player) {

        if(player == player1)
            return PlayerIdentifier.PLAYER_1;
        else if(player == player2)
            return PlayerIdentifier.PLAYER_2;
        else
            throw new RuntimeException("This player is not active player.");
    }

    private PlayerIdentifier findPlayerWhoAttacks() {
        if(this.state == GameState.PLAYER_1_ATTACKS)
            return PlayerIdentifier.PLAYER_1;
        else if(this.state == GameState.PLAYER_2_ATTACKS)
            return PlayerIdentifier.PLAYER_2;
        else
            throw new RuntimeException("The game has already finished");
    }

    private PlayerIdentifier findPlayerWhoDefends() {
        if(this.state == GameState.PLAYER_1_ATTACKS)
            return PlayerIdentifier.PLAYER_2;
        else if(this.state == GameState.PLAYER_2_ATTACKS)
            return PlayerIdentifier.PLAYER_1;
        else
            throw new RuntimeException("The game has already finished");
    }
}
