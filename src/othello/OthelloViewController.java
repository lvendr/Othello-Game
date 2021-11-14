/* 
 * File name : OthelloViewController.java
 * Author	 : Song Nguyen Nguyen, 040940830
 * Course	 : CST8221_310 – JAP, Lab Section: 313
 * Assignment: 2-part 2
 * Date		 : December 13, 2020
 * Professor : Karan Kalsi
 * Purpose	 : Frame of the Othello game, this class creates the frame of the program, and is responsible for the functionality of it.
 * Class list: Othello.java, OthelloSplashScreen.java, OthelloViewController.java, OthelloModel.java, OthelloNetworkModalViewController.java, OthelloServer.java
 */

package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * This class is responsible for creating the frame of the program (GUI), along
 * with the functionality of all the buttons, check box, text box.
 * 
 * @author Song N Nguyen
 * @version 1.0
 * @see othello
 * @since 1.8.0_261
 */
public class OthelloViewController extends JFrame {

	/** Swing components are serializable and require serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** Program's title. */
	private final static String FRAME_TITLE = "Song's Othello Client";
	/** Program's icon. */
	private final URL FRAME_ICON = OthelloViewController.class.getResource("ElmoIcon.png");
	/** Image of the white piece. */
	private ImageIcon whitePiece = new ImageIcon(OthelloViewController.class.getResource("white_s.png"));
	/** Image of the black piece. */
	private ImageIcon blackPiece = new ImageIcon(OthelloViewController.class.getResource("black_s.png"));
	/** Image of the valid move. */
	private ImageIcon validMove = new ImageIcon(OthelloViewController.class.getResource("checkmark.png"));
	/** Instantiate Controller class. */
	private final Controller control = new Controller();
	/** An empty border. */
	private final Border emptyBorder = BorderFactory.createEmptyBorder();
	/** Instantiate the Othello logic. */
	private final OthelloModel model = new OthelloModel();
	/** Keep track of the text area. */
	private final JTextArea pinkTextArea = new JTextArea(23, 1);
	/** Keep track of the number buttons. */
	private ArrayList<JButton> rowButtons = new ArrayList<JButton>();
	/** Keep track of the letter buttons. */
	private ArrayList<JButton> colButtons = new ArrayList<JButton>();
	/** Handling user input in the text field. */
	private JTextField textField;
	/** Move button. */
	private JButton moveBtt;
	/** Show the valid moves. */
	private JCheckBox checkBox;
	/**
	 * Keep track of the player turn (even number - player 1, odd number - player
	 * 2).
	 */
	private int playerTurn = 0;
	/** The score screen of player 1. */
	private JLabel player1Pieces;
	/** The score screen player 2. */
	private JLabel player2Pieces;
	/** Keep track of the board. */
	private JLabel[][] boardLabels = new JLabel[8][8];
	/** Networking of Othello game. */
	private OthelloNetworkModalViewController networkDialog = new OthelloNetworkModalViewController(this);
	/** Menu item for new connect and disconnect */
	private JMenuItem newConnection, disconnect;
	/** Current socket that is listening from */
	Socket socket = null;
	/** Current thread that is running on */
	Thread thread;

	/**
	 * Default constructor.
	 */
	public OthelloViewController() {
		super(FRAME_TITLE);
		setFrameProperties();
		createMenuBar();
		createBoardAndButtons();
		createTextField();
		createPlayerStatus();
		initialize(0);
	}

	/**
	 * This method initializes the Othello game with the provided logic, it also
	 * resets the settings of the game (move button, text area, etc.) to default
	 * upon calling.
	 * 
	 * @param mode - int game mode (0 - 6).
	 */
	private void initialize(int mode) {
		model.initialize(mode);
		playerTurn = 0;
		for (int row = 0; row < boardLabels.length; row++) {
			for (int col = 0; col < boardLabels[row].length; col++) {

				switch (model.getBoard(row, col)) {
				case 0:
					boardLabels[row][col].setIcon(null);
					break;

				case 1:
					boardLabels[row][col].setIcon(blackPiece);
					break;

				case 2:
					boardLabels[row][col].setIcon(whitePiece);
					break;

				default:
					break;
				}
			}
		}

		checkBoxSelected();

		if (!(model.canMove(1) || model.canMove(2))) {
			moveBtt.setEnabled(true);
			moveBtt.setText("skip");
			moveBtt.setActionCommand("skip");
			playerTurn = 0;
		} else {
			moveBtt.setText("move");
			moveBtt.setActionCommand("move");
			moveBtt.setEnabled(true);
		}

		for (int i = 0; i < rowButtons.size(); i++) {
			rowButtons.get(i).setBackground(Color.LIGHT_GRAY);
			colButtons.get(i).setBackground(Color.LIGHT_GRAY);
		}

		player1Pieces.setText(Integer.toString(model.getChips(1)));
		player2Pieces.setText(Integer.toString(model.getChips(2)));
		pinkTextArea.setText("Player 1 initialized with " + Integer.toString(model.getChips(1))
				+ " pieces.\nPlayer 2 initialized with " + Integer.toString(model.getChips(2)) + " pieces.");
	}

