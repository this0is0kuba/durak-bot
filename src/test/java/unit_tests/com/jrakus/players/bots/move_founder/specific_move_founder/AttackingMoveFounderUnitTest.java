package com.jrakus.players.bots.move_founder.specific_move_founder;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.bots.move_founder.MoveFounder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AttackingMoveFounderUnitTest {

    private final AttackingMoveFounder attackingMoveFounder = new AttackingMoveFounder();

    static Stream<Arguments> trivialParametersProvider() {

        int numberOfCardsInCombination1 = 1;
        List<Card> cards1 = List.of(new Card(HEARTS, TEN));
        List<MoveFounder.Action> expectedActions1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                )
        );

        Arguments arguments1 = Arguments.of(
                numberOfCardsInCombination1,
                cards1,
                expectedActions1
        );

        int numberOfCardsInCombination2 = 2;
        List<Card> cards2 = List.of(new Card(HEARTS, TEN), new Card(HEARTS, JACK));
        List<MoveFounder.Action> expectedActions2 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(HEARTS, JACK))
                )
        );

        Arguments arguments2 = Arguments.of(
                numberOfCardsInCombination2,
                cards2,
                expectedActions2
        );

        int numberOfCardsInCombination3 = 1;
        List<Card> cards3 = List.of(new Card(HEARTS, TEN), new Card(HEARTS, JACK));
        List<MoveFounder.Action> expectedActions3 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK))
                )
        );

        Arguments arguments3 = Arguments.of(
                numberOfCardsInCombination3,
                cards3,
                expectedActions3
        );

        return Stream.of(
                arguments1, arguments2, arguments3
        );
    }

    @ParameterizedTest
    @MethodSource("trivialParametersProvider")
    void testGetAllCombinationsOfCardsForTrivialCases(
            int numberOfCardsInCombination,
            List<Card> cards,
            List<MoveFounder.Action> expectedActions
    ) {

        List<MoveFounder.Action> actions =
                attackingMoveFounder.getAllCombinationsOfCards(numberOfCardsInCombination, cards);

        for (MoveFounder.Action action: actions) {
            assertTrue(
                    expectedActions.contains(action),
                    String.format("Unexpected action has been returned by method: %s ", action)
            );
        }

        for (MoveFounder.Action expectedAction: expectedActions) {
            assertTrue(
                    actions.contains(expectedAction),
                    String.format("Expected action: %s has not been returned by method", expectedAction)
            );
        }
    }

    static Stream<Arguments> mediumParametersProvider() {

        int numberOfCardsInCombination1 = 1;
        List<Card> cards1 = List.of(
                new Card(HEARTS, TEN), new Card(SPADES, TEN), new Card(DIAMONDS, TEN), new Card(CLUBS, TEN)
        );
        List<MoveFounder.Action> expectedActions1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(CLUBS, TEN))
                )
        );

        Arguments arguments1 = Arguments.of(
                numberOfCardsInCombination1,
                cards1,
                expectedActions1
        );

        int numberOfCardsInCombination2 = 2;
        List<Card> cards2 = List.of(
                new Card(HEARTS, TEN), new Card(SPADES, TEN), new Card(DIAMONDS, TEN)
        );
        List<MoveFounder.Action> expectedActions2 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(SPADES, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, TEN), new Card(DIAMONDS, TEN))
                )
        );

        Arguments arguments2 = Arguments.of(
                numberOfCardsInCombination2,
                cards2,
                expectedActions2
        );

        return Stream.of(
                arguments1, arguments2
        );
    }

    @ParameterizedTest
    @MethodSource("mediumParametersProvider")
    void testGetAllCombinationsOfCardsForMediumCases(
            int numberOfCardsInCombination,
            List<Card> cards,
            List<MoveFounder.Action> expectedActions
    ) {

        List<MoveFounder.Action> actions =
                attackingMoveFounder.getAllCombinationsOfCards(numberOfCardsInCombination, cards);

        for (MoveFounder.Action action: actions) {
            assertTrue(
                    expectedActions.contains(action),
                    String.format("Unexpected action has been returned by method: %s ", action)
            );
        }

        for (MoveFounder.Action expectedAction: expectedActions) {
            assertTrue(
                    actions.contains(expectedAction),
                    String.format("Expected action: %s has not been returned by method", expectedAction)
            );
        }
    }

    static Stream<Arguments> hardParametersProvider() {

        int numberOfCardsInCombination1 = 2;
        List<Card> cards1 = List.of(
                new Card(HEARTS, TEN), new Card(SPADES, TEN), new Card(DIAMONDS, TEN), new Card(CLUBS, TEN)
        );
        List<MoveFounder.Action> expectedActions1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(SPADES, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(CLUBS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, TEN), new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, TEN), new Card(CLUBS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN))
                )
        );

        Arguments arguments1 = Arguments.of(
                numberOfCardsInCombination1,
                cards1,
                expectedActions1
        );

        int numberOfCardsInCombination2 = 3;
        List<Card> cards2 = List.of(
                new Card(HEARTS, TEN), new Card(SPADES, TEN), new Card(DIAMONDS, TEN), new Card(CLUBS, JACK)
        );
        List<MoveFounder.Action> expectedActions2 = List.of(
                new MoveFounder.Action(
                        List.of( new Card(HEARTS, TEN), new Card(SPADES, TEN), new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(SPADES, TEN), new Card(CLUBS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, TEN), new Card(CLUBS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, TEN), new Card(DIAMONDS, TEN), new Card(CLUBS, JACK))
                )
        );

        Arguments arguments2 = Arguments.of(
                numberOfCardsInCombination2,
                cards2,
                expectedActions2
        );

        return Stream.of(
                arguments1, arguments2
        );
    }

    @ParameterizedTest
    @MethodSource("hardParametersProvider")
    void testGetAllCombinationsOfCardsForHardCases(
            int numberOfCardsInCombination,
            List<Card> cards,
            List<MoveFounder.Action> expectedActions
    ) {
        List<MoveFounder.Action> actions =
                attackingMoveFounder.getAllCombinationsOfCards(numberOfCardsInCombination, cards);

        for (MoveFounder.Action action: actions) {
            assertTrue(
                    expectedActions.contains(action),
                    String.format("Unexpected action has been returned by method: %s ", action)
            );
        }

        for (MoveFounder.Action expectedAction: expectedActions) {
            assertTrue(
                    actions.contains(expectedAction),
                    String.format("Expected action: %s has not been returned by method", expectedAction)
            );
        }
    }
}
