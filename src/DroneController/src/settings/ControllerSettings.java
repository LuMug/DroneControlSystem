package settings;

/**
 * This class is used to load the config file settings and values into real variables.
 * 
 * @author Luca Di Bello
 */
public class ControllerSettings {
    
    /**
     * SettingManager object that allows the user to interact within the config file-
     */
    private SettingsManager manager = new SettingsManager();

    //Controller settings
    /**
     * degreesSensibility setting.
     */
    private float controllerDegreesSensibility = 5f;

    //CommandManager settings
    /**
     * jari_address setting.
     */
    private String communicationJariAddress;
    
    /**
     * tello_address setting.
     */
    private String communicationTelloAddress;
    
    /**
     * tello_command_send_port setting.
     */
    private int communicationSendPortCommand;
    
    /**
     * tello_command_listen_port setting.
     */
    private int communicationListenPortCommand;
    
    /**
     * tello_state_listen_port setting.
     */
    private int telloStatePort;
    
    /**
     * heightThreshold setting.
     */
    private int heightThreshold;
    
    /**
     * Default constructor.
     */
    public ControllerSettings() {
        updateSettings();
    }
    
    /**
     * This method loads into the variables all the controller-related settings.
     * 
     * @throws IllegalArgumentException thrown when a
     * setting is without value or non-existent
     */
    private void loadControllerSettings() throws IllegalArgumentException {
        communicationJariAddress = manager.getSetting("jari_address");
        communicationTelloAddress = manager.getSetting("tello_address");
        communicationSendPortCommand = getIntSetting("tello_command_send_port");
        communicationListenPortCommand = getIntSetting("tello_command_listen_port");
        telloStatePort = getIntSetting("tello_state_listen_port");
        controllerDegreesSensibility = getFloatSetting("degreesSensibility");
        heightThreshold = getIntSetting("heightThreshold");
    }

    
    private int getIntSetting(String settingName) throws IllegalArgumentException {
        try {
            return Integer.parseInt(manager.getSetting(settingName));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    String.format(
                            "[Error] Error in setting with name: %s. The value isn't a number..",
                            settingName
                    )
            );
        }
    }
    
    /**
     * This method allows to read a specific setting from the file and 
     * return its value as a float.
     * @param settingName Setting name.
     * @return Value of the setting as float.
     * @throws IllegalArgumentException thrown when a
     * setting is without value, non-existent or it's value isn't a number.
     */
    private float getFloatSetting(String settingName) throws IllegalArgumentException {
        try {
            return Float.parseFloat(manager.getSetting(settingName));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    String.format(
                            "[Error] Error in setting with name: %s. The value isn't a number..",
                            settingName
                    )
            );
        }
    }
    
    /**
     * Reload all the settings.
     * @throws IllegalArgumentException thrown when a
     * setting is without value or non-existent 
     */
    public void updateSettings() throws IllegalArgumentException{
        loadControllerSettings();
    }

    /**
     * Getter method for telloStatePort attribute.
     * @return telloStatePort attribute value.
     */
    public int getTelloStatePort() {
        return telloStatePort;
    }

    /**
     * Getter method for controllerDegreesSensibility attribute.
     * @return controllerDegreesSensibility attribute value.
     */
    public float getControllerDegreesSensibility() {
        return controllerDegreesSensibility;
    }

    /**
     * Getter method for communicationJariAddress attribute.
     * @return communicationJariAddress attribute value.
     */
    public String getCommunicationJariAddress() {
        return communicationJariAddress;
    }
    
    /**
     * Getter method for communicationTelloAddress attribute.
     * @return communicationTelloAddress attribute value.
     */
    public String getCommunicationTelloAddress() {
        return communicationTelloAddress;
    }

    /**
     * Getter method for communicationSendPortCommand attribute.
     * @return communicationSendPortCommand attribute value.
     */
    public int getCommunicationSendPortCommand() {
        return communicationSendPortCommand;
    }
    
    /**
     * Getter method for communicationSendPortCommand attribute.
     * @return communicationSendPortCommand attribute value.
     */
    public int getCommunicationListenPortCommand() {
        return communicationListenPortCommand;
    }
    
    /**
     * Getter method for heightThreshold attribute.
     * @return heightThreshold attribute value.
     */
    public int getHeightThreshold() {
        return heightThreshold;
    }
}  
