import java.net.Socket;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import coms.Client;
import coms.ClientCollector;

// TODO: If a client is connected for more than say, one day, send a "heartbeat" message to them. If they don't respond,
//  assume they have lost connection or something.
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
	}

	public Server() {
		listener = new ClientCollector(port) {
			public void connected(Socket sock) {
				Client client = new Client(sock) {
					private String name = "";
					public void disconnected() {
						close();
						clients.remove(this);
						if (isValidated())
							System.out.println("There are now " + clients.size() + " validated clients.");
					}
					public void receive(String message) {
						System.out.println("Received:\n" + message);
						String[] words = message.split(" ");
						switch (words.length == 0 ? "EMPTY" : words[0]) {
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
							case "name":
								name = message.substring(words[0].length() + 1);
								break;
							case "notification":
								if (isValidated()) {
									clients.get(random.nextInt(clients.size())).send(name == "" ? "notification" : "notification " + name);
									break;
								}
							default:
								send("Unknown command, terminating connection.");
								disconnected();
						}
					}
				};
				client.start();
			}
		};
		listener.start();
	}
}
