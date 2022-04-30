package com.cs455.randomnotification;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Simple thread to connect to a TCP socket and send/receive messages by line.
 * @author Matthew Rease
 */
public class Server extends Thread {
    private final String remoteAddr; // Server address
    private final int port;          // Server port

    private Socket sock;                 // Initial socket for connection to server
    private PrintWriter serverOut;       // Connection to send commands to server
    private final LinkedBlockingQueue<String> outbox =
            new LinkedBlockingQueue<>(); // Queue for outbound messages

    public void close() {
        try {
            sock.close();
        }
        catch (Exception e) {
            System.out.println("Unable to close server socket...");
            e.printStackTrace();
        }
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
        //System.out.println("Adding to message queue.");
        outbox.add(message);
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

        try {
            serverOut = new PrintWriter(sock.getOutputStream());
        }
        catch (Exception e) {
            System.out.println("Could not create PrintWriter for server!");
            e.printStackTrace();
            close();
            return;
        }
        new Thread(() -> {
            while (true) {
                try {
                    serverOut.println(outbox.take());
                    serverOut.flush();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                if (!Server.this.isAlive())
                    break;
            }
        }).start();

        Scanner serverIn; // Connection to receive commands from server
        try {
            serverIn = new Scanner(sock.getInputStream());
        }
        catch (Exception e) {
            System.out.println("Could not create Scanner for server!");
            e.printStackTrace();
            serverOut.close();
            close();
            return;
        }

        connected();
        while (serverIn.hasNextLine()) {
            String message = serverIn.nextLine();
            receive(message);
        }
        serverIn.close();
        serverOut.close();
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
