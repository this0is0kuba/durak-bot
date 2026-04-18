package com.jrakus.players.game_elements.utils;

import com.jrakus.game_state.DurakGame;
import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.GameState;
import com.jrakus.players.game_elements.GameInfo;
import com.jrakus.players.game_elements.PublicState;

import java.util.List;

public class PublicStateExtractor {

    public PublicState extractPublicState(DurakGame durakGame, boolean isPlayer1Active) {

        List<Card> attackingCards = durakGame.showCurrentAttackingCards();
        List<Card> defendingCards = durakGame.showCurrentDefendingCards();

        List<Card> playerHand = isPlayer1Active ?
                durakGame.showPlayer1Hand() : durakGame.showPlayer2Hand();

        List<Card> visibleCardsForPlayer = isPlayer1Active ?
                durakGame.showVisibleCardsForPlayer1() : durakGame.showVisibleCardsForPlayer2();

        int numberOfCardsOnOpponentHand = isPlayer1Active ?
                durakGame.getNumberOfCardsOfPlayer2() : durakGame.getNumberOfCardsOfPlayer1();

        List<Card> discardPile = durakGame.getDiscardPile();
        Card trumpCard = durakGame.showTrumpCard();
        int numberOfCardsOnDeck = durakGame.getNumberOfCardsOnDeck();

        GameState.GameStateEnum gameStateEnum = durakGame.getGameState();

        GameInfo gameInfo = switch (gameStateEnum) {
            case ACTIVE_GAME -> GameInfo.ACTIVE_GAME;
            case PLAYER_1_WON -> isPlayer1Active ? GameInfo.YOU_WON : GameInfo.OPPONENT_WON;
            case PLAYER_2_WON -> !isPlayer1Active ? GameInfo.YOU_WON : GameInfo.OPPONENT_WON;
            case DRAW -> GameInfo.DRAW;
        };

        Boolean didPlayer1StartGame = durakGame.didPlayer1StartGame();

        return new PublicState.PublicStateBuilder()
                .attackingCards(attackingCards)
                .defendingCards(defendingCards)
                .yourHand(playerHand)
                .discardPile(discardPile)
                .certainOpponentHand(visibleCardsForPlayer)
                .trumpCard(trumpCard)
                .numberOfCardsOnOpponentHand(numberOfCardsOnOpponentHand)
                .numberOfCardsOnDeck(numberOfCardsOnDeck)
                .gameInfo(gameInfo)
                .areYouPlayer1(isPlayer1Active)
                .didPlayer1StartGame(didPlayer1StartGame)
                .build();
    }
}
