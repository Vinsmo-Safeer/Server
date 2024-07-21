package main;

import command.CommandHandler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

    public static void send(String message, String ip, int port, boolean output) {
        try (Socket socket = new Socket(ip, port)) {
            if (output)
                System.out.println("Sending " + message + " to " + ip + ":" + port);
            OutputStream outputStream = socket.getOutputStream();

            // Send length of the message first
            byte[] messageBytes = message.getBytes();
            outputStream.write(intToBytes(messageBytes.length));

            // Send the actual message
            outputStream.write(messageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String receive(int port) {

        String message = null;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                    // Read signal from client
                    InputStream input = clientSocket.getInputStream();
                    byte[] data = new byte[1024];
                    int bytesRead = input.read(data);
                    String signal = new String(data, 0, bytesRead);

                    message = signal;

                    break;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
    }

    public static void receiveCommand(int port) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                    // Read signal from client
                    InputStream input = clientSocket.getInputStream();
                    byte[] data = new byte[1024];
                    int bytesRead = input.read(data);
                    String signal = new String(data, 0, bytesRead);

                    CommandHandler.handleCommand(signal);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void receiveFile(String saveFilePath, int port) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for connections...");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected.");

            InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(saveFilePath);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            System.out.println("File received and saved as " + saveFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static byte[] intToBytes(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
        };
    }
}
