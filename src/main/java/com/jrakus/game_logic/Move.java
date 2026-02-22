package com.jrakus.game_logic;

import com.jrakus.game_state.components.Card;

import java.util.List;

public class Move {

    private final MoveKind moveKind;
    private final List<Card> cards;

    public enum MoveKind {ATTACK, DEFEND, STOP_ATTACK, TAKE_CARDS}

    public Move(MoveKind moveKind, List<Card> cards) {
        this.moveKind = moveKind;
        this.cards = cards;
    }

    public MoveKind getMoveKind() {
        return moveKind;
    }

    public List<Card> getCards() {
        return cards;
    }
}
