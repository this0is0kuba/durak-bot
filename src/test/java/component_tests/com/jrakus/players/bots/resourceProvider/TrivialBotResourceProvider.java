package com.jrakus.players.bots.resourceProvider;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.game_elements.GameInfo;
import com.jrakus.players.game_elements.Move;
import com.jrakus.players.game_elements.PublicState;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;

public class TrivialBotResourceProvider {

    public static class ResourcesForDefend {

        public static Arguments getArguments1() {

            List<Card> attackingCards = List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, JACK), new Card(CLUBS, JACK));
            List<Card> defendingCards = List.of(new Card(HEARTS, JACK));
            List<Card> yourHand = List.of(new Card(DIAMONDS, QUEEN), new Card(DIAMONDS, KING), new Card(CLUBS, KING));
            List<Card> discardPile = List.of();
            List<Card> certainOpponentHand = List.of();
            Card trumpCard = new Card(SPADES, ACE);
            int numberOfCardsOnOpponentHand = 2;
            int numberOfCardsOnDeck = 12;
            GameInfo gameInfo = GameInfo.ACTIVE_GAME;

            PublicState publicState = new PublicState.PublicStateBuilder()
                    .attackingCards(attackingCards)
                    .defendingCards(defendingCards)
                    .yourHand(yourHand)
                    .discardPile(discardPile)
                    .certainOpponentHand(certainOpponentHand)
                    .trumpCard(trumpCard)
                    .numberOfCardsOnOpponentHand(numberOfCardsOnOpponentHand)
                    .numberOfCardsOnDeck(numberOfCardsOnDeck)
                    .gameInfo(gameInfo)
                    .areYouPlayer1(false)
                    .didPlayer1StartGame(true)
                    .build();

            Move expectedMove = new Move(
                    Move.MoveKind.DEFEND,
                    List.of(new Card(DIAMONDS, QUEEN), new Card(CLUBS, KING))
            );

            return Arguments.of(publicState, expectedMove);
        }

    }
}
