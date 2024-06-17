package org.example.server.user.thread;

import org.example.server.message.AnswerMessageManager;
import org.example.server.message.Message;
import org.example.server.topic.TopicManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class UserThread implements Runnable {

    private final Socket userSocket;

    private final AnswerMessageManager answerMessageManager = AnswerMessageManager.getInstance();

    public UserThread(Socket userSocket) {
        this.userSocket = userSocket;
    }

    @Override
    public void run() {
        try (DataInputStream reader = new DataInputStream(userSocket.getInputStream())) {
            while (true) {
                String json = reader.readUTF();
                Message message = new Message(json, userSocket);
                answerMessageManager.addMessage(message);
            }
        } catch (IOException e) {
            TopicManager topicManager = TopicManager.getInstance();
            topicManager.disconnectClient(userSocket);
        }
    }

}
