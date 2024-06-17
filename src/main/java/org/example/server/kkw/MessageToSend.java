package org.example.server.kkw;

import org.example.server.user.thread.User;

import java.util.List;

public record MessageToSend(List<User> users, String message) {
}
