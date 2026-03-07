package com.jrakus.players;

import com.jrakus.game_elements.GameInfo;
import com.jrakus.game_elements.Move;
import com.jrakus.game_elements.Player;
import com.jrakus.game_elements.PublicState;
import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.Card.*;
import com.jrakus.game_state.components.GameState.*;

import static com.jrakus.game_elements.Move.MoveKind.*;
import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static com.jrakus.game_state.components.GameState.GameStateEnum.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LocalGamer implements Player {

    private static final Map<String, Rank> abbreviationToRank = Map.of(
            "9", NINE,
            "10", TEN,
            "j", JACK,
            "q", QUEEN,
            "k", KING,
            "a", ACE
    );

    private static final Map<String, Suit> abbreviationToSuit = Map.of(
            "1", HEARTS,
            "2", DIAMONDS,
            "3", CLUBS,
            "4", SPADES
    );

    private static final Map<Rank, String> rankToDisplayedText = Map.of(
            NINE, "9",
            TEN, "10",
            JACK, "J",
            QUEEN, "Q",
            KING, "K",
            ACE, "A"
    );

    private static final Map<Suit, String> suitToDisplayedText = Map.of(
            HEARTS, "♥",
            DIAMONDS, "♦",
            CLUBS, "♣",
            SPADES, "♠"
    );

    @Override
    public Move defend(PublicState publicState) {

        System.out.print("Enter cards that you want to play: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        if (input.isBlank()) {
            return new Move(TAKE_CARDS);
        }

        List<Card> cards = retrieveCardsFromText(input);

        return new Move(DEFEND, cards);
    }

    @Override
    public Move attack(PublicState publicState) {

        System.out.print("Enter cards that you want to play (e.g. 'a 1, a 4'): ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        if (input.isBlank()) {
            return new Move(STOP_ATTACK);
        }

        List<Card> cards = retrieveCardsFromText(input);

        return new Move(ATTACK, cards);
    }

    @Override
    public void displayCurrentState(PublicState publicState) {
        clearConsole();
        displayGameInTerminal(publicState);
    }

    private List<Card> retrieveCardsFromText(String input) {

        String[] cardsDescription = input.split(",");
        List<Card> cards = new ArrayList<>();

        for(String cardDescription: cardsDescription) {

            String[] cardDescriptionArray = cardDescription.trim().split(" ");

            String rankAbbreviation = cardDescriptionArray[0];
            String suitAbbreviation = cardDescriptionArray[1];

            Rank rank = abbreviationToRank.get(rankAbbreviation);
            Suit suit = abbreviationToSuit.get(suitAbbreviation);

            cards.add( new Card(suit, rank));
        }

        return cards;
    }

    private void displayGameInTerminal(PublicState publicState) {

        displayGameTitle();

        displayTopPartOfTable(publicState.getNumberOfCardsOnOpponentHand());

        displayMiddlePartOfTable(
                publicState.getNumberOfCardsOnDeck(),
                publicState.getTrumpCard(),
                publicState.getAttackingCards(),
                publicState.getDefendingCards()
        );

        displayBottomPartOfTable(publicState.getYourHand());

        displayCurrentGameState(publicState.getGameInfo());
    }

    private static void displayGameTitle() {
        System.out.println("██████╗ ██╗   ██╗██████╗  █████╗ ██╗  ██╗     ██████╗  █████╗ ███╗   ███╗███████╗");
        System.out.println("██╔══██╗██║   ██║██╔══██╗██╔══██╗██║ ██╔╝    ██╔════╝ ██╔══██╗████╗ ████║██╔════╝");
        System.out.println("██║  ██║██║   ██║██████╔╝███████║█████╔╝     ██║  ███╗███████║██╔████╔██║█████╗  ");
        System.out.println("██║  ██║██║   ██║██╔══██╗██╔══██║██╔═██╗     ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝  ");
        System.out.println("██████╔╝╚██████╔╝██║  ██║██║  ██║██║  ██╗    ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗");
        System.out.println("╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝     ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝");
        System.out.println("─────────────────────────────────────────────────────────────────────────────────");
        System.out.println();
    }

    private static void displayTopPartOfTable(int numberOfCards) {
        String[] lines = createOpponentCards(numberOfCards);

        for(String line: lines) {
            System.out.println(line);
        }
    }

    private static void displayMiddlePartOfTable(
            int numberOfCardsOnDeck,
            Card trump,
            List<Card> attackingCards,
            List<Card> defendingCards
    ) {
        String[] lines = createDeck(numberOfCardsOnDeck, trump);
        String[] lines2 = createBattlefield(attackingCards, defendingCards);
        String[] lines3 = createCheatSheet();

        String blankSpace = "       ";

        if (attackingCards.size() < 3) {
            blankSpace = blankSpace.repeat(7 - (attackingCards.size()));
        }

        for(int i = 0; i < 8; i++) {
            System.out.println(lines[i] + "     " + lines2[i] + blankSpace + lines3[i]);
        }

    }

    private static void displayBottomPartOfTable(List<Card> userCards) {
        String[] lines = createVisibleCards(userCards);

        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static void displayCurrentGameState(GameInfo gameInfo) {

        System.out.println("CURRENT STATE: " + gameInfo);
        System.out.println("─────────────────────────────────────────────────────────────────────────────────");
        System.out.println();
    }

    private static String[] createBattlefield(List<Card> attackingCards, List<Card> defendingCards) {
        String[] lines = createVisibleCards(attackingCards);
        String[] lines2 = createVisibleCards(defendingCards);

        String[] newLines = new String[8];

        for (int i = 0; i < 3; i++) {
            newLines[i] = lines[i];
        }

        int additionalSpace = 6;
        int cardWidth = 10;
        for (int i = 3; i < 5; i++) {
            newLines[i] = lines2[i - 3] + lines[i].substring(additionalSpace + defendingCards.size() * cardWidth);
        }

        for (int i = 5; i < 8; i++) {
            newLines[i] = lines2[i - 3] + " ".repeat(cardWidth * (attackingCards.size() - defendingCards.size()));
        }

        return newLines;
    }

    private static String[] createCheatSheet() {

        return new String[] {
                "       ",
                "       ",
                " ♥ → 1 ",
                " ♦ → 2 ",
                " ♣ → 3 ",
                " ♠ → 4 ",
                "       ",
                "       ",
        };
    }

    private static String[] createOpponentCards(int numberOfCards) {

        String[] allLinesToPrint = new String[5];

        String[] cardBack = {
                "┌───────┐",
                "│░░░░░░░│",
                "│░░░░░░░│",
                "│░░░░░░░│",
                "└───────┘"
        };

        int overlap = 3;

        for (int i = 0; i < 5; i++) {
            StringBuilder lineToPrint = new StringBuilder();

            String line = cardBack[i];
            lineToPrint.append("     ").append(line);

            for (int j = 1; j < numberOfCards; j++) {
                lineToPrint.append(line.substring(overlap));
            }

            allLinesToPrint[i] = lineToPrint.toString();
        }

        return allLinesToPrint;
    }

    private static String[] createDeck(int numberOfCards, Card trump) {

        String rank = rankToDisplayedText.get(trump.rank());
        String suit = suitToDisplayedText.get(trump.suit());

        String[] deck;

        if(numberOfCards > 1) {
            deck = new String[]{
                                  "              ",
                                  "              ",
                                  "┌───────┐────┐",
                                  "│░░░░░░░│    │",
                    String.format("│░░ %-2s░░│%s   │", numberOfCards, suit),
                    String.format("│░░░░░░░│  %2s│", rank),
                                  "└───────┘────┘",
                                  "              "
            };
        } else if(numberOfCards == 1) {
            deck = new String[]{
                                  "         ",
                                  "         ",
                                  "┌───────┐",
                    String.format("│%-2s     │", rank),
                    String.format("│   %s   │", suit),
                    String.format("│     %2s│", rank),
                                  "└───────┘",
                                  "         "
            };
        } else {
            deck = new String[]{
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         "
            };
        }

        return deck;
    }

    private static String[] createVisibleCards(List<Card> cards) {

        String[] allLinesToPrint = new String[5];

        // Each line of a single card
        // We'll fill this for each card in the hand
        for (int i = 0; i < 5; i++) {
            int additionalSpace = 6;
            StringBuilder line = new StringBuilder(" ".repeat(additionalSpace));
            for (Card card : cards) {

                String rank = rankToDisplayedText.get(card.rank());
                String suit = suitToDisplayedText.get(card.suit());

                String cardLine = switch (i) {
                    case 0 ->               "┌───────┐";
                    case 1 -> String.format("│%-2s     │", rank);
                    case 2 -> String.format("│   %s   │", suit);
                    case 3 -> String.format("│     %2s│", rank);
                    case 4 ->               "└───────┘";
                    default -> "";
                };

                line.append(cardLine).append(" "); // space between cards
            }
            allLinesToPrint[i] = line.toString();
        }

        return allLinesToPrint;
    }

    public static void clearConsole() {
        System.out.print("\n".repeat(50));
    }

}
