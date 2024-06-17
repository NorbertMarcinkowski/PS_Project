package org.example.server.communication;

import com.sun.source.tree.WhileLoopTree;
import org.example.server.user.thread.UserThread;

import java.io.IOException;
import java.net.*;

public class CommunicationThread implements Runnable {

    private ServerSocket serverSocket;

    public CommunicationThread(int port) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), port);
        this.serverSocket = new ServerSocket();
        serverSocket.bind(inetSocketAddress);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket userSocket = serverSocket.accept();

                UserThread userThread = new UserThread(userSocket);
                Thread thread = new Thread(userThread);
                thread.start();
            } catch (IOException e) {
                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                } catch (IOException e1) {
                    System.out.println("Communication thread closed");
                }
                break;
            }
        }
    }
}
