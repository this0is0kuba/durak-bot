package com.jrakus.game_elements;

public interface Player {

    public Move defend(PublicState publicState);

    public Move attack(PublicState publicState);

    public void displayCurrentState(PublicState publicState);
}
