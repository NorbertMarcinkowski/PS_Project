package org.example.server.read.config.file;

import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

@Getter
public class ConfigFile {

    private String serverID;

    public void loadConfigJsonFile(String fileName) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("config.json")) {
            Object obj = parser.parse(reader);

            JSONObject jsonObject = (JSONObject) obj;

            getServerId(jsonObject);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void getServerId(JSONObject jsonObject) {
        serverID = (String) jsonObject.get("ServerID");
    }
}
