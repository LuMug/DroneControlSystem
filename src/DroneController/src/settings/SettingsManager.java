package settings;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles a config file. By default it uses a file placed in 
 * this path: 'config/config.dcs' but through a second parameterized constructor 
 * it is possible to set a different one. The character that identifies a 
 * commented line within the config file is the '#' character, 
 * also it is possible to replace it through the constructors.
 *
 * @author Luca Di Bello
 */
public class SettingsManager {

    /**
     * Path of the configuration file. By default the path is 'config/config.dcs' (path
     * relative).
     */
    private Path filePath = Paths.get("config", "config.dcs");

    /**
     * Character that identifies a commented line within the
     * config.
     */
    private char commentCharacter = this.DEFAULT_COMMENT_CHARACTER;

    /**
     * String that divides the setting name from the value
     * setting. Example:
     * setting_name<division_character>setting_value.
     */
    private char settingDelimiter = this.DEFAULT_SETTING_DELIMITER;

    /**
     * Default character for the commentCharacter parameter.
     */
    private final char DEFAULT_COMMENT_CHARACTER = '#';

    /**
     * Default string for the stringDelimiter parameter.
     */
    private final char DEFAULT_SETTING_DELIMITER = '=';

    /**
     * Default constructor
     */
    public SettingsManager() {
    }

    /**
     * Parametrized constructor.
     *
     * @param filePath config file location (path).
     */
    public SettingsManager(Path filePath) {
        this(filePath,'\u0000','\u0000');
    }

    /**
     * Costruttore parametrizzato.
     *
     * @param filePath config file location (path).
     * @param settingDelimiter Delimiter that divides the setting name and the its value.
     * @param commentCharacter Delimiter which indentifies if a line it's commented.
     */
    public SettingsManager(Path filePath, char settingDelimiter, char commentCharacter) {
        this.filePath = filePath;
        setSettingDelimiter(settingDelimiter);
        setCommentCharacter(commentCharacter);
    }

    /**
     ** This method allows you to generate a dictionary containing all the
     * settings in the configuration file. The dictionary is structured in this way:
     * <ul>
     *  <li>Search key = Setting name</li>
     *  <li>Return value = Setting value</li>
     * </ul>
     *
     * @return Dictionary of strings where you can use the name
     * of the setting as a search key within it.
     */
    public Map<String, String> getSettings() {
        //Dictionary
        Map<String, String> map = new HashMap<>();
        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.length() > 0) {
                    if (line.charAt(0) == this.commentCharacter) {
                        continue;
                    }

                    String[] data = line.split("" + settingDelimiter);

                    if (data.length == 2) {
                        String key = data[0];
                        String value = data[1];

                        map.put(key, value);
                    }
                }
            }

        } catch (IOException ex) {
            System.err.println("[Error] Error while generating settings dictionary");
        }

        return map;
    }

    /**
     * This method allows you to take the value of a specific setting just 
     * using its name.
     *
     * @param settingName Name of the setting.
     * @return Setting value.
     * @throws IllegalArgumentException throwed when a
     * setting is without value or non-existent
     */
    public String getSetting(String settingName) throws IllegalArgumentException {
        Map<String, String> map = getSettings();
        String data = map.get(settingName);

        if (data != null) {
            return data;
        } else {
            throw new IllegalArgumentException(
                    "Invalid setting name: " + settingName + " maybe the "
                    + "setting hasn't any value or it's not "
                    + "present in the configuration file."
            );
        }
    }

    /**
     * This method allows you to set/modify a value of a specific setting just using
     * it's name.
     * 
     * @param settingName Name of the setting.
     * @param value Value which will be set as setting value.
     * @throws IllegalArgumentException throwed when a
     * setting is without value or non-existent 
     */
    public void setSetting(String settingName, String value) throws IllegalArgumentException {
        //Read all file lines
        try {
            List<String> lines = Files.readAllLines(filePath);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.length() > 0) {
                    if (line.charAt(0) != '#') {
                        //Not comment
                        String scrapedSetting = line.split("" + getSettingDelimiter())[0];
                        if (scrapedSetting.equals(settingName)) {
                            //Build new setting string
                            String setting = scrapedSetting + getSettingDelimiter() + value;
                            lines.set(i, setting);
                            break;
                        }
                    }
                }
            }

            Files.write(filePath, lines, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
    }

    /**
     * Getter method for commentCharacter parameter.
     *
     * @return Delimiter which indentifies if a line it's commented.
     */
    public char getCommentCharacter() {
        return commentCharacter;
    }

    /**
     * Setter method for commentCharacter parameter.
     *
     * @param commentCharacter Character that indentifies if a line it's commented.
     */
    private void setCommentCharacter(char commentCharacter) {
        char nullChar = '\u0000';
        if (commentCharacter == nullChar) {
            this.commentCharacter = this.DEFAULT_COMMENT_CHARACTER;
        } else {
            this.commentCharacter = commentCharacter;
        }
    }

    /**
     * Getter method for settingDelimiter parameter.
     * 
     * @return Delimiter that divides the setting name and the its value.
     */
    public char getSettingDelimiter() {
        return settingDelimiter;
    }

    /**
     * Setter method for settingDelimiter parameter.
     *
     * @param settingDelimiter New delimiter that divides the setting name and the its value.
     */
    private void setSettingDelimiter(char settingDelimiter) {
        char nullChar = '\u0000';
        if (settingDelimiter == nullChar) {
            this.settingDelimiter = this.DEFAULT_SETTING_DELIMITER;
        } else {
            this.settingDelimiter = settingDelimiter;
        }
    }
}
