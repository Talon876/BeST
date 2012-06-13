package org.nolat.best.preferences;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import mjson.Json;

import org.apache.log4j.Logger;

public class Prefs {

    private static final Logger log = Logger.getLogger(Prefs.class);
    private static final File PREF_FILE = new File(System.getProperty("user.home") + "/.BeST/prefs.json");
    public static Json prefs;

    public static void load() {
        if (PREF_FILE.exists()) {
            loadPrefs();
        } else {
            createDefaultPrefs();
        }
    }

    private static void loadPrefs() {
        try {
            BufferedReader input = new BufferedReader(new FileReader(PREF_FILE));
            String theFile = "";
            String currLine = "";
            while ((currLine = input.readLine()) != null) {
                theFile += currLine;
            }
            prefs = Json.read(theFile);
            input.close();
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void save() {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(PREF_FILE));
            output.write(beautifyJson(prefs.toString()));
            output.flush();
            output.close();
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void toggleBooleanProperty(String propertyName) {
        log.info("Toggling " + propertyName + " to " + !Prefs.prefs.at(propertyName).asBoolean());
        prefs.set(propertyName, !Prefs.prefs.at(propertyName).asBoolean());
        save();
    }

    private static void createDefaultPrefs() {
        log.info("Creating default properties file at " + PREF_FILE.getAbsolutePath());
        HashMap<String, Object> prefMap = new HashMap<String, Object>();

        prefMap.put("screenshot.help.enabled", true);
        prefMap.put("screenshot.crosshair.enabled", true);
        prefMap.put("app.displaywelcome", true);
        prefMap.put("app.collectdata", true);

        prefs = Json.make(prefMap);
        log.info("Default Properties: " + beautifyJson(Json.make(prefMap).toString()));
        save();
    }

    private static String beautifyJson(String json) {
        return json.replace("{", "{\n").replace(",", ",\n").replace("}", "\n}");
    }

    public static Json getEnvironmentProperties() {
        Json properties = Json.object();
        String[] propertiesList = { "java.version", "java.vendor", "java.home", "java.vm.version",
                "java.class.version", "os.name", "os.arch", "os.version" };
        for (String property : propertiesList) {
            properties.set(property, System.getProperty(property));
        }
        return properties;
    }
}
