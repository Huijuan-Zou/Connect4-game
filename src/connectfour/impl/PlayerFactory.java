package connectfour.impl;

import java.awt.Color;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import connectfour.api.Player;

/**
 * @author Huijuan Zou
 * Factory to create new players. 
 * Only two players are allowed to create for each model.
 * To create new Players, one can use default setting by calling
 * PlayerFactory.getPlayer(). Or you can use customized setting:
 * create a new player, and call PlayerFactory.getPlayer(player);
 * The default setting is human playing with computer.
 * You can create your own player type to let human play with human.
 */
public class PlayerFactory {
  private static List<Player> playerList = new CopyOnWriteArrayList<Player>();
  public static final String DEFAULT_PLAYER1_NAME  = "Player1";
  public static final String DEFAULT_PLAYER2_NAME  = "Computer Player";
  public static final String DEFAULT_PLAYER1_TYPE  = "Human";
  public static final String DEFAULT_PLAYER2_TYPE  = "Computer";
  public static final Color DEFAULT_PLAYER1_COLOR  = Color.red;
  public static final Color DEFAULT_PLAYER2_COLOR  = Color.black;
  public static final boolean DEFAULT_PLAYER1_ACTIVE  = true;
  public static final boolean DEFAULT_PLAYER2_ACTIVE  = false;

  private PlayerFactory() {
  }

  /**
   * At most two players will be created. 
   * The two players cannot have identical color or name.
   * @return default setting of players. 
   * @throws IllegalArugmentException if a third player is intended to 
   * be created, or try to add default player to list that has player
   *  with name identical as player1 and color identical as player2,
   * or alternatively, as identical color/name is not allowed.
   */
  public static Player getPlayer() {
    Player player;
    Player newPlayer1 = new PlayerImpl
        .Builder(DEFAULT_PLAYER1_NAME, DEFAULT_PLAYER1_COLOR)
        .withIsActive(DEFAULT_PLAYER1_ACTIVE)
        .withType(DEFAULT_PLAYER1_TYPE).build();
    Player newPlayer2 = new PlayerImpl
        .Builder(DEFAULT_PLAYER2_NAME, DEFAULT_PLAYER2_COLOR)
        .withIsActive(DEFAULT_PLAYER2_ACTIVE)
        .withType(DEFAULT_PLAYER2_TYPE).build();
    if (playerList.size() > 1) {
      throw new IllegalArgumentException("Already have two players. "
          + "Cannot create more players.");
    }
    if (playerList.isEmpty()) {
      player = newPlayer1;
      playerList.add(newPlayer1);
    } else if ((!playerList.get(0)
        .getPlayerColor().
        equals(DEFAULT_PLAYER1_COLOR) &&
        !playerList.get(0)
        .getPlayerName()
        .equals(DEFAULT_PLAYER1_NAME))
        ) {
      player = newPlayer1;
      playerList.add(newPlayer1);
    } else if (!playerList.get(0)
        .getPlayerColor()
        .equals(DEFAULT_PLAYER2_COLOR) &&
        !playerList.get(0)
        .getPlayerName().
        equals(DEFAULT_PLAYER2_NAME)) {
      player = newPlayer2;
      playerList.add(newPlayer2);
    } else {
      throw new IllegalArgumentException("Cannot get default player "
          + "because of identical name or color."
          + " Please create your customized player instead.");
    }
    return player;
  }

  /**
   * @throws IllegalArugmentException if a third player is intended to 
   * be created, or same color/name already exists in the player list.
   * @param player customized player.
   * @return customized player.
   */
  public static Player getPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player should not be null");
    }
    if (player.getPlayerColor().equals(Color.white)) {
      throw new IllegalArgumentException("Color cannot be white.");
    }
    if (playerList.size() >1) {
      throw new IllegalArgumentException("Already have two players. "
          + "Cannot create more players.");
    }
    if (playerList.size() == 1 && 
        player.getPlayerName().equals(playerList.get(0).getPlayerName())) {
      throw new IllegalArgumentException("Player name should be different.");
    }
    if (playerList.size() == 1 && 
        player.getPlayerColor().equals(playerList.get(0).getPlayerColor())) {
      throw new IllegalArgumentException("Player color should be different.");
    }
    playerList.add(player);
    return player;
  }

  /**
   * @return list of players generated. 
   * At most two players are allowed.
   */
  public static List<Player> getPlayerList(){
    return playerList;
  }

  /**
   * remove all the players.
   */
  public static void reset() {
    playerList = new CopyOnWriteArrayList<Player>();
  }
}