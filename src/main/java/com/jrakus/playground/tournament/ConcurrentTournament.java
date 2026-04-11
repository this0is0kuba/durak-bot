package com.jrakus.playground.tournament;

import com.jrakus.players.Player;
import com.jrakus.playground.GameRoom;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentTournament {

    private final AtomicInteger numberOfWinsByPlayer1 = new AtomicInteger(0);
    private final AtomicInteger numberOfWinsByPlayer2 = new AtomicInteger(0);
    private final AtomicInteger numberOfDraws = new AtomicInteger(0);

    public void playTournament(Player player1, Player player2, int numberOfGamesPerThread, int numberOfThreads) {

        try (ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {

            for (int t = 0; t < numberOfThreads; t++) {
                int finalT = t;
                executor.submit(() -> {
                    for (int i = 0; i < numberOfGamesPerThread; i++) {
                        System.out.printf("Thread %s started a new game nr. %s%n", finalT, i);
                        playSingleGame(player1, player2);
                    }
                });
            }

            executor.shutdown();
        }
    }

    private void playSingleGame(Player player1, Player player2) {
        GameRoom game = new GameRoom(player1, player2);
        Optional<Player> optionalWinner = game.startGame();

        if (optionalWinner.isPresent()) {
            if (player1 == optionalWinner.get()) {
                numberOfWinsByPlayer1.incrementAndGet();
            } else {
                numberOfWinsByPlayer2.incrementAndGet();
            }
        } else {
            numberOfDraws.incrementAndGet();
        }
    }

    public void displayStatistics() {
        int totalGames = numberOfWinsByPlayer1.get() + numberOfWinsByPlayer2.get() + numberOfDraws.get();

        System.out.println("=== Tournament Statistics ===");
        System.out.println("Total games played: " + totalGames);
        System.out.println("Player 1 wins: " + numberOfWinsByPlayer1);
        System.out.println("Player 2 wins: " + numberOfWinsByPlayer2);
        System.out.println("Draws: " + numberOfDraws);

        if (totalGames > 0) {
            double p1WinRate = (numberOfWinsByPlayer1.get() * 100.0) / totalGames;
            double p2WinRate = (numberOfWinsByPlayer2.get() * 100.0) / totalGames;
            double drawRate = (numberOfDraws.get() * 100.0) / totalGames;

            System.out.printf("Player 1 win rate: %.2f%%\n", p1WinRate);
            System.out.printf("Player 2 win rate: %.2f%%\n", p2WinRate);
            System.out.printf("Draw rate: %.2f%%\n", drawRate);
        }
    }
}
