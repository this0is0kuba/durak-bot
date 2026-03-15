package com.jrakus.game_state;

import com.jrakus.game_state.components.*;
import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;
import com.jrakus.game_state.validators.DurakGameValidator;
import com.jrakus.game_state.validators.TableValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.jrakus.game_state.components.Card.Rank.*;
import static com.jrakus.game_state.components.Card.Suit.*;
import static com.jrakus.game_state.components.GameState.GameStateEnum.DRAW;
import static com.jrakus.game_state.components.GameState.GameStateEnum.PLAYER_1_WON;
import static org.junit.jupiter.api.Assertions.*;

class DurakGameComponentTest {

    private DurakGame game;

    @BeforeEach
    void setUp() {
        game = createGameWithDeterministicDeck();
    }

    private DurakGame createGameWithDeterministicDeck() {

        List<Card> cardsOnHand1 = new ArrayList<>(6);
        List<Card> cardsOnHand2 = new ArrayList<>(6);
        List<Card> cardsOnDeck = new ArrayList<>(2);

        // --- Player 1 (6 cards)
        cardsOnHand1.add(new Card(SPADES, JACK));
        cardsOnHand1.add(new Card(SPADES, QUEEN));
        cardsOnHand1.add(new Card(HEARTS, NINE));
        cardsOnHand1.add(new Card(HEARTS, ACE));
        cardsOnHand1.add(new Card(CLUBS, KING));
        cardsOnHand1.add(new Card(DIAMONDS, NINE));

        // --- Player 2 (6 cards)
        cardsOnHand2.add(new Card(SPADES, NINE));
        cardsOnHand2.add(new Card(SPADES, KING));
        cardsOnHand2.add(new Card(HEARTS, JACK));
        cardsOnHand2.add(new Card(CLUBS, QUEEN));
        cardsOnHand2.add(new Card(CLUBS, ACE));
        cardsOnHand2.add(new Card(DIAMONDS, JACK));

        // --- Deck
        cardsOnDeck.add(new Card(DIAMONDS, TEN));
        cardsOnDeck.add(new Card(CLUBS, NINE));

        Deck deck = new Deck(cardsOnDeck);
        DurakGamePlayer player1 = new DurakGamePlayer(cardsOnHand1);
        DurakGamePlayer player2 = new DurakGamePlayer(cardsOnHand2);

        return new DurakGame(
                deck,
                new DiscardPile(),
                new Table(new TableValidator()),
                new GameState(),
                new DurakGameValidator(),
                player1,
                player2,
                player1
        );
    }

    private DurakGame createEndGame() {

        List<Card> cardsOnHand1 = new ArrayList<>(2);
        List<Card> cardsOnHand2 = new ArrayList<>(2);
        Card trumpCard = new Card(HEARTS, ACE);

        // --- Player 1 (6 cards)
        cardsOnHand1.add(new Card(SPADES, JACK));
        cardsOnHand1.add(new Card(HEARTS, JACK));

        // --- Player 2 (6 cards)
        cardsOnHand2.add(new Card(SPADES, QUEEN));
        cardsOnHand2.add(new Card(HEARTS, ACE));

        Deck deck = new Deck(trumpCard);
        DurakGamePlayer player1 = new DurakGamePlayer(cardsOnHand1);
        DurakGamePlayer player2 = new DurakGamePlayer(cardsOnHand2);

        return new DurakGame(
                deck,
                new DiscardPile(),
                new Table(new TableValidator()),
                new GameState(),
                new DurakGameValidator(),
                player1,
                player2,
                player1
        );
    }

    @Test
    void shouldDistributeSixCardsToEachPlayer() {
        assertEquals(6, game.showActivePlayerHand().size());
        assertEquals(2, game.getNumberOfCardsOnDeck());
    }

    @Test
    void shouldExposeCorrectTrumpCard() {

        Card trump = game.showTrumpCard();

        assertNotNull(trump);
        assertEquals(DIAMONDS, trump.suit());
        assertEquals(TEN, trump.rank());
    }

    @Test
    void attackShouldMoveCardToTable() {

        Card attackCard = game.showActivePlayerHand().getFirst();

        game.doAttack(List.of(attackCard));

        assertEquals(1, game.showCurrentAttackingCards().size());
        assertTrue(game.showCurrentAttackingCards().contains(attackCard));
    }

    @Test
    void defendShouldAddCardToDefendingSide() {

        Card attackCard = new Card(HEARTS, NINE);
        game.doAttack(List.of(attackCard));

        Card defendCard = new Card(HEARTS, JACK);
        game.doDefend(List.of(defendCard));

        assertEquals(1, game.showCurrentDefendingCards().size());
        assertTrue(game.showCurrentDefendingCards().contains(defendCard));
    }

    @Test
    void stopAttackShouldMoveCardsToDiscardPile() {

        Card attackCard = new Card(CLUBS, KING);
        game.doAttack(List.of(attackCard));

        Card defendCard = new Card(DIAMONDS, JACK);
        game.doDefend(List.of(defendCard));

        game.stopAttack();

        assertEquals(2, game.getDiscardPile().size());
        assertEquals(0, game.showCurrentAttackingCards().size());
        assertEquals(0, game.showCurrentDefendingCards().size());
    }

