package cs3500.freecell.model;

import java.util.List;

import cs3500.freecell.Card;
import cs3500.freecell.Suit;
import cs3500.freecell.Value;

import java.util.ArrayList;
import java.util.Collections;

//Update Javadoc based on feedback from Assignment 2
/**
 * Represents a simple model implementation of {@link FreecellModel} which controls the state
 * of the game and allows a player to start a valid game and move one card at a time to any pile.
 */

public class SimpleFreecellModel implements FreecellModel<Card> {

  // Access changed to protected from private, so MultiMoveFreecellModel has access.
  protected List<List<Card>> cascadePiles;
  private List<List<Card>> openPiles;
  private List<List<Card>> foundationPiles;
  private Boolean gameStarted;

  /**
   * Constructs the default instance of SimpleFreecellModel with no arguments.
   */
  public SimpleFreecellModel() {
    cascadePiles = new ArrayList<List<Card>>();
    openPiles = new ArrayList<List<Card>>();
    foundationPiles = new ArrayList<List<Card>>(4);
    gameStarted = false;

    for (int i = 0; i < 4; i++) {
      ArrayList<Card> foundationPile = new ArrayList<Card>();
      foundationPiles.add(foundationPile);
    }
  }


  @Override
  public List<Card> getDeck() {
    List<Card> deckOfCards = new ArrayList<Card>(52);

    for (Suit suit : Suit.values()) {
      for (Value val : Value.values()) {
        deckOfCards.add(new Card(val, suit));
      }
    }
    return deckOfCards;
  }


  /**
   * Checks to ensure the given parameters are valid for the game.
   *
   * @param deck         the given deck of cards for the game.
   * @param cascadePiles the number of cascade piles for game.
   * @param openPiles    the number of open piles used for the game.
   */
  private void checkValidStart(List<Card> deck, int cascadePiles, int openPiles) {

    if (cascadePiles < 4 || openPiles < 1) {
      throw new IllegalArgumentException("Invalid number of piles.");
    }

    if ((deck.size() != 52)) {
      throw new IllegalArgumentException("Invalid size of deck");
    }

    List<Card> seen = new ArrayList<>();

    for (int i = 0; i < deck.size(); i++) {
      if (deck.get(i) == null) {
        throw new IllegalArgumentException("There cannot be null card in deck.");
      }

      if (seen.contains(deck.get(i))) {
        throw new IllegalArgumentException("Duplicate cards found in deck.");
      } else {
        seen.add(deck.get(i));
      }
    }
  }


  /**
   * Initializes the cascade and open piles for the start of the game.
   *
   * @param cascadePiles the desired number of cascade piles to be used in the game.
   * @param openPiles    the desired number of open piles to be used in the game.
   */
  private void generatePiles(int cascadePiles, int openPiles) {
    for (int i = 0; i < cascadePiles; i++) {
      List<Card> cascade = new ArrayList<Card>();
      this.cascadePiles.add(cascade);
    }

    for (int i = 0; i < openPiles; i++) {
      List<Card> open = new ArrayList<Card>();
      this.openPiles.add(open);
    }

  }

