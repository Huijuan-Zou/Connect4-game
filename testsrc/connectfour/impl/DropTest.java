package connectfour.impl;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class DropTest {
  
  @Test (expected = IllegalArgumentException.class)
  public void testDrop_nullColor() {
    new Drop(1, 2, null);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testDrop_negativeIndexCase1() {
    new Drop(1, -2, Color.red);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testDrop_negativeIndexCase2() {
    new Drop(-2, -2, Color.red);
  }
  
  @Test (expected = IllegalArgumentException.class)
  public void testDrop_negativeIndexCase3() {
    new Drop(-1, 88, Color.red);
  }
  
  @Test 
  public void testDrop_one() {
    Drop drop = new Drop(1, 2, Color.red);
    assertEquals(Color.red, drop.getDropColor());
    assertEquals(1, drop.getRowIndex());
    assertEquals(2, drop.getColIndex());
  }
  
  @Test 
  public void testDrop_multiple() {
    Drop drop1 = new Drop(1, 2, Color.red);
    assertEquals(Color.red, drop1.getDropColor());
    assertEquals(1, drop1.getRowIndex());
    assertEquals(2, drop1.getColIndex());
    
    Drop drop2 = new Drop(20, 9, Color.blue);
    assertEquals(Color.blue, drop2.getDropColor());
    assertEquals(20, drop2.getRowIndex());
    assertEquals(9, drop2.getColIndex());
    
    Drop drop3 = new Drop(2, 13, Color.green);
    assertEquals(Color.green, drop3.getDropColor());
    assertEquals(2, drop3.getRowIndex());
    assertEquals(13, drop3.getColIndex());
  }
  
  @ Test
  public void testDrop_toString() {
    Drop drop = new Drop(1, 2, Color.red);
    assertEquals("Drop [rowIndex=" + 1 + 
        ", colIndex=" + 2 + ", buttonColor=" +
        Color.red + "]", drop.toString() );
  }
}
