package settings;
/**
 * Enum containing values for the flip command.
 * @author Luca Di Bello
 */
public enum FlipCommand {
    LEFT("l"), RIGHT("r"), FORWARD("f"), BACK("b");
    
    private String value; 
    
    private FlipCommand(String value){
        this.value = value;
    }
    
    public String getValue() { return value; }
}
