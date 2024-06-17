package org.example.server.topic;

import org.example.server.user.thread.User;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public record TopicManager(List<Topic> topics) {
    private static TopicManager instance;

    public static synchronized TopicManager getInstance() {
        if (instance == null) {
            instance = new TopicManager(new ArrayList<>());
        }
        return instance;
    }

    public synchronized List<Topic> getTopics() {
        return topics;
    }

    public synchronized boolean isTopicExists(String topicName) {
        return topics.stream()
                .anyMatch(topic -> topic.getName().equals(topicName));
    }

    public synchronized List<User> getUsersFromTopic(String topicName) throws IOException {
        Topic topic = getTopic(topicName);
        return topic.getUsers();
    }

    public synchronized Topic getTopic(String topicName) throws IOException {
        return topics.stream()
                .filter(topic -> topic.getName().equals(topicName))
                .findFirst()
                .orElseThrow(() -> new IOException("not exist"));
    }

    public synchronized void removeTopic(Topic topic) throws IOException {
        topic.getUsers()
                .forEach(user -> {
                    AtomicBoolean flag = new AtomicBoolean(false);
                    topics.forEach(t -> {
                        if (!t.getName().equals(topic.getName())) {
                            if (t.getUsers().stream()
                                    .anyMatch(u -> u.equals(user))) {
                                flag.set(true);
                            }
                            if (t.getOwnerId().equals(user.username())) {
                                flag.set(true);
                            }
                        }
                    });
                    if (!flag.get()) {
                        try {
                            if (user.userSocket() != null) {
                                user.userSocket().close();
                            }
                        } catch (IOException e) {
                        }
                    }
                });
        topics.remove(topic);
    }

    public synchronized void addTopic(Topic topic) {
        if (!isTopicExists(topic.getName())) {
            topics.add(topic);
        }
    }

    public synchronized void addSubscriber(User user, String topicName) {
        try {
            Topic topic = getTopic(topicName);
            if (!topic.getUsers().contains(user)) {
                topic.addClient(user);
            }
        } catch (IOException e) {

        }
    }

    public synchronized void withdraw(String topicName, User user) {
        try {
            Topic topic = getTopic(topicName);
            if (topic.getOwnerSocket().equals(user.userSocket())) {
                removeTopic(topic);
            } else {
                topic.removeClient(user);
            }
        } catch (IOException e) {

        }
    }

    public void disconnectClient(Socket userSocket) {
        List<Topic> topicsToRemove = new ArrayList<>();
        Iterator<Topic> topicIterator = topics.iterator();

        while (topicIterator.hasNext()) {
            Topic topic = topicIterator.next();
            Iterator<User> userIterator = topic.getUsers().iterator();
            while (userIterator.hasNext()) {
                User user = userIterator.next();
                if (user.userSocket().equals(userSocket)) {
                    userIterator.remove();
                }
            }
            if (topic.getOwnerSocket().equals(userSocket)) {
                topicsToRemove.add(topic);
            }
        }

        topicsToRemove.forEach(topic -> {
            try {
                removeTopic(topic);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
