package com.jrakus.game_logic;

import com.jrakus.game_state.components.DurakGamePlayer;

public interface Player {

    public DurakGamePlayer getDurakGamePlayer();
    public Move defend();
    public Move attack();
}
