package org.example.server.topic;

import lombok.Getter;
import org.example.server.user.thread.User;

import java.net.Socket;
import java.util.List;

@Getter
public class Topic {
    private final Socket ownerSocket;

    private final String ownerId;

    private String name;

    private List<User> users;

    public Topic(Socket ownerSocket, String ownerId, String name, List<User> users) {
        this.ownerSocket = ownerSocket;
        this.ownerId = ownerId;
        this.name = name;
        this.users = users;
    }

    public void addClient(User user) {
        users.add(user);
    }

    public void removeClient(User user) {
        users.remove(user);
    }
}
