package org.example.server.manage.thread;

import org.example.server.kkw.KkwManager;
import org.example.server.kkw.MessageToSend;
import org.example.server.message.AnswerMessageManager;
import org.example.server.message.Message;
import org.example.server.statement.InvalidStatementException;
import org.example.server.statement.Mode;
import org.example.server.statement.Statement;
import org.example.server.statement.StatementFactory;
import org.example.server.topic.Topic;
import org.example.server.topic.TopicManager;
import org.example.server.user.thread.User;
import org.example.server.user.thread.UserThread;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ManageThread implements Runnable {

    private final AnswerMessageManager answerMessageManager = AnswerMessageManager.getInstance();

    @Override
    public void run() {
        while (true) {
            try {
                if (answerMessageManager.isEmpty()) {
                    Thread.sleep(1);
                } else {
                    try {
                        Message message = answerMessageManager.getFirst();
                        Statement statement = StatementFactory.create(message);
                        manageAction(statement, message);
                    } catch (InvalidStatementException e) {
                        System.out.println("cant parse message");
                    } finally {
                        answerMessageManager.removeFirstMessage();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("something went wrong");
                break;
            }
        }
    }

    private void manageAction(Statement statement, Message message) {
        switch (statement.getType()) {
            case MESSAGE -> addMessageToKkw(statement, message);
            case STATUS -> addStatusToKkw(statement, message);
            case REGISTER -> registerTopic(statement, message);
            case WITHDRAW -> withdraw(statement, message);
        }
    }

    private void withdraw(Statement statement, Message message) {
        TopicManager topicManager = TopicManager.getInstance();
        topicManager.withdraw(statement.getTopic(), new User(statement.getId(), message.userSocket()));
    }

    private void registerTopic(Statement statement, Message message) {
        TopicManager topicManager = TopicManager.getInstance();
        if (statement.getMode() == Mode.SUBSCRIBER) {
            topicManager.addSubscriber(new User(statement.getId(), message.userSocket()), statement.getTopic());
        } else {
            List<User> usersList = new ArrayList<>();
            topicManager.addTopic(new Topic(message.userSocket(), statement.getId(), statement.getTopic(), usersList));
        }
    }


    private void addMessageToKkw(Statement statement, Message message) {
        try {
            TopicManager topicManager = TopicManager.getInstance();
            List<User> users = topicManager.getUsersFromTopic(statement.getTopic());
            MessageToSend messageToSend = new MessageToSend(users, message.json());
            KkwManager kkwManager = KkwManager.getInstance();
            kkwManager.addMessage(messageToSend);
        } catch (IOException e) {
            System.out.println("cant add message to kkw" + e.getMessage());
        }
    }

    private void addStatusToKkw(Statement statement, Message message) {
        List<User> users = List.of(new User(statement.getId(), message.userSocket()));
        TopicManager topicManager = TopicManager.getInstance();

        StringBuilder payload = new StringBuilder();
        topicManager.getTopics().forEach(topic -> {
            payload.append(topic.getName()).append(": ").append(topic.getOwnerId());
        });
        statement.setPayload(payload.toString());
        MessageToSend messageToSend = new MessageToSend(users, statement.toString());

        KkwManager kkwManager = KkwManager.getInstance();
        kkwManager.addMessage(messageToSend);
    }
}
