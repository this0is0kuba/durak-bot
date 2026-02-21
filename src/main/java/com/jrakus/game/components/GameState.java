package com.jrakus.game.components;

public class GameState {

    private GameStateEnum state = GameStateEnum.ACTIVE_GAME;

    public enum GameStateEnum {
        ACTIVE_GAME,
        PLAYER_1_WON,
        PLAYER_2_WON,
        DRAW
    }

    public GameStateEnum getInternalState() {
        return state;
    }

    public void checkGameStateAfterTakingCards(
            Player currentAttackingPlayer,
            Player player1
    ) {
        boolean isTheEndOfTheGame = currentAttackingPlayer.showCardsOnHand().isEmpty();

        if(isTheEndOfTheGame) {
            if (currentAttackingPlayer == player1) {
                state = GameStateEnum.PLAYER_1_WON;
            } else {
                state = GameStateEnum.PLAYER_2_WON;
            }
        }
    }

    public void checkGameStateAfterAttack(
            Player currentAttackingPlayer,
            Player startingPlayer,
            Player player1
    ) {
        boolean attackerStartedTheGame = (currentAttackingPlayer == startingPlayer);
        boolean attackingPlayerHasNoCards = currentAttackingPlayer.showCardsOnHand().isEmpty();

        if(attackingPlayerHasNoCards && !attackerStartedTheGame) {
            if (currentAttackingPlayer == player1) {
                state = GameStateEnum.PLAYER_1_WON;
            } else {
                state = GameStateEnum.PLAYER_2_WON;
            }
        }
    }

    public void checkGameStateAfterDefend(
            Player currentDefendingPlayer,
            Player currentAttackingPlayer,
            Player player1
    ) {
        boolean attackingPlayerHasNoCards = currentDefendingPlayer.showCardsOnHand().isEmpty();
        boolean defendingPlayerHasNoCards = currentDefendingPlayer.showCardsOnHand().isEmpty();

        if (attackingPlayerHasNoCards && defendingPlayerHasNoCards) {
            state = GameStateEnum.DRAW;
        }

        if(attackingPlayerHasNoCards) {
            if (currentAttackingPlayer == player1) {
                state = GameStateEnum.PLAYER_1_WON;
            } else {
                state = GameStateEnum.PLAYER_2_WON;
            }
        }
    }
}
