package connectfour.api;

import java.awt.Color;

/**
 * @author Huijuan Zou
 * Player class get player's information of name, type, color 
 * and whether it is player's turn to press button.
 */
public interface Player {

  /**
   * @return player's name. Two players must have different names.
   */
  String getPlayerName();

  /**
   * @return player's type, human or computer. 
   * Two players can have same type.
   */
  String getPlayerType();

  /**
   * @return player's color. 
   * Once it is player's turn to drop, corresponding JLabel turns this color.
   * Two players must have different colors.
   */
  Color getPlayerColor();

  /**
   * @return boolean value indicating if it is player's turn to press button.
   */
  boolean getPlayerState();

  /**
   * @param isActive true if player is active, false if not active.
   */
  void setPlayerState(boolean isActive);
}