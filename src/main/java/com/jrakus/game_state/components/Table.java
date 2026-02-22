package com.jrakus.game_state.components;

import com.jrakus.game_state.validators.TableValidator;

import java.util.*;

public class Table {

    private final List<Card> attackingCards = new ArrayList<>();
    private final List<Card> defendingCards = new ArrayList<>();

    private final TableValidator tableValidator = new TableValidator();

    public void addAttackingCards(List<Card> newAttackingCards) {
        tableValidator.checkNewAttackingCards(newAttackingCards, attackingCards, defendingCards);
        attackingCards.addAll(newAttackingCards);
    }

    public void addDefendingCards(List<Card> newDefendingCards, Card.Suit trump) {
        tableValidator.checkNewDefendingCards(newDefendingCards, getUndefendedCards(), trump);
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

    private List<Card> getUndefendedCards() {
        int indexOfFirstUndefendedCard = defendingCards.size();
        int indexOfLastUndefendedCard = attackingCards.size();

        return attackingCards.subList(indexOfFirstUndefendedCard, indexOfLastUndefendedCard);
    }

    @Override
    public String toString() {
        return "Table{" +
                "attackingCards=" + attackingCards +
                ", defendingCards=" + defendingCards +
                '}';
    }
}
