package com.jrakus.players.bots.move_founder;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.bots.move_founder.specific_move_founder.AttackingMoveFounder;
import com.jrakus.players.bots.move_founder.specific_move_founder.DefendingMoveFounder;

import java.util.*;
import java.util.stream.Collectors;

public class MoveFounder {

    public record Action(List<Card> cardsToPlay) {}

    private final AttackingMoveFounder attackingMoveFounder;
    private final DefendingMoveFounder defendingMoveFounder;

    public MoveFounder(AttackingMoveFounder attackingMoveFounder, DefendingMoveFounder defendingMoveFounder) {
        this.attackingMoveFounder = attackingMoveFounder;
        this.defendingMoveFounder = defendingMoveFounder;
    }

    public List<Action> getAllPossibleAttacks(
            List<Card> attackingCards,
            List<Card> defendingCards,
            List<Card> yourHand,
            int numberOfCardsOnOpponentHand
    ) {

        boolean isFirstAttack = attackingCards.isEmpty();

        if (isFirstAttack) {
            return getAllPossibleAttacksForFirstAttack(
                    yourHand,
                    numberOfCardsOnOpponentHand
            );
        } else {

            Set<Card.Rank> ranksThatAreOnTable = new HashSet<>();

            ranksThatAreOnTable.addAll(
                    attackingCards.stream().map(Card::rank).collect(Collectors.toSet())
            );
            ranksThatAreOnTable.addAll(
                    defendingCards.stream().map(Card::rank).collect(Collectors.toSet())
            );

            return getAllPossibleAttacksForNextAttack(
                    yourHand,
                    numberOfCardsOnOpponentHand,
                    attackingCards.size(),
                    ranksThatAreOnTable
            );
        }
    }

    public List<Action> getAllPossibleDefends(
            List<Card> attackingCards,
            List<Card> defendingCards,
            List<Card> yourHand,
            Card.Suit trump
    ) {

        Map<Card, List<Card>> mapAttackingCardToPossibleDefendingCards = new HashMap<>();

        List<Card> newAttackingCards = attackingCards.subList(
                defendingCards.size(), attackingCards.size()
        );

        for(Card card: newAttackingCards) {
            mapAttackingCardToPossibleDefendingCards.put(
                    card,
                    getAllDefendsForSpecificCard(card, yourHand, trump)
            );
        }

        return defendingMoveFounder.findAllCombinationsOfDefendingCards(mapAttackingCardToPossibleDefendingCards);
    }

    private List<Card> getAllDefendsForSpecificCard(Card attackingCard, List<Card> yourHand, Card.Suit trump) {
        return yourHand.stream().filter(
                card -> card.isCardStrongerThan(attackingCard, trump)
        ).toList();
    }


    private List<Action> getAllPossibleAttacksForFirstAttack(List<Card> yourHand, int numberOfCardsOnOpponentHand) {

        Map<Card.Rank, List<Card>> groupedCardsByRank = yourHand.stream()
                .collect(Collectors.groupingBy(Card::rank));

        List<Action> allActions = new ArrayList<>();

        for (Card.Rank rank: groupedCardsByRank.keySet()) {

            List<Card> cardsWithTheSameRank = groupedCardsByRank.get(rank);

            for(int i = 1; i <= cardsWithTheSameRank.size() && i <= numberOfCardsOnOpponentHand; i++) {
                List<Action> actions = attackingMoveFounder.getAllCombinationsOfCards(i, cardsWithTheSameRank);
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
            List<Action> actions = attackingMoveFounder.getAllCombinationsOfCards(i, possibleCardsToPlay);
            allActions.addAll(actions);
        }

        // Action with empty list means that user should stop attack
        allActions.add(new Action(List.of()));

        return allActions;
    }
}
