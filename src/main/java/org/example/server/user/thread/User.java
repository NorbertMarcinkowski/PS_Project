package org.example.server.user.thread;

import java.net.Socket;

public record User(String username, Socket userSocket) {
}
