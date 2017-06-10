package connectfour.impl;

import java.awt.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;

import connectfour.api.ConnectFourListener;
import connectfour.api.Player;

/**
 * @author Huijuan Zou
 *  ConnectFourModel deals with the logic computation of connect four game.
 *  Call on ConnectFourView if something needs to show.
 *  For this specific game, only human or computer players are allowed.
 *  For this specific class, you can only play have two players :
 *  either human-human or human-computer players. Switch the mode to 
 *  true if you want human-human players playing. Switch mode to false
 *  if want human-computer players playing.
 */
public class ConnectFourModel {
  private static final ConnectFourModel INSTANCE = 
      new ConnectFourModel();
  private List<ConnectFourListener> listeners = 
      new ArrayList<ConnectFourListener>();
  private static final int COL_NUM = 7;
  private static final int ROW_NUM = 6;
  private static Player player1 = PlayerFactory.getPlayer();
  private static Player player2 = PlayerFactory.getPlayer();
  private Drop drop;
  private Player activePlayer = player1;
  private boolean gameActive = false;

  private ConnectFourModel() {
  }

  public static ConnectFourModel getInstance() {
    return INSTANCE;
  }

  public Player getPlayer1() {
    return player1;
  }

  public Player getPlayer2() {
    return player2;
  }

  public boolean getGameActive() {
    return this.gameActive;
  }

  public Player getActivePlayer() {
    return this.activePlayer;
  }

  public void setActivePlayer(Player player) {
    if (player.equals(player1)) {
      player1.setPlayerState(true);
      player2.setPlayerState(false);
      this.activePlayer = player1;
    } else {
      player1.setPlayerState(false);
      player2.setPlayerState(true);
      this.activePlayer = player2;
    }
  }

  /**
   * switches the modes. 
   * @param mode true: Human-human or false: human-computer mode
   */
  public void switchMode(boolean mode) {
    PlayerFactory.reset();
    if (mode) {
      player1 = new PlayerImpl.Builder("Alice", Color.red)
          .withType("Human")
          .withIsActive(true).build();
      player2 = new PlayerImpl.Builder("John", Color.blue)
          .withType("Human")
          .withIsActive(false).build();
    } else {
      player1 = PlayerFactory.getPlayer();
      player2 = PlayerFactory.getPlayer();
    }
    firePlayerModeEvent();
  }

  /**
   * call view to switch mode.
   */
  private void firePlayerModeEvent() {
    for (ConnectFourListener listener: listeners) {
      listener.playerMode();
    }
  }

  /**
   * This method is created for the view to add itself as listener,
   * and act if model needs view to do something.
   * @param listener ConnectFourListener. 
   * @return list of listeners.
   */
  public boolean addGameListener(ConnectFourListener listener) {
    return listeners.add(listener);
  }

  /**
   * Check to see if board is full. If full game draw. 
   * @param colIndex column Index of new drop.
   * @param grid grid of the game, place where players can drop.
   */
  public void checkBoard(int colIndex,JLabel[][] grid) {
    if (grid == null) {
      throw new IllegalArgumentException("Grid cannot be null");
    }
    if (colIndex < 0) {
      throw new IllegalArgumentException("Index cannot be negative");
    }
    boolean full = true;
    for (int i = 0; i < COL_NUM; i++) {
      if (grid[0][i].getBackground().equals(Color.white)) {
        full = false;
        break;
      }
    }
    if (full) {
      fireGameDrawEvent();
    } else if (!grid[0][colIndex]
        .getBackground().equals(Color.white)) {
      if (activePlayer.getPlayerType().equals("Computer")) {
        computerDrop(grid);
      } else {
        fireColumnFullEvent(colIndex);
      }
    } else {
      findDrop(colIndex, grid);
    }
  }

  /**
   * Call view to notify players game draw.
   */
  private void fireGameDrawEvent() {
    for (ConnectFourListener listener: listeners) {
      listener.gameDraw();
    }
  }

  /**
   * Call on view to notify players column is full.
   * @param colIndex column index of new drop.
   */
  private void fireColumnFullEvent(int colIndex) {
    for (ConnectFourListener listener: listeners) {
      listener.columnFull(colIndex);
    }
  }