  // Made private for HW3, as this should not be public to the client
  /**
   * Deals the cards in round-robin fashion.
   *
   * @param deck         the given deck of card for the game.
   * @param cascadePiles the desired number of cascade piles to be used in the game.
   */
  private void dealCards(List<Card> deck, int cascadePiles) {
    for (int i = 0; i < cascadePiles; i++) {
      for (int j = i; j < deck.size(); j += cascadePiles) {
        this.cascadePiles.get(i).add(deck.get(j));
      }
    }
  }


  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle) {

    if (deck == null) {
      throw new IllegalArgumentException("Can not start game with null deck.");
    }

    // Ensures valid parameters are given.
    this.checkValidStart(deck, numCascadePiles, numOpenPiles);


    if (gameStarted) {
      cascadePiles = new ArrayList<List<Card>>();
      openPiles = new ArrayList<List<Card>>();
      foundationPiles = new ArrayList<List<Card>>(4);
      gameStarted = false;

      for (int i = 0; i < 4; i++) {
        ArrayList<Card> foundationPile = new ArrayList<Card>();
        foundationPiles.add(foundationPile);
      }
    }

    this.gameStarted = true;


    // Generates cascade and open piles
    this.generatePiles(numCascadePiles, numOpenPiles);

    // If true shuffles the deck of cards
    if (shuffle) {
      Collections.shuffle(deck);
    }

    // Deals the cards
    this.dealCards(deck, numCascadePiles);
  }


  /**
   * Check is the move is valid and if it is returns the card that is being moved.
   *
   * @param source     the pile number of the given type, starting at 0
   * @param sourceType the type of the source pile see @link{PileType}
   * @param cardIndex  the index of the card to be moved from the source pile, starting at 0
   * @return the Card being moved if valid
   */
  private Card validSourceMove(int source, PileType sourceType, int cardIndex) {
    Card card = null;

    switch (sourceType) {
      case CASCADE:
        Card last = getCascadeCardAt(source,
                getNumCardsInCascadePile(source) - 1);

        if (getCascadeCardAt(source, cardIndex) != last) {
          throw new IllegalArgumentException("Can only move last card in a Cascade pile.");
        } else {
          card = getCascadeCardAt(source, cardIndex);
        }
        break;

      case OPEN:

        if (getOpenCardAt(source) == null) {
          throw new IllegalArgumentException("Can not move card from an empty pile.");
        } else if (cardIndex > 0) {
          throw new IllegalArgumentException("Can only move card in 0 index from Open pile.");
        } else {
          card = getOpenCardAt(source);
        }
        break;

      case FOUNDATION:
        throw new IllegalArgumentException("Cannot move card from foundation pile.");

      default:
        throw new IllegalArgumentException("Not a valid pile type.");

    }
    return card;

  }


  /**
   * Ensures if a valid move is being made to the destination pile and moves it.
   *
   * @param source         the type of the source pile see @link{PileType}
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param destination    the type of the destination pile see @link{PileType}
   * @param destPileNumber the pile number of the given type, starting at 0
   * @param move           the card that is being moved
   */
  private void moveToDest(PileType source, int pileNumber,
                          PileType destination, int destPileNumber, Card move) {
    switch (destination) {
      case CASCADE:
        if (getNumCardsInCascadePile(destPileNumber) != 0) {
          Card last = getCascadeCardAt(destPileNumber,
                  getNumCardsInCascadePile(destPileNumber) - 1);

          if (last.getColor().equals(move.getColor())) {
            throw new IllegalArgumentException("Colors have to alternate.");
          } else if (last.getVal() - move.getVal() != 1) {
            throw new IllegalArgumentException("Card moved must be 1 lower than current card.");
          }
        }
        if (source == PileType.CASCADE) {
          cascadePiles.get(destPileNumber).add(move);
          cascadePiles.get(pileNumber).remove(move);
        } else if (source == PileType.OPEN) {
          cascadePiles.get(destPileNumber).add(move);
          openPiles.get(pileNumber).remove(move);
        }
        break;

      case OPEN:

        if (getNumCardsInOpenPile(destPileNumber) != 0) {
          throw new IllegalArgumentException("Open pile can have at most one card");
        } else {
          if (source == PileType.CASCADE) {
            openPiles.get(destPileNumber).add(move);
            cascadePiles.get(pileNumber).remove(move);
          }

          if (source == PileType.OPEN) {
            openPiles.get(destPileNumber).add(move);
            openPiles.get(pileNumber).remove(move);
          }
        }
        break;

      case FOUNDATION:

        if (getNumCardsInFoundationPile(destPileNumber) == 0) {
          if (move.getVal() != 1) {
            throw new IllegalArgumentException("Can only move an Ace.");
          }
        } else {
          Card last = getFoundationCardAt(destPileNumber,
                  getNumCardsInFoundationPile(destPileNumber) - 1);

          if (!last.getSuit().equals(move.getSuit())) {
            throw new IllegalArgumentException("All cards must be of same suit");
          } else if (move.getVal() - last.getVal() != 1) {
            throw new IllegalArgumentException("Card moved must be 1 greater than card in pile.");
          }
        }
        if (source == PileType.CASCADE) {
          foundationPiles.get(destPileNumber).add(move);
          cascadePiles.get(pileNumber).remove(move);
        }

        if (source == PileType.OPEN) {
          foundationPiles.get(destPileNumber).add(move);
          openPiles.get(pileNumber).remove(move);
        }
        break;

      default:
        throw new IllegalArgumentException("Not a valid pile type.");
    }
  }


  @Override
  public void move(PileType source, int pileNumber,
                   int cardIndex, PileType destination, int destPileNumber) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }

    if (isGameOver()) {
      throw new IllegalStateException("Game had ended.");
    }

    Card move = this.validSourceMove(pileNumber, source, cardIndex);
    this.moveToDest(source, pileNumber, destination, destPileNumber, move);

  }

  /**
   * Checks to see if all piles of the given pile type are empty.
   *
   * @param piles the list of piles
   * @return false if there are still cards in any of the piles
   */
  private boolean checkEmpty(List<List<Card>> piles) {
    for (List<Card> cards : piles) {
      if (!cards.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isGameOver() {
    int count = 0;
    for (int i = 0; i < 4; i++) {
      count = count + getNumCardsInFoundationPile(i);
    }
    return count == 52 && checkEmpty(cascadePiles) && checkEmpty(openPiles);

  }

  /**
   * Abstract helper method ot get the number of cards in a given pile.
   *
   * @param index    the pile index
   * @param numPiles the total number of piles
   * @param pile     the pile of a given type
   * @return the number of cards in a given pile.
   */
  private int getNumCardsinPile(int index, int numPiles, List<List<Card>> pile) {
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started.");
    } else if (index < 0 || index > numPiles - 1) {
      throw new IllegalArgumentException("Invalid index.");
    } else {
      return pile.get(index).size();
    }

  }

  @Override
  public int getNumCardsInFoundationPile(int index) {
    return getNumCardsinPile(index, 4, this.foundationPiles);
  }


  /**
   * Get the number of piles of a given type.
   *
   * @param pile all the piles of a given type
   * @return the number of piles of a given type
   */
  private int getNumPiles(List<List<Card>> pile) {
    if (!gameStarted) {
      return -1;
    } else {
      return pile.size();
    }
  }

  @Override
  public int getNumCascadePiles() {
    return getNumPiles(this.cascadePiles);
  }

  @Override
  public int getNumCardsInCascadePile(int index) {
    return getNumCardsinPile(index, getNumCascadePiles(), cascadePiles);
  }

  @Override
  public int getNumCardsInOpenPile(int index) {
    return getNumCardsinPile(index, getNumOpenPiles(), openPiles);
  }

  @Override
  public int getNumOpenPiles() {
    return getNumPiles(this.openPiles);
  }

  /**
   * Helpers methods to abstract get Card at.
   *
   * @param pileIndex the pile number where card is
   * @param cardIndex the card index of card you would like returned
   * @param numPiles  the number of pile type
   * @param numCards  the number of cards in given pile
   * @param pile      the piles of the given piletype
   * @return the Card at a given index in a given pile
   */
  private Card getCardAt(int pileIndex, int cardIndex, int numPiles,
                         int numCards, List<List<Card>> pile) {
    if (!gameStarted) {
      throw new IllegalStateException("The game has not started.");
    } else if (pileIndex < 0 || pileIndex > numPiles - 1) {
      throw new IllegalArgumentException("Invalid pile index.");
    } else if (cardIndex < 0 || cardIndex > numCards - 1) {
      throw new IllegalArgumentException("Invalid Card index.");
    } else {
      return pile.get(pileIndex).get(cardIndex);
    }
  }

  @Override
  public Card getFoundationCardAt(int pileIndex, int cardIndex) {
    return getCardAt(pileIndex, cardIndex, 4, getNumCardsInFoundationPile(pileIndex),
            this.foundationPiles);
  }

  @Override
  public Card getCascadeCardAt(int pileIndex, int cardIndex) {
    return getCardAt(pileIndex, cardIndex, getNumCascadePiles(),
            getNumCardsInCascadePile(pileIndex), this.cascadePiles);
  }

  @Override
  public Card getOpenCardAt(int pileIndex) {
    if (getNumCardsInOpenPile(pileIndex) == 0) {
      return null;
    } else if (getNumCardsInOpenPile(pileIndex) > 1) {
      throw new IllegalArgumentException("An open pile can only have one card.");
    } else {
      return getCardAt(pileIndex, 0, getNumOpenPiles(), getNumCardsInOpenPile(pileIndex),
              this.openPiles);
    }
  }
}
