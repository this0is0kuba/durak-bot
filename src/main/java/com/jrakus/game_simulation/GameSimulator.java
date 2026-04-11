package com.jrakus.game_simulation;

import com.jrakus.game_state.DurakGame;
import com.jrakus.players.Player;
import com.jrakus.players.bots.RandomBot;
import com.jrakus.playground.GameRoom;

import java.util.Optional;

public class GameSimulator {

    public enum Result {
        PLAYER_1_WON, PLAYER_2_WON, DRAW
    }

    public Result runRandomSimulation(DurakGame durakGame) {

        Player player1 = new RandomBot();
        Player player2 = new RandomBot();

        GameRoom game = new GameRoom(player1, player2, durakGame);
        Optional<Player> optionalWinner = game.startGame();

        if (optionalWinner.isPresent()) {
            if (player1 == optionalWinner.get()) {
                return Result.PLAYER_1_WON;
            } else {
                return Result.PLAYER_2_WON;
            }
        } else {
            return Result.DRAW;
        }
    }
}
