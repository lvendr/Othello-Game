/* 
 * File name : OthelloNetworkModalViewController.java
 * Author	 : Song Nguyen Nguyen, 040940830
 * Course	 : CST8221_310 – JAP, Lab Section: 313
 * Assignment: 2-part 2
 * Date		 : December 13, 2020
 * Professor : Karan Kalsi
 * Purpose	 : Handling the networking side of Othello game.
* Class list: Othello.java, OthelloSplashScreen.java, OthelloViewController.java, OthelloModel.java, OthelloNetworkModalViewController.java, OthelloServer.java
 */

package othello;

import java.awt.Color;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;

/**
 * This class is responsible for handling the networking aspect of Othello game,
 * there is not much functionality right now, but it is able to read the IP
 * address and port number from user.
 * 
 * @author Song N Nguyen
 * @version 1.0
 * @see othello
 * @since 1.8.0_261
 */
public class OthelloNetworkModalViewController extends JDialog {

	/** Swing components are serializable and require serialVersionUID. */
	private static final long serialVersionUID = 1L;
	/** Whether the user pressed the Connect button. */
	private Boolean hasConnected = false;
	/**
	 * A reference to the private inner Controller class for use by the two buttons.
	 */
	private Controller handler = new Controller();

	/** The CombobBox for port number. */
	private JComboBox<String> portInput;

	/** The text field where the user will enter the hostname to connect to. */
	private JTextField addressInput;

	/** The text field where the user will enter the player name */
	private JTextField nameInput;

	/** Label to display the errors */
	private JLabel status;

