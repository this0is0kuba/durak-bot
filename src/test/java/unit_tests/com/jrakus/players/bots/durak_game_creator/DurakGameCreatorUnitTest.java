package com.jrakus.players.bots.durak_game_creator;

import com.jrakus.game_state.components.*;
import com.jrakus.players.game_elements.GameInfo;
import com.jrakus.players.game_elements.PublicState;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jrakus.game_state.components.Card.Suit.*;
import static com.jrakus.game_state.components.Card.Rank.*;

public class DurakGameCreatorUnitTest {

    private final DurakGameCreator durakGameCreator = new DurakGameCreator();

    @Test
    void itShouldCreateCorrectStartingGame() {

        List<Card> attackingCards = List.of();
        List<Card> defendingCards = List.of();
        List<Card> playerHand = List.of(
                new Card(HEARTS, NINE), new Card(HEARTS, TEN), new Card(HEARTS, JACK),
                new Card(HEARTS, QUEEN), new Card(HEARTS, KING), new Card(HEARTS, ACE)
        );

        List<Card> discardPile = List.of();
        List<Card> visibleCardsForPlayer = List.of();
        Card trumpCard = new Card(DIAMONDS, NINE);
        int numberOfCardsOnOpponentHand = 6;
        int numberOfCardsOnDeck = 12;
        GameInfo gameInfo = GameInfo.ACTIVE_GAME;
        boolean areYouPlayer1 = true;
        boolean didPlayer1StartGame = true;

        PublicState publicState = new PublicState.PublicStateBuilder()
                .attackingCards(attackingCards)
                .defendingCards(defendingCards)
                .yourHand(playerHand)
                .discardPile(discardPile)
                .certainOpponentHand(visibleCardsForPlayer)
                .trumpCard(trumpCard)
                .numberOfCardsOnOpponentHand(numberOfCardsOnOpponentHand)
                .numberOfCardsOnDeck(numberOfCardsOnDeck)
                .gameInfo(gameInfo)
                .areYouPlayer1(areYouPlayer1)
                .didPlayer1StartGame(didPlayer1StartGame)
                .build();
    }

//    Deck deck = new Deck(
//
//    );
//
//    DurakGame expectedDurakGame = new DurakGame(
//            deck,
//            discardPile,
//            table,
//            gameState,
//            durakGamePlayer1,
//            durakGamePlayer2,
//            activePlayer,
//            startingPlayer,
//            currentAttackingPlayer,
//            currentDefendingPlayer
//    );
}
