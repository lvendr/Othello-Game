/* 
 * File name : OthelloServer.java
 * Author	 : Song Nguyen Nguyen, 040940830
 * Course	 : CST8221_310 – JAP, Lab Section: 313
 * Assignment: 2-part 2
 * Date		 : December 13, 2020
 * Professor : Karan Kalsi
 * Purpose	 : Server of the Othello game.
 * Class list: Othello.java, OthelloSplashScreen.java, OthelloViewController.java, OthelloModel.java, OthelloNetworkModalViewController.java, OthelloServer.java
 */

package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

/**
 * Main server class of the Othello game
 * 
 * @author Song N Nguyen
 * @version 1.0
 * @see othello
 * @since 1.8.0_261
 */
public class OthelloServer {
	/** Storing the sockets */
	static public Vector<Socket> sockets;
	/** Storing the threads */
	static public Vector<OthelloServerThread> threads;

	/**
	 * This method broadcasts the same message to all sockets
	 * 
	 * @param message - String message
	 * @throws IOException - Error if socket does not exist
	 */
	static public void broadcast(String message) throws IOException {
		for (Socket socket : sockets) {
			OutputStream outStream = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
			out.println(message);
		}
	}

	/**
	 * Main method of Othello server
	 * 
	 * @param args - port value
	 */
	public static void main(String[] args) {
		sockets = new Vector<Socket>();
		threads = new Vector<OthelloServerThread>();
		int port;
		try {
			if (args.length != 0)
				port = Integer.valueOf(args[0]);
			else {
				System.out.println("Using default port: 62000");
				port = 62000;
			}
		} catch (Exception e) {
			System.out.println("ERROR: Invalid port number: " + args[0]);
			System.out.println("Using default port: 62000");
			port = 62000;
		}

		try {
			int i = 1;
			@SuppressWarnings("resource")
			ServerSocket s = new ServerSocket(port);

			while (true) {
				Socket incoming = s.accept();
				sockets.add(incoming);
				System.out.println("Inbound connection #" + i);
				Runnable r = new OthelloServerThread(incoming);
				threads.add((OthelloServerThread) r);
				Thread t = new Thread(r);
				t.start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/**
 * This class handles the client input for one server socket connection.
 * 
 * @author Song N Nguyen
 * @version 1.0
 * @see othello
 * @since 1.8.0_261
 */
class OthelloServerThread implements Runnable {
	/** Player name */
	String name;

	/**
	 * Constructs a handler.
	 * 
	 * @param i the incoming socket
	 * @param c the counter for the handlers (used in prompts)
	 */
	public OthelloServerThread(Socket i) {
		incoming = i;
	}

	public void run() {
		try {
			try {
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				@SuppressWarnings("resource")
				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);

				out.println("Connection successful.");
				out.println("Welcome to Song's Othello Server.");
				out.println("Use '/help' for commands.");
				name = in.nextLine();
				System.out.println(name + " has connected.");

				// echo client input
				while (in.hasNextLine()) {
					String line = in.nextLine();
					System.out.println(line);

					if (line.startsWith("/")) {
						switch (line) {
						case "/help":
							out.println("HELP");
							out.println("/help: this message.");
							out.println("/bye: disconnect.");
							out.println("/who: shows the names of all connected players.");
							out.println("/name (name): rename yourself.");
							break;

						case "/bye":
							out.println("SERVER: Disconnecting.");
							out.println("Disconnected from server.");
							incoming.close();
							break;

						case "/who":
							for (OthelloServerThread thread : OthelloServer.threads) {
								out.println(thread.name);
							}
							break;
						default:
							if(line.startsWith("/name")) {
								String newName = line.substring(6, line.length());
								System.out.println(name + " changed name into " + newName + ".");
								name = newName;
							}
							break;
						}
					} else if (!line.equals(name + " has disconnected.")) {
						for (Socket socket : OthelloServer.sockets) {
							if (socket != incoming) {
								OutputStream oS = socket.getOutputStream();
								PrintWriter output = new PrintWriter(oS, true /* autoFlush */);
								output.println(name + ": " + line);
							}
						}
					}

				}
			} finally {
				incoming.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Current socket */
	private Socket incoming;
}