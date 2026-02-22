package com.jrakus.game.validators;

import com.jrakus.game.components.Card;
import com.jrakus.game.components.GameState;
import com.jrakus.game.components.DurakGamePlayer;
import com.jrakus.game.exceptions.DurakGameException;

import java.util.HashSet;
import java.util.List;

public class DurakGameValidator {

    public void checkAttacker(DurakGamePlayer attackingDurakGamePlayer, DurakGamePlayer activeDurakGamePlayer, DurakGamePlayer currentAttackingDurakGamePlayer) {
        if(attackingDurakGamePlayer != activeDurakGamePlayer)
            throw new DurakGameException(String.format("This is not turn for player: %s", attackingDurakGamePlayer));

        if(attackingDurakGamePlayer != currentAttackingDurakGamePlayer)
            throw new DurakGameException(String.format("Player %s does not attack now", attackingDurakGamePlayer));
    }

    public void checkDefender(DurakGamePlayer defendingDurakGamePlayer, DurakGamePlayer activeDurakGamePlayer, DurakGamePlayer currentDefendingDurakGamePlayer) {
        if(defendingDurakGamePlayer != activeDurakGamePlayer)
            throw new DurakGameException(String.format("This is not turn for player: %s", defendingDurakGamePlayer));

        if(defendingDurakGamePlayer != currentDefendingDurakGamePlayer)
            throw new DurakGameException(String.format("Player %s does not defend now", defendingDurakGamePlayer));
    }

    public void checkIfPlayerHasCardsThatHePlays(DurakGamePlayer durakGamePlayer, List<Card> cards) {
        List<Card> cardsOnHand = durakGamePlayer.showCardsOnHand();

        if(!new HashSet<>(cardsOnHand).containsAll(cards)) {
            throw new DurakGameException(String.format("Player %s does not have cards that he wants to play", durakGamePlayer));
        }
    }

    public void checkIfGameIsStillActive(GameState.GameStateEnum state) {
        if(state != GameState.GameStateEnum.ACTIVE_GAME) {
            throw new DurakGameException("The game has already ended");
        }
    }
}
