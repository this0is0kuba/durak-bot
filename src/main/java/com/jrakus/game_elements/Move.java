package com.jrakus.game_elements;

import com.jrakus.game_state.components.Card;

import java.io.Serializable;
import java.util.List;

public record Move(MoveKind moveKind, List<Card> cards) implements Serializable {

    public enum MoveKind {ATTACK, DEFEND, STOP_ATTACK, TAKE_CARDS}

    public Move(MoveKind moveKind) {
        this(moveKind, List.of());
    }
}
