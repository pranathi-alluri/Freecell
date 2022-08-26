package cs3500.freecell.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cs3500.freecell.Card;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.view.FreecellTextView;

// Update Javadoc based on feedback given from Assignment 2.
/**
 * Represents a controller for the FreeCell game which implements {@link FreecellController}, which
 * reads the input given by the user, and prints the game state to output.
 */

public class SimpleFreecellController implements FreecellController<Card> {
  private final FreecellModel<Card> model;
  private final Readable rd;
  // Made private for HW3 as client should not be able to access the view text.
  private FreecellTextView view;

  /**
   * Creates the constructor for a SimpleFreecellController.
   * @param model the model provided by the user for the freecell game
   * @param rd the readable object which handles user input
   * @param ap the appendable object which handles output
   * @throws IllegalArgumentException if the model, readable, or appendable are null
   */

  public SimpleFreecellController(FreecellModel<Card> model, Readable rd, Appendable ap) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null.");
    }
    if (rd == null) {
      throw new IllegalArgumentException("Readable is not initialized properly.");
    }
    if (ap == null) {
      throw new IllegalArgumentException("Appendable is not initialized properly.");
    }
    this.model = model;
    this.rd = rd;
    this.view = new FreecellTextView(this.model, ap);
  }


  /**
   * Renders the message to the output and catches any exceptions.
   * @param message message that is output
   */
  private void renderMessage(String message) {
    try {
      view.renderMessage(message);
    } catch (IOException e) {
      throw new IllegalStateException("Input not initialized properly.");
    }
  }

  /**
   * Renders the board to the output and catches any exceptions.
   */
  private void renderBoard() {
    try {
      view.renderBoard();
    } catch (IOException e) {
      throw new IllegalStateException("Input not initialized properly.");
    }
  }

  /**
   * Returns the valid PileType from the given user input.
   * @param input the input from the user
   * @return PileType of source or destination based on the string provided by the user
   */
  private PileType getPileType(String input) {
    Character firstChar = input.charAt(0);
    PileType pile = null;
    switch (firstChar) {
      case 'C':
        pile = PileType.CASCADE;
        break;
      case 'F':
        pile = PileType.FOUNDATION;
        break;
      case 'O':
        pile = PileType.OPEN;
        break;
      default:
        throw new IllegalArgumentException("Invalid pile.");
    }
    return pile;
  }

  // Edit in HW3 to remove catch NullPointerException.
  /**
   * Checks to make sure the given pile type is valid.
   * @param input the input from the user
   * @return true if the provided pile by the user is valid.
   * @throws IllegalArgumentException if the pile type is invalid
   */
  private boolean isValidPile(String input) {
    Character firstChar = input.charAt(0);
    String index = input.substring(1);
    if (!(firstChar.equals('C') || firstChar.equals('F') || firstChar.equals('O'))) {
      throw new IllegalArgumentException("Please input a valid Pile Type.");
    } else {
      try {
        Integer.parseInt(index);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Please input a valid index for the pile.");
      }
    }
    return true;
  }


  /**
   * Returns the index of the card to be moved if the given inout if an integer, else returns the
   * index of the pile.
   * @param input the string input by user
   * @return int index of either the card being moved or the index of the source/dest pile
   */
  private int getIndex(String input) {
    String index;
    int result;

    if (checkNum(Character.toString(input.charAt(0)))) {
      index = input;
    } else {
      index = input.substring(1);
    }

    result = Integer.parseInt(index);
    return result;
  }

  /**
   * Ensures that the index provided by the user is valid.
   * @param num the string input of an index by the user
   * @return true if the index is valid
   */
  private boolean checkNum(String num) {
    try {
      Integer.parseInt(num);
    } catch (NumberFormatException e) {
      return false;
    }

    return true;
  }


  /**
   * Ensures that the card index provided by the user is valid.
   * @param input the string input of the card index by the user
   * @return true if the index is valid
   * @throws IllegalArgumentException if an invalid card index is input
   */
  private boolean checkCardIndex(String input) {
    try {
      return Integer.parseInt(input) >= 1;
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("\n" + "Invalid card index. Try again.");
    }
  }



  @Override
  public void playGame(List<Card> deck, int numCascades, int numOpens, boolean shuffle) {
    if (deck == null) {
      throw new IllegalArgumentException("Deck cannot be null.");
    }

    // Edit in HW3 to remove catch Exception, which may inadvertently catch an unexpected exception.
    try {
      this.model.startGame(deck, numCascades, numOpens, shuffle);
    } catch (IllegalArgumentException ie) {
      this.renderMessage("Could not start game.");
      return;
    } catch (IllegalStateException ie) {
      this.renderMessage("Could not start game.");
      return;
    }

    Scanner scan = new Scanner(this.rd);

    ArrayList<String> commands = new ArrayList<String>();

    if (!scan.hasNext()) {
      throw new IllegalStateException("No more elements in Readable");
    } else {
      while (scan.hasNext()) {

        String input = scan.next();

        this.renderBoard();


        if (input.toUpperCase().startsWith("Q")) {
          this.renderMessage("\n" + "Game quit prematurely.");
          return;
        }

        if (commands.size() == 1) {
          try {
            if (checkCardIndex(input)) {
              commands.add(input);
            }
          } catch (IllegalArgumentException e) {
            renderMessage("\n" + e.getMessage());
          }
        } else if ((commands.size() == 0) || (commands.size() == 2)) {
          try {
            if (isValidPile(input)) {
              commands.add(input);
            }
          } catch (IllegalArgumentException e) {
            if (commands.size() == 2) {
              renderMessage("\n" + "Invalid destination pile. Try again.");
            } else {
              renderMessage("\n" + "Invalid source pile. Try again.");
            }
          }
        }

        if (commands.size() == 3) {
          PileType sourcePile = getPileType(commands.get(0));
          int sourcePileIndex = getIndex(commands.get(0));
          int cardIndex = getIndex(commands.get(1));
          PileType destPile = getPileType(commands.get(2));
          int destPileIndex = getIndex(commands.get(2));

          try {
            model.move(sourcePile, sourcePileIndex - 1, cardIndex - 1, destPile,
                    destPileIndex - 1);
          } catch (IllegalArgumentException e) {
            this.renderMessage("\n" + e.getMessage());
          }

          this.renderBoard();
          commands.clear();
        }
      }
    }

    if (model.isGameOver()) {
      this.renderMessage("\n" + "Game over.");
      return;
    }

    return;
  }
}


