package com.jrakus.game_logic;

import com.jrakus.game_state.components.Card;

import java.util.List;
import java.util.Objects;

public class PublicState {

    private final List<Card> attackingCards;
    private final List<Card> defendingCards;
    private final List<Card> yourHand;
    private final List<Card> discardPile;
    private final List<Card> certainOpponentHand;
    private final Card trumpCard;

    private final int numberOfCardsOnOpponentHand;
    private final int numberOfCardsOnDeck;

    private PublicState(PublicStateBuilder builder) {
        this.attackingCards = List.copyOf(builder.attackingCards);
        this.defendingCards = List.copyOf(builder.defendingCards);
        this.yourHand = List.copyOf(builder.yourHand);
        this.discardPile = List.copyOf(builder.discardPile);
        this.certainOpponentHand = List.copyOf(builder.certainOpponentHand);
        this.trumpCard = builder.trumpCard;
        this.numberOfCardsOnOpponentHand = builder.numberOfCardsOnOpponentHand;
        this.numberOfCardsOnDeck = builder.numberOfCardsOnDeck;
    }

    public static class PublicStateBuilder {

        private List<Card> attackingCards;
        private List<Card> defendingCards;
        private List<Card> yourHand;
        private List<Card> discardPile;
        private List<Card> certainOpponentHand;
        private Card trumpCard;

        // Use boxed types to detect "not set"
        private Integer numberOfCardsOnOpponentHand;
        private Integer numberOfCardsOnDeck;

        public PublicStateBuilder attackingCards(List<Card> attackingCards) {
            this.attackingCards = Objects.requireNonNull(attackingCards, "attackingCards");
            return this;
        }

        public PublicStateBuilder defendingCards(List<Card> defendingCards) {
            this.defendingCards = Objects.requireNonNull(defendingCards, "defendingCards");
            return this;
        }

        public PublicStateBuilder yourHand(List<Card> yourHand) {
            this.yourHand = Objects.requireNonNull(yourHand, "yourHand");
            return this;
        }

        public PublicStateBuilder discardPile(List<Card> discardPile) {
            this.discardPile = Objects.requireNonNull(discardPile, "discardPile");
            return this;
        }

        public PublicStateBuilder certainOpponentHand(List<Card> certainOpponentHand) {
            this.certainOpponentHand = Objects.requireNonNull(certainOpponentHand, "certainOpponentHand");
            return this;
        }

        public PublicStateBuilder trumpCard(Card trumpCard) {
            this.trumpCard = Objects.requireNonNull(trumpCard, "trumpCard");
            return this;
        }

        public PublicStateBuilder numberOfCardsOnOpponentHand(int numberOfCardsOnOpponentHand) {
            if (numberOfCardsOnOpponentHand < 0) {
                throw new IllegalArgumentException("numberOfCardsOnOpponentHand cannot be negative");
            }
            this.numberOfCardsOnOpponentHand = numberOfCardsOnOpponentHand;
            return this;
        }

        public PublicStateBuilder numberOfCardsOnDeck(int numberOfCardsOnDeck) {
            if (numberOfCardsOnDeck < 0) {
                throw new IllegalArgumentException("numberOfCardsOnDeck cannot be negative");
            }
            this.numberOfCardsOnDeck = numberOfCardsOnDeck;
            return this;
        }

        public PublicState build() {
            validateRequired();
            return new PublicState(this);
        }

        private void validateRequired() {
            if (attackingCards == null) {
                throw new IllegalStateException("attackingCards not initialized");
            }
            if (defendingCards == null) {
                throw new IllegalStateException("defendingCards not initialized");
            }
            if (yourHand == null) {
                throw new IllegalStateException("yourHand not initialized");
            }
            if (discardPile == null) {
                throw new IllegalStateException("discardPile not initialized");
            }
            if (certainOpponentHand == null) {
                throw new IllegalStateException("certainOpponentHand not initialized");
            }
            if (trumpCard == null) {
                throw new IllegalStateException("trumpCard not initialized");
            }
            if (numberOfCardsOnOpponentHand == null) {
                throw new IllegalStateException("numberOfCardsOnOpponentHand not initialized");
            }
            if (numberOfCardsOnDeck == null) {
                throw new IllegalStateException("numberOfCardsOnDeck not initialized");
            }
        }
    }
}