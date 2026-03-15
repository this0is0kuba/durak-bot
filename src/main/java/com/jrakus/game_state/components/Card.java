package com.jrakus.game_state.components;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record Card(Suit suit, Rank rank) implements Serializable {

    public enum Suit {HEARTS, DIAMONDS, CLUBS, SPADES}

    public enum Rank {
        NINE(1), TEN(2), JACK(3), QUEEN(4), KING(5), ACE(6);

        private final int rankValue;

        Rank(int rankValue) {
            this.rankValue = rankValue;
        }

        public int getRankValue() {
            return rankValue;
        }
    }

    public boolean isCardStrongerThan(Card attackingCard, Suit trump) {
        int rankValueDefending = this.rank().getRankValue();
        int rankValueAttacking = attackingCard.rank().getRankValue();

        boolean isCardDefendingTrump = (this.suit() == trump);
        boolean isCardAttackingTrump = (attackingCard.suit() == trump);

        if (isCardAttackingTrump && isCardDefendingTrump) {
            return rankValueDefending - rankValueAttacking > 0;
        }

        if (isCardDefendingTrump) {
            return true;
        }

        if (isCardAttackingTrump) {
            return false;
        }

        Suit suitAttacking = attackingCard.suit();
        Suit suitDefending = this.suit();

        if (suitAttacking != suitDefending) {
            return false;
        }

        return rankValueDefending - rankValueAttacking > 0;
    }

    @Override
    @NotNull
    public String toString() {
        return "Card{" +
                "suit=" + suit +
                ", rank=" + rank +
                '}';
    }
}
