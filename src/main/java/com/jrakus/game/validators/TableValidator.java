package com.jrakus.game.validators;

import com.jrakus.game.components.Card;
import com.jrakus.game.exceptions.DurakGameException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableValidator {

    public void checkNewDefendingCards(
            List<Card> newDefendingCards,
            List<Card> attackingCardsToBeat,
            Card.Suit trump
    ) {

        if(newDefendingCards.size() != attackingCardsToBeat.size())
            throw new DurakGameException("The amount of defending cards is different than attacking cards");

        for(int i = 0; i < newDefendingCards.size(); i++) {

            Card attackingCard = attackingCardsToBeat.get(i);
            Card defendingCard = newDefendingCards.get(i);

           checkIfDefendingCardIsStronger(defendingCard, attackingCard, trump);
        }
    }

    public void checkNewAttackingCards(
            List<Card> newAttackingCards,
            List<Card> oldAttackingCards,
            List<Card> oldDefendingCards
    ) {

        int newNumberOfCardsOnTable = oldAttackingCards.size() + newAttackingCards.size();

        if (newNumberOfCardsOnTable > 5)
            throw new DurakGameException("Exceeded limit of attacking cards on table.");

        boolean areAnyAttackingCardsOnTable = !oldAttackingCards.isEmpty();

        if (areAnyAttackingCardsOnTable) {
            checkIfNewAttackingCardsAreValid(newAttackingCards, oldAttackingCards, oldDefendingCards);
        }
    }

    private void checkIfNewAttackingCardsAreValid(
            List<Card> newAttackingCards,
            List<Card> oldAttackingCards,
            List<Card> oldDefendingCards
    ) {

        Set<Card.Rank> allAttackingRanks = new HashSet<>();

        oldAttackingCards.forEach(attackingCard -> allAttackingRanks.add(attackingCard.rank()));
        oldDefendingCards.forEach(defendingCard -> allAttackingRanks.add(defendingCard.rank()));

        for(Card newAttackingCard: newAttackingCards) {

            Card.Rank rank = newAttackingCard.rank();

            if(!allAttackingRanks.contains(rank))
                throw new DurakGameException(
                        String.format("Rank: %s has not been used before. You can't play it.", rank)
                );
        }
    }

    private void checkIfDefendingCardIsStronger(Card defendingCard, Card attackingCard, Card.Suit trump) {
        boolean isAttackingCardStronger = (compareCards(attackingCard, defendingCard, trump) > 0);

        if(isAttackingCardStronger)
            throw new DurakGameException(String.format(
                    "Attacking card %s is stronger than defending card %s",
                    attackingCard,
                    defendingCard
            ));
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
}
