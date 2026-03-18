package com.jrakus;

import com.jrakus.players.Player;
import com.jrakus.players.bots.TrivialBot;
import com.jrakus.players.users.LocalUser;
import com.jrakus.players.users.RemoteUser;
import com.jrakus.playground.GameRoom;
import com.jrakus.playground.displays.TerminalPrinter;

public class Main {
    static void main() {

        Player player1 = new TrivialBot();
        Player player2 = new LocalUser(new TerminalPrinter());

        GameRoom game = new GameRoom(player1, player2);

        System.out.println("The game has started");
        game.startGame();

        RemoteUser.closeSocketConnection();
    }
}
