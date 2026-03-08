package com.jrakus.playground;

import com.jrakus.game_elements.GameInfo;
import com.jrakus.game_elements.PublicState;
import com.jrakus.game_elements.Move;
import com.jrakus.game_elements.Player;
import com.jrakus.game_state.DurakGame;
import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.GameState;
import com.jrakus.playground.exceptions.DurakGameInvalidMoveException;

import java.util.List;

public class GameRoom {
    private final Player player1;
    private final Player player2;

    private final DurakGame durakGame = new DurakGame();

    public GameRoom(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void startGame() {

        while(durakGame.getGameState() == GameState.GameStateEnum.ACTIVE_GAME) {

            Player activePlayer = getActivePlayer();
            Player attackingPlayer = getAttackingPlayer();
            PublicState publicState = getPublicState(activePlayer);

            updateGameStateForPlayers();

            if (activePlayer == attackingPlayer) {
              attackMove(activePlayer, publicState);

            } else {
               defendMove(activePlayer, publicState);
            }
        }

        updateGameStateForPlayers();
    }

    private void updateGameStateForPlayers() {

        Player inactivePlayer = getInactivePlayer();
        Player activePlayer = getActivePlayer();

        PublicState publicStateForActivePlayer = getPublicState(activePlayer);
        PublicState publicStateForInactivePlayer = getPublicState(inactivePlayer);

        inactivePlayer.displayCurrentState(publicStateForInactivePlayer);
        activePlayer.displayCurrentState(publicStateForActivePlayer);
    }

    private void attackMove(Player activePlayer, PublicState publicState) {
        Move selectedMove = activePlayer.attack(publicState);

        Move.MoveKind kind = selectedMove.moveKind();
        List<Card> cards = selectedMove.cards();

        switch (kind) {

            case ATTACK -> durakGame.doAttack(cards);
            case STOP_ATTACK -> durakGame.stopAttack();

            default -> throw new DurakGameInvalidMoveException(
                    String.format("Move Kind: %s is not allowed now", kind)
            );
        }
    }

    private void defendMove(Player activePlayer, PublicState publicState) {
        Move selectedMove = activePlayer.defend(publicState);

        Move.MoveKind kind = selectedMove.moveKind();
        List<Card> cards = selectedMove.cards();

        switch (kind) {

            case DEFEND -> durakGame.doDefend(cards);
            case TAKE_CARDS -> durakGame.takeCardsFromTable();

            default -> throw new DurakGameInvalidMoveException(
                    String.format("Move Kind: %s is not allowed now", kind)
            );
        }
    }

    private Player getActivePlayer() {
        return durakGame.isPlayer1Active() ? player1 : player2;
    }

    private Player getInactivePlayer() {
        return durakGame.isPlayer1Active() ? player2 : player1;
    }

    private Player getAttackingPlayer() {
        return durakGame.isPlayer1Attacking() ? player1 : player2;
    }

    private PublicState getPublicState(Player player) {

        List<Card> attackingCards = durakGame.showCurrentAttackingCards();
        List<Card> defendingCards = durakGame.showCurrentDefendingCards();

        List<Card> playerHand = player == getActivePlayer() ?
                durakGame.showActivePlayerHand() : durakGame.showInactivePlayerHand();

        List<Card> visibleCardsForPlayer = player == getActivePlayer() ?
                durakGame.showVisibleCardsForActivePlayer() : durakGame.showVisibleCardsForInactivePlayer();

        int numberOfCardsOnOpponentHand = player == getActivePlayer() ?
                durakGame.getNumberOfCardsOfInactivePlayer() : durakGame.getNumberOfCardsOfActivePlayer();

        List<Card> discardPile = durakGame.getDiscardPile();
        Card trumpCard = durakGame.showTrumpCard();
        int numberOfCardsOnDeck = durakGame.getNumberOfCardsOnDeck();

        GameState.GameStateEnum gameStateEnum = durakGame.getGameState();

        GameInfo gameInfo = switch (gameStateEnum) {
            case ACTIVE_GAME -> GameInfo.ACTIVE_GAME;
            case PLAYER_1_WON -> player1 == player ? GameInfo.YOU_WON : GameInfo.OPPONENT_WON;
            case PLAYER_2_WON -> player2 == player ? GameInfo.YOU_WON : GameInfo.OPPONENT_WON;
            case DRAW -> GameInfo.DRAW;
        };

        return new PublicState.PublicStateBuilder()
                .attackingCards(attackingCards)
                .defendingCards(defendingCards)
                .yourHand(playerHand)
                .discardPile(discardPile)
                .certainOpponentHand(visibleCardsForPlayer)
                .trumpCard(trumpCard)
                .numberOfCardsOnOpponentHand(numberOfCardsOnOpponentHand)
                .numberOfCardsOnDeck(numberOfCardsOnDeck)
                .gameInfo(gameInfo)
                .build();
    }
}
