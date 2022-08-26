package cs3500.freecell.model.multimove;


import cs3500.freecell.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.SimpleFreecellModel;

/**
 * Represents a multi-move model implementation of {@link FreecellModel} which allows player to
 * move multiple cards from one cascade pile to another, if the move is valid.
 */
public class MultiMoveFreecellModel extends SimpleFreecellModel {

  /**
   * Constructor for a multi-move model of freecell.
    */
  public MultiMoveFreecellModel() {
    super();
  }

  /**
   * Checks to make sure there are enough intermediate slots available.
   * @param size the number of cards that are being moved.
   * @param destPileNumber the index of destination pile where cards are getting moved.
   * @throws IllegalArgumentException if number of cards being moved is greater than the maximum
   *     number of cards that can be moved
   */
  private void enoughIntermediateSlots(int size, int destPileNumber) {
    double numEmptyOpen = 0;
    for (int i = 0; i < getNumOpenPiles(); i++) {
      if (getNumCardsInOpenPile(i) == 0) {
        numEmptyOpen++;
      }
    }

    double numCascOpen = 0;
    for (int i = 0; i < getNumCascadePiles(); i++) {
      if (getNumCardsInCascadePile(i) == 0 && i != destPileNumber) {
        numCascOpen++;
      }
    }

    double maxNumCards = (numEmptyOpen + 1) * Math.pow(2, numCascOpen);
    if (size > maxNumCards) {
      throw new IllegalArgumentException("Cannot move number of cards.");
    }
  }

  /**
   * Ensures moving multiple cards is valid by checking to the build of the cards so that they are
   * alternating in color and descending.
   * @param cascPileNumber index of source Cascade Pile.
   * @param cardIndex the card index of first card in multiple cards being moved.
   * @param destPileNumber index of destination cascade pile.
   * @throws IllegalArgumentException if cards are not alternating colors and/or not descending in
   *     value.
   */
  private void validBuild(int cascPileNumber, int cardIndex, int destPileNumber) {
    Card last = getCascadeCardAt(destPileNumber,
            getNumCardsInCascadePile(destPileNumber) - 1);

    Card current;
    Card next;

    for (int i = cardIndex - 1; i < getNumCardsInCascadePile(cascPileNumber) - 2; i++) {
      if (i == (cardIndex - 1)) {
        current = last;
        next = getCascadeCardAt(cascPileNumber, cardIndex);
      } else {
        current = getCascadeCardAt(cascPileNumber, i);
        next = getCascadeCardAt(cascPileNumber, i + 1);
      }

      if (next.getColor().equals(current.getColor())) {
        throw new IllegalArgumentException("Colors have to alternate.");
      } else if (current.getVal() - next.getVal() != 1) {
        throw new IllegalArgumentException("Card moved must be 1 lower than current card.");
      }
    }
  }

  /**
   * Helps move multiple cards from source pile to destination pile.
   * @param cardIndex the index of first card being moved.
   * @param pile the source pile of cards being moved.
   * @param destPile the destination pile, where the cards will be moved to.
   */
  private void moveMultiple(int cardIndex, int pile, int destPile) {
    for (int i = cardIndex; i < getNumCardsInCascadePile(pile); i++) {
      Card current = getCascadeCardAt(pile, i);
      cascadePiles.get(destPile).add(current);
      cascadePiles.get(pile).remove(i);
      i -= 1;
    }
  }

  @Override
  public void move(PileType source, int pileNumber,
                   int cardIndex, PileType destination, int destPileNumber) {
    if (source == PileType.CASCADE && destination == PileType.CASCADE) {
      this.validBuild(pileNumber, cardIndex, destPileNumber);
      this.enoughIntermediateSlots(getNumCardsInCascadePile(pileNumber) - cardIndex,
              destPileNumber);
      this.moveMultiple(cardIndex, pileNumber, destPileNumber);
    } else {
      super.move(source, pileNumber, cardIndex, destination, destPileNumber);
    }
  }

}
