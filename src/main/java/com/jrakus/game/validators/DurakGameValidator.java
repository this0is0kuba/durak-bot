package com.jrakus.game.validators;

import com.jrakus.game.components.Card;
import com.jrakus.game.components.Player;
import com.jrakus.game.exceptions.DurakGameException;

import java.util.HashSet;
import java.util.List;

public class DurakGameValidator {

    public void checkAttacker(Player attackingPlayer, Player activePlayer, Player currentAttackingPlayer) {
        if(attackingPlayer != activePlayer)
            throw new DurakGameException(String.format("This is not turn for player: %s", attackingPlayer));

        if(attackingPlayer != currentAttackingPlayer)
            throw new DurakGameException(String.format("Player %s does not attack now", attackingPlayer));
    }

    public void checkDefender(Player defendingPlayer, Player activePlayer, Player currentDefendingPlayer) {
        if(defendingPlayer != activePlayer)
            throw new DurakGameException(String.format("This is not turn for player: %s", defendingPlayer));

        if(defendingPlayer != currentDefendingPlayer)
            throw new DurakGameException(String.format("Player %s does not defend now", defendingPlayer));
    }

    public void checkIfPlayerHasCardsThatHePlays(Player player, List<Card> cards) {
        List<Card> cardsOnHand = player.showCardsOnHand();

        if(!new HashSet<>(cardsOnHand).containsAll(cards)) {
            throw new DurakGameException(String.format("Player %s does not have cards that he wants to play", player));
        }
    }
}