  /**
   * findDrop finds the index of the next available grid element in a column.
   * Change the active status of players to make them alternately play.
   * @param colIndex column that is button pressed by active player
   * @param grid grid for new drops.
   * @return the index of new Drop;
   */
  public void  findDrop(int colIndex, JLabel[][] grid) {
    if (grid == null) {
      throw new IllegalArgumentException("Grid cannot be null");
    }
    if (colIndex < 0) {
      throw new IllegalArgumentException("Index cannot be negative");
    }
    int rowIndex = ROW_NUM - 1;
    for (int i = ROW_NUM - 1; i >= 0; i--) {
      if (grid[i][colIndex].getBackground().equals(Color.white)) {
        rowIndex = i;
        break;
      }
    }
    if (player1.getPlayerState()) {
      player1.setPlayerState(false);
      player2.setPlayerState(true);
      drop = new Drop(rowIndex, colIndex, player1.getPlayerColor());
      activePlayer = player1;
    } else {
      activePlayer = player2;
      player1.setPlayerState(true);
      player2.setPlayerState(false);
      drop = new Drop(rowIndex, colIndex, player2.getPlayerColor());
    }
    if (gameActive) {
      firePlayerDropEvent();
    }
  }

  /**
   * Check if active player wins by adding the new drop.
   * Decide the next step.
   * @param grid grid of the game, place where players can drop.
   * @param newDrop most recent drop.
   */
  public boolean checkWin( JLabel[][] grid, Drop newDrop) {
    if (grid == null || newDrop == null) {
      throw new IllegalArgumentException("Input cannot be null");
    }
    if (isWinner(grid, newDrop)) {
      fireGameWinEvent(activePlayer.getPlayerName());
      return true;
    }
    makeDrop(grid);
    return false;
  }

  /**
   * Check if active player wins by adding the new drop.
   * @param grid grid of the game, place where players can drop
   * @param newDrop most recent drop.
   * @return whether active player wins.
   */
  private boolean isWinner(JLabel[][] grid, Drop newDrop) {
    return checkRow(grid, newDrop) 
        || checkColumn(grid, newDrop) 
        || checkDiagonal(grid, newDrop);
  }

  /**
   * Check whether row of 4 forms along a column
   * @param grid place where active player can drop.
   * @param newDrop most recent drop.
   * @return true if a row of 4 forms along a column. 
   *         false if not.
   */
  private boolean checkColumn(JLabel[][] grid, Drop newDrop) {
    int rowIndex = newDrop.getRowIndex();
    int colIndex = newDrop.getColIndex();
    int count = 0;
    for (int i = rowIndex; i < ROW_NUM; i++) {
      if (grid[i][colIndex].getBackground()
          .equals(newDrop.getDropColor())) {
        count++;
      } else {
        break;
      }
    }
    return count >= 4;
  }

  /**
   * check each row to see if row of 4 exists after new drop.
   * @param grid grid of the game, place where players can drop.
   * @param newDrop the new position one player want to drop.
   * @return whether a row of 4 is found.
   */
  private boolean checkRow(JLabel[][] grid, Drop newDrop) {
    int rowIndex = newDrop.getRowIndex();
    int colIndex = newDrop.getColIndex();
    //check if left of new drop forms 4 in a row
    int countLeft = 0;
    for (int i = colIndex; i >= 0; i-- ) {
      if (grid[rowIndex][i].getBackground()
          .equals(newDrop.getDropColor())) {
        countLeft++;
      } else {
        break;
      }
    }
    if (countLeft >= 4) {
      return true;
    }

    //check if right of new drop forms 4 in a row
    int countRight = 0;
    rowIndex = newDrop.getRowIndex();
    colIndex = newDrop.getColIndex();
    for (int i = colIndex; i < COL_NUM; i++ ) {
      if (grid[rowIndex][i].getBackground()
          .equals(newDrop.getDropColor())) {
        countRight++;
      } else {
        break;
      }
    }
    if (countRight >= 4) {
      return true;
    }
    if (countRight + countLeft - 1 >= 4) {
      return true;
    }
    return false;
  }

