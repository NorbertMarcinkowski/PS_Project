package org.example.server.statement;

public class InvalidStatementException extends RuntimeException {
    public InvalidStatementException(String message) {
        super(message);
    }
}
