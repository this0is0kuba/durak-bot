package com.jrakus.game_state.validators;

import com.jrakus.game_state.components.Card;
import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;

import static com.jrakus.game_state.components.Card.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TableValidator {

    public void checkNewDefendingCards(
            boolean isAttackMove,
            List<Card> newDefendingCards,
            List<Card> attackingCardsToBeat,
            Suit trump
    ) {

        checkIfThereIsDefendMove(isAttackMove);

        if(newDefendingCards.size() != attackingCardsToBeat.size())
            throw new DurakGameInvalidStateException("The amount of defending cards is different than attacking cards");

        for(int i = 0; i < newDefendingCards.size(); i++) {

            Card attackingCard = attackingCardsToBeat.get(i);
            Card defendingCard = newDefendingCards.get(i);

           checkIfDefendingCardIsStronger(defendingCard, attackingCard, trump);
        }
    }

    public void checkNewAttackingCards(
            boolean isAttackMove,
            List<Card> newAttackingCards,
            List<Card> oldAttackingCards,
            List<Card> oldDefendingCards
    ) {

        checkIfThereIsAttackMove(isAttackMove);
        checkIfThereIsAtLeast1AttackingCard(newAttackingCards);
        checkIfTheLimitOfCardsHasBeenExceeded(oldAttackingCards, newAttackingCards);

        boolean isItFirstAttack = oldAttackingCards.isEmpty();

        if (isItFirstAttack) {
            checkIfCardsHaveTheSameRank(newAttackingCards);
        } else {
            checkIfNewAttackingCardsAreValid(newAttackingCards, oldAttackingCards, oldDefendingCards);
        }
    }

    private void checkIfThereIsDefendMove(boolean isAttackMove) {
        if(isAttackMove)
            throw new DurakGameInvalidStateException("There is an attack turn now! You can't defend now.");
    }

    private void checkIfThereIsAttackMove(boolean isAttackMove) {
        if(!isAttackMove)
            throw new DurakGameInvalidStateException("There is a defend turn now! You can't attack.");
    }

    private void checkIfThereIsAtLeast1AttackingCard(List<Card> attackingCards) {
        if(attackingCards.isEmpty())
            throw new DurakGameInvalidStateException("You can't attack with 0 cards.");
    }

    private void checkIfTheLimitOfCardsHasBeenExceeded(List<Card> oldAttackingCards, List<Card> newAttackingCards) {
        int newNumberOfCardsOnTable = oldAttackingCards.size() + newAttackingCards.size();

        if (newNumberOfCardsOnTable > 5)
            throw new DurakGameInvalidStateException("Exceeded limit of attacking cards on table.");
    }

    private void checkIfNewAttackingCardsAreValid(
            List<Card> newAttackingCards,
            List<Card> oldAttackingCards,
            List<Card> oldDefendingCards
    ) {

        Set<Rank> allAttackingRanks = new HashSet<>();

        oldAttackingCards.forEach(attackingCard -> allAttackingRanks.add(attackingCard.rank()));
        oldDefendingCards.forEach(defendingCard -> allAttackingRanks.add(defendingCard.rank()));

        for(Card newAttackingCard: newAttackingCards) {

            Rank rank = newAttackingCard.rank();

            if(!allAttackingRanks.contains(rank))
                throw new DurakGameInvalidStateException(
                        String.format("Rank: %s has not been used before. You can't play it.", rank)
                );
        }
    }

    private void checkIfCardsHaveTheSameRank(List<Card> cards) {

        Set<Rank> ranks = cards.stream().map(Card::rank).collect(Collectors.toSet());

        if(ranks.size() > 1) {
            throw new DurakGameInvalidStateException("Played cards do not have the same ranks!");
        }
    }

    private void checkIfDefendingCardIsStronger(Card defendingCard, Card attackingCard, Suit trump) {
        boolean isAttackingCardStronger = (compareCards(attackingCard, defendingCard, trump) > 0);

        if(isAttackingCardStronger)
            throw new DurakGameInvalidStateException(String.format(
                    "Attacking card %s is stronger than defending card %s",
                    attackingCard,
                    defendingCard
            ));
    }

    private int compareCards(Card card1, Card card2, Suit trump) {

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
