package com.jrakus.players.bots.move_founder.specific_move_founder;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.bots.move_founder.MoveFounder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefendingMoveFounder {

    public List<MoveFounder.Action> getAllCombinationsOfCards(
            Map<Card, List<Card>> attackingCardToDefendingCards,
            List<Card> attackingCardList
    ) {

        int numberOfAttackingCards = attackingCardToDefendingCards.size();

        List<MoveFounder.Action> allFoundActions = new ArrayList<>();
        List<Card> chosenDefendingCards = new ArrayList<>(Collections.nCopies(numberOfAttackingCards, null));

        findAllDefendingCombinationsRecursively(
                attackingCardToDefendingCards, attackingCardList, chosenDefendingCards,
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

        if (currentAttackingCardIndex < numberOfAttackingCards) {

            Card attackingCard = allAttackingCards.get(currentAttackingCardIndex);
            List<Card> defendingCardList = attackingCardToDefendingCards.get(attackingCard);

            for (Card defendingCard: defendingCardList) {
                if (!chosenDefendingCards.subList(0, currentAttackingCardIndex).contains(defendingCard)) {

                    chosenDefendingCards.set(currentAttackingCardIndex, defendingCard);
                    int newCurrentAttackingCardIndex = currentAttackingCardIndex + 1;

                    findAllDefendingCombinationsRecursively(
                            attackingCardToDefendingCards, allAttackingCards, chosenDefendingCards,
                            allFoundActions, newCurrentAttackingCardIndex, numberOfAttackingCards
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
