package connectfour.impl;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import connectfour.api.ConnectFourListener;

/**
 * @author Huijuan Zou
 * View for the game. Deals with all the interface stuff.
 * Communicate with model.
 * The view interface has three areas: main panel taking care
 * of the view of all the drops; the button panel to let human
 * player to press button and play; the control panel has a list of 
 * player names with corresponding player color and start/restart button,
 * it also has a notifying text area. Start button is to start for 
 * the first time, after that, you need to click restart button to restart 
 * game during the game or after gameWin/gameDraw events.
 */
public class ConnectFourView implements ConnectFourListener {
  private ConnectFourModel game;
  private static final int COL_NUM = 7;
  private static final int ROW_NUM = 6;
  private static JButton[] buttonList = new JButton[COL_NUM];
  private JLabel[][] grid = new JLabel[ROW_NUM][COL_NUM];
  private JTextArea textArea = new JTextArea("", 15, 15);
  private JPanel gridPanel;
  private JFrame frame;
  private boolean gameStarted = false;
  private JLabel player1Color;
  private JLabel player2Color;
  private JLabel player1Name;
  private JLabel player2Name;

  public ConnectFourView(ConnectFourModel game) {
    this.game = game;
    game.addGameListener(this);
    setUp();
  }

  /**
   * Set up the board.
   */
  private void setUp(){
    //main frame
    frame = new JFrame();
    // main panel
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 7, 2, 2));
    for (int i = 0; i < COL_NUM; i++) {
      JButton button = new JButton();
      int index = i;
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          buttonPressed(index);
        }
      });
      buttonList[i] = button;
      buttonPanel.add(button);
    }
    //center panel
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BorderLayout());
    gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(6, 7, 2, 2));
    for (int i = 0; i < ROW_NUM; i++) {
      for (int j = 0; j < COL_NUM; j++) {
        grid[i][j] = new JLabel();
        grid[i][j].setBorder(new LineBorder(Color.BLACK));
        grid[i][j].setBackground(Color.white);
        grid[i][j].setOpaque(true);
        gridPanel.add(grid[i][j]);
      }
    }
    centerPanel.add(gridPanel,BorderLayout.CENTER);
    centerPanel.add(buttonPanel,BorderLayout.SOUTH);
    //right panel
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BorderLayout());
    JButton startBtn = new JButton("start");
    startBtn.setBackground(Color.green);
    startBtn.setOpaque(true);
    startBtn.setMaximumSize(new Dimension(1, 1));
    startBtn.setBorder(new LineBorder(Color.BLACK));
    startBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        gameStart();
      }
    });
    JButton restartBtn = new JButton("restart");
    restartBtn.setBackground(Color.green);
    restartBtn.setOpaque(true);
    restartBtn.setBorder(new LineBorder(Color.BLACK));
    restartBtn.setMaximumSize(new Dimension(1, 1));
    restartBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        restart();
      }
    });
    JButton twoPlayersBtn = new JButton("Human Human Mode");
    twoPlayersBtn.setOpaque(true);
    twoPlayersBtn.setBorder(new LineBorder(Color.BLACK));
    twoPlayersBtn.setMaximumSize(new Dimension(1, 1));
    twoPlayersBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        switchMode(true);
      }
    });
    JButton onePlayerBtn = new JButton("Human Computer Mode");
    onePlayerBtn.setOpaque(true);
    onePlayerBtn.setBorder(new LineBorder(Color.BLACK));
    onePlayerBtn.setMaximumSize(new Dimension(1, 1));
    onePlayerBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        switchMode(false);
      }
    });
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new GridLayout(4,1));
    controlPanel.add(startBtn);
    controlPanel.add(restartBtn);
    controlPanel.add(twoPlayersBtn);
    controlPanel.add(onePlayerBtn);  
    rightPanel.add(new JScrollPane(textArea), BorderLayout.SOUTH);
    rightPanel.add(controlPanel, BorderLayout.CENTER );
    JPanel playerPanel = new JPanel();
    playerPanel.setLayout(new GridLayout(1,2));
    player1Color = new JLabel();
    player1Color.setBackground(game.getPlayer1().getPlayerColor());
    player1Color.setOpaque(true);
    player1Color.setMaximumSize(new Dimension(1, 1));
    player2Color = new JLabel();
    player2Color.setBackground(game.getPlayer2().getPlayerColor());
    player2Color.setOpaque(true);
    player2Color.setMaximumSize(new Dimension(1, 1));
    JPanel colorPanel = new JPanel();
    colorPanel.setLayout(new GridLayout(2,1));
    colorPanel.add(player1Color);
    colorPanel.add(player2Color);
    player1Name = new JLabel(game.getPlayer1().getPlayerName());
    player1Name.setBorder(new LineBorder(Color.BLACK));
    player1Name.setMaximumSize(new Dimension(1, 1));
    player2Name = new JLabel(game.getPlayer2().getPlayerName());
    player2Name.setBorder(new LineBorder(Color.BLACK));
    player2Name.setMaximumSize(new Dimension(1, 1));
    JPanel namePanel = new JPanel();
    namePanel.setLayout(new GridLayout(2,1));
    namePanel.add(player1Name);
    namePanel.add(player2Name);
    playerPanel.add(colorPanel);
    playerPanel.add(namePanel); 
    rightPanel.add(playerPanel, BorderLayout.NORTH);
    //adding to main panel
    panel.add(centerPanel, BorderLayout.CENTER);
    panel.add(rightPanel, BorderLayout.EAST);
    //adding to frame
    frame.getContentPane().add(panel);
    //to get center location of specific screen
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int locX = (int) dim.getWidth() * 4 / 12;
    int locY = (int) dim.getHeight() * 2 / 12;
    frame.setTitle("Connect Four");
    frame.setSize(700, 600);
    frame.setLocation(locX, locY);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    frame.setFocusable(true);
    frame.setResizable(false);
  }

  /**
   * call model to check if board/specific-column is full.
   * @param index number of column that is pressed by player.
   */
  private void buttonPressed(int index) { 
    game.checkBoard(index, grid);
  } 

  /** 
   * @param mode mode of human-human or human-computer.
   */
  private void switchMode(boolean mode) {
    game.switchMode(mode);
  }

  public boolean getGameStarted() {
    return this.gameStarted;
  }

  @Override
  public void playerMode() {
    textArea.append("Mode switched!\n");
    player1Color.setBackground(game.getPlayer1().getPlayerColor());
    player2Color.setBackground(game.getPlayer2().getPlayerColor());
    player1Name.setText(game.getPlayer1().getPlayerName());
    player2Name.setText(game.getPlayer2().getPlayerName());
    restart();
  }

  @Override
  public void gameStart() {
    if (!gameStarted) {
      textArea.append("Game started!\n");
      this.gameStarted = true;
      game.setGameActive(true);
      game.makeDrop(grid);
    }
  }

  @Override
  public void playerDrop(Drop newDrop) {
    grid[newDrop.getRowIndex()][newDrop.getColIndex()]
        .setBackground(newDrop.getDropColor());
    game.checkWin(grid, newDrop);
  }

  @Override
  public void columnFull(int column) {
    textArea.append("Column is full. Try other columns\n");
  }

  @Override
  public void gameWin(String winnerName) {
    textArea.append("Congratulations! " + winnerName + " wins!\n");
    game.setGameActive(false);
  }

  @Override
  public void gameDraw() {
    textArea.append("Game draw. Press restart to start a new game!\n");
    game.setGameActive(false);
  }

  @Override
  public void playerTurnToMove(String playerName) {
    textArea.append(playerName + "'s turn to move.\n");    
  }

  @Override
  public void restart() {
    for (int i = 0; i < ROW_NUM; i++) {
      for (int j = 0; j < COL_NUM; j++) {
        grid[i][j].setBackground(Color.white);
      }
    }
    textArea.append("game restarted!\n");
    game.setGameActive(true);
    game.makeDrop(grid);
  }
}