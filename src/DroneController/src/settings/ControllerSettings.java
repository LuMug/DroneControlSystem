package settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luca6
 */
public class ControllerSettings {
    private SettingsManager manager = new SettingsManager();

    //Controller settings
    private float controllerSensibility = 2f;
    private float controllerDegreesSensibility = 5f;
    private int controllerHeightPointsNumber = 20;

    //CommandManager settings
    private String communicationJariAddress;
    private String communicationTelloAddress;
    private int communicationSendPortCommand;
    private int communicationListenPortCommand;

    public ControllerSettings() {
        updateSettings();
    }
    
    private void loadControllerSettings() throws IllegalArgumentException{
        communicationJariAddress = manager.getSetting("jari_address");
        communicationTelloAddress = manager.getSetting("tello_address");
        communicationSendPortCommand = getIntSetting("tello_command_send_port");
        communicationListenPortCommand = getIntSetting("tello_command_listen_port");
    }
    
    private void loadCommandManagerSettings() throws IllegalArgumentException{
        controllerDegreesSensibility = getFloatSetting("degrees_sensibility");
        controllerSensibility = getFloatSetting("sensibility");
        controllerHeightPointsNumber = getIntSetting("height_points_number");
    }
    
    
    private int getIntSetting(String settingName) throws IllegalArgumentException{
        try{
            return Integer.parseInt(manager.getSetting(settingName));
        }
        catch(NumberFormatException ex){
            throw new IllegalArgumentException(
                    String.format(
                            "[Error] Error in setting with name: %s. The value isn't a number..",
                            settingName
                    )
            );
        }
    }
    private float getFloatSetting(String settingName) throws IllegalArgumentException{
        try{
            return Float.parseFloat(manager.getSetting(settingName));
        }
        catch(NumberFormatException ex){
            throw new IllegalArgumentException(
                    String.format(
                            "[Error] Error in setting with name: %s. The value isn't a number..",
                            settingName
                    )
            );
        }
    }
    
    public void updateSettings(){
        loadControllerSettings();
        loadCommandManagerSettings();
    }
    
    public float getControllerSensibility() {
        return controllerSensibility;
    }

    public float getControllerDegreesSensibility() {
        return controllerDegreesSensibility;
    }

    public int getControllerHeightPointsNumber() {
        return controllerHeightPointsNumber;
    }

    public String getCommunicationJariAddress() {
        return communicationJariAddress;
    }

    public String getCommunicationTelloAddress() {
        return communicationTelloAddress;
    }

    public int getCommunicationSendPortCommand() {
        return communicationSendPortCommand;
    }

    public int getCommunicationListenPortCommand() {
        return communicationListenPortCommand;
    }
}
