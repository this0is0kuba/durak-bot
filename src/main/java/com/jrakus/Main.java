package com.jrakus;

import com.jrakus.game_elements.Player;
import com.jrakus.players.LocalUser;
import com.jrakus.playground.GameRoom;

public class Main {
    static void main() {

        Player player1 = new LocalUser();
        Player player2 = new LocalUser();

        GameRoom game = new GameRoom(player1, player2);

        game.startGame();
    }
}
