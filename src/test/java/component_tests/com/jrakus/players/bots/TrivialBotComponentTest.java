package com.jrakus.players.bots;

import com.jrakus.players.bots.resourceProvider.TrivialBotResourceProvider;
import com.jrakus.players.game_elements.Move;
import com.jrakus.players.game_elements.PublicState;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrivialBotComponentTest {

    // TODO: Add more tests

    private final TrivialBot trivialBot = new TrivialBot();

    private static Stream<Arguments> cardsProviderForDefending() {
        return Stream.of(
                TrivialBotResourceProvider.ResourcesForDefend.getArguments1()
        );
    }

    @ParameterizedTest
    @MethodSource("cardsProviderForDefending")
    void shouldDefend(PublicState publicState, Move expectedMove) {

        Move move = trivialBot.defend(publicState);
        assertEquals(expectedMove, move);
    }

}
