package com.jrakus.players.bots;

import com.jrakus.game_state.DurakGame;
import com.jrakus.game_state.components.*;
import com.jrakus.players.Player;
import com.jrakus.game_simulation.GameSimulator;
import com.jrakus.players.bots.durak_game_creator.DurakGameCreator;
import com.jrakus.players.bots.move_founder.MoveFounder;
import com.jrakus.players.game_elements.Move;
import com.jrakus.players.game_elements.PublicState;
import com.jrakus.playground.displays.Display;

import java.util.*;

import static com.jrakus.players.game_elements.Move.MoveKind.*;

public class SimpleMonteCarloBot implements Player {

    private Display display = null;

    private final MoveFounder moveFounder;
    private final GameSimulator gameSimulator;
    private final DurakGameCreator durakGameCreator;
    private final int numberOfIterations;

    public SimpleMonteCarloBot() {
        this(new MoveFounder(), new GameSimulator(), new DurakGameCreator(), 1000);
    }

    public SimpleMonteCarloBot(
            MoveFounder moveFounder,
            GameSimulator gameSimulator,
            DurakGameCreator durakGameCreator,
            int numberOfIterations
    ) {
        this.moveFounder = moveFounder;
        this.gameSimulator = gameSimulator;
        this.durakGameCreator = durakGameCreator;
        this.numberOfIterations = numberOfIterations;
    }

    public SimpleMonteCarloBot(
            MoveFounder moveFounder,
            GameSimulator gameSimulator,
            DurakGameCreator durakGameCreator,
            int numberOfIterations,
            Display display
    ) {
        this.moveFounder = moveFounder;
        this.gameSimulator = gameSimulator;
        this.durakGameCreator = durakGameCreator;
        this.numberOfIterations = numberOfIterations;
        this.display = display;
    }

    @Override
    public Move defend(PublicState publicState) {

        List<MoveFounder.Action> possibleActions = moveFounder.getAllPossibleDefends(
                publicState.getAttackingCards(),
                publicState.getDefendingCards(),
                publicState.getYourHand(),
                publicState.getTrumpCard().suit()
        );

        Map<MoveFounder.Action, Integer> actionToNumberOfWins = new HashMap<>(possibleActions.size());

        for (MoveFounder.Action action: possibleActions) {
            actionToNumberOfWins.put(action, 0);
        }

        for(int i = 0; i < numberOfIterations; i++) {
            for (MoveFounder.Action action : possibleActions) {

                DurakGame durakGame = durakGameCreator.createRandomGameFromPublicState(publicState, false);

                if (action.cardsToPlay().isEmpty()) {
                    durakGame.takeCardsFromTable();
                } else {
                    durakGame.doDefend(action.cardsToPlay());
                }
                GameSimulator.Result result = gameSimulator.runRandomSimulation(durakGame);

                boolean areYouPlayer1 = publicState.areYouPlayer1();

                if (result == GameSimulator.Result.PLAYER_1_WON && areYouPlayer1) {
                    actionToNumberOfWins.compute(action, (key, wins) -> wins + 1);
                }

                if (result == GameSimulator.Result.PLAYER_2_WON && !areYouPlayer1) {
                    actionToNumberOfWins.compute(action, (key, wins) -> wins + 1);
                }
            }
        }

        MoveFounder.Action theBestAction = findActionWithTheMostWins(actionToNumberOfWins);

        if (theBestAction.cardsToPlay().isEmpty()) {
            return new Move(TAKE_CARDS);
        }

        return new Move(DEFEND, theBestAction.cardsToPlay());
    }

    @Override
    public Move attack(PublicState publicState) {
        List<MoveFounder.Action> possibleActions = moveFounder.getAllPossibleAttacks(
                publicState.getAttackingCards(),
                publicState.getDefendingCards(),
                publicState.getYourHand(),
                publicState.getNumberOfCardsOnOpponentHand()
        );

        Map<MoveFounder.Action, Integer> actionToNumberOfWins = new HashMap<>(possibleActions.size());

        for (MoveFounder.Action action: possibleActions) {
            actionToNumberOfWins.put(action, 0);
        }

        for(int i = 0; i < numberOfIterations; i++) {
            for (MoveFounder.Action action : possibleActions) {

                DurakGame durakGame = durakGameCreator.createRandomGameFromPublicState(publicState, true);

                if (action.cardsToPlay().isEmpty()) {
                    durakGame.stopAttack();
                }
                else {
                    durakGame.doAttack(action.cardsToPlay());
                }

                GameSimulator.Result result = gameSimulator.runRandomSimulation(durakGame);

                boolean areYouPlayer1 = publicState.areYouPlayer1();

                if (result == GameSimulator.Result.PLAYER_1_WON && areYouPlayer1) {
                    actionToNumberOfWins.compute(action, (key, wins) -> wins + 1);
                }

                if (result == GameSimulator.Result.PLAYER_2_WON && !areYouPlayer1) {
                    actionToNumberOfWins.compute(action, (key, wins) -> wins + 1);
                }
            }
        }

        MoveFounder.Action theBestAction = findActionWithTheMostWins(actionToNumberOfWins);

        if (theBestAction.cardsToPlay().isEmpty()) {
            return new Move(STOP_ATTACK);
        }

        return new Move(ATTACK, theBestAction.cardsToPlay());
    }

    @Override
    public void displayCurrentState(PublicState publicState) {
        if(display != null) {
            display.display(publicState);
        }
    }

    private MoveFounder.Action findActionWithTheMostWins(Map<MoveFounder.Action, Integer> actionToNumberOfWins) {

        MoveFounder.Action theBestAction = null;
        Integer theMostWins = -1;

        for (MoveFounder.Action action: actionToNumberOfWins.keySet()) {
            
            Integer numberOfWins = actionToNumberOfWins.get(action);
            
            if (numberOfWins > theMostWins) {
                theBestAction = action;
                theMostWins = numberOfWins;
            }
        }
        
        return theBestAction;
    }
}
