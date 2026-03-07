package com.jrakus.players;

import com.jrakus.game_elements.Move;
import com.jrakus.game_elements.Player;
import com.jrakus.game_elements.PublicState;
import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.Card.*;
import com.jrakus.playground.displays.Display;

import static com.jrakus.game_elements.Move.MoveKind.*;
import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LocalUser implements Player {

    private final Display display;

    public LocalUser(Display display) {
        this.display = display;
    }

    private static final Map<String, Card.Rank> abbreviationToRank = Map.of(
            "9", NINE,
            "10", TEN,
            "j", JACK,
            "q", QUEEN,
            "k", KING,
            "a", ACE
    );

    private static final Map<String, Card.Suit> abbreviationToSuit = Map.of(
            "1", HEARTS,
            "2", DIAMONDS,
            "3", CLUBS,
            "4", SPADES
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
        display.display(publicState);
    }

    private List<Card> retrieveCardsFromText(String input) {

        String[] cardsDescription = input.split(",");
        List<Card> cards = new ArrayList<>();

        for(String cardDescription: cardsDescription) {

            String[] cardDescriptionArray = cardDescription.trim().split(" ");

            String rankAbbreviation = cardDescriptionArray[0];
            String suitAbbreviation = cardDescriptionArray[1];

            Card.Rank rank = abbreviationToRank.get(rankAbbreviation);
            Card.Suit suit = abbreviationToSuit.get(suitAbbreviation);

            cards.add( new Card(suit, rank));
        }

        return cards;
    }

}
