package connectfour.impl;

/**
 * @author Huijuan Zou
 * The entrance to get the game interface.
 */
public class ConnectFourController {

  /**
   * @param args input of main class.
   */
  public static void main(String[] args) {
    ConnectFourModel game = ConnectFourModel.getInstance();
    new ConnectFourView(game);  
  }
}