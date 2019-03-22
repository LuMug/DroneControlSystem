package settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author luca6
 */
public class SettingsManager {

    private Path filePath = Paths.get("config", "config.dcs");
    private static final char COMMENT_CHARACTER = '#';
    private String delimiter = "=";

    //Costructor 1 
    public SettingsManager() {
    }

    //Costructor 2 
    public SettingsManager(Path filePath) {
        this.filePath = filePath;
    }

    public Map<String, String> getSettings() {
        //Dictionary
        Map<String, String> map = new HashMap<>();
        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.length() > 0) {
                    if (line.charAt(0) == this.COMMENT_CHARACTER) {
                        continue;
                    }

                    String[] data = line.split(delimiter);

                    if (data.length == 2) {
                        String key = data[0];
                        String value = data[1];

                        map.put(key, value);
                    }
                }
            }

        } catch (IOException ex) {
        }

        return map;
    }

    public String getSetting(String settingName) throws IllegalArgumentException {
        Map<String, String> map = getSettings();
        String data = map.get(settingName);

        if (data != null) {
            return data;
        } else {
            throw new IllegalArgumentException("Invalid setting name: " + settingName);
        }
    }
}
