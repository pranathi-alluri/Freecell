package cs3500.freecell;

/**
 * Represents a card in a standard deck.
 */
public class Card {

  private final Value value;
  private final Suit suit;

  /**
   * Constructs a typical card that takes in a value and Suit.
   *
   * @param value an value type of the card see @link{Value}
   * @param suit the suit type of the card see @link{Suit}.
   * @throws IllegalArgumentException if the value is less than 1 or greater than 13 or null
   */
  public Card(Value value, Suit suit) {
    if (value == null) {
      throw new IllegalStateException("Value cannot be null");
    }
    if ( suit == null) {
      throw new IllegalStateException("Suit cannot be null");

    }
    this.value = value;
    this.suit = suit;
  }


  /**
   * Get the integer value of the card.
   * @return the value of the card as an integer.
   */
  public int getVal() {
    return this.value.ordinal() + 1;
  }

  /**
   * Gets the suit type of the card.
   * @return the suit type of the card as a string.
   */
  public String getSuit() {
    switch (suit) {
      case SPADE:
        return "♠";

      case HEART:
        return "♥";

      case CLUB:
        return "♣";

      case DIAMOND:
        return "♦";

      default:
        throw new IllegalArgumentException("Not a valid Suit");
    }
  }

  /**
   * Gets the color of the card.
   * @return color of the card as a String.
   */
  public String getColor() {
    String color = "";
    switch (suit) {
      case CLUB:

      case SPADE:
        color = "black";
        break;

      case HEART:

      case DIAMOND:
        color = "red";
        break;

      default:
        throw new IllegalArgumentException("Not a valid Suit");
    }
    return color;
  }

  /**
   * Gets the value of the card as a string.
   *
   * @return the value of the card as a string.
   */
  public String getValue() {
    switch (value) {
      case ACE:
        return "A";

      case JACK:
        return "J";

      case QUEEN:
        return "Q";

      case KING:
        return "K";

      default:
        return "" + getVal();
    }
  }

  @Override
  public String toString() {
    return getValue() + getSuit();
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if (!(that instanceof Card)) {
      return false;
    }

    return (this.getSuit().equals(((Card) that).getSuit())
            && this.getValue().equals(((Card) that).getValue()));
  }

  @Override
  public int hashCode() {
    return this.getValue().hashCode();
  }
}
