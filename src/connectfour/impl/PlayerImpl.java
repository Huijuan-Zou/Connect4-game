package connectfour.impl;

import java.awt.Color;

import connectfour.api.Player;

/**
 * @author Huijuan Zou
 * Implementation for player interface.
 * Default is human player.
 * No empty/null color, name, type is allowed. 
 * For withType, now only computer and human players are allowed.
 */
public class PlayerImpl implements Player {
  private final String name;
  private final String type;
  private final Color color;
  private boolean isActive;

  public static class Builder {
    private final String name;
    private final Color color;
    private String type = "Human";
    private boolean isActive = false;

    public Builder(String name, Color color) {
      if (name == null || name.equals("")) {
        throw new IllegalArgumentException("name cannot be empty or null");
      }
      if (color == null) {
        throw new IllegalArgumentException("color cannot be empty or null");
      }
      this.color = color;
      this.name = name;
    }

    public Builder withIsActive(boolean isActive) {
      this.isActive = isActive;
      return this;
    }

    public Builder withType(String type) {
      if (type == null || type.equals("")) {
        throw new IllegalArgumentException("type cannot be empty or null");
      }
      if (!type.equals("Computer") && !type.equals("Human")) {
        throw new IllegalArgumentException("illegal Type!");
      }
      this.type = type;
      return this;
    }

    public PlayerImpl build() {
      return new PlayerImpl(this);
    }
  }

  private PlayerImpl(Builder builder) {
    this.name = builder.name;
    this.type = builder.type;
    this.color = builder.color;
    this.isActive = builder.isActive;
  }

  @Override
  public String getPlayerName() {
    return this.name;
  }

  @Override
  public String getPlayerType() {
    return this.type;
  }

  @Override
  public Color getPlayerColor() {
    return this.color;
  }

  @Override
  public boolean getPlayerState() {
    return this.isActive;
  }

  @Override
  public void setPlayerState(boolean isActive) {
    this.isActive = isActive;
  } 

  @Override
  public String toString() {
    return "PlayerImpl [name=" + name + ","
        + " type=" + type + ", color=" + color 
        + ", active=" + isActive + "]";
  }
}