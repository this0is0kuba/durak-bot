package com.jrakus.players.bots.cardSelector;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.game_elements.PublicState;

import java.util.*;
import java.util.stream.Collectors;

public class CardHelper {

    public record Action(List<Card> cardsToPlay) {}

    public List<Action> getAllPossibleAttacks(PublicState publicState) {

        boolean isFirstAttack = !publicState.getAttackingCards().isEmpty();

        if (isFirstAttack) {
            return getAllPossibleAttacksForFirstAttack(
                    publicState.getYourHand(),
                    publicState.getNumberOfCardsOnOpponentHand()
            );
        } else {

            Set<Card.Rank> ranksThatAreOnTable = new HashSet<>();

            ranksThatAreOnTable.addAll(
                    publicState.getAttackingCards().stream().map(Card::rank).collect(Collectors.toSet())
            );
            ranksThatAreOnTable.addAll(
                    publicState.getDefendingCards().stream().map(Card::rank).collect(Collectors.toSet())
            );

            return getAllPossibleAttacksForNextAttack(
                    publicState.getYourHand(),
                    publicState.getNumberOfCardsOnOpponentHand(),
                    publicState.getAttackingCards().size(),
                    ranksThatAreOnTable
            );
        }
    }

    public List<Action> getAllPossibleDefends(PublicState publicState) {

        Map<Card, List<Card>> mapAttackingCardToPossibleDefendingCards = new HashMap<>();

        List<Card> newAttackingCards = publicState.getAttackingCards().subList(
                publicState.getDefendingCards().size(), publicState.getAttackingCards().size()
        );

        for(Card card: newAttackingCards) {
            mapAttackingCardToPossibleDefendingCards.put(
                    card,
                    getAllDefendsForSpecificCard(card, publicState.getYourHand(), publicState.getTrumpCard().suit())
            );
        }

        return findAllCombinationsOfDefendingCards(mapAttackingCardToPossibleDefendingCards);
    }

    private List<Card> getAllDefendsForSpecificCard(Card attackingCard, List<Card> yourHand, Card.Suit trump) {
        return yourHand.stream().filter(
                card -> card.isCardStrongerThan(attackingCard, trump)
        ).toList();
    }

    private List<Action> findAllCombinationsOfDefendingCards(
            Map<Card, List<Card>> attackingCardToDefendingCards
    ) {
        int numberOfAttackingCards = attackingCardToDefendingCards.size();
        List<Card> attackingCardList = new ArrayList<>(attackingCardToDefendingCards.keySet());

        List<Action> allFoundActions = new ArrayList<>();

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
            List<Action> allFoundActions,
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
                    new Action(new ArrayList<>(chosenDefendingCards))
            );
        }
    }


    private List<Action> getAllPossibleAttacksForFirstAttack(List<Card> yourHand, int numberOfCardsOnOpponentHand) {

        Map<Card.Rank, List<Card>> groupedCardsByRank = yourHand.stream()
                .collect(Collectors.groupingBy(Card::rank));

        List<Action> allActions = new ArrayList<>();

        for (Card.Rank rank: groupedCardsByRank.keySet()) {

            List<Card> cardsWithTheSameRank = groupedCardsByRank.get(rank);

            for(int i = 1; i <= cardsWithTheSameRank.size() && i <= numberOfCardsOnOpponentHand; i++) {
                List<Action> actions = getAllCombinationsOfCards(i, cardsWithTheSameRank);
                allActions.addAll(actions);
            }
        }

        return allActions;
    }

    private List<Action> getAllPossibleAttacksForNextAttack(
            List<Card> yourHand,
            int numberOfCardsOnOpponentHand,
            int numberOfAttackingCardsOnTable,
            Set<Card.Rank> alreadyUsedRanks
    ) {

        List<Action> allActions = new ArrayList<>();

        List<Card> possibleCardsToPlay = yourHand.stream().filter(
                card -> alreadyUsedRanks.contains(card.rank())
                ).toList();

        int maxNumberOfCardsOnTable = 5;
        int limitCardsToPlay = maxNumberOfCardsOnTable - numberOfAttackingCardsOnTable;

        for(
                int i = 1;
                i <= possibleCardsToPlay.size() && i <= numberOfCardsOnOpponentHand && i <= limitCardsToPlay;
                i++
        ) {
            List<Action> actions = getAllCombinationsOfCards(i, possibleCardsToPlay);
            allActions.addAll(actions);
        }

        // Action with empty list means that user should stop attack
        allActions.add(new Action(List.of()));

        return allActions;
    }

    private List<Action> getAllCombinationsOfCards(int numberOfCardsInAction, List<Card> yourHand) {
        List<Action> allActions = new ArrayList<>();
        List<Integer> indicators = new ArrayList<>();

        for(int i = 0; i < numberOfCardsInAction; i++) {
            indicators.add(i);
        }

        int indexReachedByFirstIndicator = indicators.getFirst();
        int maxIndexThatCanBeReachedByFirstIndicator = yourHand.size() - numberOfCardsInAction;


        while (indexReachedByFirstIndicator < maxIndexThatCanBeReachedByFirstIndicator) {
            List<Card> possibleCardsToPlay = indicators.stream().map(yourHand::get).toList();

            Action possibleAction = new Action(possibleCardsToPlay);
            allActions.add(possibleAction);

            updateIndicators(indicators, yourHand.size());
            indexReachedByFirstIndicator = indicators.getFirst();
        }

        return allActions;
    }

    private void updateIndicators(List<Integer> indicators, int max) {

        // We start from the last indicator
        int indicatorIndex = indicators.size() - 1;
        int indexReachedByIndicator = indicators.get(indicatorIndex);
        int maxIndexIndicatorCanReach = max - (indicators.size() - indicatorIndex - 1);

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
        while (indicatorToUpdate < indicators.size()) {

            indicators.set(indicatorToUpdate, newIndexForIndicator);

            newIndexForIndicator++;
            indicatorToUpdate++;
        }
    }
}
