package com.jrakus;

import com.jrakus.game_elements.Player;
import com.jrakus.players.RemoteUser;
import com.jrakus.playground.GameRoom;


import java.util.List;

public class Main {
    static void main() {

        List<RemoteUser> remoteUsers = RemoteUser.createTwoRemoteUsers(5000);
        Player player1 = remoteUsers.getFirst();
        Player player2 = remoteUsers.getLast();

        GameRoom game = new GameRoom(player1, player2);

        System.out.println("The game has started");
        game.startGame();

        RemoteUser.closeSocketConnection();
    }
}