	/**
	 * Initial constructor.
	 * 
	 * @param mainView - JFrame of the main Othello game.
	 */
	public OthelloNetworkModalViewController(JFrame mainView) {
		super(mainView, "Enter Network Address", true);

		setUndecorated(true);

		// store the UI here
		Container networkPanel = getContentPane();

		JPanel networkLayout = new JPanel(new BorderLayout());
		// store the labels, text field, and combo box
		JPanel networkComponents = new JPanel(new GridLayout(4, 1, 0, 0));
		// store the connect & cancel buttons
		JPanel networkButtons = new JPanel(new BorderLayout());

		// creating the label for address, and storing the text field
		JLabel address, port, name;
		address = new JLabel("Address:");
		address.setDisplayedMnemonicIndex(0);
		address.setDisplayedMnemonic(KeyEvent.VK_A);
		JPanel addressPanel = new JPanel(new BorderLayout());
		addressPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		addressPanel.add(address, BorderLayout.WEST);
		addressInput = new JTextField(20);
		address.setLabelFor(addressInput);
		JPanel addressTextFieldPanel = new JPanel();
		addressTextFieldPanel.add(addressInput);
		addressTextFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		addressPanel.add(addressTextFieldPanel, BorderLayout.EAST);

		networkComponents.add(addressPanel);

		// creating the label for port, and storing the combo box
		port = new JLabel("Port:");
		port.setDisplayedMnemonicIndex(0);
		port.setDisplayedMnemonic(KeyEvent.VK_P);
		JPanel portPanel = new JPanel(new BorderLayout());
		portPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		portPanel.add(port, BorderLayout.WEST);
		portInput = new JComboBox<String>();
		portInput.addItem("");
		portInput.addItem("31000");
		portInput.addItem("41000");
		portInput.addItem("51000");
		port.setLabelFor(portInput);
		portInput.setEditable(true);
		JPanel portPane = new JPanel();
		portPane.add(portInput);
		portPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 65));
		portPanel.add(portPane, BorderLayout.CENTER);
		networkComponents.add(portPanel);

		// creating the label for address, and storing the text field
		name = new JLabel("Name:");
		name.setDisplayedMnemonicIndex(0);
		name.setDisplayedMnemonic(KeyEvent.VK_N);
		JPanel namePanel = new JPanel(new BorderLayout());
		namePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
		namePanel.add(name, BorderLayout.WEST);
		nameInput = new JTextField(20);
		name.setLabelFor(nameInput);
		JPanel nameTextFieldPanel = new JPanel();
		nameTextFieldPanel.add(nameInput);
		nameTextFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		namePanel.add(nameTextFieldPanel, BorderLayout.EAST);

		networkComponents.add(namePanel);

		// storing the label status
		status = new JLabel();
		JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
		statusPanel.add(status, BorderLayout.WEST);
		networkComponents.add(statusPanel);

		// creating the connect button
		JButton connect, cancel;
		connect = new JButton("Connect");
		connect.setActionCommand("C");
		connect.addActionListener(handler);

		// creating the cancel button
		cancel = new JButton("Cancel");
		cancel.setActionCommand("X");
		cancel.addActionListener(handler);
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(connect);
		buttonsPanel.add(cancel);
		// storing connect & cancel buttons to the right side
		networkButtons.add(buttonsPanel, BorderLayout.EAST);

		// adding the components
		networkLayout.add(networkComponents, BorderLayout.CENTER);
		networkLayout.add(networkButtons, BorderLayout.SOUTH);
		// setting up the gray border of 5 pixels
		networkLayout.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5, false));
		// add everything to the content pane
		networkPanel.add(networkLayout);
		super.setResizable(false);
		pack();
	}

	/**
	 * This method returns the value the user has entered.
	 * 
	 * @return - String of the actual value, unless there was an error or the user
	 *         pressed the cancel button.
	 */

	public String getAddress() {
		if (hasConnected) {
			return (addressInput.getText());
		} else {
			// You can return whatever error message you like. This isn't cast in stone.
			return ("Error:  Invalid Address or attempt cancelled.");
		}
	}

	/**
	 * This method returns the port the user has selected OR ENTERED in the Combo
	 * Box.
	 * 
	 * @return - int value of the port selected. Returns -1 on invalid port or
	 *         cancel pressed.
	 */

	public int getPort() {
		int portnum;
		if (hasConnected) {
			// Ensure the user has entered digits.
			// You should probably also ensure the port numbers are in the correct range.
			// I did not. That's from 0 to 65535, by the way. Treat it like invalid input.
			try {
				portnum = Integer.parseInt((String) portInput.getSelectedItem());
			} catch (NumberFormatException nfe) {
				// I've been using a negative portnum as an error state in my main code.
				portnum = -1;
			}

			return portnum;
		}
		return -1;
	}

	public String getName() {
		return nameInput.getText();
	}
	
	public void setName(String newName) {
		nameInput.setText(newName);
	}
	
	/**
	 * Responsible for final cleanup and hiding the modal. Does not do much at the
	 * moment.
	 */
	public void hideModal() {
		setVisible(false);
	}

	/**
	 * Returns whether or not the user had pressed connect.
	 * 
	 * @return - True if the user pressed Connect, false if the user backed out with
	 *         cancel.
	 */
	public boolean pressedConnect() {
		return hasConnected;
	}

	/**
	 * The Controller for managing user input in the network dialogue.
	 * 
	 * @author Daniel Cormier
	 * @version 1.3
	 * @since 1.8.0_261
	 * @see OthelloViewController
	 */

	private class Controller implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			String s = evt.getActionCommand();

			// I set the action command on my connect button to "C".
			if ("C".equals(s)) {

				status.setText("");
				
				if (nameInput.getText().length() < 3)
					status.setText("Error: Name must be at least three characters long.");


				try {
					int portnum = Integer.parseInt((String) portInput.getSelectedItem());
					if (portnum < 0 || portnum > 65535)
						status.setText("Error: Valid port ranges are from 0 to 65535.");
				} catch (NumberFormatException nfe) {
					status.setText("Error: Valid port ranges are from 0 to 65535.");
				}

				if (addressInput.getText().isEmpty())
					status.setText("Error: Address must not be blank.");

				if (status.getText().isEmpty()) {
					hasConnected = true;
					hideModal();
				} else
					hasConnected = false;
			} else // My "Cancel" button has an action command of "X" and gets called here.
			{
				hasConnected = false;
				hideModal();
			}
		}

	}
}
