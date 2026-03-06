package com.jrakus.players;

import com.jrakus.game_elements.Move;
import com.jrakus.game_elements.Player;
import com.jrakus.game_elements.PublicState;
import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.Card.*;

import static com.jrakus.game_elements.Move.MoveKind.*;
import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import com.jrakus.game_state.components.DurakGamePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LocalGamer implements Player {

    private final DurakGamePlayer durakGamePlayer;

    private final Map<String, Rank> abbreviationToRank = Map.of(
            "9", NINE,
            "10", TEN,
            "j", JACK,
            "q", QUEEN,
            "k", KING,
            "a", ACE
    );

    private final Map<String, Suit> abbreviationToSuit = Map.of(
            "1", HEARTS,
            "2", DIAMONDS,
            "3", CLUBS,
            "4", SPADES
    );

    public LocalGamer(DurakGamePlayer durakGamePlayer) {
        this.durakGamePlayer = durakGamePlayer;
    }

    @Override
    public DurakGamePlayer getDurakGamePlayer() {
        return durakGamePlayer;
    }

    @Override
    public Move defend(PublicState publicState) {

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
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        if (input.isBlank()) {
            return new Move(STOP_ATTACK);
        }

        List<Card> cards = retrieveCardsFromText(input);

        return new Move(ATTACK, cards);
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

}
