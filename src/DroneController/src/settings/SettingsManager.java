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
 * Questa classe si occupa di gestire un file di config. Di default utilizza un file 
 * posto in questa path: 'config/config.dcs' ma tramite un secondo costruttore parametrizzato
 * è possibile impostarne uno differente.
 * @author Luca Di Bello
 */
public class SettingsManager {
    
    /**
     * Path del file di config. Di default la path è 'config/config.dcs' (path relativa)
     */
    private Path filePath = Paths.get("config", "config.dcs");
    private static final char COMMENT_CHARACTER = '#';
    private String delimiter = "=";

    /**
     * Costruttore vuoto
     */
    public SettingsManager() {
    }

    /**
     * Costruttore parametrizzato
     * @param filePath Path del file di config.
     */
    public SettingsManager(Path filePath) {
        this.filePath = filePath;
    }
    
    /**
     * Questo metodo permette di generare un dizionario contenente tutte le impostazioni
     * presenti nel file di config. Il dizionario è strutturato così: 
     * <ul>
     *  <li>Chiave di ricerca = Nome impostazione</li>
     *  <li>Valore di ritorno = Valore dell'impostazione</li>
     * </ul>
     * @return Dizionario di strighe dove si può utilizzare il nome 
     * dell'impostazione come chiave di ricerca al suo interno. 
     */
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
            System.err.println("[Error] Error while generating settings dictionary");
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
