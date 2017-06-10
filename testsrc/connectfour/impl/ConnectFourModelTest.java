package connectfour.impl;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.junit.Before;
import org.junit.Test;

import connectfour.api.Player;

public class ConnectFourModelTest {
  private ConnectFourModel game;
  private Player player1;
  private Player player2;
  private static final int COL_NUM = 7;
  private static final int ROW_NUM = 6;
  private JLabel[][] grid = new JLabel[ROW_NUM][COL_NUM];

  @Before
  public void setUp() {
    game = ConnectFourModel.getInstance();
    game.setGameActive(false);
    game.switchMode(true);
    player1 = game.getPlayer1();
    player2 = game.getPlayer2();
    for (int i = 0; i < ROW_NUM; i++) {
      for (int j = 0; j < COL_NUM; j++) {
        grid[i][j] = new JLabel();
        grid[i][j].setBorder(new LineBorder(Color.BLACK));
        grid[i][j].setBackground(Color.white);
        grid[i][j].setOpaque(true);
      }
    }
  }

  @Test
  public void testSwitchMode() {
    game.switchMode(true);
    game.switchMode(false);
  }

  @Test
  public void testCheckBoard_emptyBoard() {
    game.checkBoard(0, grid);
  }

  @Test
  public void testCheckBoard_emptyBoard_activePlayer2() {
    game.setActivePlayer(player2);
    game.checkBoard(0, grid);
  }

  /* default setting active player is player1 */
  @Test
  public void testCheckBoard() {
    game.setActivePlayer(player1);
    for (int i = 0; i < ROW_NUM ; i++) {
      grid[i][0].setBackground(Color.red);
    }
    game.checkBoard(1, grid);
  }

  @Test
  public void testCheckBoard_activePlayer2() {
    game.switchMode(false);
    game.setActivePlayer(player2);
    for (int i = 0; i < ROW_NUM; i++) {
      grid[i][1].setBackground(Color.red);
    }
    game.checkBoard(1, grid);
  }

  @Test
  public void testCheckBoard_full() {
    game.setActivePlayer(player1);
    for (int i = 0; i < ROW_NUM; i++) {
      for (int j = 0; j < COL_NUM; j++) {
        grid[i][j].setBackground(Color.red);
      }  
    }
    game.checkBoard(1, grid);
  }

