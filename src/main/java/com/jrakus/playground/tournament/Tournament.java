package com.jrakus.playground.tournament;

import com.jrakus.players.Player;
import com.jrakus.playground.GameRoom;

import java.util.Optional;

public class Tournament {

    int numberOfWinsByPlayer1 = 0;
    int numberOfWinsByPlayer2 = 0;
    int numberOfDraws = 0;

    public void playTournament(Player player1, Player player2, int numberOfGames) {

        for (int i = 0; i < numberOfGames; i++) {
            System.out.println("The game nr. " + i + " has been started");
            GameRoom game = new GameRoom(player1, player2);

            Optional<Player> optionalWinner = game.startGame();

            if (optionalWinner.isPresent()) {
                if(player1 == optionalWinner.get()) {
                    numberOfWinsByPlayer1++;
                } else {
                    numberOfWinsByPlayer2++;
                }
            } else {
                numberOfDraws++;
            }
        }
    }

    public void displayStatistics() {
        int totalGames = numberOfWinsByPlayer1 + numberOfWinsByPlayer2 + numberOfDraws;

        System.out.println("=== Tournament Statistics ===");
        System.out.println("Total games played: " + totalGames);
        System.out.println("Player 1 wins: " + numberOfWinsByPlayer1);
        System.out.println("Player 2 wins: " + numberOfWinsByPlayer2);
        System.out.println("Draws: " + numberOfDraws);

        if (totalGames > 0) {
            double p1WinRate = (numberOfWinsByPlayer1 * 100.0) / totalGames;
            double p2WinRate = (numberOfWinsByPlayer2 * 100.0) / totalGames;
            double drawRate = (numberOfDraws * 100.0) / totalGames;

            System.out.printf("Player 1 win rate: %.2f%%\n", p1WinRate);
            System.out.printf("Player 2 win rate: %.2f%%\n", p2WinRate);
            System.out.printf("Draw rate: %.2f%%\n", drawRate);
        }
    }
}
