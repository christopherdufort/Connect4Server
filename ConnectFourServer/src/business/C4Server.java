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
 * @author Christopher, Elliot, Nader
 *
 */
public class C4Server {
	private C4ServerSession servSess;
	private final int portNumber;
	private final String serverIp;

	public C4Server(int portNumber) throws UnknownHostException {
		this.portNumber = portNumber;

		InetAddress addr = InetAddress.getLocalHost();
		this.serverIp = addr.getHostAddress();
	}

	public void startServer() throws IOException {

		@SuppressWarnings("resource")
		ServerSocket servSock = new ServerSocket(portNumber);

		System.out.println(serverIp);

		while (true) {

			Socket clntSock = servSock.accept();

			servSess = new C4ServerSession(clntSock);
			servSess.startSession();

			clntSock.close();
		}
	}

}
