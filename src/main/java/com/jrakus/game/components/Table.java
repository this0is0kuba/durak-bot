package com.jrakus.game.components;

import java.util.*;

public class Table {
    private final List<Card> attackingCards = new ArrayList<>();
    private final List<Card> defendingCards = new ArrayList<>();

    public void addAttackingCards(List<Card> newAttackingCards) {
        attackingCards.addAll(newAttackingCards);
    }

    public void addDefendingCards(List<Card> newDefendingCards, Card.Suit trump) {

        checkDefendingCards(attackingCards, newDefendingCards, trump);
        defendingCards.addAll(newDefendingCards);
    }

    public Set<Card> clearTable() {

        int numberOfAllCards = attackingCards.size() + defendingCards.size();
        List<Card> allCards = new ArrayList<>(numberOfAllCards);

        allCards.addAll(attackingCards);
        allCards.addAll(defendingCards);

        attackingCards.clear();
        defendingCards.clear();

        return new HashSet<>(allCards);
    }

    public List<Card> showAttackingCards() {
        return attackingCards;
    }

    public List<Card> showDefendingCards() {
        return defendingCards;
    }

    private void checkDefendingCards(List<Card> attackingCards, List<Card> defendingCards, Card.Suit trump) {
        if(attackingCards.size() != defendingCards.size())
            throw new RuntimeException("The amount of defending cards is different than attacking cards");

        for(int i = 0; i < attackingCards.size(); i++) {

            Card attackingCard = attackingCards.get(i);
            Card defendingCard = defendingCards.get(i);

            boolean isAttackingCardStronger = (compareCards(attackingCard, defendingCard, trump) > 0);

            if(isAttackingCardStronger)
                throw new RuntimeException(String.format(
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

    @Override
    public String toString() {
        return "Table{" +
                "attackingCards=" + attackingCards +
                ", defendingCards=" + defendingCards +
                '}';
    }
}
