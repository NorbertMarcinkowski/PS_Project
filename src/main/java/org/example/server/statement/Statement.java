package org.example.server.statement;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Statement {

    private Type type;

    private String id;

    private String topic;

    private Mode mode;

    private String timestamp;

    private String payload;

    @Override
    public String toString() {
        return "{" +
                "type: \"" + type + "\", " +
                "id: \"" + id + "\", " +
                "topic: \"" + topic + "\", " +
                "mode: \"" + mode + "\", " +
                "timestamp: \"" + timestamp + "\", " +
                "payload: \"" + payload + "\"" +
                '}';
    }
}
