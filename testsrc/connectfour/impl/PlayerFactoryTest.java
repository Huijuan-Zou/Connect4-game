package connectfour.impl;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.List;

import org.junit.Test;

import connectfour.api.Player;

public class PlayerFactoryTest {

  //identical name with player2 && identical color with player1
  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayer_Default_sameColorNameCase1() {
    Player player = new PlayerImpl.Builder("Player2", Color.red)
        .withType("Human")
        .withIsActive(true).build(); 
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player);
    PlayerFactory.getPlayer(); 

  } 

  //identical name with player1 && identical color with player2
  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayer_Default_sameColorNameCase2() {
    Player player2 = new PlayerImpl.Builder("Player1", Color.black)
        .withType("Human")
        .withIsActive(true).build(); 
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player2);
    PlayerFactory.getPlayer();  
  }

  @Test
  public void testGetPlayer_Default_multiple() {
    //same player as player1
    Player player1 = new PlayerImpl.Builder("Player1", Color.red)
        .withType("Human")
        .withIsActive(true).build(); 
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player1);
    PlayerFactory.getPlayer(); 

    //same player as player2
    Player player2 = new PlayerImpl.Builder("Player2", Color.black)
        .withType("Human")
        .withIsActive(false).build(); 
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player2);
    PlayerFactory.getPlayer();

    //none of player field is identical
    Player playerNonIdentical = new PlayerImpl.Builder("Alice", Color.blue)
        .withType("Computer")
        .withIsActive(false).build(); 
    PlayerFactory.reset();
    PlayerFactory.getPlayer(playerNonIdentical);
    PlayerFactory.getPlayer(); 
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayer_Default_moreThanTwo() {
    PlayerFactory.reset();
    PlayerFactory.getPlayer();
    PlayerFactory.getPlayer();
    PlayerFactory.getPlayer();
  } 

  @Test
  public void testGetPlayer_Default_getTwo() {
    PlayerFactory.reset();
    PlayerFactory.getPlayer();
    PlayerFactory.getPlayer();
  } 

  @Test
  public void testGetPlayer_Default_getOne() {
    PlayerFactory.reset();
    PlayerFactory.getPlayer();
  } 

  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayer_customized_null() {
    PlayerFactory.reset();
    PlayerFactory.getPlayer(null);
  } 

  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayer_customized_useBackgroundColor() {
    Player player1 = new PlayerImpl.Builder("Alice", Color.white)
        .withType("Human")
        .withIsActive(false).build();
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player1);
  } 

  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayer_customized_moreThanTwo() {
    Player player1 = new PlayerImpl.Builder("Alice", Color.blue)
        .withType("Human")
        .withIsActive(false).build();
    Player player2 = new PlayerImpl.Builder("Jelly", Color.red)
        .withType("Human")
        .withIsActive(false).build();
    Player player3 = new PlayerImpl.Builder("Lily", Color.green)
        .withType("Human")
        .withIsActive(false).build();
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player1);
    PlayerFactory.getPlayer(player2);
    PlayerFactory.getPlayer(player3);
  } 

  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayer_customized_identicalName() {
    Player player1 = new PlayerImpl.Builder("Alice", Color.blue)
        .withType("Human")
        .withIsActive(false).build();
    Player player2 = new PlayerImpl.Builder("Alice", Color.blue)
        .withType("Human")
        .withIsActive(false).build();
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player1);
    PlayerFactory.getPlayer(player2);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetPlayer_customized_identicalColor() {
    Player player1 = new PlayerImpl.Builder("Alice", Color.blue)
        .withType("Human")
        .withIsActive(false).build();
    Player player2 = new PlayerImpl.Builder("Lily", Color.blue)
        .withType("Human")
        .withIsActive(false).build();
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player1);
    PlayerFactory.getPlayer(player2);
  } 

  @Test 
  public void testGetPlayer_customized_getOne() {
    Player player1 = new PlayerImpl.Builder("Alice", Color.blue)
        .withType("Human")
        .withIsActive(false).build();
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player1);
    List<Player> playerList = PlayerFactory.getPlayerList();
    assertEquals(player1, playerList.get(0));
  } 

  @Test 
  public void testGetPlayer_customized_getTwo() {
    Player player1 = new PlayerImpl.Builder("Alice", Color.blue)
        .withType("Human")
        .withIsActive(true).build();
    Player player2 = new PlayerImpl.Builder("Lily", Color.green)
        .withType("Human")
        .withIsActive(false).build();
    PlayerFactory.reset();
    PlayerFactory.getPlayer(player1);
    PlayerFactory.getPlayer(player2);
    List<Player> playerList = PlayerFactory.getPlayerList();
    assertEquals(player1, playerList.get(0));
    assertEquals(player2, playerList.get(1));
  }
}