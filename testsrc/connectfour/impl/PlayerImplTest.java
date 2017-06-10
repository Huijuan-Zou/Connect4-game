package connectfour.impl;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import connectfour.api.Player;

public class PlayerImplTest {
  private Player player;

  @Before
  public void setUp() {
    player = new PlayerImpl.Builder("Alice", Color.red)
        .withType("Human")
        .withIsActive(true).build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBuilder_emptyName() {
    new PlayerImpl.Builder("", Color.red).build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBuilder_nullName() {
    new PlayerImpl.Builder(null, Color.red).build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBuilder_nullColorCase1() {
    new PlayerImpl.Builder("Alice", null).build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBuilder_nullColorCase2() {
    new PlayerImpl.Builder(null, null).build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBuilder_nullColorCase3() {
    new PlayerImpl.Builder("", null).build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBuilder_emptyType() {
    new PlayerImpl.Builder("Alice", Color.red).withType("").build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBuilder_nullType() {
    new PlayerImpl.Builder("Alice", Color.red).withType(null).build();
  }

  @Test (expected = IllegalArgumentException.class)
  public void testBuilder_illegalType() {
    new PlayerImpl.Builder("Alice", Color.red).withType("Cat").build();
  }

  @Test 
  public void testBuilder() {
    assertEquals("Alice", player.getPlayerName());
    assertEquals(Color.red, player.getPlayerColor());
    assertEquals("Human", player.getPlayerType());
    assertEquals(true, player.getPlayerState());
  }

  @Test 
  public void testSetter() {
    player.setPlayerState(false);
    assertEquals(false, player.getPlayerState());
  }

  @Test 
  public void testToString() {
    assertEquals("PlayerImpl [name=" + "Alice" + ","
        + " type=" + "Human" 
        + ", color=" + Color.red 
        + ", active=" + true
        + "]", player.toString());
  }
}