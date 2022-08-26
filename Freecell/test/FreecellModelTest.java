import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.Card;
import cs3500.freecell.Suit;
import cs3500.freecell.Value;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * JUnit test cases for the freecell model.
 */
public class FreecellModelTest {
  private FreecellModel fc1;
  private List<Card> nullDeck;
  private List<Card> not52Deck;
  private List<Card> standard;
  private FreecellView fv;

  @Before
  public void setUp() {
    fc1 = new SimpleFreecellModel();

    nullDeck = new ArrayList<Card>();
    not52Deck = new ArrayList<Card>();
    standard = new ArrayList<Card>();

    fv = new FreecellTextView(fc1);

    for (int i = 0; i <= 52; i++) {
      nullDeck.add(null);
    }

    for (Suit suit : Suit.values()) {
      for (Value val: Value.values()) {
        not52Deck.add(new Card(val, suit));
      }
    }

    for (Value val: Value.values()) {
      not52Deck.add(new Card(val, Suit.SPADE));
    }

    for (Suit suit : Suit.values()) {
      for (Value val: Value.values()) {
        standard.add(new Card(val, suit));
      }
    }

  }




  @Test
  public void testGetDeck() {
    assertEquals(52, fc1.getDeck().size());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    fc1.startGame(nullDeck, 5, 6, true);
  }


  @Test (expected = IllegalArgumentException.class)
  public void testStartGameWithLargeDeck() {
    fc1.startGame(not52Deck, 7, 3, false);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCascadePiles() {
    fc1.startGame(standard, 3, 2, false);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testOpenPile() {
    fc1.startGame(standard, 8, 0, false);
  }


  String noShuffle = "F1:\n" +
          "F2:\n" +
          "F3:\n" +
          "F4:\n" +
          "O1:\n" +
          "O2:\n" +
          "O3:\n" +
          "O4:\n" +
          "C1: A♣, 9♣, 4♦, Q♦, 7♥, 2♠, 10♠\n" +
          "C2: 2♣, 10♣, 5♦, K♦, 8♥, 3♠, J♠\n" +
          "C3: 3♣, J♣, 6♦, A♥, 9♥, 4♠, Q♠\n" +
          "C4: 4♣, Q♣, 7♦, 2♥, 10♥, 5♠, K♠\n" +
          "C5: 5♣, K♣, 8♦, 3♥, J♥, 6♠\n" +
          "C6: 6♣, A♦, 9♦, 4♥, Q♥, 7♠\n" +
          "C7: 7♣, 2♦, 10♦, 5♥, K♥, 8♠\n" +
          "C8: 8♣, 3♦, J♦, 6♥, A♠, 9♠";


  @Test
  public void testToStringNoShuffle() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    assertEquals(noShuffle, fv.toString());
  }

  @Test
  public void testStartGameDealShuffle() {
    fc1.startGame(fc1.getDeck(), 8, 4, true);
    assertNotEquals(noShuffle, fv.toString());
  }

  @Test
  public void testToString() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, 0, 6, PileType.OPEN, 1);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    fc1.move(PileType.CASCADE, 0, 5, PileType.FOUNDATION, 0);

    String current = "F1: A♠, 2♠\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1: 9♠\n" +
            "O2: 10♠\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♣, 9♣, 4♦, Q♦, 7♥\n" +
            "C2: 2♣, 10♣, 5♦, K♦, 8♥, 3♠, J♠\n" +
            "C3: 3♣, J♣, 6♦, A♥, 9♥, 4♠, Q♠\n" +
            "C4: 4♣, Q♣, 7♦, 2♥, 10♥, 5♠, K♠\n" +
            "C5: 5♣, K♣, 8♦, 3♥, J♥, 6♠\n" +
            "C6: 6♣, A♦, 9♦, 4♥, Q♥, 7♠\n" +
            "C7: 7♣, 2♦, 10♦, 5♥, K♥, 8♠\n" +
            "C8: 8♣, 3♦, J♦, 6♥";

