package com.jrakus.game_elements;

import static com.jrakus.game_elements.Move.*;

public interface Player {

    public Move defend(PublicState publicState);
    public Move attack(PublicState publicState);
    public void displayCurrentState(PublicState publicState, MoveKind state, boolean isYourMove);
}