  @Test
  public void testCheckBoard_columnFull() {
    game.setActivePlayer(player1);
    for (int i = 0; i < ROW_NUM; i++) {
      grid[i][1].setBackground(Color.red);
    }
    game.checkBoard(1, grid);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCheckBoard_negativeIndex() {
    game.checkBoard(-1, grid);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCheckBoard_nullGrid() {
    game.checkBoard(1, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testFindDrop_negativeIndex() {
    game.findDrop(-1, grid);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testFindDrop_nullGrid() {
    game.findDrop(1, null);
  }

  @Test 
  public void testFindDrop_gameActive() {
    game.setGameActive(true);
    game.findDrop(1, grid);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCheckWin_nullGrid() {
    Drop drop = new Drop(1, 3, Color.red);
    game.checkWin(null, drop);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCheckWin_nullDrop() {
    game.checkWin(grid, null);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testCheckWin_2null() {
    game.checkWin(null, null);
  }

  /**
   * helper class to reset color back to background color
   */
  public void resetColor() {
    for (int i = 0; i < ROW_NUM; i++) {
      for (int j = 0; j < COL_NUM; j++) {
        grid[i][j].setBackground(Color.white);
      }
    }
  }

  @Test
  public void testIsWinner() {
    Color color;
    if (player1.getPlayerState()) {
      color = player1.getPlayerColor();
    } else {
      color = player2.getPlayerColor();
    }

    //test 4 in a column
    Drop dropCol = new Drop(2, 0, color);
    for (int i = 5; i >= 2; i--) {
      grid[i][0].setBackground(color);
    }
    assertTrue(game.checkWin(grid, dropCol));
    grid[2][0].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropCol));

    //test 4 in a row 
    resetColor();
    Drop dropRow = new Drop(1, 2, color);
    Drop dropLeft = new Drop(1, 0, color);
    Drop dropRight = new Drop(1, 3, color);
    for (int i = 0; i < 4; i++) {
      grid[1][i].setBackground(color);
    }
    assertTrue(game.checkWin(grid, dropRow));
    assertTrue(game.checkWin(grid, dropLeft));
    assertTrue(game.checkWin(grid, dropRight));
    grid[1][0].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropRow));

    //test 4 in diagonal SW
    resetColor();
    Drop dropDiagSW = new Drop(2, 3, color);
    int rowIndex = 2;
    int colIndex = 3;
    while (rowIndex < ROW_NUM && colIndex >= 0) {
      grid[rowIndex][colIndex].setBackground(color);
      rowIndex++;
      colIndex--;
    }
    assertTrue(game.checkWin(grid, dropDiagSW));
    grid[2][3].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropDiagSW));

    //test 4 in diagonal SE
    resetColor();
    Drop dropDiagSE = new Drop(2, 3, color);
    rowIndex = 2;
    colIndex = 3;
    while (rowIndex < ROW_NUM && colIndex < COL_NUM) {
      grid[rowIndex][colIndex].setBackground(color);
      rowIndex++;
      colIndex++;
    }
    assertTrue(game.checkWin(grid, dropDiagSE));
    grid[2][3].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropDiagSE));

    //test 4 in diagonal NW
    resetColor();
    Drop dropDiagNW = new Drop(3, 3, color);
    rowIndex = 3;
    colIndex = 3;
    while (rowIndex >= 0 && colIndex >= 0) {
      grid[rowIndex][colIndex].setBackground(color);
      rowIndex--;
      colIndex--;
    }
    assertTrue(game.checkWin(grid, dropDiagNW));
    grid[3][3].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropDiagNW));

    //test 4 in diagonal NW + SE
    resetColor();
    Drop dropDiagNWSE = new Drop(3, 3, color);
    rowIndex = 3;
    colIndex = 3;
    while (rowIndex >= 1 && colIndex >= 1) {
      grid[rowIndex][colIndex].setBackground(color);
      rowIndex--;
      colIndex--;
    }
    rowIndex = 3;
    colIndex = 3;
    while (rowIndex < ROW_NUM - 1 && colIndex < COL_NUM - 1) {
      grid[rowIndex][colIndex].setBackground(color);
      rowIndex++;
      colIndex++;
    }
    assertTrue(game.checkWin(grid, dropDiagNWSE));
    grid[1][1].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropDiagNWSE));
    grid[1][1].setBackground(color);
    grid[4][4].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropDiagNWSE));

    //test 4 in diagonal NE
    resetColor();
    Drop dropDiagNE = new Drop(3, 3, color);
    rowIndex = 3;
    colIndex = 3;
    while (rowIndex >= 0 && colIndex < COL_NUM) {
      grid[rowIndex][colIndex].setBackground(color);
      rowIndex--;
      colIndex++;
    }
    assertTrue(game.checkWin(grid, dropDiagNE));
    grid[3][3].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropDiagNE));

    //test in diagonal SW + NE
    resetColor();
    Drop dropDiagSWNE = new Drop(3, 3, color);
    rowIndex = 3;
    colIndex = 3;
    while (rowIndex < ROW_NUM - 1 && colIndex >= 2) {
      grid[rowIndex][colIndex].setBackground(color);
      rowIndex++;
      colIndex--;
    }
    rowIndex = 3;
    colIndex = 3;
    while (rowIndex >= 1 && colIndex < COL_NUM - 1) {
      grid[rowIndex][colIndex].setBackground(color);
      rowIndex--;
      colIndex++;
    }
    assertTrue(game.checkWin(grid, dropDiagSWNE));
    grid[1][5].setBackground(Color.white);
    assertFalse(game.checkWin(grid, dropDiagSWNE));

    //empty board
    resetColor();
    Drop dropEmpty = new Drop(0, 0, color);
    assertFalse(game.checkWin(grid, dropEmpty));

    //not empty board, but not winning case
    Drop drop = new Drop(2, 2, color);
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        grid[i][j].setBackground(color);
      }
    }
    assertFalse(game.checkWin(grid, drop));
  }

  @Test
  public void testMakeDrop() {
    game.setGameActive(true);
    game.setActivePlayer(player1);
    game.makeDrop(grid);

    game.switchMode(false);
    game.setActivePlayer(player2);
    game.makeDrop(grid);
  }

  @Test
  public void testSetGameActive(){
    game.setGameActive(true);
    assertEquals(true, game.getGameActive());
    game.setGameActive(false);
    assertEquals(false, game.getGameActive());
    game.setGameActive(true);
    assertEquals(true, game.getGameActive());
  }
}