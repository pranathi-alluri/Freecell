package cs3500.freecell.model;

import cs3500.freecell.model.multimove.MultiMoveFreecellModel;

/**
 * Represents the model creator for a game of freecell. The two possible models that can be created
 * are a single card move model or multi-card move model.
 */
public class FreecellModelCreator {

  /**
   * Game type can either be single-move or mutli-move for Freecell.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE;
  }

  /**
   * Creates either a single-move or multi-move FreecellModel.
   * @param type GameType of the desired model to be created.
   * @return a single-move or multi-move model of Freecell.
   */
  public static FreecellModel create(GameType type) {
    switch (type) {
      case SINGLEMOVE:
        return new SimpleFreecellModel();
      case MULTIMOVE:
        return new MultiMoveFreecellModel();
      default:
        throw new IllegalArgumentException("Not a valid GameType.");
    }
  }
}
