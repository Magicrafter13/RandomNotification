package coms;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
	private Scanner clientIn;      // Connection to receive commands from the client
	private PrintWriter clientOut; // Connection to send commands to the client
	private Socket sock;           // Initial socket for connection to the client
	private Boolean validated;     // Whether or not this client has been validated

	public String disconnect; // Command to receive when disconnecting

	public Integer clientID; // Unique ID for this player

	/**
	 * Close communications.
	 */
	public void close() {
		try {
			sock.close();
		}
		catch (Exception e) {
			System.out.println("Unable to close client socket...");
			System.out.println(e);
		}
		clientOut.close();
	}

	public void disconnected() { }

	public Socket getSock() {
		return sock;
	}

	public Boolean isValidated() {
		return validated;
	}

	/**
	 * Receive a message from the client.
	 * <br>
	 * Should be overridden by something more useful.
	 * @param message The message to receive
	 */
	public void receive(String message) { };

	/**
	 * Send a message to the client.
	 * @param message The message to send
	 */
	public String send(String message) {
		//System.out.println(message);
		clientOut.println(message);
		clientOut.flush();
		return message;
	}

	public Boolean validate() {
		if (validated)
			return false;
		validated = true;
		return true;
	}

	/**
	 * Begin the thread.
	 */
	public void run() {
		while (clientIn.hasNextLine()) {
			String message = clientIn.nextLine();
			receive(message);
			// do stuff
		}
		//System.out.println("Client has disconnected!");
		close();
		disconnected();
		//receive(disconnect);
	}

	/**
	 * Begin a connection with a game client.
	 * @param sock Socket between the server and client
	 */
	public Client(Socket sock) {
		this.sock = sock;
		disconnect = "";
		validated = false;

		try {
			clientIn = new Scanner(sock.getInputStream());
			clientOut = new PrintWriter(sock.getOutputStream());
		}
		catch (Exception e) {
			System.out.println("Could not create Scanner, or PrintWriter for client!");
			System.out.println(e);
			close();
		}
	}
}
