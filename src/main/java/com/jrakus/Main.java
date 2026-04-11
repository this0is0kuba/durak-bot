package com.jrakus;

import com.jrakus.players.Player;
import com.jrakus.players.bots.RandomBot;
import com.jrakus.players.bots.SimpleMonteCarloBot;
import com.jrakus.players.bots.TrivialBot;
import com.jrakus.playground.tournament.ConcurrentTournament;
import com.jrakus.playground.tournament.Tournament;

public class Main {
    static void main() {

        Player player1 = new SimpleMonteCarloBot();
        Player player2 = new RandomBot();

        ConcurrentTournament tournament = new ConcurrentTournament();
        tournament.playTournament(player1, player2, 125 , 4);

        tournament.displayStatistics();

//        Tournament tournament = new Tournament();
//        tournament.playTournament(player1, player2, 100);
//        tournament.displayStatistics();
    }
}
