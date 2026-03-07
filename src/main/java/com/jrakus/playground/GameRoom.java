package com.jrakus.playground;

import com.jrakus.game_elements.PublicState;
import com.jrakus.game_elements.Move;
import com.jrakus.game_elements.Player;
import com.jrakus.game_state.DurakGame;
import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.GameState;
import com.jrakus.playground.exceptions.DurakGameInvalidMoveException;

import static com.jrakus.game_elements.Move.*;
import static com.jrakus.game_elements.Move.MoveKind.*;

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
            PublicState publicState = getPublicState();

            updateGameStateForPlayers();

            if (activePlayer == attackingPlayer) {
              attackMove(activePlayer, publicState);

            } else {
               defendMove(activePlayer, publicState);
            }
        }
    }

    private void updateGameStateForPlayers() {

        Player inactivePlayer = getInactivePlayer();
        Player activePlayer = getActivePlayer();
        Player attackingPlayer = getAttackingPlayer();

        PublicState publicState = getPublicState();

        MoveKind currentStateForActivePlayer = activePlayer == attackingPlayer  ? ATTACK : DEFEND;
        MoveKind currentStateForInactivePlayer = inactivePlayer == attackingPlayer ? ATTACK : DEFEND;

        inactivePlayer.displayCurrentState(publicState, currentStateForInactivePlayer, false);
        activePlayer.displayCurrentState(publicState, currentStateForActivePlayer, true);
    }

    private void attackMove(Player activePlayer, PublicState publicState) {
        Move selectedMove = activePlayer.attack(publicState);

        Move.MoveKind kind = selectedMove.getMoveKind();
        List<Card> cards = selectedMove.getCards();

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

        Move.MoveKind kind = selectedMove.getMoveKind();
        List<Card> cards = selectedMove.getCards();

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

    private PublicState getPublicState() {

        List<Card> attackingCards = durakGame.showCurrentAttackingCards();
        List<Card> defendingCards = durakGame.showCurrentDefendingCards();
        List<Card> activePlayerHand = durakGame.showActivePlayerHand();
        List<Card> discardPile = durakGame.getDiscardPile();
        List<Card> visibleCardsForActivePlayer = durakGame.showVisibleCardsForActivePlayer();
        Card trumpCard = durakGame.showTrumpCard();
        int numberOfCardsOnOpponentHand = durakGame.getNumberOfCardsOfInactivePlayer();
        int numberOfCardsOnDeck = durakGame.getNumberOfCardsOnDeck();

        return new PublicState.PublicStateBuilder()
                .attackingCards(attackingCards)
                .defendingCards(defendingCards)
                .yourHand(activePlayerHand)
                .discardPile(discardPile)
                .certainOpponentHand(visibleCardsForActivePlayer)
                .trumpCard(trumpCard)
                .numberOfCardsOnOpponentHand(numberOfCardsOnOpponentHand)
                .numberOfCardsOnDeck(numberOfCardsOnDeck)
                .build();
    }
}
