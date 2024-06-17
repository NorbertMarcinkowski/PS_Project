package org.example.server.kkw;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class KkwManager {
    private static KkwManager instance;

    private final List<MessageToSend> messageToSendList = new ArrayList<>();
    private final ObservableList<MessageToSend> messages = FXCollections.observableList(messageToSendList);

    public static KkwManager getInstance() {
        if (instance == null) {
            instance = new KkwManager();
        }
        return instance;
    }

    public KkwManager() {
        messages.addListener((ListChangeListener<MessageToSend>) c -> {
            MessageToSend messageToSend = messages.getFirst();

            messageToSend.users()
                    .forEach(user -> sendData(user.userSocket(), messageToSend.message()));

            messages.removeFirst();
        });
    }

    private void sendData(Socket socket, String message) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            System.out.println("can't send data");
        }
    }

    public void addMessage(MessageToSend message) {
        messages.add(message);
    }

}