    @Test
    void takeCardsShouldGiveCardsToDefender() {

        Card attackCard = new Card(CLUBS, KING);
        game.doAttack(List.of(attackCard));

        int defenderHandBefore = game.showPlayer2Hand().size();

        game.takeCardsFromTable();

        int defenderHandAfter = game.showPlayer2Hand().size();

        assertEquals(defenderHandBefore + 1, defenderHandAfter);
        assertEquals(0, game.showCurrentAttackingCards().size());
    }

    @Test
    void gameShouldRemainActiveAfterBasicMoves() {

        Card attackCard = new Card(CLUBS, KING);
        game.doAttack(List.of(attackCard));

        assertEquals(GameState.GameStateEnum.ACTIVE_GAME, game.getGameState());
    }

    @Test
    void afterTakingCardsAttackerShouldAttackAgain() {

        DurakGame game = createGameWithDeterministicDeck();

        Card attackCard = new Card(CLUBS, KING);
        game.doAttack(List.of(attackCard));

        game.takeCardsFromTable();

        assertTrue(game.isPlayer1Active());
        assertTrue(game.isPlayer1Attacking());
    }

    @Test
    void afterStoppingAttackDefenderShouldAttack() {

        Card attackCard = new Card(CLUBS, KING);
        game.doAttack(List.of(attackCard));

        Card defendCard = new Card(DIAMONDS, JACK);
        game.doDefend(List.of(defendCard));

        game.stopAttack();

        assertFalse(game.isPlayer1Active());
        assertFalse(game.isPlayer1Attacking());
    }

    @Test
    void afterTakingCardsAttackerShouldTakeCardsFromDeck() {

        Card attackCard = new Card(HEARTS, NINE);
        game.doAttack(List.of(attackCard));

        game.takeCardsFromTable();

        // Attacker should take one card from deck
        assertEquals(6, game.showPlayer1Hand().size());
    }

    @Test
    void afterStoppingAttackBothPlayersShouldTakeCardsFromDeck() {

        Card attackCard = new Card(CLUBS, KING);
        game.doAttack(List.of(attackCard));

        Card defendCard = new Card(DIAMONDS, JACK);
        game.doDefend(List.of(defendCard));

        game.stopAttack();

        // Both Players should take by one card in the correct order
        assertEquals(6, game.showPlayer1Hand().size());
        assertEquals(6, game.showPlayer2Hand().size());
        assertEquals(0, game.getNumberOfCardsOnDeck());
        assertTrue(game.showPlayer1Hand().contains(new Card(CLUBS, NINE)));
        assertTrue(game.showPlayer2Hand().contains(new Card(DIAMONDS, TEN)));
    }

    @Test
    void throwErrorWhenAttackingTwice() {

        Card attackCard1 = new Card(CLUBS, KING);
        game.doAttack(List.of(attackCard1));

        Card attackCard2 = new Card(HEARTS, NINE);

        assertThrows(
                DurakGameInvalidStateException.class,
                () -> game.doAttack(List.of(attackCard2))
        );
    }

    @Test
    void throwErrorWhenUserStopAttackBeforePlayingAnyCard() {
        assertThrows(
                DurakGameInvalidStateException.class,
                () -> game.stopAttack()
        );
    }

    @Test
    void throwErrorWhenDefendIsTheFirstMove() {

        Card defendCard = new Card(CLUBS, KING);
        assertThrows(
                DurakGameInvalidStateException.class,
                () -> game.doDefend(List.of(defendCard))
        );
    }

    @Test
    void throwErrorWhenDefendingTwice() {

        Card attackCard = new Card(CLUBS, KING);
        game.doAttack(List.of(attackCard));

        Card defendCard1 = new Card(CLUBS, ACE);
        game.doDefend(List.of(defendCard1));

        Card defendCard2 = new Card(SPADES, KING);
        assertThrows(
                DurakGameInvalidStateException.class,
                () -> game.doDefend(List.of(defendCard2))
        );
    }

    @Test
    void throwErrorWhenDefendingCardsAreWeaker() {

        Card attackCard1 = new Card(HEARTS, NINE);
        Card attackCard2 = new Card(DIAMONDS, NINE);
        game.doAttack(List.of(attackCard1, attackCard2));

        Card defendCard1 = new Card(HEARTS, JACK);
        Card defendCard2 = new Card(SPADES, KING);

        assertThrows(
                DurakGameInvalidStateException.class,
                () -> game.doDefend(List.of(defendCard1, defendCard2))
        );
    }

    @Test
    void playerShouldWinWhenHas0Cards() {

        DurakGame endGame = createEndGame();

        List<Card> attackCards = List.of(
                new Card(SPADES, JACK),
                new Card(HEARTS, JACK)
        );
        endGame.doAttack(attackCards);
        endGame.takeCardsFromTable();

        assertEquals(PLAYER_1_WON, endGame.getGameState());
    }

    @Test
    void itShouldBeDrawWhenBothPlayersHave0Cards() {
        DurakGame endGame = createEndGame();

        List<Card> attackCards = List.of(
                new Card(SPADES, JACK),
                new Card(HEARTS, JACK)
        );
        endGame.doAttack(attackCards);

        List<Card> defendCards = List.of(
                new Card(SPADES, QUEEN),
                new Card(HEARTS, ACE)
        );
        endGame.doDefend(defendCards);

        assertEquals(DRAW, endGame.getGameState());
    }
}