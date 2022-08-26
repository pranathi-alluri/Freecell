import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;


import cs3500.freecell.Card;
import cs3500.freecell.controller.FailingAppendable;
import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.model.multimove.MultiMoveFreecellModel;
import cs3500.freecell.view.FreecellTextView;
import cs3500.freecell.view.FreecellView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit test cases for the freecell controller.
 */
public class FreecellControllerTest {
  // your tests go here

  private Readable empty;
  private Readable source;
  private Readable quit;
  private Readable invalidSource;
  private Readable cIndex;
  private Readable oIndex;
  private Readable fIndex;
  private Readable invalidCard;
  private Readable invalidDest;
  private Readable cDest;
  private Readable fDest;
  private Readable oDest;
  private Readable open;
  private Readable parseSource;
  private Readable game;
  private Appendable ap1;
  private FreecellModel fc1;
  private List<Card> deck;
  private FreecellView fv1;
  private Readable quitCard;
  private Readable quitDest;
  private FreecellModel multi;
  private FreecellView fv2;
  private Readable invalid;
  private Appendable ap2;
  private Readable gameErrors;

  @Before
  public void setUp() {
    invalid = null;
    empty = new StringReader("");
    source = new StringReader("C1");
    quit = new StringReader("q");
    quitCard = new StringReader("C1 q C4");
    quitDest = new StringReader("C1 7 Q");
    invalidSource = new StringReader("something");
    cIndex = new StringReader("Cblah");
    oIndex = new StringReader("Oblah");
    fIndex = new StringReader("Fblah");
    invalidCard = new StringReader("C1 blah");
    invalidDest = new StringReader("C1 1 blah");
    cDest = new StringReader("C1 1 Cblah");
    fDest = new StringReader("C1 1 Fblah");
    oDest = new StringReader("C1 1 Oblah");
    open = new StringReader("C1 7 O1 C1");
    parseSource = new StringReader("C0 3 O1");
    game = new StringReader("" +
            "C1 1 F1 C2 1 F1 C3 1 F1 C4 1 F1 C5 1 F1 C6 1 F1 C7 1 F1 C8 1 F1 C9 1 F1 C10 1 F1 " +
            "C11 1 F1 C12 1 F1 C13 1 F1 " +
            "C14 1 F2 C15 1 F2 C16 1 F2 C17 1 F2 C18 1 F2 C19 1 F2 C20 1 F2 C21 1 F2 C22 1 F2 " +
            "C23 1 F2 C24 1 F2 C25 1 F2 C26 1 F2 " +
            "C27 1 F3 C28 1 F3 C29 1 F3 C30 1 F3 C31 1 F3 C32 1 F3 C33 1 F3 C34 1 F3 C35 1 F3 " +
            "C36 1 F3 C37 1 F3 C38 1 F3 C39 1 F3 " +
            "C40 1 F4 C41 1 F4 C42 1 F4 C43 1 F4 C44 1 F4 C45 1 F4 C46 1 F4 C47 1 F4 C48 1 F4 " +
            "C49 1 F4 C50 1 F4 C51 1 F4 C52 1 F4");
    gameErrors = new StringReader("" +
            "C1 1 F1 C2 1 F2 C2 1 F1 C3 1 F1 C4 1 F1 C5 1 F1 C6 1 F1 C7 1 F1 C8 1 F1 C9 1 F1" +
            "C10 1 F1 C11 1 F1 C12 1 F1 C13 1 F1 " +
            "C14 1 F2 C15 1 F2 C16 1 F2 C17 1 F2 C18 1 F2 C19 1 F2 C20 1 F2 C21 1 F2 C22 1 F2 " +
            "C23 1 F2 C24 1 F2 C25 1 F2 C26 1 F2 " +
            "C27 1 F3 C28 1 F3 C29 1 F3 C30 1 F3 C31 1 F3 C32 1 F3 C33 1 F3 C34 1 F3 C35 1 F3 " +
            "C36 1 F3 C37 1 F3 C38 1 F3 C39 1 F3 " +
            "C40 1 F4 C41 1 F4 C42 1 F4 C43 1 F4 C44 1 F4 C45 1 F4 C46 1 F4 C47 1 F4 C48 1 F4 " +
            "C49 1 F4 C50 1 F4 C51 1 F4 C52 1 F4");
    ap1 = new StringBuffer();
    ap2 = null;
    fc1 = new SimpleFreecellModel();
    multi = new MultiMoveFreecellModel();
    deck = fc1.getDeck();
    fv1 = new FreecellTextView(fc1, ap1);
    fv2 = new FreecellTextView(multi, ap1);


  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAppendable() {
    new SimpleFreecellController(fc1, empty, ap2).playGame(deck,
            8, 4, false);
    new SimpleFreecellController(multi, empty, ap2).playGame(deck,
            8, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullReadable() {
    new SimpleFreecellController(fc1, invalid, ap1).playGame(deck,
            8, 4, false);
    new SimpleFreecellController(multi, invalid, ap1).playGame(deck,
            8, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    new SimpleFreecellController(null, empty, ap1).playGame(deck,
            8, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDeck() {
    new SimpleFreecellController(fc1, empty, ap1).playGame(null,
            8, 4, false);
    new SimpleFreecellController(multi, empty, ap1).playGame(null,
            8, 4, false);
  }

  @Test
  public void testOutputGameNotStarted() {
    new SimpleFreecellController(fc1, empty, ap1).playGame(deck, 0,
            4, false);
    assertEquals("Could not start game.", this.ap1.toString());
  }

  @Test
  public void testOutputGameNotStartedMulti() {
    new SimpleFreecellController(multi, empty, ap1).playGame(deck, 0,
            4, false);
    assertEquals("Could not start game.", this.ap1.toString());
  }

  @Test
  public void testOutputBoard() {
    new SimpleFreecellController(fc1, source, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString(), this.ap1.toString());
  }

  @Test
  public void testOutputBoardMulti() {
    new SimpleFreecellController(multi, source, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString(), this.ap1.toString());
  }

  @Test
  public void testSourceQuit() {
    new SimpleFreecellController(fc1, quit, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + "\n" + "Game quit prematurely.", this.ap1.toString());
  }

  @Test
  public void testSourceQuitMulti() {
    new SimpleFreecellController(multi, quit, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + "\n" + "Game quit prematurely.", this.ap1.toString());
  }

  @Test
  public void testCardQuit() {
    new SimpleFreecellController(fc1, quitCard, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + fv1.toString() + "\n" + "Game quit prematurely.",
            this.ap1.toString());
  }

  @Test
  public void testCardQuitMulti() {
    new SimpleFreecellController(multi, quitCard, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + fv2.toString() + "\n" + "Game quit prematurely.",
            this.ap1.toString());
  }

  @Test
  public void testDestQuit() {
    new SimpleFreecellController(fc1, quitDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + fv1.toString() + fv1.toString() + "\n"
            + "Game quit prematurely.", this.ap1.toString());
  }

  @Test
  public void testDestQuitMulti() {
    new SimpleFreecellController(multi, quitDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + fv2.toString() + fv2.toString() + "\n"
            + "Game quit prematurely.", this.ap1.toString());
  }


  @Test
  public void testInvalidSourcePile() {
    new SimpleFreecellController(fc1, invalidSource, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + "\n" + "Invalid source pile. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidSourcePileMulti() {
    new SimpleFreecellController(multi, invalidSource, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + "\n" + "Invalid source pile. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidCascSourceIndex() {
    new SimpleFreecellController(fc1, cIndex, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + "\n" + "Invalid source pile. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidCascSourceIndexMulti() {
    new SimpleFreecellController(multi, cIndex, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + "\n" + "Invalid source pile. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidFoundSourceIndex() {
    new SimpleFreecellController(fc1, fIndex, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + "\n" + "Invalid source pile. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidFoundSourceIndexMulti() {
    new SimpleFreecellController(multi, fIndex, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + "\n" + "Invalid source pile. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidOpenSourceIndex() {
    new SimpleFreecellController(fc1, oIndex, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + "\n" + "Invalid source pile. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidOpenSourceIndexMulti() {
    new SimpleFreecellController(multi, oIndex, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + "\n" + "Invalid source pile. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidCardIndex() {
    new SimpleFreecellController(fc1, invalidCard, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + fv1.toString() + "\n"
                    + "\nInvalid card index. Try again.",
            this.ap1.toString());
  }

  @Test
  public void testInvalidCardIndexMulti() {
    new SimpleFreecellController(multi, invalidCard, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + fv2.toString() + "\n"
                    + "\nInvalid card index. Try again.",
            this.ap1.toString());
  }



  @Test
  public void testInvalidDestPile() {
    new SimpleFreecellController(fc1, invalidDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + fv1.toString() + fv1.toString() + "\n"
            + "Invalid destination pile. Try again.", this.ap1.toString());
  }

  @Test
  public void testInvalidDestPileMulti() {
    new SimpleFreecellController(multi, invalidDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + fv2.toString() + fv2.toString() + "\n"
            + "Invalid destination pile. Try again.", this.ap1.toString());
  }

  @Test
  public void testInvalidCascDestIndex() {
    new SimpleFreecellController(fc1, cDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + fv1.toString() + fv1.toString() + "\n"
            + "Invalid destination pile. Try again.", this.ap1.toString());
  }

  @Test
  public void testInvalidCascDestIndexMulti() {
    new SimpleFreecellController(multi, cDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + fv2.toString() + fv2.toString() + "\n"
            + "Invalid destination pile. Try again.", this.ap1.toString());
  }

  @Test
  public void testInvalidFoundDestIndex() {
    new SimpleFreecellController(fc1, fDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + fv1.toString() + fv1.toString() + "\n"
            + "Invalid destination pile. Try again.", this.ap1.toString());
  }

  @Test
  public void testInvalidFoundDestIndexMulti() {
    new SimpleFreecellController(multi, fDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + fv2.toString() + fv2.toString() + "\n"
            + "Invalid destination pile. Try again.", this.ap1.toString());
  }

  @Test
  public void testInvalidOpenDestIndex() {
    new SimpleFreecellController(fc1, oDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + fv1.toString() + fv1.toString() + "\n"
            + "Invalid destination pile. Try again.", this.ap1.toString());
  }

  @Test
  public void testInvalidOpenDestIndexMulti() {
    new SimpleFreecellController(multi, oDest, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv2.toString() + fv2.toString() + fv2.toString() + "\n"
            + "Invalid destination pile. Try again.", this.ap1.toString());
  }

  @Test
  public void testMove() {
    new SimpleFreecellController(fc1, open, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString(), this.ap1.toString().substring(801, 1067));
  }


  @Test
  public void testInvalidMove() {
    new SimpleFreecellController(fc1, parseSource, ap1).playGame(deck, 8,
            4, false);
    assertEquals(fv1.toString() + fv1.toString() + fv1.toString() + "\n" + "Invalid index."
            + fv1.toString(), this.ap1.toString());
  }

  @Test
  public void testGameOver() {
    new SimpleFreecellController(fc1, game, ap1).playGame(deck, 52,
            4, false);
    assertEquals(fv1.toString() + "\n" + "Game over.",
            this.ap1.toString().substring(96294, 96795));
  }

  @Test
  public void testGameOverErrors() {
    new SimpleFreecellController(fc1, gameErrors, ap1).playGame(deck, 52,
            4, false);
    assertEquals(fv1.toString() + "\n" + "Game over.", this.ap1.toString());
  }

  @Test
  public void testGameOverMulti() {
    new SimpleFreecellController(multi, game, ap1).playGame(deck, 52,
            4, false);
    assertEquals(fv2.toString() + "\n" + "Game over.",
            this.ap1.toString().substring(96294, 96795));
  }

  @Test
  public void testGameOverErrorsMulti() {
    new SimpleFreecellController(multi, gameErrors, ap1).playGame(deck, 52,
            4, false);
    assertEquals(fv2.toString() + "\n" + "Game over.", this.ap1.toString());
  }

  @Test (expected = IOException.class)
  public void testIOException() throws IOException {
    Appendable outputLog = new FailingAppendable().append(null);
    new SimpleFreecellController(fc1, empty, outputLog);
    fail("IOException expected but didn't happen");
  }













}

