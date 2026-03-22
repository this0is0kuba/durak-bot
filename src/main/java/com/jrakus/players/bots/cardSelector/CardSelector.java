package com.jrakus.players.bots.cardSelector;

import com.jrakus.game_state.components.Card;

import java.util.*;

public class CardSelector {

    private static final int MAX_NUMBER_OF_CARDS_ON_TABLE = 5;

    public Optional<Card> findTheWeakestCardToDefend(
            Card attackingCard,
            Card.Suit trump,
            List<Card> yourCards
    ) {
        List<Card> cardsToDefend = getPossibleCardsToDefend(yourCards, attackingCard, trump);

        if(cardsToDefend.isEmpty()) {
            return Optional.empty();
        }

        Optional<Card> weakestNormalCard = getWeakestNonTrumpCard(cardsToDefend, trump);

        if (weakestNormalCard.isPresent()) {
            return weakestNormalCard;
        }

        return getWeakestTrumpCard(cardsToDefend, trump);
    }

    public List<Card> findTheWeakestCardsToAttack(
            List<Card> yourCards,
            int numberOfCardsOnOpponentHand,
            Card.Suit trump
    ) {

        List<Card> normalCards = getSortedNonTrumpCards(yourCards, trump);
        List<Card> trumpCards = getSortedTrumpCards(yourCards, trump);

        Card firstAttackingCard;

        if (!normalCards.isEmpty()) {
            firstAttackingCard = normalCards.getFirst();
        } else {
            // If there were not any normal cards it means we need use trump cards to attack - it must be at least one
            firstAttackingCard = trumpCards.getFirst();
        }

        // If there are more cards with the same rank that are not trump, we use it
        List<Card> cardsToAttack = getAllCardsWithTheSameRank(firstAttackingCard.rank(), normalCards);

        int numberOfCardsPossibleToPlay = getSmallerNumber(cardsToAttack.size(), numberOfCardsOnOpponentHand);
        return cardsToAttack.subList(0, numberOfCardsPossibleToPlay);
    }

    public List<Card> findPossibleCardsToAttack(
            List<Card> yourCards,
            int numberOfCardsOnOpponentHand,
            List<Card> attackingCards,
            List<Card> defendingCards,
            Card.Suit trump,
            int numberOfCardsOnDeck
    ) {

        Set<Card.Rank> ranksThatAreOnTable = getAllRanksOnTheTable(attackingCards, defendingCards);

        List<Card> cardsToAttack = getValidCardsToAttack(
                yourCards,
                trump,
                ranksThatAreOnTable,
                numberOfCardsOnDeck
        );

        int numberOfCardsPossibleToPlay = getNumberOfCardsPossibleToPlay(
                attackingCards,
                cardsToAttack,
                numberOfCardsOnOpponentHand
        );

        return cardsToAttack.subList(0, numberOfCardsPossibleToPlay);
    }

    private List<Card> getValidCardsToAttack(
            List<Card> yourCards,
            Card.Suit trump,
            Set<Card.Rank> ranksThatAreOnTable,
            int numberOfCardsOnDeck
    ) {
        List<Card> trumpCards = getSortedTrumpCards(yourCards, trump);
        List<Card> normalCards = getSortedNonTrumpCards(yourCards, trump);

        List<Card> trumpCardsPossibleToPlay = getCardsPossibleToPlay(trumpCards, ranksThatAreOnTable);
        List<Card> normalCardsPossibleToPlay = getCardsPossibleToPlay(normalCards, ranksThatAreOnTable);

        List<Card> cardsToAttack = new ArrayList<>(normalCardsPossibleToPlay);

        // Play trump cards only if there are less than 3 cards on the Deck
        if (numberOfCardsOnDeck <= 2) {
            cardsToAttack.addAll(trumpCardsPossibleToPlay);
        }

        return cardsToAttack;
    }

    private List<Card> getCardsPossibleToPlay(List<Card> cards, Set<Card.Rank> ranksThatAreOnTable) {
        return cards.stream().filter(
                c -> ranksThatAreOnTable.contains(c.rank())
        ).toList();
    }

    private Set<Card.Rank> getAllRanksOnTheTable(List<Card> attackingCards, List<Card> defendingCards) {
        Set<Card.Rank> ranksThatAreOnTable = new HashSet<>(
                attackingCards.stream().map(Card::rank).toList()
        );

        ranksThatAreOnTable.addAll(
                defendingCards.stream().map(Card::rank).toList()
        );

        return ranksThatAreOnTable;
    }

    private List<Card> getPossibleCardsToDefend(
            List<Card> yourCards,
            Card attackingCard,
            Card.Suit trump
    ) {

        return yourCards.stream().filter(
                card -> card.isCardStrongerThan(attackingCard, trump)
        ).toList();
    }

    private Optional<Card> getWeakestTrumpCard(List<Card> cards, Card.Suit trump) {

        List<Card> trumpCards = getSortedTrumpCards(cards, trump);

        if (trumpCards.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(trumpCards.getFirst());
    }

    private Optional<Card> getWeakestNonTrumpCard(List<Card> cards, Card.Suit trump) {

        List<Card> normalCards = getSortedNonTrumpCards(cards, trump);

        if (normalCards.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(normalCards.getFirst());
    }

    private List<Card> getSortedTrumpCards(List<Card> cards, Card.Suit trump) {
        List<Card> trumpCards = new ArrayList<>(cards.stream().filter(
                card -> card.suit() == trump
        ).toList());

        trumpCards.sort(Comparator.comparingInt(c -> c.rank().getRankValue()));

        return trumpCards;
    }

    private List<Card> getSortedNonTrumpCards(List<Card> cards, Card.Suit trump) {

        List<Card> normalCards = new ArrayList<>(cards.stream().filter(
                card -> card.suit() != trump
        ).toList());

        normalCards.sort(Comparator.comparingInt(c -> c.rank().getRankValue()));

        return normalCards;
    }

    private List<Card> getAllCardsWithTheSameRank(Card.Rank rank, List<Card> cards) {
        return cards.stream().filter(
                c -> c.rank() == rank
        ).toList();
    }

    private int getSmallerNumber(int number1, int number2) {
        return Math.min(
                number1,
                number2
        );
    }

    private int getSmallerNumber(int number1, int number2, int number3) {
        return Math.min(
                number1,
                getSmallerNumber(number2, number3)
        );
    }

    private int getNumberOfCardsPossibleToPlay(
            List<Card> attackingCards,
            List<Card> cardsToAttack,
            int numberOfCardsOnOpponentHand
    ) {
        int numberOfCardsThatFitOnTable = MAX_NUMBER_OF_CARDS_ON_TABLE - attackingCards.size();

        return getSmallerNumber(numberOfCardsThatFitOnTable, numberOfCardsOnOpponentHand, cardsToAttack.size());
    }
}
