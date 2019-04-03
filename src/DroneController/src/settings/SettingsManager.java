package settings;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Questa classe si occupa di gestire un file di config. Di default utilizza un file 
 * posto in questa path: 'config/config.dcs' ma tramite un secondo costruttore parametrizzato
 * è possibile impostarne uno differente. Il carattere che identifica una riga commentata
 * all'interno del file di config è il carattere '#', anch'esso è possibile sostituirlo
 * attraverso i costruttori.
 * 
 * @author Luca Di Bello
 */
public class SettingsManager {
    
    /**
     * Path del file di config. Di default la path è 'config/config.dcs' (path relativa).
     */
    private Path filePath = Paths.get("config", "config.dcs");
    
    /**
     * Carattere che identifica una riga commentata all'interno del file di config.
     */
    private char commentCharacter = this.DEFAULT_COMMENT_CHARACTER;
    
    /**
     * Stringa che divide il nome dell'impostazione dal valore dell'impostazione.
     * Esempio: nome_impostazioe<carattere_divisione>valore_impostazione.
     */
    private String settingDelimiter = this.DEFAULT_SETTING_DELIMITER;
    
    /**
     * Carattere di default per il parametro commentCharacter.
     */
    private final char DEFAULT_COMMENT_CHARACTER = '#'; 
    
    /**
     * Stringa di default per il parametro stringDelimiter.
     */
    private final String DEFAULT_SETTING_DELIMITER = "="; 


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
     * Costruttore parametrizzato
     * @param filePath Path del file di config.
     */
    public SettingsManager(Path filePath, String settingDelimiter, char commentCharacter) {
        this.filePath = filePath;
        this.settingDelimiter = settingDelimiter;
        this.commentCharacter = commentCharacter;
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
                    if (line.charAt(0) == this.commentCharacter) {
                        continue;
                    }

                    String[] data = line.split(settingDelimiter);

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
     * Questo metodo permette di prendere il valore di un impostazione specifica.
     * @param settingName Nome dell'impostazione.
     * @return valore dell'impostazione
     * @throws IllegalArgumentException Lanciata quando viene richiesta un 
     * impostazione senza valore o inesistente
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
    
    public void setSetting(String settingName, String value) throws IllegalArgumentException{
        //Read all file lines
        try{
            List<String> lines = Files.readAllLines(filePath);
        
            for(int i = 0; i < lines.size();i++){
                String line = lines.get(i);
                if(line.length() > 0){
                    if(line.charAt(0) != '#'){
                        //Not comment
                        String scrapedSetting = line.split(getSettingDelimiter())[0];
                        System.out.println("Found setting: " + scrapedSetting);
                        if(scrapedSetting.equals(settingName)){
                            //Build new setting string
                            System.out.println("[SUCCESS!] Found correct setting");
                            String setting = scrapedSetting + getSettingDelimiter() + value;
                            lines.set(i, setting);
                            break;
                        }
                    }
                    else{
                        System.out.println("Found commented line");
                    }
                }
                else{
                    System.out.println("Found empty line");
                }
            }

            Files.write(filePath, lines, Charset.forName("UTF-8"));
        }
        catch(IOException ex){
            System.err.println("IOException: " + ex.getMessage());
        }
    }
    
    /**
     * Metodo getter per il carattere che indentifica una riga commentata nel file di config.
     * @return Carattere che indentifica la stringa commentata.
     */
    public char getCommentCharacter() {
        return commentCharacter;
    }
    
    /**
     * Metoodo setter che permette di impostare il carattere che identifica 
     * una stringa di commento nel file di config.
     * @param commentCharacter Nuovo carattere che indentifica la stringa di commento.
     */
    private void setCommentCharacter(char commentCharacter) {
        char nullChar = '\u0000';
        if(commentCharacter == nullChar){
            this.commentCharacter = this.DEFAULT_COMMENT_CHARACTER;
        }
        else{
            this.commentCharacter = commentCharacter;
        }
    }
    
    /**
     * Metodo getter che permette di ricavare la stringa che divide il nome 
     * dell'impostazione dal valore dell'impostazione.
     * @return Stringa che contiene il divisore tra il nome 
     * dell'impostazione e il valore dell'impostazione.
     */
    public String getSettingDelimiter() {
        return settingDelimiter;
    }

    /**
     * Metodo setter che permette di impostare la stringa che divide il nome dell'impostazione
     * ed il valore dell'impostazione.
     * @param settingDelimiter Stringa di divisione nuova.
     */
    private void setSettingDelimiter(String settingDelimiter) {
        if(settingDelimiter == null || settingDelimiter.equals("")){
            this.settingDelimiter = this.DEFAULT_SETTING_DELIMITER;
        }
        else{
            this.settingDelimiter = settingDelimiter;
        }
    }
}
