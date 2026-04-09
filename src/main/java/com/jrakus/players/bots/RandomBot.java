package com.jrakus.players.bots;

import com.jrakus.players.Player;
import com.jrakus.players.bots.move_founder.MoveFounder;
import com.jrakus.players.game_elements.Move;
import com.jrakus.players.game_elements.PublicState;
import com.jrakus.playground.displays.Display;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.jrakus.players.game_elements.Move.MoveKind.*;

public class RandomBot implements Player {

    private Display display = null;
    private MoveFounder moveFounder;

    public RandomBot() {
        this(new MoveFounder());
    }

    public RandomBot(MoveFounder moveFounder) {
        this.moveFounder = moveFounder;
    }

    public RandomBot(MoveFounder moveFounder, Display display) {
        this.moveFounder = moveFounder;
        this.display = display;
    }

    @Override
    public Move defend(PublicState publicState) {
        List<MoveFounder.Action> possibleDefends = moveFounder.getAllPossibleDefends(
                publicState.getAttackingCards(),
                publicState.getDefendingCards(),
                publicState.getYourHand(),
                publicState.getTrumpCard().suit()
        );

        MoveFounder.Action randomDefend = chooseRandomAction(possibleDefends);

        if (randomDefend.cardsToPlay().isEmpty()) {
            return new Move(TAKE_CARDS);
        }

        return new Move(DEFEND, randomDefend.cardsToPlay());
    }

    @Override
    public Move attack(PublicState publicState) {

        List<MoveFounder.Action> possibleAttacks = moveFounder.getAllPossibleAttacks(
                publicState.getAttackingCards(),
                publicState.getDefendingCards(),
                publicState.getYourHand(),
                publicState.getNumberOfCardsOnOpponentHand()
        );

        MoveFounder.Action randomAttack = chooseRandomAction(possibleAttacks);

        if (randomAttack.cardsToPlay().isEmpty()) {
            return new Move(STOP_ATTACK);
        }

        return new Move(ATTACK, randomAttack.cardsToPlay());
    }

    // Bot does not need to see the game after moves, but we can display it for ourselves.
    @Override
    public void displayCurrentState(PublicState publicState) {
        if(display != null) {
            display.display(publicState);
        }
    }

    private MoveFounder.Action chooseRandomAction(List<MoveFounder.Action> possibleActions) {
        int numberOfActions = possibleActions.size();
        int randomActionIndex = ThreadLocalRandom.current().nextInt(0, numberOfActions);

        return possibleActions.get(randomActionIndex);
    }
}
