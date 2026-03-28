package com.jrakus.players.bots.card_selector;

import com.jrakus.game_state.components.Card;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardSelectorUnitTest {

    private final CardSelector cardSelector = new CardSelector();

    static Stream<Arguments> cardProvider1() {

        Card attackingCards1 =  new Card(DIAMONDS, TEN);
        Card.Suit trump1 = HEARTS;
        List<Card> yourCards1 = List.of(new Card(DIAMONDS, JACK));
        Optional<Card> expectedCard1 = Optional.of(new Card(DIAMONDS, JACK));

        Arguments arguments1 = Arguments.of(
                attackingCards1, trump1, yourCards1, expectedCard1
        );

        Card attackingCards2 =  new Card(HEARTS, TEN);
        Card.Suit trump2 = HEARTS;
        List<Card> yourCards2 = List.of(new Card(DIAMONDS, JACK));
        Optional<Card> expectedCard2 = Optional.empty();

        Arguments arguments2 = Arguments.of(
                attackingCards2, trump2, yourCards2, expectedCard2
        );

        Card attackingCards3 =  new Card(CLUBS, KING);
        Card.Suit trump3 = DIAMONDS;
        List<Card> yourCards3 = List.of(new Card(HEARTS, JACK), new Card(DIAMONDS, TEN), new Card(CLUBS, ACE));
        Optional<Card> expectedCard3 = Optional.of(new Card(CLUBS, ACE));

        Arguments arguments3 = Arguments.of(
                attackingCards3, trump3, yourCards3, expectedCard3
        );

        Card attackingCards4 =  new Card(DIAMONDS, KING);
        Card.Suit trump4 = DIAMONDS;
        List<Card> yourCards4 = List.of(new Card(HEARTS, JACK), new Card(DIAMONDS, ACE), new Card(CLUBS, ACE));
        Optional<Card> expectedCard4 = Optional.of(new Card(DIAMONDS, ACE));

        Arguments arguments4 = Arguments.of(
                attackingCards4, trump4, yourCards4, expectedCard4
        );

        Card attackingCards5 =  new Card(DIAMONDS, QUEEN);
        Card.Suit trump5 = HEARTS;
        List<Card> yourCards5 = List.of(
                new Card(DIAMONDS, TEN), new Card(DIAMONDS, JACK), new Card(DIAMONDS, KING), new Card(DIAMONDS, ACE),
                new Card(HEARTS, QUEEN), new Card(HEARTS, ACE),
                new Card(CLUBS, KING),
                new Card(SPADES, KING)
        );
        Optional<Card> expectedCard5 = Optional.of(new Card(DIAMONDS, KING));

        Arguments arguments5 = Arguments.of(
                attackingCards5, trump5, yourCards5, expectedCard5
        );

        return Stream.of(
                arguments1, arguments2, arguments3, arguments4, arguments5
        );
    }

    @ParameterizedTest
    @MethodSource("cardProvider1")
    void shouldFindTheWeakestCardToDefend(
            Card attackingCard,
            Card.Suit trump,
            List<Card> yourCards,
            Optional<Card> expectedCard
    ) {
        Optional<Card> card = cardSelector.findTheWeakestCardToDefend(
                attackingCard,
                trump,
                yourCards
        );

        assertEquals(expectedCard, card);
    }

    static Stream<Arguments> cardProvider2() {

        List<Card> yourCards1 = List.of(new Card(DIAMONDS, TEN));
        int numberOfOpponentCards1 = 6;
        Card.Suit trump1 = HEARTS;
        List<Card> expectedCards1 = List.of(new Card(DIAMONDS, TEN));

        Arguments arguments1 = Arguments.of(
                yourCards1, numberOfOpponentCards1, trump1, expectedCards1
        );

        List<Card> yourCards2 = List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN));
        int numberOfOpponentCards2 = 6;
        Card.Suit trump2 = HEARTS;
        List<Card> expectedCards2 = List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN));

        Arguments arguments2 = Arguments.of(
                yourCards2, numberOfOpponentCards2, trump2, expectedCards2
        );

        List<Card> yourCards3 = List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN), new Card(HEARTS, TEN));
        int numberOfOpponentCards3 = 6;
        Card.Suit trump3 = HEARTS;
        List<Card> expectedCards3 = List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN));

        Arguments arguments3 = Arguments.of(
                yourCards3, numberOfOpponentCards3, trump3, expectedCards3
        );
        
        List<Card> yourCards4 = List.of(new Card(CLUBS, KING), new Card(HEARTS, KING), new Card(SPADES, KING));
        int numberOfOpponentCards4 = 2;
        Card.Suit trump4 = DIAMONDS;
        List<Card> expectedCards4 = List.of(new Card(CLUBS, KING), new Card(HEARTS, KING));

        Arguments arguments4 = Arguments.of(
                yourCards4, numberOfOpponentCards4, trump4, expectedCards4
        );

        List<Card> yourCards5 = List.of(
                new Card(CLUBS, KING), new Card(HEARTS, KING), new Card(SPADES, KING),
                new Card(CLUBS, TEN), new Card(HEARTS, TEN),
                new Card(DIAMONDS, NINE)
        );
        int numberOfOpponentCards5 = 3;
        Card.Suit trump5 = DIAMONDS;
        List<Card> expectedCards5 = List.of(new Card(CLUBS, TEN), new Card(HEARTS, TEN));

        Arguments arguments5 = Arguments.of(
                yourCards5, numberOfOpponentCards5, trump5, expectedCards5
        );

        List<Card> yourCards6 = List.of(
                new Card(DIAMONDS, KING)
        );
        int numberOfOpponentCards6 = 10;
        Card.Suit trump6 = DIAMONDS;
        List<Card> expectedCards6 = List.of(new Card(DIAMONDS, KING));

        Arguments arguments6 = Arguments.of(
                yourCards6, numberOfOpponentCards6, trump6, expectedCards6
        );

        return Stream.of(
                arguments1, arguments2, arguments3, arguments4, arguments5, arguments6
        );
    }

    @ParameterizedTest
    @MethodSource("cardProvider2")
    void shouldFindTheWeakestCardsToAttack(
            List<Card> yourCards,
            int numberOfOpponentCards,
            Card.Suit trump,
            List<Card> expectedCards
    ) {
        List<Card> card = cardSelector.findTheWeakestCardsForFirstAttack(
                yourCards,
                numberOfOpponentCards,
                trump
        );

        assertEquals(expectedCards, card);
    }

    static Stream<Arguments> cardProvider3() {

        List<Card> yourCards1 = List.of(new Card(CLUBS, JACK));
        int numberOfOpponentCards1 = 6;
        List<Card> attackingCards1 = List.of(new Card(CLUBS, NINE));
        List<Card> defendingCards1 = List.of(new Card(CLUBS, TEN));
        Card.Suit trump1 = HEARTS;
        int numberOfCardsOnDeck1 = 0;
        List<Card> expectedCards1 = List.of();

        Arguments arguments1 = Arguments.of(
                yourCards1, numberOfOpponentCards1, attackingCards1, defendingCards1, trump1, numberOfCardsOnDeck1,
                expectedCards1
        );


        List<Card> yourCards2 = List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN));
        int numberOfOpponentCards2 = 6;
        List<Card> attackingCards2 = List.of(new Card(CLUBS, NINE));
        List<Card> defendingCards2 = List.of(new Card(CLUBS, TEN));
        Card.Suit trump2 = HEARTS;
        int numberOfCardsOnDeck2 = 0;
        List<Card> expectedCards2 = List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN));

        Arguments arguments2 = Arguments.of(
                yourCards2, numberOfOpponentCards2, attackingCards2, defendingCards2, trump2, numberOfCardsOnDeck2,
                expectedCards2
        );

        List<Card> yourCards3 = List.of(new Card(DIAMONDS, TEN), new Card(CLUBS, TEN));
        int numberOfOpponentCards3 = 1;
        List<Card> attackingCards3 = List.of(new Card(CLUBS, NINE));
        List<Card> defendingCards3 = List.of(new Card(CLUBS, TEN));
        Card.Suit trump3 = HEARTS;
        int numberOfCardsOnDeck3 = 0;
        List<Card> expectedCards3 = List.of(new Card(DIAMONDS, TEN));

        Arguments arguments3 = Arguments.of(
                yourCards3, numberOfOpponentCards3, attackingCards3, defendingCards3, trump3, numberOfCardsOnDeck3,
                expectedCards3
        );

        List<Card> yourCards4 = List.of(new Card(CLUBS, TEN), new Card(HEARTS, TEN));
        int numberOfOpponentCards4 = 6;
        List<Card> attackingCards4 = List.of(new Card(CLUBS, NINE));
        List<Card> defendingCards4 = List.of(new Card(CLUBS, TEN));
        Card.Suit trump4 = HEARTS;
        int numberOfCardsOnDeck4 = 3;
        List<Card> expectedCards4 = List.of(new Card(CLUBS, TEN));

        Arguments arguments4 = Arguments.of(
                yourCards4, numberOfOpponentCards4, attackingCards4, defendingCards4, trump4, numberOfCardsOnDeck4,
                expectedCards4
        );

        List<Card> yourCards5 = List.of(new Card(CLUBS, TEN), new Card(HEARTS, TEN));
        int numberOfOpponentCards5 = 6;
        List<Card> attackingCards5 = List.of(new Card(CLUBS, NINE));
        List<Card> defendingCards5 = List.of(new Card(CLUBS, TEN));
        Card.Suit trump5 = HEARTS;
        int numberOfCardsOnDeck5 = 2;
        List<Card> expectedCards5 = List.of(new Card(CLUBS, TEN), new Card(HEARTS, TEN));

        Arguments arguments5 = Arguments.of(
                yourCards5, numberOfOpponentCards5, attackingCards5, defendingCards5, trump5, numberOfCardsOnDeck5,
                expectedCards5
        );

        List<Card> yourCards6 = List.of(
                new Card(DIAMONDS, TEN), new Card(HEARTS, TEN), new Card(SPADES, TEN)
        );
        int numberOfOpponentCards6 = 1;
        List<Card> attackingCards6 = List.of(
                new Card(CLUBS, NINE), new Card(SPADES, NINE), new Card(HEARTS, NINE), new Card(SPADES, KING)
        );
        List<Card> defendingCards6 = List.of(
                new Card(CLUBS, TEN), new Card(SPADES, JACK), new Card(HEARTS, JACK), new Card(SPADES, ACE)
        );
        Card.Suit trump6 = HEARTS;
        int numberOfCardsOnDeck6 = 10;
        List<Card> expectedCards6 = List.of(new Card(DIAMONDS, TEN));

        Arguments arguments6 = Arguments.of(
                yourCards6, numberOfOpponentCards6, attackingCards6, defendingCards6, trump6, numberOfCardsOnDeck6,
                expectedCards6
        );

        return Stream.of(arguments1, arguments2, arguments3, arguments4, arguments5, arguments6);
    }

    @ParameterizedTest
    @MethodSource("cardProvider3")
    void shouldFindPossibleCardsToAttack(
            List<Card> yourCards,
            int numberOfCardsOnOpponentHand,
            List<Card> attackingCards,
            List<Card> defendingCards,
            Card.Suit trump,
            int numberOfCardsOnDeck,
            List<Card> expectedCards
    ) {
        List<Card> card = cardSelector.findOptimalCardsForSecondAndNextAttack(
                yourCards, numberOfCardsOnOpponentHand,
                attackingCards,
                defendingCards, trump,
                numberOfCardsOnDeck
        );

        assertEquals(expectedCards, card);
    }
}
