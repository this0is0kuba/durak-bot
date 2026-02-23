package com.jrakus.playground;

import com.jrakus.game_logic.Move;
import com.jrakus.game_logic.Player;
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

            if (activePlayer == attackingPlayer) {
              attackMove(activePlayer);

            } else {
               defendMove(activePlayer);
            }
        }
    }

    private void attackMove(Player activePlayer) {
        Move selectedMove = activePlayer.attack();

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

    private void defendMove(Player activePlayer) {
        Move selectedMove = activePlayer.defend();

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

    private Player getAttackingPlayer() {
        return durakGame.isPlayer1Attacking() ? player1 : player2;
    }
}
