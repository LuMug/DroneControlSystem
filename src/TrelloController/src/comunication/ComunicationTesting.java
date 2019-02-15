package comunication;

/**
 * The scope of this class is to test the communication between the controller and 
 * the drone simulator.
 * @author Luca Di Bello
 */
public class ComunicationTesting {
    public static void main(String[] args) {
        CommandManager manager = new CommandManager();
        
        while(true){
            try{
                manager.sendCommand("prova invio dati :)");
                Thread.sleep(1000);
            }
            catch(InterruptedException ex){}
        }
    }
}
