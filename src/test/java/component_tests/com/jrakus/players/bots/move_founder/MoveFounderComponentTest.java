package com.jrakus.players.bots.move_founder;

import com.jrakus.game_state.components.Card;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoveFounderComponentTest {
    private final MoveFounder moveFounder = new MoveFounder();

    static Stream<Arguments> trivialAttackProvider() {

        List<Card> attackingCards1 = List.of();
        List<Card> defendingCards1 = List.of();
        List<Card> yourHand1 = List.of(new Card(HEARTS, TEN));
        int numberOfCardsOnOpponentHand1 = 6;
        List<MoveFounder.Action> expectedPossibleAttacks1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                )
        );

        Arguments arguments1 = Arguments.of(
                attackingCards1,
                defendingCards1,
                yourHand1,
                numberOfCardsOnOpponentHand1,
                expectedPossibleAttacks1
        );

        List<Card> attackingCards2 = List.of(new Card(DIAMONDS, TEN));
        List<Card> defendingCards2 = List.of(new Card(DIAMONDS, KING));
        List<Card> yourHand2 = List.of(new Card(HEARTS, TEN), new Card(HEARTS, JACK));
        int numberOfCardsOnOpponentHand2 = 6;
        List<MoveFounder.Action> expectedPossibleAttacks2 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(List.of())
        );

        Arguments arguments2 = Arguments.of(
                attackingCards2,
                defendingCards2,
                yourHand2,
                numberOfCardsOnOpponentHand2,
                expectedPossibleAttacks2
        );

        List<Card> attackingCards3 = List.of(new Card(DIAMONDS, TEN));
        List<Card> defendingCards3 = List.of(new Card(DIAMONDS, JACK));
        List<Card> yourHand3 = List.of(new Card(HEARTS, TEN), new Card(HEARTS, JACK));
        int numberOfCardsOnOpponentHand3 = 6;
        List<MoveFounder.Action> expectedPossibleAttacks3 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(HEARTS, JACK))
                ),
                new MoveFounder.Action(List.of())
        );

        Arguments arguments3 = Arguments.of(
                attackingCards3,
                defendingCards3,
                yourHand3,
                numberOfCardsOnOpponentHand3,
                expectedPossibleAttacks3
        );

        List<Card> attackingCards4 = List.of();
        List<Card> defendingCards4 = List.of();
        List<Card> yourHand4 = List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, TEN), new Card(CLUBS, TEN));
        int numberOfCardsOnOpponentHand4 = 6;
        List<MoveFounder.Action> expectedPossibleAttacks4 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(CLUBS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(CLUBS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, TEN), new Card(CLUBS, TEN))
                )
        );

        Arguments arguments4 = Arguments.of(
                attackingCards4,
                defendingCards4,
                yourHand4,
                numberOfCardsOnOpponentHand4,
                expectedPossibleAttacks4
        );

        return Stream.of(
                arguments1, arguments2, arguments3, arguments4
        );
    }

    @ParameterizedTest
    @MethodSource("trivialAttackProvider")
    void testGetAllPossibleAttacksForTrivialCases(
            List<Card> attackingCards,
            List<Card> defendingCards,
            List<Card> yourHand,
            int numberOfCardsOnOpponentHand,
            List<MoveFounder.Action> expectedPossibleAttacks
    ) {
        List<MoveFounder.Action> possibleAttacks = moveFounder.getAllPossibleAttacks(
                attackingCards,
                defendingCards,
                yourHand,
                numberOfCardsOnOpponentHand
        );

        for (MoveFounder.Action action: possibleAttacks) {
            assertTrue(
                    expectedPossibleAttacks.contains(action),
                    String.format("Unexpected attack has been returned by method: %s ", action)
            );
        }

        for (MoveFounder.Action expectedAction: expectedPossibleAttacks) {
            assertTrue(
                    possibleAttacks.contains(expectedAction),
                    String.format("Expected attack: %s has not been returned by method", expectedAction)
            );
        }
    }

    static Stream<Arguments> difficultAttackProvider() {
        List<Card> attackingCards1 = List.of();
        List<Card> defendingCards1 = List.of();
        List<Card> yourHand1 = List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, TEN));
        int numberOfCardsOnOpponentHand1 = 1;
        List<MoveFounder.Action> expectedPossibleAttacks1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, TEN))
                )
        );

        Arguments arguments1 = Arguments.of(
                attackingCards1,
                defendingCards1,
                yourHand1,
                numberOfCardsOnOpponentHand1,
                expectedPossibleAttacks1
        );

        List<Card> attackingCards2 = List.of(new Card(HEARTS, TEN), new Card(SPADES, KING));
        List<Card> defendingCards2 = List.of(new Card(HEARTS, KING), new Card(DIAMONDS, NINE));
        List<Card> yourHand2 = List.of(
                new Card(SPADES, NINE), new Card(HEARTS, NINE), new Card(SPADES, TEN), new Card(HEARTS, ACE));
        int numberOfCardsOnOpponentHand2 = 2;
        List<MoveFounder.Action> expectedPossibleAttacks2 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(SPADES, NINE))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, NINE))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, TEN))
                ),

                new MoveFounder.Action(
                        List.of(new Card(SPADES, NINE), new Card(HEARTS, NINE))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, NINE), new Card(SPADES, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, NINE), new Card(SPADES, TEN))
                ),

                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments2 = Arguments.of(
                attackingCards2,
                defendingCards2,
                yourHand2,
                numberOfCardsOnOpponentHand2,
                expectedPossibleAttacks2
        );

        List<Card> attackingCards3 = List.of(
                new Card(HEARTS, NINE), new Card(DIAMONDS, NINE), new Card(SPADES, NINE), new Card(CLUBS, NINE)
        );
        List<Card> defendingCards3 = List.of(
                new Card(HEARTS, TEN), new Card(DIAMONDS, TEN), new Card(SPADES, JACK), new Card(CLUBS, JACK)
        );
        List<Card> yourHand3 = List.of(
                new Card(SPADES, TEN), new Card(CLUBS, TEN), new Card(HEARTS, JACK)
        );
        int numberOfCardsOnOpponentHand3 = 6;
        List<MoveFounder.Action> expectedPossibleAttacks3 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(SPADES, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(CLUBS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK))
                ),
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments3 = Arguments.of(
                attackingCards3,
                defendingCards3,
                yourHand3,
                numberOfCardsOnOpponentHand3,
                expectedPossibleAttacks3
        );

        return Stream.of(
                arguments1, arguments2, arguments3
        );
    }

    @ParameterizedTest
    @MethodSource("difficultAttackProvider")
    void testGetAllPossibleAttacksForDifficultCases(
            List<Card> attackingCards,
            List<Card> defendingCards,
            List<Card> yourHand,
            int numberOfCardsOnOpponentHand,
            List<MoveFounder.Action> expectedPossibleAttacks
    ) {
        List<MoveFounder.Action> possibleAttacks = moveFounder.getAllPossibleAttacks(
                attackingCards,
                defendingCards,
                yourHand,
                numberOfCardsOnOpponentHand
        );

        for (MoveFounder.Action action: possibleAttacks) {
            assertTrue(
                    expectedPossibleAttacks.contains(action),
                    String.format("Unexpected attack has been returned by method: %s ", action)
            );
        }

        for (MoveFounder.Action expectedAction: expectedPossibleAttacks) {
            assertTrue(
                    possibleAttacks.contains(expectedAction),
                    String.format("Expected attack: %s has not been returned by method", expectedAction)
            );
        }
    }

    static Stream<Arguments> trivialDefendsProvider() {

        List<Card> attackingCards1 = List.of(new Card(HEARTS, NINE));
        List<Card> defendingCards1 = List.of();
        List<Card> yourHand1 = List.of(new Card(HEARTS, TEN));
        Card.Suit trump1 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments1 = Arguments.of(
                attackingCards1,
                defendingCards1,
                yourHand1,
                trump1,
                expectedPossibleDefends1
        );

                
        List<Card> attackingCards2 = List.of(new Card(HEARTS, NINE));
        List<Card> defendingCards2 = List.of();
        List<Card> yourHand2 = List.of(new Card(HEARTS, TEN), new Card(HEARTS, JACK));
        Card.Suit trump2 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends2 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, JACK))
                ),
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments2 = Arguments.of(
                attackingCards2,
                defendingCards2,
                yourHand2,
                trump2,
                expectedPossibleDefends2
        );

        List<Card> attackingCards3 = List.of(new Card(HEARTS, NINE), new Card(SPADES, NINE));
        List<Card> defendingCards3 = List.of();
        List<Card> yourHand3 = List.of(new Card(HEARTS, TEN), new Card(SPADES, TEN));
        Card.Suit trump3 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends3 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN), new Card(SPADES, TEN))
                ),
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments3 = Arguments.of(
                attackingCards3,
                defendingCards3,
                yourHand3,
                trump3,
                expectedPossibleDefends3
        );
        
        List<Card> attackingCards4 = List.of(new Card(HEARTS, ACE));
        List<Card> defendingCards4 = List.of();
        List<Card> yourHand4 = List.of(new Card(HEARTS, TEN));
        Card.Suit trump4 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends4 = List.of(
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments4 = Arguments.of(
                attackingCards4,
                defendingCards4,
                yourHand4,
                trump4,
                expectedPossibleDefends4
        );

        List<Card> attackingCards5 = List.of(new Card(HEARTS, ACE));
        List<Card> defendingCards5 = List.of();
        List<Card> yourHand5 = List.of(new Card(DIAMONDS, NINE));
        Card.Suit trump5 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends5 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE))
                ),
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments5 = Arguments.of(
                attackingCards5,
                defendingCards5,
                yourHand5,
                trump5,
                expectedPossibleDefends5
        );

        List<Card> attackingCards6 = List.of(new Card(HEARTS, NINE));
        List<Card> defendingCards6 = List.of();
        List<Card> yourHand6 = List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, NINE));
        Card.Suit trump6 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends6 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(HEARTS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE))
                ),
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments6 = Arguments.of(
                attackingCards6,
                defendingCards6,
                yourHand6,
                trump6,
                expectedPossibleDefends6
        );

        return Stream.of(
                arguments1, arguments2, arguments3, arguments4, arguments5, arguments6
        );
    }

    @ParameterizedTest
    @MethodSource("trivialDefendsProvider")
    void testGetAllPossibleDefendsForTrivialCases(
            List<Card> attackingCards,
            List<Card> defendingCards,
            List<Card> yourHand,
            Card.Suit trump,
            List<MoveFounder.Action> expectedPossibleDefends
    ) {
        List<MoveFounder.Action> possibleDefends = moveFounder.getAllPossibleDefends(
                attackingCards,
                defendingCards,
                yourHand,
                trump
        );

        for (MoveFounder.Action action: possibleDefends) {
            assertTrue(
                    expectedPossibleDefends.contains(action),
                    String.format("Unexpected defend has been returned by method: %s ", action)
            );
        }

        for (MoveFounder.Action expectedAction: expectedPossibleDefends) {
            assertTrue(
                    possibleDefends.contains(expectedAction),
                    String.format("Expected defend: %s has not been returned by method", expectedAction)
            );
        }
    }

    static Stream<Arguments> difficultDefendsProvider() {

        List<Card> attackingCards1 = List.of(new Card(HEARTS, NINE), new Card(CLUBS, NINE));
        List<Card> defendingCards1 = List.of(new Card(CLUBS, TEN));
        List<Card> yourHand1 = List.of(new Card(CLUBS, TEN), new Card(DIAMONDS, NINE));
        Card.Suit trump1 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends1 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(CLUBS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE))
                ),
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments1 = Arguments.of(
                attackingCards1,
                defendingCards1,
                yourHand1,
                trump1,
                expectedPossibleDefends1
        );

        List<Card> attackingCards2 = List.of(new Card(HEARTS, NINE), new Card(CLUBS, NINE), new Card(DIAMONDS, NINE));
        List<Card> defendingCards2 = List.of();
        List<Card> yourHand2 = List.of(new Card(HEARTS, TEN), new Card(DIAMONDS, TEN));
        Card.Suit trump2 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends2 = List.of(
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments2 = Arguments.of(
                attackingCards2,
                defendingCards2,
                yourHand2,
                trump2,
                expectedPossibleDefends2
        );

        List<Card> attackingCards3 = List.of(new Card(HEARTS, NINE), new Card(CLUBS, NINE), new Card(SPADES, NINE));
        List<Card> defendingCards3 = List.of(new Card(HEARTS, KING));
        List<Card> yourHand3 = List.of(
                new Card(HEARTS, TEN), new Card(CLUBS, TEN), new Card(CLUBS, JACK), new Card(DIAMONDS, TEN));
        Card.Suit trump3 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends3 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(CLUBS, TEN), new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of(new Card(CLUBS, JACK), new Card(DIAMONDS, TEN))
                ),
                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments3 = Arguments.of(
                attackingCards3,
                defendingCards3,
                yourHand3,
                trump3,
                expectedPossibleDefends3
        );

        List<Card> attackingCards4 = List.of(
                new Card(HEARTS, NINE), new Card(CLUBS, NINE),
                new Card(SPADES, NINE), new Card(SPADES, TEN), new Card(DIAMONDS, TEN)
        );
        List<Card> defendingCards4 = List.of(
                new Card(HEARTS, TEN), new Card(CLUBS, TEN)
        );
        List<Card> yourHand4 = List.of(
                new Card(SPADES, JACK), new Card(SPADES, QUEEN), new Card(SPADES, KING),
                new Card(DIAMONDS, NINE), new Card(DIAMONDS, JACK)
        );
        Card.Suit trump4 = DIAMONDS;
        List<MoveFounder.Action> expectedPossibleDefends4 = List.of(
                new MoveFounder.Action(
                        List.of(new Card(SPADES, JACK), new Card(SPADES, QUEEN), new Card(DIAMONDS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, JACK), new Card(SPADES, KING), new Card(DIAMONDS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, JACK), new Card(DIAMONDS, NINE), new Card(DIAMONDS, JACK))
                ),

                new MoveFounder.Action(
                        List.of(new Card(SPADES, QUEEN), new Card(SPADES, JACK), new Card(DIAMONDS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, QUEEN), new Card(SPADES, KING), new Card(DIAMONDS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, QUEEN), new Card(DIAMONDS, NINE), new Card(DIAMONDS, JACK))
                ),

                new MoveFounder.Action(
                        List.of(new Card(SPADES, KING), new Card(SPADES, JACK), new Card(DIAMONDS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, KING), new Card(SPADES, QUEEN), new Card(DIAMONDS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(SPADES, KING), new Card(DIAMONDS, NINE), new Card(DIAMONDS, JACK))
                ),

                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE), new Card(SPADES, JACK), new Card(DIAMONDS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE), new Card(SPADES, QUEEN), new Card(DIAMONDS, JACK))
                ),
                new MoveFounder.Action(
                        List.of(new Card(DIAMONDS, NINE), new Card(SPADES, KING), new Card(DIAMONDS, JACK))
                ),

                new MoveFounder.Action(
                        List.of()
                )
        );

        Arguments arguments4 = Arguments.of(
                attackingCards4,
                defendingCards4,
                yourHand4,
                trump4,
                expectedPossibleDefends4
        );

        return Stream.of(
                arguments1, arguments2, arguments3, arguments4
        );
    }

    @ParameterizedTest
    @MethodSource("difficultDefendsProvider")
    void testGetAllPossibleDefendsForDifficultCases(
            List<Card> attackingCards,
            List<Card> defendingCards,
            List<Card> yourHand,
            Card.Suit trump,
            List<MoveFounder.Action> expectedPossibleDefends
    ) {
        List<MoveFounder.Action> possibleDefends = moveFounder.getAllPossibleDefends(
                attackingCards,
                defendingCards,
                yourHand,
                trump
        );

        for (MoveFounder.Action action: possibleDefends) {
            assertTrue(
                    expectedPossibleDefends.contains(action),
                    String.format("Unexpected defend has been returned by method: %s ", action)
            );
        }

        for (MoveFounder.Action expectedAction: expectedPossibleDefends) {
            assertTrue(
                    possibleDefends.contains(expectedAction),
                    String.format("Expected defend: %s has not been returned by method", expectedAction)
            );
        }
    }
}
