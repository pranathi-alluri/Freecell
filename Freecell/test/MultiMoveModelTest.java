import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.Card;
import cs3500.freecell.Suit;
import cs3500.freecell.Value;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.FreecellModelCreator;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.multimove.MultiMoveFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * JUnit test cases for the multi-move freecell model.
 */
public class MultiMoveModelTest {
  private FreecellModelCreator creator;
  private FreecellModel multiModel;
  private List<Card> nullDeck;
  private List<Card> not52Deck;
  private List<Card> standard;
  private Readable rd2;
  private Readable rdNotColors;
  private Readable rdNotDesc;
  private Readable rdNotInter;
  private Appendable ap;
  private FreecellView fv;

  @Before
  public void setUp() {
    creator = new FreecellModelCreator();
    multiModel = new MultiMoveFreecellModel();
    nullDeck = new ArrayList<Card>();
    not52Deck = new ArrayList<Card>();
    standard = new ArrayList<Card>();
    rd2 = new StringReader("C48 1 C23 C23 1 C11");
    rdNotColors = new StringReader("C1 4 C4");
    rdNotDesc = new StringReader("C3 5 C8");
    rdNotInter = new StringReader("C4 7 O1 C6 6 O2 C4 6 O3 C7 6 O4 C4 5 C2 C2 7 C6");



    ap = new StringBuffer();
    fv = new FreecellTextView(multiModel, ap);

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



  @Test(expected = IllegalArgumentException.class)
  public void testCreateSingleMove() {
    FreecellModel singleModel = creator.create(FreecellModelCreator.GameType.SINGLEMOVE);
    singleModel.startGame(singleModel.getDeck(), 8, 4, false);
    singleModel.move(PileType.CASCADE, 1, 4, PileType.CASCADE, 3);
  }

  @Test
  public void testCreateMultiMove() {
    FreecellModel multi = creator.create(FreecellModelCreator.GameType.MULTIMOVE);
    multi.startGame(multi.getDeck(), 8, 4, false);

    multi.move(PileType.CASCADE, 3, 6, PileType.OPEN, 0);
    multi.move(PileType.CASCADE, 3, 5, PileType.OPEN, 2);
    multi.move(PileType.CASCADE, 3, 4, PileType.CASCADE, 1);

    assertEquals(8, multi.getNumCardsInCascadePile(1));
    assertEquals(new Card(Value.TEN, Suit.HEART),
            multi.getCascadeCardAt(1, 7));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testInavlidSourcePile() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 8, 1, PileType.CASCADE, 6);
    multiModel.move(PileType.CASCADE, -1, 1, PileType.CASCADE, 6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInavlidDestPile() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 6, 3, PileType.CASCADE, -1);
    multiModel.move(PileType.CASCADE, 4, 2, PileType.CASCADE, 8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardIndex() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 6, -1, PileType.CASCADE, -1);
    multiModel.move(PileType.CASCADE, 4, 9, PileType.CASCADE, 8);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColor() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 0, 3, PileType.CASCADE, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNotDescendingValues() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 2, 4, PileType.CASCADE, 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNotEnoughIntermediatePiles() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 3, 6, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 5, 5, PileType.OPEN, 1);
    multiModel.move(PileType.CASCADE, 3, 5, PileType.OPEN, 2);
    multiModel.move(PileType.CASCADE, 6, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 3, 4, PileType.CASCADE, 1);

