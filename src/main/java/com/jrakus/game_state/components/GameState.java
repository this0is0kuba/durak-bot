package com.jrakus.game_state.components;

public class GameState {

    private GameStateEnum state;

    public enum GameStateEnum {
        ACTIVE_GAME,
        PLAYER_1_WON,
        PLAYER_2_WON,
        DRAW
    }

    public GameState() {
        this.state = GameStateEnum.ACTIVE_GAME;
    }

    public GameState(GameStateEnum internalState) {
        this.state = internalState;
    }

    public GameStateEnum getInternalState() {
        return state;
    }

    public void checkGameStateAfterTakingCards(
            DurakGamePlayer currentAttackingPlayer,
            DurakGamePlayer player1
    ) {
        boolean isTheEndOfTheGame = currentAttackingPlayer.showCardsOnHand().isEmpty();

        if(isTheEndOfTheGame) {
            chooseWinner(currentAttackingPlayer, player1);
        }
    }

    public void checkGameStateAfterAttack(
            DurakGamePlayer currentAttackingPlayer,
            DurakGamePlayer startingPlayer,
            DurakGamePlayer player1
    ) {
        boolean attackerStartedTheGame = (currentAttackingPlayer == startingPlayer);
        boolean attackingPlayerHasNoCards = currentAttackingPlayer.showCardsOnHand().isEmpty();

        if(attackingPlayerHasNoCards && !attackerStartedTheGame) {
            chooseWinner(currentAttackingPlayer, player1);
        }
    }

    public void checkGameStateAfterDefend(
            DurakGamePlayer currentDefendingPlayer,
            DurakGamePlayer currentAttackingPlayer,
            DurakGamePlayer player1
    ) {
        boolean attackingPlayerHasNoCards = currentAttackingPlayer.showCardsOnHand().isEmpty();
        boolean defendingPlayerHasNoCards = currentDefendingPlayer.showCardsOnHand().isEmpty();

        if (attackingPlayerHasNoCards && defendingPlayerHasNoCards) {
            state = GameStateEnum.DRAW;
        }
        else if(attackingPlayerHasNoCards) {
            chooseWinner(currentAttackingPlayer, player1);
        } else if(defendingPlayerHasNoCards) {
            chooseWinner(currentDefendingPlayer, player1);
        }
    }

    private void chooseWinner(DurakGamePlayer winner, DurakGamePlayer player1) {
        if (winner == player1) {
            state = GameStateEnum.PLAYER_1_WON;
        } else {
            state = GameStateEnum.PLAYER_2_WON;
        }
    }

    @Override
    public String toString() {
        return "GameState{" +
                "state=" + state +
                '}';
    }
}
