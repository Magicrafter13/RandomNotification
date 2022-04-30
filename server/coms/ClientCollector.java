package coms;

import java.net.ServerSocket;
import java.net.Socket;

public class ClientCollector extends Thread {
	private ServerSocket connectionManager; // Manages all client connections

	/**
	 * Close communications.
	 */
	public void close() {
		try {
			connectionManager.close();
		}
		catch (Exception e) {
			System.out.println("Unable to close listener...");
			System.out.println(e);
		}
	}

	/**
	 * Fires when a connection is established with a client.
	 * <br>
	 * Should be overridden by something more useful.
	 * @param sock Socket between the server and the client.
	 */
	public void connected(Socket sock) {
		System.out.println("Client connected.");
	};

	/**
	 * Begin the thread.
	 */
	public void run() {
		System.out.println("Listening for connections on port " + connectionManager.getLocalPort() + "...");
		// Wait for connections.
		while (true) {
			try {
				Socket sock = connectionManager.accept();
				System.out.println("Connection established with " + sock.getInetAddress());
				connected(sock);
			}
			catch (Exception e) {
				if (!connectionManager.isBound()) {
					System.out.println("Could not accept a connection, will no longer wait for new clients.");
					System.out.println(e);
				}
				break;
			}
		}
		close();
	}

	/**
	 * Open communications and wait for connections from clients.
	 */
	public ClientCollector(Integer port) {
		// Open Listener
		try {
			connectionManager = new ServerSocket(port);
		}
		catch (Exception e) {
			System.out.println("Unable to bind to port " + port + "!");
			System.out.println(e);
		}
	}
}
