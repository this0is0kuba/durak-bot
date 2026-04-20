package com.jrakus.playground;

import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;
import com.jrakus.players.game_elements.GameInfo;
import com.jrakus.players.game_elements.PublicState;
import com.jrakus.players.game_elements.Move;
import com.jrakus.players.Player;
import com.jrakus.game_state.DurakGame;
import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.GameState;
import com.jrakus.players.game_elements.utils.PublicStateExtractor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GameRoom {
    private final Player player1;
    private final Player player2;

    private final DurakGame durakGame;
    private final PublicStateExtractor publicStateExtractor;

    public GameRoom(Player player1, Player player2) {
        this(player1, player2, new DurakGame(), new PublicStateExtractor());
    }

    public GameRoom(Player player1, Player player2, DurakGame durakGame) {
        this.player1 = player1;
        this.player2 = player2;
        this.durakGame = durakGame;
        this.publicStateExtractor = new PublicStateExtractor();
    }

    public GameRoom(Player player1, Player player2, DurakGame durakGame, PublicStateExtractor publicStateExtractor) {
        this.player1 = player1;
        this.player2 = player2;
        this.durakGame = durakGame;
        this.publicStateExtractor = publicStateExtractor;
    }

    public Optional<Player> startGame() {

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

        return getWinner();
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

            default -> throw new DurakGameInvalidStateException(
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

            default -> throw new DurakGameInvalidStateException(
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

    private Optional<Player> getWinner() {
        GameState.GameStateEnum gameStateEnum = durakGame.getGameState();

        return switch (gameStateEnum) {
            case ACTIVE_GAME, DRAW -> Optional.empty();
            case PLAYER_1_WON -> Optional.of(player1);
            case PLAYER_2_WON -> Optional.of(player2);
        };
    }

    private PublicState getPublicState(Player player) {
        return publicStateExtractor.extractPublicState(durakGame, player1 == player);
    }
}
