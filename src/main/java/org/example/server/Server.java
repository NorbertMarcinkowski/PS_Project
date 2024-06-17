package org.example.server;

import org.example.server.communication.CommunicationThread;
import org.example.server.kkw.KkwManager;
import org.example.server.manage.thread.ManageThread;
import org.example.server.read.config.file.ConfigFile;
import org.example.server.topic.Topic;
import org.example.server.topic.TopicManager;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.run();
        } catch (IOException e) {
            System.out.println("can't start server");
        }
    }

    public void run() throws IOException {
        ConfigFile configFile = new ConfigFile();
        configFile.loadConfigJsonFile("config.json");

        ManageThread manageThread = new ManageThread();
        Thread thread = new Thread(manageThread);
        thread.start();

        CommunicationThread communicationThread = new CommunicationThread(5555);
        Thread thread2 = new Thread(communicationThread);
        thread2.start();

        KkwManager kkwManager = KkwManager.getInstance();

        new Thread(
                () -> {
                    while (true) {
                        Scanner scanner = new Scanner(System.in);
                        String command = scanner.nextLine();
                        if (command.equals("exit")) {
                            thread.interrupt();
                        }
                        if (command.equals("status")) {
                            TopicManager topicManager = TopicManager.getInstance();
                            List<Topic> topicList = topicManager.getTopics();
                            if (topicList.isEmpty()) {
                                System.out.println("topic list is empty");
                                continue;
                            }
                            topicList.forEach(topic -> {
                                System.out.println("Producer: " + topic.getOwnerId());
                                System.out.println("Topic: " + topic.getName());
                                topic.getUsers()
                                        .forEach(user -> {
                                            System.out.println(" - " + user.username());
                                        });
                            });
                        }
                    }
                }
        ).start();
    }

}
