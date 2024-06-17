package org.example.server.message;

import java.net.Socket;

public record Message(String json, Socket userSocket) {
}
