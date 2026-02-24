package com.jrakus.game_state.validators;

import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.components.GameState;
import com.jrakus.game_state.components.DurakGamePlayer;
import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;

import java.util.HashSet;
import java.util.List;

public class DurakGameValidator {

    public void checkIfGameIsStillActive(GameState.GameStateEnum state) {
        if(state != GameState.GameStateEnum.ACTIVE_GAME) {
            throw new DurakGameInvalidStateException("The game has already ended");
        }
    }

    public void checkIfPlayerCanPlay(DurakGamePlayer playerToCheck, DurakGamePlayer activePlayer) {
        if(playerToCheck != activePlayer)
            throw new DurakGameInvalidStateException(String.format("This is not turn for player: %s", playerToCheck));
    }

    public void checkIfPlayerHasCardsThatHePlays(DurakGamePlayer durakGamePlayer, List<Card> cards) {
        List<Card> cardsOnHand = durakGamePlayer.showCardsOnHand();

        if(!new HashSet<>(cardsOnHand).containsAll(cards)) {
            throw new DurakGameInvalidStateException(String.format("Player %s does not have cards that he wants to play", durakGamePlayer));
        }
    }

    public void checkIfAttackHasCorrectAmountOfCards(DurakGamePlayer opponent, List<Card> cards) {
        int amountOfOpponnentsCards = opponent.countCardsOnHand();

        if(cards.size() < 1 || cards.size() > amountOfOpponnentsCards) {
            throw new DurakGameInvalidStateException(String.format("Count of attacking cards must be between 1 and %d", amountOfOpponnentsCards));
        }
    }
}
