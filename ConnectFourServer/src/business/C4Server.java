/**
 * 
 */
package business;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * C4Server class is the server side class which is responsible for retrieving
 * the local IP address and creating the server socket as well as displaying the
 * servers IP address to the console. C4Server will create sessions as necessary
 * 
 * @author Christopher Dufort
 * @author Elliot Wu
 * @author Nader Baydoun
 * 
 * @version Java 1.8
 */
public class C4Server {

	private C4ServerSession servSess;
	private final int portNumber;
	private final String serverIp;

	/**
	 * C4Server constructor accepting a port number used to reprieve the IP
	 * address of the local host machine.
	 * 
	 * @param portNumber
	 *            port number on which this server should listen for connection
	 *            requests
	 * @throws UnknownHostException
	 *             in the case of no IP being found.
	 */
	public C4Server(int portNumber) throws UnknownHostException {
		this.portNumber = portNumber;

		InetAddress addr = InetAddress.getLocalHost();
		this.serverIp = addr.getHostAddress();
	}

	/**
	 * startSever is called to create a server socket and display the IP Address
	 * of the servers host machine. Contain an infinite loop that runs forever,
	 * accepting and servicing connections.
	 * 
	 * @throws IOException
	 */
	public void startServer() throws IOException {

		@SuppressWarnings("resource")
		ServerSocket servSock = new ServerSocket(portNumber);

		// Display servers IP to console.
		System.out.println(serverIp);

		// Infinite loop creating and starting new sessions servicing client
		// connections
		while (true) {
			Socket clntSock = servSock.accept();

			servSess = new C4ServerSession(clntSock);
			servSess.startSession();

			clntSock.close();
		}
	}
}