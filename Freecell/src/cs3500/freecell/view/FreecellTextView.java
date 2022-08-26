package cs3500.freecell.view;

import java.io.IOException;

import cs3500.freecell.model.FreecellModelState;

/**
 * Represents a user-friendly view of the game state by converting it to text.
 */
public class FreecellTextView implements FreecellView {

  private final FreecellModelState<?> model;
  Appendable out;

  /**
   * Constructs Freecell Text View which takes in a model.
   *
   * @param model a freecell model of the game.
   * @throws IllegalArgumentException if the given model is null.
   */
  public FreecellTextView(FreecellModelState<?> model) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null.");
    }
    this.model = model;
  }

  /**
   * Second constructor for Freeceel Text View that produce a more user-friendly output.
   *
   * @param model a freecell model of the game
   * @param out   the appendable object which handles output
   * @throws IllegalArgumentException if the given model is null.
   */
  public FreecellTextView(FreecellModelState<?> model, Appendable out) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null.");
    }
    if (out == null) {
      this.out = System.out;
    }
    this.model = model;
    this.out = out;
  }

  @Override
  public String toString() {
    return getString(model);
  }

  private <T> String getString(FreecellModelState<T> s) {
    try {

      String gameState = "";

      // Foundation piles
      for (int i = 0; i < 4; i++) {
        gameState = gameState + "F" + (i + 1) + ":";
        for (int j = 0; j < s.getNumCardsInFoundationPile(i); j++) {
          if (j == s.getNumCardsInFoundationPile(i) - 1) {
            T foundCardEnd = s.getFoundationCardAt(i, j);
            gameState = gameState + " " + foundCardEnd.toString();
          } else {
            T foundCard = s.getFoundationCardAt(i, j);
            gameState = gameState + " " + foundCard.toString() + ",";
          }
        }
        gameState = gameState + "\n";
      }

      // Open piles
      for (int i = 0; i < s.getNumOpenPiles(); i++) {
        gameState = gameState + "O" + (i + 1) + ":";
        for (int j = 0; j < s.getNumCardsInOpenPile(i); j++) {
          if (j == s.getNumCardsInOpenPile(i) - 1) {
            T validOpenCard = s.getOpenCardAt(i);
            gameState = gameState + " " + validOpenCard.toString();
          } else if (s.getOpenCardAt(j) != null) {
            continue;
          } else {
            T validOpenCard = s.getOpenCardAt(i);
            gameState = gameState + " " + validOpenCard.toString() + ",";
          }
        }
        gameState = gameState + "\n";
      }

      // Cascade Piles
      for (int i = 0; i < s.getNumCascadePiles(); i++) {
        gameState = gameState + "C" + (i + 1) + ":";
        for (int j = 0; j < s.getNumCardsInCascadePile(i); j++) {
          if (j == s.getNumCardsInCascadePile(i) - 1) {
            T cascCardEnd = s.getCascadeCardAt(i, j);
            gameState = gameState + " " + cascCardEnd.toString();
          } else {
            T cascCard = s.getCascadeCardAt(i, j);
            gameState = gameState + " " + cascCard.toString() + ",";
          }
        }

        if (i == s.getNumCascadePiles() - 1) {
          continue;
        } else {
          gameState = gameState + "\n";
        }
      }

      return gameState;

    } catch (IllegalStateException e) {
      return "";
    }
  }

  @Override
  public void renderBoard() throws IOException {
    this.out.append(this.toString());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    this.out.append(message);

  }
}
