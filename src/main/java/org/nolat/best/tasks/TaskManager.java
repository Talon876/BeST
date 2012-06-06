package org.nolat.best.tasks;


import java.util.Map;

import mjson.Json;

import org.apache.log4j.Logger;
import org.nolat.best.tasks.screenshot.ScreenshotTask;

public class TaskManager {

    private static final Logger log = Logger.getLogger(TaskManager.class);

    public static void execute(String command) {
        switch (command) {
        case "exit":
            new ExitTask();
            break;
        case "tray":
            testJsonParse();
            break;
        case "screenshot":
            new ScreenshotTask();
            break;
        default:
            log.warn("Command " + command + " was not found.");
            break;
        }
    }

    private static void testJsonParse() {
        String jsonAsString = "{\r\n" + "  \"upload\": {\r\n" + "    \"image\": {\r\n" + "      \"name\": null,\r\n"
                + "      \"title\": null,\r\n" + "      \"caption\": null,\r\n" + "      \"hash\": \"OroQd\",\r\n"
                + "      \"deletehash\": \"Ky17IS02MI3ri6B\",\r\n" + "      \"datetime\": \"2012-06-06 04:24:55\",\r\n"
                + "      \"type\": \"image\\/png\",\r\n" + "      \"animated\": \"false\",\r\n"
                + "      \"width\": 425,\r\n" + "      \"height\": 35,\r\n" + "      \"size\": 1328,\r\n"
                + "      \"views\": 0,\r\n" + "      \"bandwidth\": 0\r\n" + "    },\r\n" + "    \"links\": {\r\n"
                + "      \"original\": \"http:\\/\\/i.imgur.com\\/OroQd.png\",\r\n"
                + "      \"imgur_page\": \"http:\\/\\/imgur.com\\/OroQd\",\r\n"
                + "      \"delete_page\": \"http:\\/\\/imgur.com\\/delete\\/Ky17IS02MI3ri6B\",\r\n"
                + "      \"small_square\": \"http:\\/\\/i.imgur.com\\/OroQds.jpg\",\r\n"
                + "      \"large_thumbnail\": \"http:\\/\\/i.imgur.com\\/OroQdl.jpg\"\r\n" + "    }\r\n" + "  }\r\n"
                + "}";
        System.out.println("---------------------------------------------------------");
        Json json = Json.read(jsonAsString);
        System.out.println("json: " + json);
        Map<String, Json> map = json.asJsonMap();

        for (Map.Entry<String, Json> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + " is mapped to " + value.toString() + "\n");
        }

    }
}
