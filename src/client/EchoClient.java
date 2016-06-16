/**
 * client is the package for class placement.
 */
package client;
// import statements
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * This class allows a client to connect to a server if they know the server
 * name and port number. See EchoServer.java for the server-side code.
 * @author		Todd Kelley, Richard Barney
 * @version		1.0.0 February 26, 2016
 */
public class EchoClient {
	/** String to hold the user-supplied server name. */
	private String serverName;
	/** String for default server name (localhost). */
	public static final String DEFAULT_SERVER_NAME = "localhost";
	/** Integer to hold the port number. */
	private int portNum;

	/**
	 * Constructor.
	 * @param	serverName	String containing the server name.
	 * @param	portNum		Integer containing the port number.
	 */
	public EchoClient(String serverName, int portNum) {
		this.serverName = serverName;
		this.portNum = portNum;
	} // end constructor

	/**
	 * Void method that handles all the code to allow a client to connect
	 * to a server.
	 */
	public void runClient() {
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String sMessage;
		Socket connection;
		ObjectOutputStream output;
		ObjectInputStream input;
		try {
			// create new Socket with the server name and port number and
			// create new ObjectOutputStream and ObjectInputStream objects
			// passing the Socket object as a parameter
			connection = new Socket(InetAddress.getByName(serverName), portNum);
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
			// display message to user
			System.out.println("To Quit, enter EOF (^Z on Windows; ^D on Linux/Mac)");
			do { // keep asking for input
				System.out.print("Input> ");
				sMessage = keyboard.readLine();
				// if the message entered by the user is not null, write the 
				// message (String) to the ObjectOutputStream, flush the
				// stream, then read the message to the ObjectInputStream
				// and display the message
				if (sMessage != null) {
					output.writeObject(sMessage);
					output.flush();
					sMessage = (String) input.readObject();
					System.out.println(sMessage);
				}
			} while (sMessage != null);
			// close resources
			input.close();
			output.close();
			connection.close();
			keyboard.close();
		} catch (IOException | ClassNotFoundException ex) {
			System.err.println("Error encountered! Exiting program...");
			ex.printStackTrace();
		}
	} // end method runClient
	
	/**
	 * Entry point "main()" as required by the JVM. Takes command line arguments
	 * to determine server name and port #.
	 * @param  args   Standard command line parameters (arguments) as a String array.
	 */
	public static void main(String[] args) {
		switch (args.length) {
			case 2:
				(new EchoClient(args[0], Integer.parseInt(args[1]))).runClient();
				break;
			case 1:
				(new EchoClient(DEFAULT_SERVER_NAME, Integer.parseInt(args[0]))).runClient();
				break;
			default:
				(new EchoClient(DEFAULT_SERVER_NAME, server.EchoServer.DEFAULT_PORT)).runClient();
		}
	} // end method main
} // end class EchoClient