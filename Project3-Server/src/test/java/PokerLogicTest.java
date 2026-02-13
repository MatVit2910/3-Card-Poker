import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

//testing poker logic
class PokerLogicTest {
    static Game game;
    static Deck deck;

    @BeforeAll
    static void setUp(){
        game = new Game();
        deck = new Deck();
    }

    @Test
    void dealHandsTest(){
        ArrayList<Card> hand1 = deck.dealHand();
        ArrayList<Card> hand2 = deck.dealHand();

        assertNotEquals(hand1, hand2, "dealHand is not working");
        assertEquals(46, deck.getCards().size());
    }

	@Test
	void startGameTest(){
        game.startGame();
        assertNotNull(game.getDealerHand(), "hands are not being dealt");
        assertNotNull(game.getPlayerHand(), "hands are not being dealt");
        assertNotEquals(game.getPlayerHand(), game.getDealerHand(), "hands are the same");
    }

    @Test
    void evalHandTest(){
        Card c1 = new Card("Spades", "A", "url");
        Card c2 = new Card("Spades", "2", "url");
        Card c3 = new Card("Spades", "J", "url");
        ArrayList<Card> hand = new ArrayList<>(3);
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);
        assertEquals("Flush", game.evaluateHand(hand), "evalHand is incorrect");

        c1.setRank("A");
        c2.setRank("A");
        c3.setRank("A");

        assertEquals("Three of a Kind", game.evaluateHand(hand), "evalHand is incorrect");

        c1.setRank("J");
        c2.setRank("Q");
        c3.setRank("K");

        assertEquals("Straight Flush", game.evaluateHand(hand), "evalHand is incorrect");

    }

    @Test
    void calculatePPTest(){
        Card c1 = new Card("Spades", "A", "url");
        Card c2 = new Card("Spades", "A", "url");
        Card c3 = new Card("Spades", "J", "url");
        ArrayList<Card> hand = new ArrayList<>(3);
        hand.add(c1);
        hand.add(c2);
        hand.add(c3);

        assertEquals(40, game.calculatePairPlus(hand, 10));

        c1.setSuit("Hearts");

        assertEquals(20, game.calculatePairPlus(hand, 10));
    }

    @Test
    void compHandsTest(){
        Card c1 = new Card("Spades", "A", "url");
        Card c2 = new Card("Spades", "2", "url");
        Card c3 = new Card("Spades", "3", "url");
        ArrayList<Card> playerHand = new ArrayList<>(3);
        playerHand.add(c1);
        playerHand.add(c2);
        playerHand.add(c3);

        Card c4 = new Card("Spades", "Q", "url");
        Card c5 = new Card("Hearts", "Q", "url");
        Card c6 = new Card("Spades", "Q", "url");
        ArrayList<Card> dealerHand = new ArrayList<>(3);
        dealerHand.add(c4);
        dealerHand.add(c5);
        dealerHand.add(c6);

        assertEquals(20, game.compareHands(dealerHand, playerHand, 5, 5));

        c1.setSuit("Hearts");

        assertEquals(0, game.compareHands(dealerHand, playerHand, 5, 5));
    }

}
