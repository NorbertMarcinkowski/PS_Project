package org.example.server.message;

import java.util.ArrayList;
import java.util.List;

public class AnswerMessageManager {
    private static AnswerMessageManager instance;

    private List<Message> messages;

    private AnswerMessageManager() {
        messages = new ArrayList<>();
    }

    public static synchronized AnswerMessageManager getInstance() {
        if (instance == null) {
            instance = new AnswerMessageManager();
        }
        return instance;
    }

    public synchronized void addMessage(Message message) {
        messages.add(message);
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public synchronized void removeFirstMessage() {
        messages.removeFirst();
    }

    public synchronized Message getFirst() {
        return messages.isEmpty() ? null : messages.getFirst();
    }
}
