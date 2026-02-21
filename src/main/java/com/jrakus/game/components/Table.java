package com.jrakus.game.components;

import com.jrakus.game.exceptions.DurakGameException;

import java.util.*;

public class Table {

    private final List<Card> attackingCards = new ArrayList<>();
    private final List<Card> defendingCards = new ArrayList<>();

    public void addAttackingCards(List<Card> newAttackingCards) {
        checkNewAttackingCards(newAttackingCards);
        attackingCards.addAll(newAttackingCards);
    }

    public void addDefendingCards(List<Card> newDefendingCards, Card.Suit trump) {
        checkNewDefendingCards(newDefendingCards, trump);
        defendingCards.addAll(newDefendingCards);
    }

    public List<Card> clearTable() {

        int numberOfAllCards = attackingCards.size() + defendingCards.size();
        List<Card> allCards = new ArrayList<>(numberOfAllCards);

        allCards.addAll(attackingCards);
        allCards.addAll(defendingCards);

        attackingCards.clear();
        defendingCards.clear();

        return allCards;
    }

    public List<Card> showAttackingCards() {
        return attackingCards;
    }

    public List<Card> showDefendingCards() {
        return defendingCards;
    }

    private void checkNewDefendingCards(List<Card> newDefendingCards, Card.Suit trump) {

        int numberOfCardsToBeat = attackingCards.size() - defendingCards.size();

        if(newDefendingCards.size() != numberOfCardsToBeat)
            throw new DurakGameException("The amount of new defending cards is different than attacking cards to beat");

        List<Card> onlyAttackingCardsToBeat = attackingCards.subList(defendingCards.size(), attackingCards.size());

        for(int i = 0; i < attackingCards.size(); i++) {

            Card attackingCard = onlyAttackingCardsToBeat.get(i);
            Card defendingCard = newDefendingCards.get(i);

            boolean isAttackingCardStronger = (compareCards(attackingCard, defendingCard, trump) > 0);

            if(isAttackingCardStronger)
                throw new DurakGameException(String.format(
                        "Attacking card %s is stronger than defending card %s",
                        attackingCard,
                        defendingCard
                ));
        }
    }

    private int compareCards(Card card1, Card card2, Card.Suit trump) {

        int rankValue1 = card1.rank().getRankValue();
        int rankValue2 = card2.rank().getRankValue();

        boolean isCard1Trump = (card1.suit() == trump);
        boolean isCard2Trump = (card2.suit() == trump);

        if (isCard1Trump && isCard2Trump) {
            return rankValue1 - rankValue2;
        }

        if (isCard1Trump) {
            return 1;
        }

        if (isCard2Trump) {
            return -1;
        }

        return rankValue1 - rankValue2;
    }

    private void checkNewAttackingCards(List<Card> newAttackingCards) {

        int newNumberOfCardsOnTable = attackingCards.size() + newAttackingCards.size();

        if (newNumberOfCardsOnTable > 5)
            throw new DurakGameException("Exceeded limit of attacking cards on table.");

        boolean areAnyAttackingCardsOnTable = !attackingCards.isEmpty();

        if (areAnyAttackingCardsOnTable) {
            checkIfNewAttackingCardsAreValid(newAttackingCards);
        }
    }

    private void checkIfNewAttackingCardsAreValid(List<Card> newAttackingCards) {

        Set<Card.Rank> allAttackingRanks = new HashSet<>();

        attackingCards.forEach(attackingCard -> allAttackingRanks.add(attackingCard.rank()));
        defendingCards.forEach(defendingCard -> allAttackingRanks.add(defendingCard.rank()));

        for(Card newAttackingCard: newAttackingCards) {

            Card.Rank rank = newAttackingCard.rank();

            if(!allAttackingRanks.contains(rank))
                throw new DurakGameException(
                        String.format("Rank: %s has not been used before. You can't play it.", rank)
                );
        }
    }

    @Override
    public String toString() {
        return "Table{" +
                "attackingCards=" + attackingCards +
                ", defendingCards=" + defendingCards +
                '}';
    }
}