  /**
   * Check to see if a row of 4 forms along any diagonal.
   * @param grid place where active player can drop.
   * @param newDrop most recent drop.
   * @return whether a row of 4 forms along any diagonal.
   */
  private boolean checkDiagonal(JLabel[][] grid, Drop newDrop) {
    int rowIndex = newDrop.getRowIndex();
    int colIndex = newDrop.getColIndex();
    //check NW
    int countNW = 0;
    rowIndex = newDrop.getRowIndex();
    colIndex = newDrop.getColIndex();
    while (rowIndex >= 0 && colIndex >= 0) {
      if (grid[rowIndex][colIndex].getBackground()
          .equals(newDrop.getDropColor())){
        countNW++;
        rowIndex--;
        colIndex--;
      } else {
        break;
      }
    }
    if (countNW >= 4) {
      return true;
    }
    //check SE
    int countSE = 0;
    rowIndex = newDrop.getRowIndex();
    colIndex = newDrop.getColIndex();
    while (rowIndex < ROW_NUM && colIndex < COL_NUM) {
      if (grid[rowIndex][colIndex].getBackground()
          .equals(newDrop.getDropColor())){
        countSE++;
        rowIndex++;
        colIndex++;
      } else {
        break;
      }
    }
    if (countSE >= 4) {
      return true;
    }
    if (countNW + countSE - 1 >= 4) {
      return true;
    }

    //check SW
    int countSW = 0;
    rowIndex = newDrop.getRowIndex();
    colIndex = newDrop.getColIndex();
    while (rowIndex < ROW_NUM && colIndex >= 0) {
      if (grid[rowIndex][colIndex].getBackground()
          .equals(newDrop.getDropColor())){
        countSW++;
        rowIndex++;
        colIndex--;
      } else {
        break;
      }
    }
    if (countSW >= 4) {
      return true;
    }
    //check NE
    int countNE = 0;
    rowIndex = newDrop.getRowIndex();
    colIndex = newDrop.getColIndex();
    while (rowIndex >= 0 && colIndex < COL_NUM) {
      if (grid[rowIndex][colIndex].getBackground()
          .equals(newDrop.getDropColor())){
        countNE++;
        rowIndex--;
        colIndex++;
      } else {
        break;
      }
    }
    if (countNE >= 4) {
      return true;
    }
    if (countSW + countNE - 1 >= 4) {
      return true;
    }
    return false;
  }

  /**
   * Call on view to notify game win and winner's name.
   * change class type to private after done with unit test
   * @param winnerName winner's name.
   */
  private void fireGameWinEvent(String winnerName) {
    for (ConnectFourListener listener: listeners) {
      listener.gameWin(winnerName);
    }
  }

  /**
   * Call on view to make a drop for the active player.
   */
  private void firePlayerDropEvent() {
    for (ConnectFourListener listener: listeners) {
      listener.playerDrop(drop);
    }
  }

  /**
   * Call on view to notify the active player to drop.
   * @param player who is active.
   */
  private void firePlayerTurnToMoveEvent(Player player) {
    for (ConnectFourListener listener: listeners) {
      listener.playerTurnToMove(player.getPlayerName());
    }
  }

  /**
   * Call specific fireEvent based on game and player status.
   * If player type is computer, then go to computerDrop function.
   * Player active states don't matter if both are false or true.
   * The game will determine based on player1's active state
   */
  public void makeDrop(JLabel[][] grid) {
    if (gameActive) {
      if (player1.getPlayerState()) {
        activePlayer = player1;
      } else {
        activePlayer = player2;
      }
      if (activePlayer.getPlayerType().equals("Computer")) {
        computerDrop(grid);
      } else {
        firePlayerTurnToMoveEvent(activePlayer);
      }
    }
  }

  /**
   * choose a random column index for the computer player.
   * @param grid grid of the game.
   */
  public void computerDrop(JLabel[][] grid) {
    int colIndex = new Random().nextInt(COL_NUM);
    checkBoard(colIndex, grid);
  }

  /**
   * Set gameActive true to let players to play.
   * Set false to prevent players to play.
   * @param gameActive whether it is OK to play.
   */
  public void setGameActive(boolean gameActive) {
    this.gameActive = gameActive;
  }
}