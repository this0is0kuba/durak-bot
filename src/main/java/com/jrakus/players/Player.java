package com.jrakus.players;

import com.jrakus.players.game_elements.Move;
import com.jrakus.players.game_elements.PublicState;

public interface Player {

    public Move defend(PublicState publicState);

    public Move attack(PublicState publicState);

    public void displayCurrentState(PublicState publicState);
}
