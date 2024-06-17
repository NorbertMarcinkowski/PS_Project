package org.example.user;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

public class User {

    private String clientId;

    private Socket socket;

    public static void main(String[] args) {
        User user = new User();
        String serverAddress = "127.0.0.1";
        int serverPort = 5555;
        user.start(serverAddress, serverPort, "debil student");
    }

    public void start(String serverAddress, int serverPort, String clientId) {
        this.clientId = clientId;
        try {
            socket = new Socket(serverAddress, serverPort);

            new Thread(
                    () -> {
                        while (true) {
                            try {
                                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                                String msg = inputStream.readUTF();
                                System.out.println("return message:" + msg);
                            } catch (IOException e) {
                                break;
                            }
                        }
                    }
            ).start();
            System.out.println("Connected to the server");
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            int random = new Random().nextInt(100);
            String message = "{" +
                    "\"type\": \"register\"," +
                    "\"id\": \"" + clientId + "\"," +
                    "\"topic\": \"lunch call" + random + "\","+
                    "\"mode\": \"producer\"," +
                    "\"timestamp\": \"2024-02-15T00:00:00.000Z\"," +
                    "}";
            send(output, message);
            Thread.sleep(6000);

            message = "{" +
                    "\"type\": \"register\"," +
                    "\"id\": \"" + clientId + "\"," +
                    "\"topic\": \"lunch call" + random + "\","+
                    "\"mode\": \"subscriber\"," +
                    "\"timestamp\": \"2024-02-15T00:00:00.000Z\"," +
                    "}";
            send(output, message);
            Thread.sleep(6000);
        } catch (IOException |
                 InterruptedException e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void send(DataOutputStream output, String message) throws InterruptedException {

        try {
            output.writeUTF(message);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getStatus() {

        return null;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }
}
