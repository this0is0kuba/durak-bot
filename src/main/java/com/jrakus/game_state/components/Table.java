package com.jrakus.game_state.components;

import com.jrakus.game_state.validators.TableValidator;

import java.util.*;

public class Table {

    private final List<Card> attackingCards = new ArrayList<>();
    private final List<Card> defendingCards = new ArrayList<>();

    private boolean isAttackMove = true;

    private final TableValidator tableValidator;

    public Table(TableValidator tableValidator) {
        this.tableValidator = tableValidator;
    }

    public Table() {
        this(new TableValidator());
    }

    public void addAttackingCards(List<Card> newAttackingCards) {
        tableValidator.checkNewAttackingCards(isAttackMove, newAttackingCards, attackingCards, defendingCards);
        attackingCards.addAll(newAttackingCards);

        isAttackMove = false;
    }

    public void addDefendingCards(List<Card> newDefendingCards, Card.Suit trump) {
        tableValidator.checkNewDefendingCards(isAttackMove, newDefendingCards, getUndefendedCards(), trump);
        defendingCards.addAll(newDefendingCards);

        isAttackMove = true;
    }

    public List<Card> clearTable() {

        int numberOfAllCards = attackingCards.size() + defendingCards.size();
        List<Card> allCards = new ArrayList<>(numberOfAllCards);

        allCards.addAll(attackingCards);
        allCards.addAll(defendingCards);

        attackingCards.clear();
        defendingCards.clear();

        isAttackMove = true;

        return allCards;
    }

    public List<Card> showAttackingCards() {
        return attackingCards;
    }

    public List<Card> showDefendingCards() {
        return defendingCards;
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
