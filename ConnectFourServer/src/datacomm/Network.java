/**
 * 
 */
package datacomm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Network class that sends and receives messages.
 * 
 * @author Christopher Dufort
 * @author Elliot Wu
 * @author Nader Baydoun
 */
public class Network 
{
	private static final int BUFSIZE = 3;
	
	/**
	 * Method that sends byte message across the connection.
	 * 
	 * @param socket Socket at which the method is being sent
	 * @param message Byte array to be sent
	 * @throws IOException Can be thrown by the network
	 */
	public static void sendMessage(Socket socket, byte[] message) throws IOException
	{
		OutputStream out = socket.getOutputStream();
		out.write(message);
	}
	
	/**
	 * Method that receives message across the connection.
	 * 
	 * @param socket Socket at which the message is being received
	 * @return byte[] Byte array to be received
	 * @throws IOException Can be thrown by the network
	 */
	public static byte[] receiveMessage(Socket socket) throws IOException
	{
		InputStream in = socket.getInputStream();
		// Receive the same string back from the server
		// Length of Packet = 3 bytes
		byte[] byteBuffer = new byte[BUFSIZE];
		
		//Total bytes received so far
	    int totalBytesRcvd = 0;		
	    
	    //Bytes received in last read
	    int bytesRcvd;								
	    
	    while (totalBytesRcvd < byteBuffer.length)
	    {
	      if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,
	                        byteBuffer.length - totalBytesRcvd)) == -1)
	        throw new SocketException("Connection close prematurely");
	      totalBytesRcvd += bytesRcvd;
	    }
	    
		return byteBuffer;
	}
}
