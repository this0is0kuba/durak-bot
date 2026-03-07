package com.jrakus;

import com.jrakus.game_elements.Player;
import com.jrakus.players.LocalUser;
import com.jrakus.playground.GameRoom;
import com.jrakus.playground.displays.TerminalPrinter;

public class Main {
    static void main() {

        Player player1 = new LocalUser(new TerminalPrinter());
        Player player2 = new LocalUser(new TerminalPrinter());

        GameRoom game = new GameRoom(player1, player2);

        game.startGame();
    }
}
