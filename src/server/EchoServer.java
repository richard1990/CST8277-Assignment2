/**
 * server is the package for class placement.
 */
package server;
// import statements
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketException;
/**
 * This class functions as a server that allows clients to connect to it.
 * Clients must know the port number (8081 by default) and server name
 * to connect to server. See EchoClient.java for the client-side code.
 * @author		Todd Kelley, Richard Barney
 * @version		1.0.0 February 26, 2016
 */
public class EchoServer {
	/** ServerSocket object. */
	private ServerSocket server;
	/** Integer to hold the message number. */
	private int messagenum;
	/** Integer to hold the user-supplied port number. */
	private int portNum;
	/** Final integer for default port (8081). */
	public static final int DEFAULT_PORT = 8081;

	/**
	 * Constructor.
	 * @param	portNum		Integer holding the port number.
	 */
	public EchoServer(int portNum) {
		this.portNum = portNum;
	} // end constructor
	
	/**
	 * Void method that sets up the server connection, allowing
	 * clients to connect to it. Makes use of multi-threading
	 * to allow multiple connections.
	 */
	public void runServer() {
		System.out.println("EchoServer started, clients can now connect\nTo quit, enter EOF (^Z on Windows; ^D on Linux/Mac)");
		try {
			// create new ServerSocket object with the specified port #
			// as a parameter
			server = new ServerSocket(portNum);
			while (true) { // infinite loop to keep server running
				// setup Socket object with a connection, create a new Thread
				// passing a Runnable interface as a parameter and then call
				// method handleClientConnection to handle the client's connection
				// and execute the thread
				final Socket connection = server.accept();
				new Thread(new Runnable() {
					@Override
					public void run() {
						handleClientConnection(connection);
					}
				}).start();	
			}
		} catch(IOException ex) {
			System.err.println("Error encountered! Port is likely already in use! Exiting program...");
			ex.printStackTrace();
		}
	} // end method runServer
	
	/**
	 * Void method that handles a connection to the server.
	 * @param	connection	Socket object.
	 */
	public void handleClientConnection(Socket connection){
		// create resources
		ObjectOutputStream output;
		ObjectInputStream input;
		String sMessage;
		try {
			// get the streams from the Socket object
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
			do { // keep looping
				try {
					// put the message entered by client into a String
					// and display the message
					sMessage = (String) input.readObject();
					System.out.println(messagenum +" Output> " +sMessage);
				} catch (EOFException | SocketException e) {
					sMessage = null;
				}
				// if the message is not null, let the client know the message
				// was received by displaying the message again on the client's
				// side and increment the message #
				if (sMessage != null) {
					output.writeObject(messagenum +" FromServer> " +sMessage);
					output.flush();
					++messagenum;
				}
			} while (sMessage != null);
			// close resources
			input.close();
			output.close();
			connection.close();
		} catch (IOException | ClassNotFoundException ex) {
			System.err.println("Error encountered! Exiting program...");
			ex.printStackTrace();			
		}
	} // end method handleClientConnection
	
	/**
	 * Entry point "main()" as required by the JVM. Take command line arguments 
	 * to determine port.
	 * @param	args	Standard command line parameters (arguments) as a String array.
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			(new EchoServer(Integer.parseInt(args[0]))).runServer();
		} else {
			(new EchoServer(DEFAULT_PORT)).runServer();
		}
	} // end method main
} // end class EchoServer