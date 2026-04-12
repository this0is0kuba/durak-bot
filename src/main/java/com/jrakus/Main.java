package com.jrakus;

import com.jrakus.players.Player;
import com.jrakus.players.bots.RandomBot;
import com.jrakus.players.bots.SimpleMonteCarloBot;
import com.jrakus.players.bots.TrivialBot;
import com.jrakus.players.users.LocalUser;
import com.jrakus.playground.GameRoom;
import com.jrakus.playground.displays.TerminalPrinter;
import com.jrakus.playground.tournament.ConcurrentTournament;
import com.jrakus.playground.tournament.Tournament;

import java.util.Optional;

public class Main {
    static void main() {

        Player player1 = new SimpleMonteCarloBot();
        Player player2 = new TrivialBot();

        ConcurrentTournament tournament = new ConcurrentTournament();
        tournament.playTournament(player1, player2, 100 , 4);
        tournament.displayStatistics();

//        Tournament tournament = new Tournament();
//        tournament.playTournament(player1, player2, 100);
//        tournament.displayStatistics();

//        GameRoom game = new GameRoom(player1, player2);
//        game.startGame();
    }
}
