package com.jrakus.game_elements;

import com.jrakus.game_state.components.DurakGamePlayer;

public interface Player {

    public DurakGamePlayer getDurakGamePlayer();
    public Move defend(PublicState publicState);
    public Move attack(PublicState publicState);
}