	/**
	 * This method checks if the check box is selected or not, if so, then make sure
	 * the valid moves of the current player is shown.
	 */
	private void checkBoxSelected() {
		if (checkBox.isSelected()) {
			for (int row = 0; row < boardLabels.length; row++) {
				for (int col = 0; col < boardLabels[row].length; col++) {
					if (model.isValid(row, col, playerTurn % 2 + 1)) {
						boardLabels[row][col].setIcon(validMove);
					}
				}
			}

		} else {
			for (int row = 0; row < boardLabels.length; row++) {
				for (int col = 0; col < boardLabels[row].length; col++) {
					if (model.isValid(row, col, playerTurn % 2 + 1)) {
						boardLabels[row][col].setIcon(null);
					}
				}
			}
		}
	}

	/**
	 * This method creates the right side part of the "game", with the check box at
	 * the top, pink zone, and the player status screen.
	 */
	private void createPlayerStatus() {
		// store the check box, pink zone, and player status in this panel
		JPanel checkBoxPlayerStatus = new JPanel(new GridLayout());
		// arrange the panels vertically (top to bottom: check box -> pink zone ->
		// player status)
		checkBoxPlayerStatus.setLayout(new BoxLayout(checkBoxPlayerStatus, BoxLayout.Y_AXIS));
		checkBoxPlayerStatus.setBackground(Color.YELLOW);

		// store check box in this panel
		JPanel checkBoxPanel = new JPanel(new GridLayout());

		// set up the check box
		checkBox = new JCheckBox("Show Valid Moves");
		checkBox.setActionCommand("valid moves");
		checkBox.addActionListener(control);
		checkBoxPanel.add(checkBox);
		// check box
		checkBoxPlayerStatus.add(checkBoxPanel);

		// store pink zone in this panel
		JPanel pinkLabelPanel = new JPanel(new GridLayout());
		pinkLabelPanel.setBackground(Color.GRAY);

		// set up the pink zone
		pinkTextArea.setBorder(emptyBorder);
		pinkTextArea.setBackground(Color.PINK);
		pinkTextArea.setEditable(false);
		// set text
		pinkTextArea.setText("Player 1 initialized with 2 pieces.\nPlayer 2 initialized with 2 pieces.");
		pinkTextArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane scrollPane = new JScrollPane(pinkTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(Color.pink);
		scrollPane.setBorder(emptyBorder);

		pinkLabelPanel.add(scrollPane);
		// empty spaces at the top and bottom of pink zone (separate from the check box,
		// and player status)
		pinkLabelPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		// check box -> pink zone
		checkBoxPlayerStatus.add(pinkLabelPanel);

		// the player panel is created as a grid of 2 columns, 2 rows. Storing from left
		// to right, top to bottom
		JPanel playerPanel = new JPanel(new GridLayout(2, 2));
		playerPanel.setBackground(new Color(238, 238, 238));
		playerPanel.setPreferredSize(new Dimension(450, 120));

		// player 1 status
		JLabel player1 = createLabel(BorderFactory.createEmptyBorder(5, 5, 5, 5), new Color(238, 238, 238));
		player1.setText("Player 1 Pieces:");

		// player 2 status
		JLabel player2 = createLabel(BorderFactory.createEmptyBorder(5, 5, 5, 5), new Color(238, 238, 238));
		player2.setText("Player 2 Pieces:");

		// player 1 number of pieces
		player1Pieces = createLabel(BorderFactory.createEmptyBorder(5, 5, 5, 5), new Color(238, 238, 238));
		player1Pieces.setIcon(blackPiece);
		player1Pieces.setText("2");
		player1Pieces.setHorizontalAlignment(SwingConstants.RIGHT);

		// player 2 number of pieces
		player2Pieces = createLabel(BorderFactory.createEmptyBorder(5, 5, 5, 5), new Color(238, 238, 238));
		player2Pieces.setIcon(whitePiece);
		player2Pieces.setText("2");
		player2Pieces.setHorizontalAlignment(SwingConstants.RIGHT);

		// add the players information
		playerPanel.add(player1);
		playerPanel.add(player1Pieces);
		playerPanel.add(player2);
		playerPanel.add(player2Pieces);
		// check box -> pink zone -> player status
		checkBoxPlayerStatus.add(playerPanel);

		// separate from the board and buttons on the left side
		checkBoxPlayerStatus.setBorder(BorderFactory.createEmptyBorder(5, 6, 0, 5));
		checkBoxPlayerStatus.setBackground(Color.GRAY);

		// add to the frame
		getContentPane().add(checkBoxPlayerStatus, BorderLayout.CENTER);
	}

	/**
	 * This method creates the text field and the "Submit" button at the bottom of
	 * the program.
	 */
	private void createTextField() {
		// store the text field and the "Submit" button in this panel
		JPanel textFieldAndSubmitBtt = new JPanel();
		textFieldAndSubmitBtt.setLayout(new GridBagLayout());
		textFieldAndSubmitBtt.setBackground(Color.GRAY);

		// set up the text field
		textField = new JTextField();
		// stretch the text field horizontally to the entire size of the panel
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// set focus on the text field upon opening the program
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				textField.requestFocus();
			}
		});

		// set up the "Submit" button using createButton method
		JButton submitBtt = (JButton) createButton("Submit", "Submit", Color.RED, Color.BLACK, control);
		// make sure the text "Submit" is displayed fully
		submitBtt.setMargin(new Insets(0, 0, 0, 0));

		// add the text field, along with its width
		textFieldAndSubmitBtt.add(textField, gbc);
		// add the "Submit" button
		textFieldAndSubmitBtt.add(submitBtt);
		// separate the text field and the button from the board, and player status
		textFieldAndSubmitBtt.setBorder(BorderFactory.createEmptyBorder(4, 5, 5, 5));

		// add to the frame
		getContentPane().add(textFieldAndSubmitBtt, BorderLayout.SOUTH);
	}

	/**
	 * This method creates the chess board, along with the buttons (with proper
	 * spacing) on the sides of the board.
	 */
	private void createBoardAndButtons() {
		// black or white
		Color labelColor;
		// letters for the buttons
		String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H" };

		// this panel stores the chess board and the letters panel vertically using
		// BoxLayout
		JPanel chessBoardAndLetters = new JPanel();
		chessBoardAndLetters.setLayout(new BoxLayout(chessBoardAndLetters, BoxLayout.Y_AXIS));
		chessBoardAndLetters.setBackground(Color.GRAY);

		// set up the letters panel as a grid of 1 row, 8 cols, with 1 pixel horizontal
		// gap
		JPanel lettersPanel = new JPanel(new GridLayout(1, 8, 1, 0));
		lettersPanel.setBackground(Color.GRAY);
		lettersPanel.setPreferredSize(new Dimension(480, 60));
		// 2 pixels gap between the letter buttons and the board
		lettersPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		// creating the letter buttons (A - H)
		for (int i = 0; i < letters.length; i++) {
			JButton letter = createButton(letters[i], letters[i], Color.BLACK, Color.LIGHT_GRAY, control);
			letter.setBorder(emptyBorder);
			letter.setPreferredSize(new Dimension(60, 60));
			// store all the letter buttons in this panel
			lettersPanel.add(letter);
			colButtons.add(letter);
		}

		// this panel stores the numbers panel and the "move" button vertically using
		// BoxLayout
		JPanel numbersAndMoveBtt = new JPanel();
		numbersAndMoveBtt.setLayout(new BoxLayout(numbersAndMoveBtt, BoxLayout.Y_AXIS));
		numbersAndMoveBtt.setPreferredSize(new Dimension(60, 480));
		numbersAndMoveBtt.setBackground(Color.GRAY);

		// set up the numbers panel as a grid of 8 rows, 1 col, with 1 pixel vertical
		// gap
		JPanel numbersPanel = new JPanel(new GridLayout(8, 1, 0, 1));
		numbersPanel.setBackground(Color.GRAY);
		numbersPanel.setPreferredSize(new Dimension(60, 480));
		// 2 pixels gap between the number buttons and the board
		numbersPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
		// creating the number buttons (1 - 8)
		for (int i = 0; i < 8; i++) {
			JButton number;
			number = createButton(String.valueOf(i + 1), String.valueOf(i + 1), Color.BLACK, Color.LIGHT_GRAY, control);
			number.setBorder(emptyBorder);
			number.setPreferredSize(new Dimension(60, 60));
			// store all the number buttons in this panel
			numbersPanel.add(number);
			rowButtons.add(number);
		}

		// create pane to hold borders
		JPanel chessBoardPanel = new JPanel(new GridLayout(8, 8, 0, 0));
		chessBoardPanel.setBackground(Color.GRAY);
		chessBoardPanel.setPreferredSize(new Dimension(480, 480));
		// add black and white labels to the panel
		for (int i = 0; i < boardLabels.length; ++i) {
			for (int j = 0; j < boardLabels[i].length; j++) {
				// some modulus to make the chess board (starting with a black square in this
				// case)
				if ((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0))
					labelColor = Color.BLACK;
				else
					labelColor = Color.WHITE;

				// creating the board's labels with empty border, and color of either black or
				// white
				boardLabels[i][j] = createBoardLabel(emptyBorder, labelColor);
				JLabel squareCell = boardLabels[i][j];

				squareCell.setPreferredSize(new Dimension(60, 60));
				// add the newly created label into the panel (chess board)
				chessBoardPanel.add(squareCell);
			}
		}

		// storing the chess board panel itself and the letter buttons panel
		// (vertically) in the
		// same panel of chessBoardAndLetters
		chessBoardAndLetters.add(chessBoardPanel);
		chessBoardAndLetters.add(lettersPanel);

		// store the "move" button in this panel
		JPanel moveBttPanel = new JPanel(new GridLayout());
		moveBttPanel.setBackground(Color.GRAY);
		moveBttPanel.setPreferredSize(new Dimension(60, 60));
		moveBttPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 0, 0));

		// set up the "move" button
		moveBtt = (JButton) createButton("move", "move", Color.BLACK, Color.WHITE, control);
		// set font for the text "move"
		moveBtt.setFont(new Font("Default", Font.PLAIN, 10));
		moveBtt.setBorder(emptyBorder);
		// make sure the text "move" is fully displayed
		moveBtt.setMargin(new Insets(0, 0, 0, 0));
		// add the "move" button into the panel
		moveBttPanel.add(moveBtt);

		// storing the number buttons panel and the "move" button panel (vertically) in
		// the same
		// panel of numbersAndMoveBtt
		numbersAndMoveBtt.add(numbersPanel);
		numbersAndMoveBtt.add(moveBttPanel);

		// storing the (chess board + letter buttons) with the (number buttons + "move"
		// button) (horizontally) in the same panel
		JPanel boardAndButtons = new JPanel();
		boardAndButtons.setLayout(new BoxLayout(boardAndButtons, BoxLayout.X_AXIS));
		// chessBoardAndLetters + numbersAndMoveBtt
		boardAndButtons.add(chessBoardAndLetters);
		boardAndButtons.add(numbersAndMoveBtt);
		boardAndButtons.setBackground(Color.GRAY);
		boardAndButtons.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

		// add to the frame
		getContentPane().add(boardAndButtons, BorderLayout.WEST);
	}

	/**
	 * This method instantiates the menu bar, setting up the options.
	 */
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file, game, reskin, debug, network, help;
		JMenuItem newGame, load, save, exit, about;
		JRadioButtonMenuItem radioButton;

		// File menu with its options
		file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		newGame = createJMenuItem("New Game", "new game", control);
		// load and save are turned off by default
		load = createJMenuItem("Load", "load", control);
		load.setEnabled(false);
		save = createJMenuItem("Save", "save", control);
		save.setEnabled(false);
		exit = createJMenuItem("Exit", "exit", control);

		file.add(newGame);
		file.add(load);
		file.add(save);
		file.add(exit);

		// Game menu with its submenus of reskin, and debug
		game = new JMenu("Game");
		game.setMnemonic(KeyEvent.VK_G);

		// reskin options
		reskin = new JMenu("Reskin Pieces");
		ButtonGroup reskinOptions = new ButtonGroup();
		radioButton = createRadioButtonMenuItem("Normal Pieces (black and white)", "normal pieces", control);
		radioButton.setSelected(true);
		reskinOptions.add(radioButton);
		reskin.add(radioButton);

		radioButton = createRadioButtonMenuItem("Cats vs. Dogs", "cats dogs", control);
		reskinOptions.add(radioButton);
		reskin.add(radioButton);

		radioButton = createRadioButtonMenuItem("Pumpkins vs. Bats", "pumpkins bats", control);
		reskinOptions.add(radioButton);
		reskin.add(radioButton);
		game.add(reskin);

		// debug options
		debug = new JMenu("Debug Scenarios");
		ButtonGroup debugOptions = new ButtonGroup();
		radioButton = createRadioButtonMenuItem("Normal Game", "normal game", control);
		radioButton.setSelected(true);
		debugOptions.add(radioButton);
		debug.add(radioButton);

		radioButton = createRadioButtonMenuItem("Corner Test", "corner test", control);
		debugOptions.add(radioButton);
		debug.add(radioButton);

		radioButton = createRadioButtonMenuItem("Side Tests", "side test", control);
		debugOptions.add(radioButton);
		debug.add(radioButton);

		radioButton = createRadioButtonMenuItem("1x Capture Test", "1x capture", control);
		debugOptions.add(radioButton);
		debug.add(radioButton);

		radioButton = createRadioButtonMenuItem("2x Capture Test", "2x capture", control);
		debugOptions.add(radioButton);
		debug.add(radioButton);

		radioButton = createRadioButtonMenuItem("Empty Board", "empty board", control);
		debugOptions.add(radioButton);
		debug.add(radioButton);

		radioButton = createRadioButtonMenuItem("Inner Square Test", "inner square", control);
		debugOptions.add(radioButton);
		debug.add(radioButton);
		game.add(debug);

		// Network menu
		network = new JMenu("Network");
		network.setMnemonic(KeyEvent.VK_N);
		newConnection = createJMenuItem("New Connection", "new connection", control);
		disconnect = createJMenuItem("Disconnect", "disconnect", control);
		disconnect.setEnabled(false);

		network.add(newConnection);
		network.add(disconnect);

		// Help menu
		help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		about = createJMenuItem("About", "about", control);

		help.add(about);

		menuBar.add(file);
		menuBar.add(game);
		menuBar.add(network);
		menuBar.add(help);

		// set the Menu Bar
		setJMenuBar(menuBar);
	}

	/**
	 * This method creates the labels for the chess board.
	 * 
	 * @param inBorder - Border type for the label.
	 * @param inColor  - Color background color for the label.
	 * @return - JLabel newly created label with desired background color, and
	 *         border style.
	 */
	private JLabel createBoardLabel(Border inBorder, Color inColor) {
		JLabel label = new JLabel();
		label.setBorder(inBorder);
		label.setOpaque(true);
		label.setBackground(inColor);
		// center the content
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}

	/**
	 * This method creates a custom label with border style, and background color.
	 * 
	 * @param inBorder - Border type for the label.
	 * @param inColor  - Color background color for the label.
	 * @return - JLabel newly created label with desired background color, and
	 *         border style.
	 */
	private JLabel createLabel(Border inBorder, Color inColor) {
		JLabel label = new JLabel();
		label.setBorder(inBorder);
		label.setOpaque(true);
		label.setBackground(inColor);
		return label;
	}

	/**
	 * This method creates a custom JMenuItem with text, action command, and action
	 * listener.
	 * 
	 * @param inText    - String name of the JMenuItem.
	 * @param inAC      - String value of the action command.
	 * @param inHandler - Controller handler of the events.
	 * @return - JMenuItem a newly created menu item.
	 */
	private JMenuItem createJMenuItem(String inText, String inAC, Controller inHandler) {
		JMenuItem newJMenu = new JMenuItem(inText);
		newJMenu.setActionCommand(inAC);
		newJMenu.addActionListener(inHandler);

		return newJMenu;
	}

	/**
	 * This method creates a custom button with text, action command, foreground
	 * color, background color, and an event manager.
	 * 
	 * @param inText    - String text of the button.
	 * @param inAC      - String action command of the button.
	 * @param inFGC     - Color foreground color of the button.
	 * @param inBGC     - Color background color of the button.
	 * @param inHandler - Controller event manager that handles pressing event of
	 *                  the button.
	 * @return - JButton custom made button.
	 */
	private JButton createButton(String inText, String inAC, Color inFGC, Color inBGC, Controller inHandler) {
		JButton newButton = new JButton(inText);
		newButton.setActionCommand(inAC);
		newButton.addActionListener(inHandler);
		newButton.setBackground(inBGC);
		newButton.setForeground(inFGC);

		return newButton;
	}

	/**
	 * This method creates a custom JRadioButtonMenuItem with text, action command,
	 * and event managers.
	 * 
	 * @param inText    - String text of the menu item.
	 * @param inAC      - String action command of the menu item.
	 * @param inHandler - Controller event manager of the menu item.
	 * @return - JRadioButtonMenuItem custom made menu item.
	 */
	private JRadioButtonMenuItem createRadioButtonMenuItem(String inText, String inAC, Controller inHandler) {
		JRadioButtonMenuItem newButton = new JRadioButtonMenuItem(inText);
		newButton.setActionCommand(inAC);
		newButton.addActionListener(inHandler);

		return newButton;
	}

	/**
	 * This method changes the appearance of the pressed buttons, in either row or
	 * column.
	 * 
	 * @param inButton   - JButton to be changed background color.
	 * @param inPosition - int position of that button in the ArrayList.
	 * @param isRow      - true if it is a row button, false if it is a column
	 *                   button.
	 */
	private void pressedButton(JButton inButton, int inPosition, boolean isRow) {
		inButton.setBackground(Color.WHITE);
		if (isRow) {
			for (int i = 0; i < rowButtons.size(); i++) {
				if (i != inPosition)
					rowButtons.get(i).setBackground(Color.LIGHT_GRAY);
			}
		} else {
			for (int i = 0; i < colButtons.size(); i++) {
				if (i != inPosition)
					colButtons.get(i).setBackground(Color.LIGHT_GRAY);
			}
		}
	}

	/**
	 * This method is called from within the constructor to set the frame
	 * properties.
	 */
	private final void setFrameProperties() {
		// get the current screen size
		Toolkit tkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = tkit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		// set frame size - width and height
		setSize(1020, 620);

		// frame can not be resizable
		setResizable(false);

		// thick borders of 5 pixels wide, in gray
//		getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY, 5, false));

		// Display the frame in the center of the screen
		setLocation(screenWidth / 4, screenHeight / 4);

		// frame's background color
		getContentPane().setBackground(Color.GRAY);

		// set frame icon (Elmo in this case :D)
		Image img = tkit.getImage(FRAME_ICON);
		setIconImage(img);
	}

	/**
	 * Inner class to handle action command of the button that was pressed, and
	 * print it to the console. All buttons must have a unique action command.
	 * 
	 * @author Song N Nguyen
	 * @version 1.0
	 * @see othello
	 * @since 1.8.0_261
	 */
	class Controller implements ActionListener {
		/** keep track of the row being chosen by the user */
		private int userRow;
		/** keep track of the column being chosen by the user */
		private int userCol;
		/** keep track of the current game mode that's being initialized */
		private int gameMode = 0;

		@Override
		public void actionPerformed(ActionEvent e) {

			Object eventSource = e.getActionCommand();

			// handling action command for each button press
			switch (eventSource.toString()) {
			case "1":
				userRow = 0;
				pressedButton((JButton) e.getSource(), userRow, true);
				break;

			case "2":
				userRow = 1;
				pressedButton((JButton) e.getSource(), userRow, true);
				break;

			case "3":
				userRow = 2;
				pressedButton((JButton) e.getSource(), userRow, true);
				break;

			case "4":
				userRow = 3;
				pressedButton((JButton) e.getSource(), userRow, true);
				break;

			case "5":
				userRow = 4;
				pressedButton((JButton) e.getSource(), userRow, true);
				break;

			case "6":
				userRow = 5;
				pressedButton((JButton) e.getSource(), userRow, true);
				break;

			case "7":
				userRow = 6;
				pressedButton((JButton) e.getSource(), userRow, true);
				break;

			case "8":
				userRow = 7;
				pressedButton((JButton) e.getSource(), userRow, true);
				break;

			case "A":
				userCol = 0;
				pressedButton((JButton) e.getSource(), userCol, false);
				break;

			case "B":
				userCol = 1;
				pressedButton((JButton) e.getSource(), userCol, false);
				break;

			case "C":
				userCol = 2;
				pressedButton((JButton) e.getSource(), userCol, false);
				break;

			case "D":
				userCol = 3;
				pressedButton((JButton) e.getSource(), userCol, false);
				break;

			case "E":
				userCol = 4;
				pressedButton((JButton) e.getSource(), userCol, false);
				break;

			case "F":
				userCol = 5;
				pressedButton((JButton) e.getSource(), userCol, false);
				break;

			case "G":
				userCol = 6;
				pressedButton((JButton) e.getSource(), userCol, false);
				break;

			case "H":
				userCol = 7;
				pressedButton((JButton) e.getSource(), userCol, false);
				break;

			// handling valid moves
			case "valid moves":
				JCheckBox cb = (JCheckBox) e.getSource();
				if (cb.isSelected()) {
					for (int row = 0; row < boardLabels.length; row++) {
						for (int col = 0; col < boardLabels[row].length; col++) {
							if (model.isValid(row, col, (playerTurn % 2) + 1)) {
								boardLabels[row][col].setIcon(validMove);
							}
						}
					}

				} else {
					for (int row = 0; row < boardLabels.length; row++) {
						for (int col = 0; col < boardLabels[row].length; col++) {
							if (model.isValid(row, col, (playerTurn % 2) + 1)) {
								boardLabels[row][col].setIcon(null);
							}
						}
					}

				}
				break;

			// handling the move command
			case "move":
				// switch turn if the current player makes the valid move
				if (model.canMove(playerTurn % 2 + 1)) {
					if (model.isValid(userRow, userCol, (playerTurn % 2) + 1)) {
						pinkTextArea.append("\nPlayer " + (playerTurn % 2 + 1) + " has captured "
								+ model.move(userRow, userCol, (playerTurn % 2) + 1) + " piece(s).");
						playerTurn++;
					}
				}

				// updating the board
				for (int row = 0; row < boardLabels.length; row++) {
					for (int col = 0; col < boardLabels[row].length; col++) {

						switch (model.getBoard(row, col)) {
						case 0:
							if (!model.isValid(row, col, (playerTurn % 2) + 1))
								boardLabels[row][col].setIcon(null);
							break;

						case 1:
							boardLabels[row][col].setIcon(blackPiece);
							break;

						case 2:
							boardLabels[row][col].setIcon(whitePiece);
							break;

						default:
							break;
						}
					}
				}

				// check if the check box is selected or not
				checkBoxSelected();

				// current players scores
				player1Pieces.setText(Integer.toString(model.getChips(1)));
				player2Pieces.setText(Integer.toString(model.getChips(2)));

				// if there is no move left
				if (!model.canMove(playerTurn % 2 + 1)) {
					pinkTextArea.append("\nPlayer " + (playerTurn % 2 + 1) + " has no valid move. Press skip.");
					moveBtt.setText("skip");
					moveBtt.setActionCommand("skip");
					playerTurn = 0;
				}

				// reset the buttons again after the user make the move
				for (int i = 0; i < rowButtons.size(); i++) {
					rowButtons.get(i).setBackground(Color.LIGHT_GRAY);
					colButtons.get(i).setBackground(Color.LIGHT_GRAY);
				}

				break;

			// skip if there is no move left for either player
			case "skip":
				moveBtt.setEnabled(false);
				pinkTextArea.append("\nEND OF GAME");
				pinkTextArea.append("\nPlayer 1 finishes with " + model.getChips(1) + " chip(s).");
				pinkTextArea.append("\nPlayer 2 finishes with " + model.getChips(2) + " chip(s).");

				if (model.getChips(1) > model.getChips(2))
					pinkTextArea.append("\nPlayer 1 wins!");
				else if (model.getChips(1) == model.getChips(2))
					pinkTextArea.append("\nTIE!!");
				else
					pinkTextArea.append("\nPlayer 2 wins!");

				pinkTextArea.append("\n\nPlay again? (Select 'New Game' from the File menu.)");
				break;

			// add text from the text field to the text area
			case "Submit":
				if (!thread.isAlive()) {
					if (!textField.getText().isEmpty()) {
						pinkTextArea.append("\n" + textField.getText());
						textField.setText("");
					}
				} else {
					try {
						OutputStream outStream = socket.getOutputStream();
						PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
						if (!textField.getText().startsWith("/"))
							pinkTextArea.append("\n" + networkDialog.getName() + ": " + textField.getText());
						else if (textField.getText().startsWith("/name")) {
							networkDialog.setName(textField.getText().substring(6, textField.getText().length()));
						}
						out.println(textField.getText());
						textField.setText("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				break;

			// reskin options
			case "normal pieces":
			case "cats dogs":
			case "pumpkins bats":
				if (e.getActionCommand().equals("normal pieces")) {
					blackPiece = new ImageIcon(OthelloViewController.class.getResource("black_s.png"));
					whitePiece = new ImageIcon(OthelloViewController.class.getResource("white_s.png"));
				} else if (e.getActionCommand().equals("cats dogs")) {
					blackPiece = new ImageIcon(OthelloViewController.class.getResource("cat.png"));
					whitePiece = new ImageIcon(OthelloViewController.class.getResource("dog.png"));
				} else {
					blackPiece = new ImageIcon(OthelloViewController.class.getResource("bat.png"));
					whitePiece = new ImageIcon(OthelloViewController.class.getResource("pumpkin.png"));
				}

				// update the current pieces to the new skins immediately
				for (int row = 0; row < boardLabels.length; row++) {
					for (int col = 0; col < boardLabels[row].length; col++) {

						switch (model.getBoard(row, col)) {
						case 0:
							boardLabels[row][col].setIcon(null);
							break;

						case 1:
							boardLabels[row][col].setIcon(blackPiece);
							break;

						case 2:
							boardLabels[row][col].setIcon(whitePiece);
							break;

						default:
							break;
						}
					}
				}

				// update the players icons
				player1Pieces.setIcon(blackPiece);
				player2Pieces.setIcon(whitePiece);
				break;

			// game modes
			case "normal game":
				gameMode = 0;
				break;

			case "corner test":
				gameMode = 1;
				break;

			case "side test":
				gameMode = 2;
				break;

			case "1x capture":
				gameMode = 3;
				break;

			case "2x capture":
				gameMode = 4;
				break;

			case "empty board":
				gameMode = 5;
				break;

			case "inner square":
				gameMode = 6;
				break;

			// when New Game option is called
			case "new game":
				initialize(gameMode);
				break;

			// Exit the game
			case "exit":
				dispose();
				break;

			// new connection
			case "new connection":
				Point thisLocation = getLocation();
				Dimension parentSize = getSize();
				Dimension dialogSize = networkDialog.getSize();

				int offsetX = (parentSize.width - dialogSize.width) / 2 + thisLocation.x;
				int offsetY = (parentSize.height - dialogSize.height) / 2 + thisLocation.y;

				networkDialog.setLocation(offsetX, offsetY);
				networkDialog.setVisible(true);
				if (networkDialog.pressedConnect()) {
					newConnection.setEnabled(false);
					disconnect.setEnabled(true);
					pinkTextArea.append("\nNegotiating Connection to " + networkDialog.getAddress() + " on port "
							+ networkDialog.getPort());

					try {
						socket = new Socket();
						socket.connect(new InetSocketAddress(InetAddress.getByName(networkDialog.getAddress()),
								networkDialog.getPort()), networkDialog.getPort());
						Runnable r = new OthelloNetworkController();
						thread = new Thread(r);
						thread.start();
					} catch (IOException ex) {
						pinkTextArea.append(
								"\nError: Connection refused. Server is not available. Check port or restart server.");
						newConnection.setEnabled(true);
						disconnect.setEnabled(false);
					}
				}

				break;

			// disconnecting
			case "disconnect":
				pinkTextArea.append("\nDisconnecting from network connection.");
				try {
					OutputStream outStream = socket.getOutputStream();
					PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
					out.println(networkDialog.getName() + " has disconnected.");
					socket.close();
					newConnection.setEnabled(true);
					disconnect.setEnabled(false);
				} catch (IOException ex) {
				}
				break;

			// showing the message dialog
			case "about":
				JOptionPane.showMessageDialog(null, "Othello Game\nby Song Nguyen\nNovember 2020", "About",
						JOptionPane.INFORMATION_MESSAGE);
				break;

			default:
				break;
			}
		}
	}

	/**
	 * Handling the network aspect of Othello game by using thread
	 * 
	 * @author Song N Nguyen
	 * @version 1.0
	 * @see othello
	 * @since 1.8.0_261
	 */
	class OthelloNetworkController implements Runnable {
		String name;

		public void rename(String newName) {
			name = newName;
		}

		public String getUserName() {
			return name;
		}

		public void run() {
			try {
				InputStream inStream = socket.getInputStream();
				OutputStream outStream = socket.getOutputStream();
				PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
				try {
					@SuppressWarnings("resource")
					Scanner in = new Scanner(inStream);
					name = networkDialog.getName();
					out.println(name);

					while (in.hasNextLine()) {
						String line = in.nextLine();
						pinkTextArea.append("\n" + line);
					}
				} finally {
					socket.close();
					newConnection.setEnabled(true);
					disconnect.setEnabled(false);
				}
			} catch (IOException e) {
				pinkTextArea
						.append("\nError: Connection refused. Server is not available. Check port or restart server.");
				newConnection.setEnabled(true);
				disconnect.setEnabled(false);
			}
		}
	}
}
