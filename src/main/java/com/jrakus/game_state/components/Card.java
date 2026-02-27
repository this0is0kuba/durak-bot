package com.jrakus.game_state.components;

import org.jetbrains.annotations.NotNull;

public record Card(Suit suit, Rank rank) {

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

    @Override
    @NotNull
    public String toString() {
        return "Card{" +
                "suit=" + suit +
                ", rank=" + rank +
                '}';
    }
}
