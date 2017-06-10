package connectfour.api;

import connectfour.impl.Drop;

/**
 * @author Huijuan Zou
 * ConnectFourListener is the interface for the game connectFour.
 * Two players will play the game alternately.  
 * Game ends when board is full, or one player wins.
 */
public interface ConnectFourListener {

  /**
   * switch Modes to human-human or human-computer.
   */
  void  playerMode();

  /**
   * Game starts after press start button.
   */
  void gameStart();

  /**
   * Notify which player is ready to move;
   */
  void playerTurnToMove(String playerName);

  /**
   * One player drops. 
   * This player must be active status.
   * @param drop information including color, column index and row index.
   */
  void playerDrop(Drop drop);

  /**
   * No space in this column is available.
   * Player cannot drop to a full column.
   * @param column column index
   */
  void columnFull(int column);

  /**
   * Fire event when one player wins.
   * @param player player who wins.
   */
  void gameWin(String winnerName);

  /**
   * Game is over when board is full 
   * and no player wins.
   */
  void gameDraw();

  /**
   * reset everything to restart the game.
   */
  void restart();
}
