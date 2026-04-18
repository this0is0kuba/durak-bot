package com.jrakus.players.bots.durak_game_creator;

import com.jrakus.game_state.DurakGame;
import com.jrakus.game_state.components.*;
import com.jrakus.game_state.exceptions.DurakGameInvalidStateException;
import com.jrakus.players.game_elements.PublicState;

import java.util.*;
import java.util.stream.Collectors;

public class DurakGameCreator {

    public DurakGame createRandomGameFromPublicState(PublicState publicState, boolean isAttackMove) {

        Set<Card> fullDeck = Deck.getFullDeckAsSet();

        Set<Card> knownCards = mergeAllCardsAsSet(
                new ArrayList<>(publicState.getAttackingCards()),
                new ArrayList<>(publicState.getDefendingCards()),
                new ArrayList<>(publicState.getDiscardPile()),
                new ArrayList<>(publicState.getYourHand()),
                new ArrayList<>(List.of(publicState.getTrumpCard())),
                new ArrayList<>(publicState.getCertainOpponentHand())
        );

        Set<Card> unknownCards = new HashSet<>(fullDeck);
        unknownCards.removeAll(knownCards);

        Set<Card> randomlySelectedUnknownCards = chooseRandomCards(
                unknownCards, publicState.getNumberOfCardsOnDeck() - 1
        );
        Set<Card> restOfUnknownCards = new HashSet<>(unknownCards);
        restOfUnknownCards.removeAll(randomlySelectedUnknownCards);

        List<Card> deckCards = prepareDeckCards(
                randomlySelectedUnknownCards, publicState.getTrumpCard(), publicState.getNumberOfCardsOnDeck()
        );

        List<Card> opponentHand = new ArrayList<>(publicState.getCertainOpponentHand());
        opponentHand.addAll(new ArrayList<>(restOfUnknownCards));

        Deck deck;

        if (deckCards.isEmpty()) {
            deck = new Deck(publicState.getTrumpCard());
        } else {
            deck = new Deck(deckCards);
        }

        DiscardPile discardPile = new DiscardPile(new ArrayList<>(publicState.getDiscardPile()));

        Table table = new Table(
                new ArrayList<>(publicState.getAttackingCards()), new ArrayList<>(publicState.getDefendingCards())
        );

        DurakGamePlayer player = new DurakGamePlayer(new ArrayList<>(publicState.getYourHand()), 1);
        DurakGamePlayer opponent = new DurakGamePlayer(opponentHand, 2);

        DurakGamePlayer player1 = publicState.areYouPlayer1() ? player : opponent;
        DurakGamePlayer player2 = publicState.areYouPlayer1() ? opponent : player;
        DurakGamePlayer startingPlayer = publicState.didPlayer1StartGame() ? player1 : player2;
        DurakGamePlayer attackingPlayer = isAttackMove ? player : opponent;
        DurakGamePlayer defendingPlayer = isAttackMove ? opponent : player;

        if (deckCards.size() + opponentHand.size() + publicState.getYourHand().size() +
                publicState.getDiscardPile().size() + publicState.getAttackingCards().size() +
                publicState.getDefendingCards().size() != 24)
            throw new DurakGameInvalidStateException("Cards that were played are not unique");

        return new DurakGame(
                deck, discardPile, table, new GameState(), player1, player2, player, startingPlayer, attackingPlayer,
                defendingPlayer
        );
    }

    @SafeVarargs
    private Set<Card> mergeAllCardsAsSet(List<Card>... list) {
        return Arrays.stream(list).flatMap(List::stream).collect(Collectors.toSet());
    }

    private Set<Card> chooseRandomCards(Set<Card> cards, int numberOfCards) {

        if (numberOfCards <= 0) {
            return new HashSet<>();
        }

        List<Card> cardList = new ArrayList<>(cards);
        Collections.shuffle(cardList);

        return new HashSet<>(cardList.subList(0, numberOfCards));
    }

    private List<Card> prepareDeckCards(Set<Card> hiddenCards, Card trumpCard, int numberOfCardsOnDeck) {

        if (numberOfCardsOnDeck == 0) {
            return new ArrayList<>();
        }

        List<Card> deck = new ArrayList<>();

        List<Card> shuffledHiddenCards = new ArrayList<>(hiddenCards);
        Collections.shuffle(shuffledHiddenCards);

        deck.add(trumpCard);
        deck.addAll(shuffledHiddenCards);

        return deck;
    }
}
