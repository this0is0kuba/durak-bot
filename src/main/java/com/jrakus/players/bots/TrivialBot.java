package com.jrakus.players.bots;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.Player;
import com.jrakus.players.bots.cardSelector.CardSelector;
import com.jrakus.players.game_elements.Move;
import com.jrakus.players.game_elements.PublicState;

import java.util.*;

import static com.jrakus.players.game_elements.Move.MoveKind.*;

public class TrivialBot implements Player {

    private final CardSelector cardSelector = new CardSelector();

    @Override
    public Move defend(PublicState publicState) {

        List<Card> newAttackingCards = getNewAttackingCards(
                publicState.getAttackingCards(),
                publicState.getDefendingCards()
        );

        List<Card> cardsToDefend = findCardsToDefend(newAttackingCards, publicState);

        if(cardsToDefend.isEmpty()) {
            return new Move(TAKE_CARDS);
        }

        return new Move(DEFEND, cardsToDefend);
    }

    @Override
    public Move attack(PublicState publicState) {

        List<Card> cardsToAttack = findCardsToAttack(publicState);

        if (cardsToAttack.isEmpty()) {
            return new Move(STOP_ATTACK);
        } else {
            return new Move(ATTACK, cardsToAttack);
        }

    }

    // Bot does not need to see the game after moves
    @Override
    public void displayCurrentState(PublicState publicState) {}

    private List<Card> getNewAttackingCards(List<Card> attackingCards, List<Card> defendingCards) {

        List<Card> newAttackingCards = new ArrayList<>();

        for (int i = defendingCards.size(); i < attackingCards.size(); i++) {
            newAttackingCards.add(attackingCards.get(i));
        }

        return newAttackingCards;
    }

    private List<Card> findCardsToDefend(List<Card> newAttackingCards, PublicState publicState) {
        List<Card> cardsToDefend = new ArrayList<>(newAttackingCards.size());

        for(Card card: newAttackingCards) {

            List<Card> unusedCards = publicState.getYourHand().stream().filter(
                    yourCard -> !cardsToDefend.contains(yourCard)
            ).toList();

            Optional<Card> defendingCard = cardSelector.findTheWeakestCardToDefend(
                    card,
                    publicState.getTrumpCard().suit(),
                    unusedCards
            );

            if (defendingCard.isPresent()) {
                cardsToDefend.add(card);
            } else {
                return List.of();
            }
        }

        return cardsToDefend;
    }

    private List<Card> findCardsToAttack(PublicState publicState) {

        boolean isFirstAttack = publicState.getAttackingCards().isEmpty();

        if (isFirstAttack) {
            return findCardsForFirstAttack(publicState);

        } else {
            return findCardsForNextAttack(publicState);
        }
    }

    private List<Card> findCardsForFirstAttack(PublicState publicState) {

        return cardSelector.findTheWeakestCardsToAttack(
                publicState.getYourHand(),
                publicState.getNumberOfCardsOnOpponentHand(),
                publicState.getTrumpCard().suit()
        );
    }

    private List<Card> findCardsForNextAttack(PublicState publicState) {

        return cardSelector.findPossibleCardsToAttack(
                publicState.getYourHand(),
                publicState.getNumberOfCardsOnOpponentHand(),
                publicState.getAttackingCards(),
                publicState.getDefendingCards(),
                publicState.getTrumpCard().suit(),
                publicState.getNumberOfCardsOnDeck()
        );
    }
}
