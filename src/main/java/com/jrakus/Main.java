package com.jrakus;

import com.jrakus.players.Player;
import com.jrakus.players.bots.RandomBot;
import com.jrakus.players.bots.TrivialBot;
import com.jrakus.players.bots.card_selector.CardSelector;
import com.jrakus.playground.GameRoom;
import com.jrakus.playground.Tournament;
import com.jrakus.playground.displays.TerminalPrinter;

public class Main {
    static void main() {

        Player player1 = new TrivialBot();
        Player player2 = new RandomBot();

        Tournament tournament = new Tournament();
        tournament.playTournament(player1, player2, 100);
        tournament.displayStatistics();
    }
}
