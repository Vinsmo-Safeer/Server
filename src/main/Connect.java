package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connect {

    static String currentClientIP = null;

    public static String connect() {
        String connectedIP = null;

        int port = 11111; // Choose a port number

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {

                    System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());
                    connectedIP = clientSocket.getInetAddress().getHostAddress();
                    currentClientIP = connectedIP;


                    break;

                } catch (IOException e) {
                    System.err.println("Error while communicating with client: " + e.getMessage());
                    connect();
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
            connect();
        }

        return connectedIP;
    }

    public static void checkConnection() {
        // On another thread
        new Thread(() -> {
            while (true) {

//                System.out.println("Checking connection is still active in 1 minute");

                // Wait for 5 seconds
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String serverAddress = currentClientIP; // Replace with your server's IP address
                int port = 22222; // Replace with your server's port number

                try (Socket socket = new Socket(serverAddress, port)) {
//                    System.out.println("Connected to server at " + serverAddress + ":" + port);
                    Info.clientIP = serverAddress;

                } catch (IOException e) {
                    System.err.println("Could not connect to server at " + serverAddress + ":" + port);
                    currentClientIP = null;
                    connect();
                    break;
                }
            }
        }).start();
    }
}
