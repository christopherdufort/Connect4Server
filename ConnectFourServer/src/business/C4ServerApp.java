package business;

import java.io.IOException;

/**
 * C4ServerApp is the starting point for the Connect 4 server application. This
 * class contains the main method which is the starting point for the program.
 * 
 * @author Christopher Dufort
 * @author Elliot Wu
 * @author Nader Baydoun
 * 
 * @version Java 1.8
 */
public class C4ServerApp {
	// Port is currently hard coded for singular thread.
	private static int PORTNUMBER = 50000;

	/**
	 * @param args
	 *            Command line arguments that may be accepted when running.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Create Instance of Server.
		C4Server serv = new C4Server(PORTNUMBER);
		// Start Server
		serv.startServer();
	}

}
