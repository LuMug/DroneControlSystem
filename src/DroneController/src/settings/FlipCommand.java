package settings;
/**
 * Enum containing values for the flip command.
 * @author Luca Di Bello
 */
public enum FlipCommand {
    LEFT("l"), RIGHT("r"), FORWARD("f"), BACK("b");
    
    /**
     * Value of the enum.
     */
    private String value; 
    
    /**
     * Parametrized constructor.
     * @param value Value which will be the enum value.
     */
    private FlipCommand(String value){
        this.value = value;
    }
    
    /**
     * Getter method for getting the enum string value.
     * @return Value of the enum as String
     */
    public String getValue() { return value; }
}
