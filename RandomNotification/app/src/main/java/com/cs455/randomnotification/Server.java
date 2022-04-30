package com.cs455.randomnotification;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Simple thread to connect to a TCP socket and send/receive messages by line.
 * @author Matthew Rease
 */
public class Server extends Thread {
    private final String remoteAddr; // Server address
    private final int port;          // Server port

    private PrintWriter serverOut; // Connection to send commands to server
    private Socket sock;           // Initial socket for connection to server

    public void close() {
        try {
            sock.close();
        }
        catch (Exception e) {
            System.out.println("Unable to close server socket...");
            e.printStackTrace();
        }
        serverOut.close();
    }

    /**
     * Fires when a connection is established with a server.
     * <br>
     * Should be overridden by something more useful.
     */
    public void connected() {
        System.out.println("Server connected.");
    }

    /**
     * Fires when a connection is lost or terminated with a server.
     * <br>
     * Should be overridden by something more useful.
     */
    public void disconnected() {
        System.out.println("Server disconnected.");
    }

    /**
     * Receive a message from the client.
     * <br>
     * Should be overridden by something more useful.
     * @param message The message to receive
     */
    public void receive(String message) {
        System.out.println("Server said: " + message);
    }

    /**
     * Sends a command to the server.
     * @param message The command
     */
    public void send(String message) {
        new Thread(() -> {
            serverOut.println(message);
            serverOut.flush();
        }).start();
    }

    /**
     * Begin the thread.
     */
    public void run() {
        //System.out.println("Thread running.");
        try {
            sock = new Socket(remoteAddr, port);
        }
        catch (Exception e) {
            System.out.println("Unable to connect to " + remoteAddr + " on port " + port);
            e.printStackTrace();
            return;
        }

        Scanner serverIn; // Connection to receive commands from server
        try {
            serverIn = new Scanner(sock.getInputStream());
            serverOut = new PrintWriter(sock.getOutputStream());
        }
        catch (Exception e) {
            System.out.println("Could not create Scanner, or PrintWriter for server!");
            e.printStackTrace();
            close();
            return;
        }

        connected();
        while (serverIn.hasNextLine()) {
            String message = serverIn.nextLine();
            receive(message);
        }
        serverIn.close();
        close();
        disconnected();
    }

    /**
     * Connect to a game server.
     * @param remoteAddr The server's address
     * @param port the server's listener socket
     */
    public Server(String remoteAddr, int port) {
        this.remoteAddr = remoteAddr;
        this.port = port;
    }
}
