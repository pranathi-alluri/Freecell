import org.junit.Test;

import cs3500.freecell.Card;
import cs3500.freecell.Suit;
import cs3500.freecell.Value;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * Tests for the card class.
 */
public class CardTest {
  Card aceSpades = new Card(Value.ACE, Suit.SPADE);
  Card eightHearts = new Card(Value.EIGHT, Suit.HEART);
  Card kingDiamond = new Card(Value.KING, Suit.DIAMOND);
  Card jackClub = new Card(Value.JACK, Suit.CLUB);
  Card queenHeart = new Card(Value.QUEEN, Suit.HEART);

  @Test
  public void testGetVal() {
    assertEquals(1, aceSpades.getVal());
    assertEquals(8, eightHearts.getVal());
    assertEquals(11, jackClub.getVal());
  }

  @Test
  public void testGetSuit() {
    assertEquals("♥", queenHeart.getSuit());
    assertEquals("♦", kingDiamond.getSuit());
    assertEquals("♣", jackClub.getSuit());
  }

  @Test
  public void testGetColor() {
    assertEquals("black", aceSpades.getColor());
    assertEquals("red", eightHearts.getColor());
    assertEquals("black", jackClub.getColor());
    assertEquals("red", kingDiamond.getColor());
  }

  @Test
  public void testGetValues() {
    assertEquals("A", aceSpades.getValue());
    assertEquals("8", eightHearts.getValue());
    assertEquals("J", jackClub.getValue());
  }

  @Test
  public void testToString() {
    assertEquals("K♦", kingDiamond.toString());
    assertEquals("Q♥", queenHeart.toString());
    assertEquals("J♣", jackClub.toString());
    assertEquals("A♠", aceSpades.toString());

  }

  @Test
  public void equalsWorks() {
    assertEquals(jackClub, jackClub);

    assertNotEquals(queenHeart, kingDiamond);
  }

  @Test
  public void hashCodeisHashofVal() {
    assertEquals("8".hashCode(), eightHearts.hashCode());
  }



}
