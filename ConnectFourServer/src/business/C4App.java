/**
 * 
 */
package business;

import java.io.IOException;

/**
 * @author Christopher, Elliot, Nader
 *
 */
public class C4App {
//TODO rename class to C4ServerApp
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		C4Server serv = new C4Server(50000);
		serv.startServer();

	}

}