    multiModel.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNotEnoughIntermediatePilesEmptyDest() {
    multiModel.startGame(multiModel.getDeck(), 8, 10, false);
    multiModel.move(PileType.CASCADE, 3, 6, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 5, 5, PileType.OPEN, 1);
    multiModel.move(PileType.CASCADE, 3, 5, PileType.OPEN, 2);
    multiModel.move(PileType.CASCADE, 6, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 3, 4, PileType.CASCADE, 1);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 4);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.OPEN, 5);
    multiModel.move(PileType.CASCADE, 7, 3, PileType.OPEN, 6);
    multiModel.move(PileType.CASCADE, 7, 2, PileType.OPEN, 7);
    multiModel.move(PileType.CASCADE, 7, 1, PileType.OPEN, 8);
    multiModel.move(PileType.CASCADE, 7, 0, PileType.OPEN, 9);

    multiModel.move(PileType.CASCADE, 1, 6, PileType.CASCADE, 7);
  }

  @Test
  public void testMultiMove() {
    multiModel.startGame(multiModel.getDeck(), 52, 4, false);
    multiModel.move(PileType.CASCADE, 22, 0, PileType.CASCADE, 10);
    multiModel.move(PileType.CASCADE, 10, 0, PileType.CASCADE, 24);
    multiModel.move(PileType.CASCADE, 24, 0, PileType.CASCADE, 12);

    assertEquals(4, multiModel.getNumCardsInCascadePile(12));
    assertEquals(new Card(Value.QUEEN, Suit.DIAMOND),
            multiModel.getCascadeCardAt(12, 1));
  }

  @Test
  public void testMutliMoveDefault() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 3, 6, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 3, 5, PileType.OPEN, 2);
    multiModel.move(PileType.CASCADE, 3, 4, PileType.CASCADE, 1);

    assertEquals(8, multiModel.getNumCardsInCascadePile(1));
    assertEquals(new Card(Value.TEN, Suit.HEART),
            multiModel.getCascadeCardAt(1, 7));
  }



  @Test
  public void testSingleMoveWillThrowExceptionWhenAttemptMultiMove() {
    FreecellModel fc = new SimpleFreecellModel();
    new SimpleFreecellController(fc, rd2, ap).playGame(fc.getDeck(), 52,
            4, false);
    assertEquals("Can only move last card in a Cascade pile.",
            ap.toString().substring(3099, 3141));
  }


  @Test
  public void testMultiMoveWithController() {
    FreecellView fv = new FreecellTextView(multiModel, ap);
    new SimpleFreecellController(multiModel, rd2, ap).playGame(multiModel.getDeck(), 52,
            4, false);

    assertEquals(fv.toString(), ap.toString().substring(3098, 3542));

  }

  @Test
  public void testControllerNotAlternatingColors() {
    new SimpleFreecellController(multiModel, rdNotColors, ap).playGame(multiModel.getDeck(),
            8, 4, false);
    assertEquals("Colors have to alternate.",
            ap.toString().substring(802,827));
  }


  @Test
  public void testControllerNotDesc() {
    new SimpleFreecellController(multiModel, rdNotDesc, ap).playGame(multiModel.getDeck(),
            8, 4, false);
    assertEquals("Card moved must be 1 lower than current card.",
            ap.toString().substring(802,847));
  }


  @Test
  public void testControllerNotEnoughIntermidate() {
    new SimpleFreecellController(multiModel, rdNotInter, ap).playGame(multiModel.getDeck(),
            8, 4, false);
    assertEquals("Cannot move number of cards.",
            ap.toString().substring(6086,6114));
  }

  @Test
  public void testGetDeck() {
    assertEquals(52, multiModel.getDeck().size());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    multiModel.startGame(nullDeck, 5, 6, true);
  }


  @Test (expected = IllegalArgumentException.class)
  public void testStartGameWithLargeDeck() {
    multiModel.startGame(not52Deck, 7, 3, false);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCascadePiles() {
    multiModel.startGame(standard, 3, 2, false);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testOpenPile() {
    multiModel.startGame(standard, 8, 0, false);
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
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    assertEquals(noShuffle, fv.toString());
  }

  @Test
  public void testStartGameDealShuffle() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, true);
    assertNotEquals(noShuffle, fv.toString());
  }

  @Test
  public void testToString() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 0, 6, PileType.OPEN, 1);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    multiModel.move(PileType.CASCADE, 0, 5, PileType.FOUNDATION, 0);

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
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 0, 6, PileType.OPEN, 1);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    multiModel.move(PileType.CASCADE, 0, 5, PileType.FOUNDATION, 0);
    assertEquals(2, multiModel.getNumCardsInFoundationPile(0));
    assertEquals(new Card(Value.TEN, Suit.SPADE), multiModel.getOpenCardAt(1));
  }

  @Test
  public void testMoveCascFound() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    assertEquals(1, multiModel.getNumCardsInFoundationPile(0));
    assertEquals(4, multiModel.getNumCardsInCascadePile(7));
    assertEquals(1, multiModel.getNumCardsInOpenPile(3));
    assertEquals(new Card(Value.NINE, Suit.SPADE), multiModel.getOpenCardAt(3));
    assertEquals(new Card(Value.ACE, Suit.SPADE), multiModel.getFoundationCardAt(0, 0));
  }

  @Test
  public void testMoveCascCasc() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    multiModel.move(PileType.CASCADE, 7, 3, PileType.CASCADE, 5);
    assertEquals(new Card(Value.SIX, Suit.HEART), multiModel.getCascadeCardAt(5,6));
    assertEquals(3, multiModel.getNumCardsInCascadePile(7));
    assertEquals(7, multiModel.getNumCardsInCascadePile(5));
  }

  @Test
  public void testMoveOpenOpen() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 4, 5, PileType.OPEN, 3);
    multiModel.move(PileType.OPEN, 3, 0, PileType.OPEN, 1);
    assertEquals(new Card(Value.SIX, Suit.SPADE), multiModel.getOpenCardAt(1));
    assertEquals(5, multiModel.getNumCardsInCascadePile(4));
    assertEquals(0, multiModel.getNumCardsInOpenPile(3));
  }

  @Test
  public void testMoveOpenFound() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 2);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.OPEN, 1);
    multiModel.move(PileType.OPEN, 1, 0, PileType.FOUNDATION, 0);
    assertEquals(new Card(Value.ACE, Suit.SPADE), multiModel.getFoundationCardAt(0,0));
    assertEquals(4, multiModel.getNumCardsInCascadePile(7));
    assertEquals(1, multiModel.getNumCardsInOpenPile(2));
  }

  @Test
  public void testMoveOpenCasc() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    multiModel.move(PileType.CASCADE, 7, 3, PileType.OPEN, 2);
    multiModel.move(PileType.OPEN, 2, 0, PileType.CASCADE, 5);
    assertEquals(new Card(Value.SIX, Suit.HEART), multiModel.getCascadeCardAt(5,6));
    assertEquals(0, multiModel.getNumCardsInOpenPile(2));
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFromFound() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    multiModel.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFoundDoesNotExist() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 4);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFoundNotAce() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.FOUNDATION, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFoundNotSameSuit() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    multiModel.move(PileType.CASCADE, 3, 6, PileType.OPEN, 1);
    multiModel.move(PileType.CASCADE, 3, 5, PileType.OPEN, 2);
    multiModel.move(PileType.CASCADE, 3, 4, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 3, 3, PileType.FOUNDATION, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascInvalidDestPile() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.CASCADE, -1);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.CASCADE, 8);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveFoundNotOneGreater() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    multiModel.move(PileType.CASCADE, 1, 6, PileType.OPEN, 1);
    multiModel.move(PileType.CASCADE, 1, 5, PileType.FOUNDATION, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascInvalidSourcePile() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 8, 5, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, -1, 5, PileType.OPEN, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascInvalidSourceCard() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 6, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 5, -1, PileType.OPEN, 0);
  }


  @Test (expected = IllegalArgumentException.class)
  public void testMoveCascNotLastCard() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.OPEN, 0);
  }




  @Test (expected = IllegalArgumentException.class)
  public void testMoveOpenInvalidSourcePile() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.OPEN, 4, 0, PileType.CASCADE, 0);
    multiModel.move(PileType.OPEN, -1, 0, PileType.CASCADE, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveOpenInvalidDest() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 6, 5, PileType.OPEN, 4);
  }


  @Test (expected = IllegalArgumentException.class)
  public void testMoveOpenInvalidCard() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.OPEN, 1);
    multiModel.move(PileType.OPEN, 1, 1, PileType.FOUNDATION, 0);

  }

  @Test (expected = IllegalArgumentException.class)
  public void testMoveNonEmptyOpen() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 0);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.OPEN, 0);
  }

  @Test
  public void testGameNotOver() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    assertEquals(false, multiModel.isGameOver());
  }

  @Test
  public void testGameIsOver() {
    multiModel.startGame(multiModel.getDeck(), 52, 1, false);

    for (int i = 0; i <= 12; i++) {
      multiModel.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 0);
    }

    for (int i = 13; i <= 25; i++) {
      multiModel.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 1);
    }

    for (int i = 26; i <= 38; i++) {
      multiModel.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 2);
    }

    for (int i = 39; i <= 51; i++) {
      multiModel.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, 3);
    }

    assertEquals(true, multiModel.isGameOver());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testInvalidGetCardAt() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.getCascadeCardAt(8, 0);
    multiModel.getCascadeCardAt(7,6);
    multiModel.getCascadeCardAt(-1, 0);
    multiModel.getCascadeCardAt(5, -1);
    multiModel.getFoundationCardAt(4, 0);
    multiModel.getFoundationCardAt(2,13);
    multiModel.getFoundationCardAt(-1, 0);
    multiModel.getFoundationCardAt(2, -1);
    multiModel.getOpenCardAt(-1);
    multiModel.getOpenCardAt(4);
  }

  @Test
  public void testGetCardAt() {
    multiModel.startGame(multiModel.getDeck(), 8, 4, false);
    multiModel.move(PileType.CASCADE, 7, 5, PileType.OPEN, 3);
    multiModel.move(PileType.CASCADE, 7, 4, PileType.FOUNDATION, 0);
    assertEquals(new Card(Value.NINE, Suit.SPADE), multiModel.getOpenCardAt(3));
    assertEquals(new Card(Value.ACE, Suit.SPADE), multiModel.getFoundationCardAt(0, 0));
    assertEquals(new Card(Value.FOUR, Suit.HEART), multiModel.getCascadeCardAt(5,3));
  }

  @Test
  public void testGameInProgress() {
    multiModel.startGame(multiModel.getDeck(), 5, 3, true);
    multiModel.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    multiModel.startGame(multiModel.getDeck(),8, 4, false);
    assertEquals(0, multiModel.getNumCardsInOpenPile(0));
    assertEquals(6, multiModel.getNumCardsInCascadePile(7));
  }

  @Test
  public void testNumPilesGameNotStart() {
    assertEquals(-1, multiModel.getNumCascadePiles());
    assertEquals(-1, multiModel.getNumOpenPiles());
  }

  @Test
  public void testNumCascPiles() {
    multiModel.startGame(multiModel.getDeck(), 4, 3, true);
    assertEquals(4, multiModel.getNumCascadePiles());
    assertEquals(3, multiModel.getNumOpenPiles());
  }


}
