package settings;

/**
 *
 * @author Luca Di Bello
 */
public class ControllerSettings {

    private SettingsManager manager = new SettingsManager();

    //Controller settings
    private float controllerDegreesSensibility = 5f;

    //CommandManager settings
    private String communicationJariAddress;
    private String communicationTelloAddress;
    private int communicationSendPortCommand;
    private int communicationListenPortCommand;
    private int telloStatePort;

    public ControllerSettings() {
        updateSettings();
    }

    private void loadControllerSettings() throws IllegalArgumentException {
        communicationJariAddress = manager.getSetting("jari_address");
        communicationTelloAddress = manager.getSetting("tello_address");
        communicationSendPortCommand = getIntSetting("tello_command_send_port");
        communicationListenPortCommand = getIntSetting("tello_command_listen_port");
        telloStatePort = getIntSetting("tello_state_listen_port");
    }

    private void loadCommandManagerSettings() throws IllegalArgumentException {
        controllerDegreesSensibility = getFloatSetting("degreesSensibility");
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

    public void updateSettings() {
        loadControllerSettings();
        loadCommandManagerSettings();
    }

    public int getTelloStatePort() {
        return telloStatePort;
    }

    public float getControllerDegreesSensibility() {
        return controllerDegreesSensibility;
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
