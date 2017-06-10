package connectfour.impl;

import java.awt.Color;

/**
 * @author Huijuan Zou
 *  Drop class includes three fields.
 *  rowIndex andcolIndex indicate the location of the drop in the grid.
 *  buttonColor indicate which color the specific JLabel to change to.
 */
public class Drop {
  private final int rowIndex;
  private final int colIndex;
  private final Color buttonColor;

  public Drop(int rowIndex, int colIndex, Color buttonColor) {
    if (rowIndex < 0 || colIndex < 0) {
      throw new IllegalArgumentException("index cannot be negative");
    }
    if (buttonColor == null) {
      throw new IllegalArgumentException("buttonColor cannot be null");
    }
    this.rowIndex = rowIndex;
    this.colIndex = colIndex;
    this.buttonColor = buttonColor;
  }

  public int getColIndex() {
    return colIndex;
  }

  public Color getDropColor() {
    return buttonColor;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  @Override
  public String toString() {
    return "Drop [rowIndex=" + rowIndex + 
        ", colIndex=" + colIndex + ", buttonColor=" +
        buttonColor + "]";
  } 
}