package org.nolat.best.tasks;

import org.apache.log4j.Logger;
import org.nolat.best.tasks.screenshot.ScreenshotTask;

public class TaskManager {

    private static final Logger log = Logger.getLogger(TaskManager.class);

    public static void execute(String command) {
        switch (command) {
        case "exit":
            new ExitTask();
            break;
        case "screenshot":
            new ScreenshotTask();
            break;
        default:
            log.warn("Command " + command + " was not found.");
            break;
        }
    }
}
