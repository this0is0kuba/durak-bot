package com.jrakus.players.bots;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.Player;
import com.jrakus.players.game_elements.Move;
import com.jrakus.players.game_elements.PublicState;

import java.util.*;

import static com.jrakus.players.game_elements.Move.MoveKind.*;

public class TrivialBot implements Player {

    @Override
    public Move defend(PublicState publicState) {

        List<Card> newAttackingCards = getNewAttackingCards(
                publicState.getAttackingCards(),
                publicState.getDefendingCards()
        );

        List<Card> cardsToDefend = new ArrayList<>(newAttackingCards.size());

        for(Card card: newAttackingCards) {

            Optional<Card> defendingCard = findTheWeakestCardToDefend(
                    card,
                    publicState.getTrumpCard().suit(),
                    publicState.getYourHand(),
                    cardsToDefend
            );

            if (defendingCard.isPresent()) {
                cardsToDefend.add(card);
            } else {
                return new Move(TAKE_CARDS);
            }
        }

        return new Move(DEFEND, cardsToDefend);
    }

    @Override
    public Move attack(PublicState publicState) {

        List<Card> cardsToAttack = new ArrayList<>();
        boolean isFirstAttack = publicState.getAttackingCards().isEmpty();

        if (isFirstAttack) {
            cardsToAttack.addAll(
                    findTheWeakestCardsToAttack(
                            publicState.getYourHand(),
                            publicState.getNumberOfCardsOnOpponentHand(),
                            publicState.getTrumpCard().suit()
                    )
            );

        } else {
            cardsToAttack.addAll(
                    findPossibleCardsToAttack(
                            publicState.getYourHand(),
                            publicState.getNumberOfCardsOnOpponentHand(),
                            publicState.getAttackingCards(),
                            publicState.getDefendingCards(),
                            publicState.getTrumpCard().suit(),
                            publicState.getNumberOfCardsOnDeck()
                    )
            );
        }

        if (cardsToAttack.isEmpty()) {
            return new Move(STOP_ATTACK);
        } else {
            return new Move(ATTACK, cardsToAttack);
        }

    }

    @Override
    public void displayCurrentState(PublicState publicState) {}

    private List<Card> getNewAttackingCards(List<Card> attackingCards, List<Card> defendingCards) {

        List<Card> newAttackingCards = new ArrayList<>();

        for (int i = defendingCards.size(); i < attackingCards.size(); i++) {
            newAttackingCards.add(attackingCards.get(i));
        }

        return newAttackingCards;
    }

    private Optional<Card> findTheWeakestCardToDefend(
            Card attackingCard,
            Card.Suit trump,
            List<Card> yourCards,
            List<Card> alreadyUsedCards
    ) {
        List<Card> cardsToDefend = new ArrayList<>();

        for (Card card: yourCards) {

            boolean isCardNotUsed = !alreadyUsedCards.contains(card);
            boolean isCardStronger = card.isCardStrongerThan(attackingCard, trump);

            if(isCardNotUsed && isCardStronger) {
                cardsToDefend.add(card);
            }
        }

        if(cardsToDefend.isEmpty()) {
            return Optional.empty();
        }

        List<Card> trumpCardsToDefend = new ArrayList<>(cardsToDefend.stream().filter(
                card -> card.suit() == trump
        ).toList());

        List<Card> normalCardsToDefend = new ArrayList<>(cardsToDefend.stream().filter(
                card -> card.suit() != trump
        ).toList());

        trumpCardsToDefend.sort(Comparator.comparingInt(c -> c.rank().getRankValue()));
        normalCardsToDefend.sort(Comparator.comparingInt(c -> c.rank().getRankValue()));

        if(normalCardsToDefend.isEmpty()) {
            return Optional.of(trumpCardsToDefend.getFirst());
        } else {
            return Optional.of(normalCardsToDefend.getFirst());
        }
    }

    private List<Card> findTheWeakestCardsToAttack(
            List<Card> yourCards,
            int numberOfCardsOnOpponentHand,
            Card.Suit trump
    ) {

        List<Card> trumpCards = new ArrayList<>(yourCards.stream().filter(
                card -> card.suit() == trump
        ).toList());

        List<Card> normalCards = new ArrayList<>(yourCards.stream().filter(
                card -> card.suit() != trump
        ).toList());

        trumpCards.sort(Comparator.comparingInt(c -> c.rank().getRankValue()));
        normalCards.sort(Comparator.comparingInt(c -> c.rank().getRankValue()));

        Card firstAttackingCard;

        if (!normalCards.isEmpty()) {
            firstAttackingCard = normalCards.getFirst();
        } else {
            firstAttackingCard = trumpCards.getFirst();
        }

        // If there are more cards with the same rank that are not trump, we use it
        List<Card> cardsToAttack = normalCards.stream().filter(
                c -> c.rank() == firstAttackingCard.rank()
        ).toList();

        if(cardsToAttack.size() > numberOfCardsOnOpponentHand) {
            cardsToAttack = cardsToAttack.subList(0, numberOfCardsOnOpponentHand);
        }

        return cardsToAttack;
    }

    private List<Card> findPossibleCardsToAttack(
            List<Card> yourCards,
            int numberOfCardsOnOpponentHand,
            List<Card> attackingCards,
            List<Card> defendingCards,
            Card.Suit trump,
            int numberOfCardsOnDeck
    ) {

        List<Card.Rank> ranksThatAreOnTable = new ArrayList<>(
                attackingCards.stream().map(Card::rank).toList()
        );

        ranksThatAreOnTable.addAll(
                defendingCards.stream().map(Card::rank).toList()
        );

        List<Card> trumpCards = new ArrayList<>(yourCards.stream().filter(
                card -> card.suit() == trump
        ).toList());

        List<Card> normalCards = new ArrayList<>(yourCards.stream().filter(
                card -> card.suit() != trump
        ).toList());

        trumpCards.sort(Comparator.comparingInt(c -> c.rank().getRankValue()));
        normalCards.sort(Comparator.comparingInt(c -> c.rank().getRankValue()));

        List<Card> trumpCardsPossibleToPlay = trumpCards.stream().filter(
                c -> ranksThatAreOnTable.contains(c.rank())
        ).toList();

        List<Card> normalCardsPossibleToPlay = trumpCards.stream().filter(
                c -> ranksThatAreOnTable.contains(c.rank())
        ).toList();

        List<Card> cardsToAttack = new ArrayList<>(normalCardsPossibleToPlay);

        // Play trump cards only if there are less than 3 cards in Deck
        if (numberOfCardsOnDeck < 3) {
            cardsToAttack.addAll(trumpCardsPossibleToPlay);
        }

        int maxNumberOfCardsOnTable = 5 - attackingCards.size();

        int maxNumberOfNewCards = Math.min(
                Math.min(
                    maxNumberOfCardsOnTable,
                    numberOfCardsOnOpponentHand
                ),
                cardsToAttack.size()
        );

        return cardsToAttack.subList(0, maxNumberOfNewCards);
    }
}
