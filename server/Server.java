import java.net.Socket;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import coms.Client;
import coms.ClientCollector;

public class Server {
	private static final int port = 4555;

	private final ClientCollector listener;
	private final CopyOnWriteArrayList<Client> clients = new CopyOnWriteArrayList<Client>();
	private final Random random = new Random();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Server();
		//while (true);
	}

	public Server() {
		listener = new ClientCollector(port) {
			public void connected(Socket sock) {
				Client client = new Client(sock) {
					public void disconnected() {
						close();
						clients.remove(this);
						if (isValidated())
							System.out.println("There are now " + clients.size() + " validated clients.");
					}
					public void receive(String message) {
						System.out.println("Received:\n" + message);
						switch (message) {
							// Ignore blank messages just in case
							case "":
								break;
							case "handshake":
								send("handshake");
								if (!isValidated()) {
									validate();
									clients.add(this);
									System.out.println("There are now " + clients.size() + " validated clients.");
								}
								break;
							case "notification":
								if (isValidated()) {
									clients.get(random.nextInt(clients.size())).send("notification");
									break;
								}
							default:
								send("Unknown command, terminating connection.");
								disconnected();
						}
					}
				};
				//client.disconnect = "";
				client.start();
			}
		};
		listener.start();
	}
}
