package com.jrakus.players.bots.move_founder.specific_move_founder;

import com.jrakus.game_state.components.Card;
import com.jrakus.players.bots.move_founder.MoveFounder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefendingMoveFounderUnitTest {

    private final DefendingMoveFounder defendingMoveFounder = new DefendingMoveFounder();

    static Stream<Arguments> trivialParametersProvider() {

        List<Card> attackingCardList1 = List.of(
                new Card(HEARTS, TEN)
        );
        Map<Card, List<Card>> attackingCardToDefendingCards1 = Map.of(
                new Card(HEARTS, TEN), List.of(new Card(HEARTS, JACK))
        );
        List<MoveFounder.Action> expectedActions1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK))
                )
        );

        Arguments arguments1 = Arguments.of(
                attackingCardToDefendingCards1,
                attackingCardList1,
                expectedActions1
        );

        List<Card> attackingCardList2 = List.of(
                new Card(HEARTS, TEN)
        );
        Map<Card, List<Card>> attackingCardToDefendingCards2 = Map.of(
                new Card(HEARTS, TEN), List.of()
        );
        List<MoveFounder.Action> expectedActions2 = List.of();

        Arguments arguments2 = Arguments.of(
                attackingCardToDefendingCards2,
                attackingCardList2,
                expectedActions2
        );

        List<Card> attackingCardList3 = List.of(
                new Card(HEARTS, TEN), new Card(HEARTS, QUEEN)
        );
        Map<Card, List<Card>> attackingCardToDefendingCards3 = Map.of(
                new Card(HEARTS, TEN), List.of(new Card(HEARTS, JACK), new Card(HEARTS, KING)),
                new Card(HEARTS, QUEEN), List.of(new Card(HEARTS, KING))
        );
        List<MoveFounder.Action> expectedActions3 = List.of(
                new MoveFounder.Action(
                    List.of(new Card(HEARTS, JACK), new Card(HEARTS, KING))
                )
        );

        Arguments arguments3 = Arguments.of(
                attackingCardToDefendingCards3,
                attackingCardList3,
                expectedActions3
        );

        return Stream.of(
                arguments1, arguments2, arguments3
        );
    }

    @ParameterizedTest
    @MethodSource("trivialParametersProvider")
    void testGetAllCombinationsOfCardsForTrivialCases(
            Map<Card, List<Card>> attackingCardToDefendingCards,
            List<Card> attackingCardList,
            List<MoveFounder.Action> expectedActions
    ) {

        List<MoveFounder.Action> actions =
                defendingMoveFounder.getAllCombinationsOfCards(attackingCardToDefendingCards, attackingCardList);

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
        List<Card> attackingCardList1 = List.of(
                new Card(HEARTS, TEN), new Card(HEARTS, QUEEN)
        );
        Map<Card, List<Card>> attackingCardToDefendingCards1 = Map.of(
                new Card(HEARTS, TEN), List.of(new Card(HEARTS, JACK), new Card(HEARTS, KING), new Card(DIAMONDS, NINE)),
                new Card(HEARTS, QUEEN), List.of(new Card(HEARTS, KING), new Card(DIAMONDS, NINE))
        );
        List<MoveFounder.Action> expectedActions1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK), new Card(HEARTS, KING))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK), new Card(DIAMONDS, NINE))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, KING), new Card(DIAMONDS, NINE))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE), new Card(HEARTS, KING))
                )
        );

        Arguments arguments1 = Arguments.of(
                attackingCardToDefendingCards1,
                attackingCardList1,
                expectedActions1
        );

        List<Card> attackingCardList2 = List.of(
                new Card(HEARTS, TEN), new Card(HEARTS, QUEEN), new Card(DIAMONDS, TEN)
        );
        Map<Card, List<Card>> attackingCardToDefendingCards2 = Map.of(
                new Card(HEARTS, TEN), List.of(new Card(HEARTS, JACK), new Card(HEARTS, KING), new Card(DIAMONDS, NINE)),
                new Card(HEARTS, QUEEN), List.of(new Card(HEARTS, KING), new Card(DIAMONDS, NINE)),
                new Card(DIAMONDS, TEN), List.of()
        );
        List<MoveFounder.Action> expectedActions2 = List.of();

        Arguments arguments2 = Arguments.of(
                attackingCardToDefendingCards2,
                attackingCardList2,
                expectedActions2
        );

        List<Card> attackingCardList3 = List.of(
                new Card(HEARTS, TEN), new Card(HEARTS, QUEEN), new Card(DIAMONDS, TEN)
        );
        Map<Card, List<Card>> attackingCardToDefendingCards3 = Map.of(
                new Card(HEARTS, TEN), List.of(new Card(HEARTS, JACK), new Card(HEARTS, KING), new Card(DIAMONDS, JACK)),
                new Card(HEARTS, QUEEN), List.of(new Card(HEARTS, KING), new Card(DIAMONDS, JACK)),
                new Card(DIAMONDS, TEN), List.of(new Card(DIAMONDS, JACK))
        );
        List<MoveFounder.Action> expectedActions3 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK), new Card(HEARTS, KING), new Card(DIAMONDS, JACK))
                )
        );

        Arguments arguments3 = Arguments.of(
                attackingCardToDefendingCards3,
                attackingCardList3,
                expectedActions3
        );

        return Stream.of(
                arguments1, arguments2, arguments3
        );
    }

    @ParameterizedTest
    @MethodSource("mediumParametersProvider")
    void testGetAllCombinationsOfCardsForMediumCases(
            Map<Card, List<Card>> attackingCardToDefendingCards,
            List<Card> attackingCardList,
            List<MoveFounder.Action> expectedActions
    ) {

        List<MoveFounder.Action> actions =
                defendingMoveFounder.getAllCombinationsOfCards(attackingCardToDefendingCards, attackingCardList);

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

        List<Card> attackingCardList1 = List.of(
                new Card(HEARTS, TEN), new Card(SPADES, TEN), new Card(CLUBS, TEN)
        );
        Map<Card, List<Card>> attackingCardToDefendingCards1 = Map.of(
                new Card(HEARTS, TEN), List.of(new Card(HEARTS, JACK), new Card(HEARTS, KING), new Card(DIAMONDS, NINE)),
                new Card(SPADES, TEN), List.of(new Card(SPADES, JACK), new Card(DIAMONDS, NINE)),
                new Card(CLUBS, TEN), List.of(new Card(CLUBS, JACK), new Card(CLUBS, KING), new Card(DIAMONDS, NINE))
        );
        List<MoveFounder.Action> expectedActions1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK), new Card(SPADES, JACK), new Card(CLUBS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK), new Card(SPADES, JACK), new Card(CLUBS, KING))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK), new Card(SPADES, JACK), new Card(DIAMONDS, NINE))
                ),

                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK), new Card(DIAMONDS, NINE), new Card(CLUBS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK), new Card(DIAMONDS, NINE), new Card(CLUBS, KING))
                ),

                new MoveFounder.Action(
                        List.of(new Card(HEARTS, KING), new Card(SPADES, JACK), new Card(CLUBS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, KING), new Card(SPADES, JACK), new Card(CLUBS, KING))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, KING), new Card(SPADES, JACK), new Card(DIAMONDS, NINE))
                ),

                new MoveFounder.Action(
                        List.of(new Card(HEARTS, KING), new Card(DIAMONDS, NINE), new Card(CLUBS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, KING), new Card(DIAMONDS, NINE), new Card(CLUBS, KING))
                ),

                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE), new Card(SPADES, JACK), new Card(CLUBS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE), new Card(SPADES, JACK), new Card(CLUBS, KING))
                )
        );

        Arguments arguments1 = Arguments.of(
                attackingCardToDefendingCards1,
                attackingCardList1,
                expectedActions1
        );

        List<Card> attackingCardList2 = List.of(
                new Card(HEARTS, TEN), new Card(HEARTS, QUEEN), new Card(DIAMONDS, NINE)
        );
        Map<Card, List<Card>> attackingCardToDefendingCards2 = Map.of(
                new Card(HEARTS, TEN), List.of(new Card(HEARTS, KING), new Card(DIAMONDS, TEN)),
                new Card(HEARTS, QUEEN), List.of(new Card(HEARTS, KING), new Card(DIAMONDS, TEN)),
                new Card(DIAMONDS, NINE), List.of(new Card(DIAMONDS, TEN))
        );
        List<MoveFounder.Action> expectedActions2 = List.of();

        Arguments arguments2 = Arguments.of(
                attackingCardToDefendingCards2,
                attackingCardList2,
                expectedActions2
        );

        return Stream.of(arguments1, arguments2);
    }

    @ParameterizedTest
    @MethodSource("hardParametersProvider")
    void testGetAllCombinationsOfCardsForHardCases(
            Map<Card, List<Card>> attackingCardToDefendingCards,
            List<Card> attackingCardList,
            List<MoveFounder.Action> expectedActions
    ) {

        List<MoveFounder.Action> actions =
                defendingMoveFounder.getAllCombinationsOfCards(attackingCardToDefendingCards, attackingCardList);

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
