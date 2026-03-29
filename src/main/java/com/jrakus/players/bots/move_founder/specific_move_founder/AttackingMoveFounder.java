package com.jrakus.players.bots.move_founder.specific_move_founder;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.bots.move_founder.MoveFounder;

import java.util.ArrayList;
import java.util.List;

public class AttackingMoveFounder {

    public List<MoveFounder.Action> getAllCombinationsOfCards(
            int numberOfCardsInCombination,
            List<Card> cards
    ) {

        List<Integer> indicators = new ArrayList<>();

        for(int i = 0; i < numberOfCardsInCombination; i++) {
            indicators.add(i);
        }

        int indexReachedByFirstIndicator = indicators.getFirst();
        int maxIndexThatCanBeReachedByFirstIndicator = cards.size() - numberOfCardsInCombination;

        if (indexReachedByFirstIndicator == maxIndexThatCanBeReachedByFirstIndicator)  {
            return List.of(new MoveFounder.Action(
                    new ArrayList<>(cards)
            ));
        }

        List<MoveFounder.Action> allActions = new ArrayList<>();

        while (indexReachedByFirstIndicator < maxIndexThatCanBeReachedByFirstIndicator) {

            List<Card> possibleCardsToPlay = indicators.stream().map(cards::get).toList();
            MoveFounder.Action possibleAction = new MoveFounder.Action(possibleCardsToPlay);
            allActions.add(possibleAction);

            updateIndicators(indicators, cards.size());
            indexReachedByFirstIndicator = indicators.getFirst();
        }

        // Add last case where indicators stopped
        List<Card> possibleCardsToPlay = indicators.stream().map(cards::get).toList();
        MoveFounder.Action possibleAction = new MoveFounder.Action(possibleCardsToPlay);
        allActions.add(possibleAction);

        return allActions;
    }

    private void updateIndicators(List<Integer> indicators, int max) {

        // We start from the last indicator
        int indicatorIndex = indicators.size() - 1;
        int indexReachedByIndicator = indicators.get(indicatorIndex);
        int maxIndexIndicatorCanReach = max - (indicators.size() - indicatorIndex);

        boolean didIndicatorReachEnd = indexReachedByIndicator == maxIndexIndicatorCanReach;

        while(didIndicatorReachEnd) {

            indicatorIndex--;
            indexReachedByIndicator = indicators.get(indicatorIndex);
            maxIndexIndicatorCanReach = max - (indicators.size() - indicatorIndex);

            didIndicatorReachEnd = indexReachedByIndicator == maxIndexIndicatorCanReach;
        }

        int newIndexForIndicator = indexReachedByIndicator + 1;
        indicators.set(indicatorIndex, newIndexForIndicator);

        // update all indicators with higher index than updated indicator
        int indicatorToUpdate = indicatorIndex + 1;
        newIndexForIndicator++;
        while (indicatorToUpdate < indicators.size()) {

            indicators.set(indicatorToUpdate, newIndexForIndicator);

            newIndexForIndicator++;
            indicatorToUpdate++;
        }
    }
}
