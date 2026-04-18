package com.jrakus.players.bots.algorithms;

import com.jrakus.game_state.DurakGame;
import com.jrakus.game_state.components.GameState;
import com.jrakus.players.bots.move_founder.MoveFounder;
import com.jrakus.players.game_elements.PublicState;
import com.jrakus.players.game_elements.utils.PublicStateExtractor;

import java.util.List;

public class MinMaxAlgorithm {

    private final MoveFounder moveFounder;
    private final PublicStateExtractor publicStateExtractor;

    public record ActionWithScore(MoveFounder.Action action, int score) {}

    public MinMaxAlgorithm() {
        this(new MoveFounder(), new PublicStateExtractor());
    }

    public MinMaxAlgorithm(MoveFounder moveFounder, PublicStateExtractor publicStateExtractor) {
        this.moveFounder = moveFounder;
        this.publicStateExtractor = publicStateExtractor;
    }

    public ActionWithScore miniMax(
            DurakGame durakGame,
            int depth,
            boolean isPlayer1Maximized,
            MoveFounder.Action action
    ) {

        if (depth == 0 || durakGame.getGameState() != GameState.GameStateEnum.ACTIVE_GAME) {
            int score = simpleGoalFunction(durakGame.getGameState(), isPlayer1Maximized);
            return new ActionWithScore(action, score);
        }

        boolean isPlayer1Active = durakGame.isPlayer1Active();
        PublicState publicState = publicStateExtractor.extractPublicState(durakGame, isPlayer1Active);

        List<MoveFounder.Action> possibleActions = getPossibleActions(durakGame.isAttackMoveNow(), publicState);

        MoveFounder.Action theBestAction = null;
        int maxEval = Integer.MIN_VALUE;

        if ((isPlayer1Active && isPlayer1Maximized) || (!isPlayer1Active && !isPlayer1Maximized)) {

            for (MoveFounder.Action possibleAction: possibleActions) {
                DurakGame newDurakGame = move(new DurakGame(durakGame), possibleAction);
                ActionWithScore actionWithScore = miniMax(newDurakGame, --depth, isPlayer1Maximized, possibleAction);

                if (actionWithScore.score > maxEval) {
                    maxEval = actionWithScore.score;
                    theBestAction = possibleAction;
                }
            }

        } else {

            int minEval = Integer.MAX_VALUE;

            for (MoveFounder.Action possibleAction: possibleActions) {
                DurakGame newDurakGame = move(new DurakGame(durakGame), action);
                ActionWithScore actionWithScore = miniMax(newDurakGame, --depth, isPlayer1Maximized, possibleAction);

                if (actionWithScore.score < minEval) {
                    minEval = actionWithScore.score;
                    theBestAction = possibleAction;
                }
            }

        }

        return new ActionWithScore(theBestAction, maxEval);
    }

    private int simpleGoalFunction(GameState.GameStateEnum gameState, boolean isPlayerNumber1) {

        if (gameState == GameState.GameStateEnum.ACTIVE_GAME)
            return 0;

        if (gameState == GameState.GameStateEnum.DRAW)
            return 0;

        if (gameState == GameState.GameStateEnum.PLAYER_1_WON && isPlayerNumber1)
            return 1;

        if (gameState == GameState.GameStateEnum.PLAYER_2_WON && !isPlayerNumber1)
            return 1;

        return -1;

    }

    private List<MoveFounder.Action> getPossibleActions(boolean isAttackMoveNow, PublicState publicState) {

        if (isAttackMoveNow) {
            return moveFounder.getAllPossibleAttacks(
                    publicState.getAttackingCards(),
                    publicState.getDefendingCards(),
                    publicState.getYourHand(),
                    publicState.getNumberOfCardsOnOpponentHand()
            );
        } else {
            return moveFounder.getAllPossibleDefends(
                    publicState.getAttackingCards(),
                    publicState.getDefendingCards(),
                    publicState.getYourHand(),
                    publicState.getTrumpCard().suit()
            );
        }
    }

    private DurakGame move(DurakGame durakGame, MoveFounder.Action action) {

        if (durakGame.isAttackMoveNow()) {
            if (action.cardsToPlay().isEmpty()) {
                durakGame.stopAttack();
            }
            else {
                durakGame.doAttack(action.cardsToPlay());
            }
        } else {
            if (action.cardsToPlay().isEmpty()) {
                durakGame.takeCardsFromTable();
            }
            else {
                durakGame.doDefend(action.cardsToPlay());
            }
        }

        return durakGame;
    }
}
