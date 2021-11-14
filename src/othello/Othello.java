/* 
 * File name : Othello.java
 * Author	 : Song Nguyen Nguyen, 040940830
 * Course	 : CST8221_310 – JAP, Lab Section: 313
 * Assignment: 2-part 2
 * Date		 : December 13, 2020
 * Professor : Karan Kalsi
 * Purpose	 : Main method of the Othello game, this class is a launch point for the splash screen, and the frame.
 * * Class list: Othello.java, OthelloSplashScreen.java, OthelloViewController.java, OthelloModel.java, OthelloNetworkModalViewController.java, OthelloServer.java
 */

package othello;

import javax.swing.*;

/**
 * This class is a launch point, responsible for creating and lauching splash
 * screen, and instantiate OthelloViewController to complete the frame.
 * 
 * @author Song N Nguyen
 * @version 1.0
 * @see othello
 * @since 1.8.0_261
 */
public class Othello {

	/**
	 * The main method.
	 * 
	 * @param args the time duration of the splash screen in milliseconds. If not
	 *             specified, the default duration is 4000 msec (4 sec).
	 */
	public static void main(String[] args) {
		int duration = 1;
		if (args.length == 1) {
			try {
				duration = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
			}
		}

		// Create the screen
		OthelloSplashScreen splashWindow = new OthelloSplashScreen(duration);
		// Show the Splash screen
		splashWindow.showSplashWindow();
		
			
		// Create and display the main application GUI
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				OthelloViewController frame = new OthelloViewController();

				// set up the Close button (X) of the frame
				// no need to use explicitly a WindowListener. The line below will generate one
				// for you.
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				frame.pack();

				// make the GUI visible
				frame.setVisible(true);
			}
		});
	}
}
