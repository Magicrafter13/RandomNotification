package com.cs455.randomnotification;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class ServerData extends ViewModel {
    private static class LiveServerThread extends MutableLiveData<Server> {
        @NonNull
        public Server getValue() {
            return Objects.requireNonNull(super.getValue());
        }
        public LiveServerThread(Server server) {
            super(server);
        }
    }

    private final LiveServerThread connection;
    private final MutableLiveData<Integer> id = new MutableLiveData<>(1);
    private final MutableLiveData<String> name = new MutableLiveData<>();

    private NotificationListener listener = null;

    private Server newServer() {
        Server server = new Server("matthewrease.net", 4555) {
            public void connected() {
                listener.onNotify("Connected to server!", "Probably not important to tell you, but it's always good to keep the user informed.");
                send("handshake");
                // If we have a custom name, send it to the server
                String str = name.getValue();
                if (str != null)
                    send("name " + str);
            }
            public void disconnected() {
                listener.onNotify("Lost connection to the server.", "Just press the notify button in the app, and it will try to reconnect.");
                // TODO: notify user (with a toast if the app is focused?)
            }
            public void receive(String message) {
                //System.out.println("Server sent: " + message);
                String[] words = message.split(" ");
                switch (words.length == 0 ? "BAD MESSAGE" : words[0]) {
                    // TODO: something with this? I know it's not required but it feels like a good idea for *something*
                    case "handshake":
                        break;
                    // User has been selected for a random notification :)
                    case "notification":
                        listener.onNotify((message.length() > words[0].length() ? message.substring(words[0].length() + 1) : "Someone") + " sent you a notification!", "hahahaha you just got randomly notified");
                        break;
                    // Server sent us something we weren't expecting... so let's send a notification anyway!
                    default:
                        listener.onNotify("Unknown message from the server.", "This doesn't mean anything for you, but we'll send you a notification anyway.");
                }
            }
        };
        server.start();
        return server;
    }

    public int nextId() {
        Integer current = id.getValue();
        if (current == null)
            current = 1;
        id.postValue(current + 1);
        return current;
    }

    public void setName(String name) {
        this.name.setValue(name);
        connection.getValue().send("name " + name);
    }

    public void sendRequest() {
        Server server = connection.getValue();
        if (!server.isAlive()) {
            server = newServer();
            connection.setValue(server);
            server.send("handshake"); // Make sure a handshake is in the outbox before notification is...
        }
        server.send("notification");
    }

    /**
     * Register a callback to be invoked when the server sends a notification.
     * @param listener The callback that will run. This value may be <code>null</code>.
     */
    public void setNotificationListener(NotificationListener listener) {
        this.listener = listener;
    }

    public ServerData() {
        connection = new LiveServerThread(newServer());
    }
}
