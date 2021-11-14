/* 
 * File name : OthelloSplashScreen.java
 * Author	 : Song Nguyen Nguyen, 040940830
 * Course	 : CST8221_310 – JAP, Lab Section: 313
 * Assignment: 2-part 2
 * Date		 : December 13, 2020
 * Professor : Karan Kalsi
 * Purpose	 : Splash screen of the Othello game, this class creates the splash screen.
* Class list: Othello.java, OthelloSplashScreen.java, OthelloViewController.java, OthelloModel.java, OthelloNetworkModalViewController.java, OthelloServer.java
 */

package othello;

import java.awt.*;
import javax.swing.*;

/**
 * This class is responsible for creating the splash screen, with custom image,
 * text, and duration of it.
 * 
 * @author Song N Nguyen
 * @version 1.0
 * @see othello
 * @since 1.8.0_261
 */
public class OthelloSplashScreen extends JWindow {

	/** Swing components are serializable and require serialVersionUID. */
	private static final long serialVersionUID = 6248477390124803341L;
	/** Splash screen duration. */
	private final int duration;
	
	/**
	 * Constructor with parameter of duration.
	 * 
	 * @param inDuration - int duration of the splash screen (in milliseconds).
	 */
	public OthelloSplashScreen(int inDuration) {
		this.duration = inDuration;
	}

	/**
	 * This method sets up the size, image of the splash screen, and shows it.
	 */
	public void showSplashWindow() {
		// custom color for content pane

		// create content pane
		JPanel content = new JPanel(new BorderLayout());
		
		// Set the window's bounds, position the window in the center of the screen
		int width = 800 + 10;
		int height = 600 + 10;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;

		// set the location and the size of the window
		setBounds(x, y, width, height);

		// image of the splash screen
		JLabel label = new JLabel(new ImageIcon(getClass().getResource("OthelloSplash.gif")));

		
		content.add(label, BorderLayout.CENTER);
		
		
		// create custom RGB color
		Color customColor = new Color(142, 140, 216);
		content.setBorder(BorderFactory.createLineBorder(customColor, 10));

		// replace the window content pane with the content JPanel
		setContentPane(content);

		// make the splash window visible
		setVisible(true);

		// Snooze for awhile, pretending the code is loading something awesome while
		// our splashscreen is entertaining the user.
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			/* log an error here? *//* e.printStackTrace(); */}
		// destroy the window and release all resources
		dispose();
	}
}
