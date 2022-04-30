package com.cs455.randomnotification;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

public class ServerData extends ViewModel {
    private static class LiveServerThread extends LiveData<Server> {
        @NonNull
        public Server getValue() {
            return Objects.requireNonNull(super.getValue());
        }
        public LiveServerThread(Server server) {
            super(server);
            server.start();
        }
    }

    private final LiveServerThread connection;
    private final MutableLiveData<Integer> id;

    private NotificationListener listener = null;

    public int nextId() {
        Integer current = id.getValue();
        if (current == null)
            current = 1;
        id.postValue(current + 1);
        return current;
    }

    public void sendRequest() {
        //System.out.println("Sending request to server...");
        Server server = connection.getValue();
        if (!server.isAlive())
            server.start();
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
        connection = new LiveServerThread(new Server("matthewrease.net", 4555) {
            public void connected() {
                send("handshake");
                //System.out.println("Connected to server.");
            }
            public void disconnected() {
                // TODO: notify user (with a toast?)
                close();
                //System.out.println("Disconnected from server!");
            }
            public void receive(String message) {
                //System.out.println("Server sent: " + message);
                switch (message) {
                    // TODO: something with this? I know it's not required but it feels like a good idea for *something*
                    case "handshake":
                        break;
                    // User has been selected for a random notification :)
                    case "notification":
                        listener.onNotify();
                        break;
                    // Server sent us something we weren't expecting... so let's send a notification anyway!
                    default:
                        listener.onNotify();
                        // TODO: Customize this message (will require refactor to serverConnect#notify
                }
            }
        });
        id = new MutableLiveData<>(1);
        System.out.println("HEY DINGUS: " + connection.getValue());
    }
}
