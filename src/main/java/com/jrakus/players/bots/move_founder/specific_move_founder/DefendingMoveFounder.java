package com.jrakus.players.bots.move_founder.specific_move_founder;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.bots.move_founder.MoveFounder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefendingMoveFounder {

    public List<MoveFounder.Action> findAllCombinationsOfDefendingCards(
            Map<Card, List<Card>> attackingCardToDefendingCards
    ) {

        int numberOfAttackingCards = attackingCardToDefendingCards.size();
        List<Card> attackingCardList = new ArrayList<>(attackingCardToDefendingCards.keySet());

        List<MoveFounder.Action> allFoundActions = new ArrayList<>();

        findAllDefendingCombinationsRecursively(
                attackingCardToDefendingCards, attackingCardList, new ArrayList<>(),
                allFoundActions, 0, numberOfAttackingCards
        );

        return allFoundActions;
    }

    private void findAllDefendingCombinationsRecursively(
            Map<Card, List<Card>> attackingCardToDefendingCards,
            List<Card> allAttackingCards,
            List<Card> chosenDefendingCards,
            List<MoveFounder.Action> allFoundActions,
            int currentAttackingCardIndex,
            int numberOfAttackingCards
    ) {

        Card attackingCard = allAttackingCards.get(currentAttackingCardIndex);
        List<Card> defendingCardList = attackingCardToDefendingCards.get(attackingCard);

        if (currentAttackingCardIndex < numberOfAttackingCards) {
            for (Card defendingCard: defendingCardList) {
                if (!chosenDefendingCards.subList(0, currentAttackingCardIndex).contains(defendingCard)) {

                    chosenDefendingCards.set(currentAttackingCardIndex, defendingCard);

                    findAllDefendingCombinationsRecursively(
                            attackingCardToDefendingCards, allAttackingCards, chosenDefendingCards,
                            allFoundActions, ++currentAttackingCardIndex, numberOfAttackingCards
                    );
                }
            }
        } else {
            allFoundActions.add(
                    new MoveFounder.Action(new ArrayList<>(chosenDefendingCards))
            );
        }
    }
}
