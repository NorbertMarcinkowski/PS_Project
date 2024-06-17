package org.example.server.statement;

import org.example.server.message.Message;
import org.json.JSONException;
import org.json.JSONObject;

public class StatementFactory {
    public static Statement create(Message message) {
        String json = message.json();

        Statement statement = new Statement();
        try {
            JSONObject jsonObject = new JSONObject(json);
            type(statement, jsonObject);
            senderId(statement, jsonObject);
            topic(statement, jsonObject);
            timestamp(statement, jsonObject);
            mode(statement, jsonObject);
        } catch (JSONException | IllegalArgumentException e) {
            throw new InvalidStatementException("invalid json");
        }

        return statement;
    }

    private static void mode(Statement statement, JSONObject jsonObject) {
        String mode = jsonObject.getString("mode");
        statement.setMode(Mode.valueOf(mode.toUpperCase()));
    }

    private static void timestamp(Statement statement, JSONObject jsonObject) {
        String timestamp = jsonObject.getString("timestamp");
        statement.setTimestamp(timestamp);
    }

    private static void topic(Statement statement, JSONObject jsonObject) {
        String topic = jsonObject.getString("topic");
        statement.setTopic(topic);
    }

    private static void senderId(Statement statement, JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        statement.setId(id);
    }

    public static void type(Statement statement, JSONObject jsonObject) {
        String type = jsonObject.getString("type");
        statement.setType(Type.valueOf(type.toUpperCase()));
    }
}