    assertEquals(current, fv.toString());
  }

  @Test
  public void testGameNotStarted() {
    assertEquals("", fv.toString());
  }

  @Test
  public void testMoveFoundation() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, 0, 6, PileType.OPEN, 1);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    fc1.move(PileType.CASCADE, 0, 5, PileType.FOUNDATION, 0);
    assertEquals(2, fc1.getNumCardsInFoundationPile(0));
    assertEquals(new Card(Value.TEN, Suit.SPADE), fc1.getOpenCardAt(1));
  }

  @Test
  public void testMoveCascFound() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    assertEquals(1, fc1.getNumCardsInFoundationPile(0));
    assertEquals(4, fc1.getNumCardsInCascadePile(7));
    assertEquals(1, fc1.getNumCardsInOpenPile(3));
    assertEquals(new Card(Value.NINE, Suit.SPADE), fc1.getOpenCardAt(3));
    assertEquals(new Card(Value.ACE, Suit.SPADE), fc1.getFoundationCardAt(0, 0));
  }

  @Test
  public void testMoveCascCasc() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    fc1.move(PileType.CASCADE, 7, 3, PileType.CASCADE, 5);
    assertEquals(new Card(Value.SIX, Suit.HEART), fc1.getCascadeCardAt(5,6));
    assertEquals(3, fc1.getNumCardsInCascadePile(7));
    assertEquals(7, fc1.getNumCardsInCascadePile(5));
  }

  @Test
  public void testMoveOpenOpen() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 4, 5, PileType.OPEN, 3);
    fc1.move(PileType.OPEN, 3, 0, PileType.OPEN, 1);
    assertEquals(new Card(Value.SIX, Suit.SPADE), fc1.getOpenCardAt(1));
    assertEquals(5, fc1.getNumCardsInCascadePile(4));
    assertEquals(0, fc1.getNumCardsInOpenPile(3));
  }

  @Test
  public void testMoveOpenFound() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 2);
    fc1.move(PileType.CASCADE, 7, 4, PileType.OPEN, 1);
    fc1.move(PileType.OPEN, 1, 0, PileType.FOUNDATION, 0);
    assertEquals(new Card(Value.ACE, Suit.SPADE), fc1.getFoundationCardAt(0,0));
    assertEquals(4, fc1.getNumCardsInCascadePile(7));
    assertEquals(1, fc1.getNumCardsInOpenPile(2));
  }

  @Test
  public void testMoveOpenCasc() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    fc1.move(PileType.CASCADE, 7, 3, PileType.OPEN, 2);
    fc1.move(PileType.OPEN, 2, 0, PileType.CASCADE, 5);
    assertEquals(new Card(Value.SIX, Suit.HEART), fc1.getCascadeCardAt(5,6));
    assertEquals(0, fc1.getNumCardsInOpenPile(2));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFromFound() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    fc1.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFoundDoesNotExist() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFoundNotAce() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.FOUNDATION, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFoundNotSameSuit() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    fc1.move(PileType.CASCADE, 3, 6, PileType.OPEN, 1);
    fc1.move(PileType.CASCADE, 3, 5, PileType.OPEN, 2);
    fc1.move(PileType.CASCADE, 3, 4, PileType.OPEN, 3);
    fc1.move(PileType.CASCADE, 3, 3, PileType.FOUNDATION, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFoundNotOneGreater() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    fc1.move(PileType.CASCADE, 1, 6, PileType.OPEN, 1);
    fc1.move(PileType.CASCADE, 1, 5, PileType.FOUNDATION, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascInvalidSourcePile() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 8, 5, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, -1, 5, PileType.OPEN, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascInvalidSourceCard() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 6, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, 5, -1, PileType.OPEN, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascInvalidDestPile() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.CASCADE, -1);
    fc1.move(PileType.CASCADE, 7, 5, PileType.CASCADE, 8);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascNotLastCard() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 4, PileType.OPEN, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascNotOppositeColor() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.CASCADE, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascNotOneLower() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 6, 5, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, 6, 4, PileType.CASCADE, 2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveOpenInvalidSourcePile() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.OPEN, 4, 0, PileType.CASCADE, 0);
    fc1.move(PileType.OPEN, -1, 0, PileType.CASCADE, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveOpenInvalidDest() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 6, 5, PileType.OPEN, 4);
  }


  @Test (expected = IllegalArgumentException.class)
  public void testMoveOpenInvalidCard() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, 7, 4, PileType.OPEN, 1);
    fc1.move(PileType.OPEN, 1, 1, PileType.FOUNDATION, 0);

  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveNonEmptyOpen() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    fc1.move(PileType.CASCADE, 7, 4, PileType.OPEN, 0);
  }

  @Test
  public void testGameNotOver() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    assertEquals(false, fc1.isGameOver());
  }

  @Test
  public void testGameIsOver() {
    fc1.startGame(fc1.getDeck(), 52, 1, false);

    for (int i = 0; i <= 12; i++) {
      fc1.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 0);
    }

    for (int i = 13; i <= 25; i++) {
      fc1.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 1);
    }

    for (int i = 26; i <= 38; i++) {
      fc1.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 2);
    }

    for (int i = 39; i <= 51; i++) {
      fc1.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 3);
    }

    assertEquals(true, fc1.isGameOver());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidGetCardAt() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.getCascadeCardAt(8, 0);
    fc1.getCascadeCardAt(7,6);
    fc1.getCascadeCardAt(-1, 0);
    fc1.getCascadeCardAt(5, -1);
    fc1.getFoundationCardAt(4, 0);
    fc1.getFoundationCardAt(2,13);
    fc1.getFoundationCardAt(-1, 0);
    fc1.getFoundationCardAt(2, -1);
    fc1.getOpenCardAt(-1);
    fc1.getOpenCardAt(4);
  }

  @Test
  public void testGetCardAt() {
    fc1.startGame(fc1.getDeck(), 8, 4, false);
    fc1.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    fc1.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    assertEquals(new Card(Value.NINE, Suit.SPADE), fc1.getOpenCardAt(3));
    assertEquals(new Card(Value.ACE, Suit.SPADE), fc1.getFoundationCardAt(0, 0));
    assertEquals(new Card(Value.FOUR, Suit.HEART), fc1.getCascadeCardAt(5,3));
  }

  @Test
  public void testGameInProgress() {
    fc1.startGame(fc1.getDeck(), 5, 3, true);
    fc1.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    fc1.startGame(fc1.getDeck(),8, 4, false);
    assertEquals(0, fc1.getNumCardsInOpenPile(0));
    assertEquals(6, fc1.getNumCardsInCascadePile(7));
  }

  @Test
  public void testNumPilesGameNotStart() {
    assertEquals(-1, fc1.getNumCascadePiles());
    assertEquals(-1, fc1.getNumOpenPiles());
  }

  @Test
  public void testNumCascPiles() {
    fc1.startGame(fc1.getDeck(), 4, 3, true);
    assertEquals(4, fc1.getNumCascadePiles());
    assertEquals(3, fc1.getNumOpenPiles());
  }

}
