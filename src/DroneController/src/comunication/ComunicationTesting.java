package comunication;

import settings.TelloComunicationData;

/**
 * The scope of this class is to test the communication between the controller and 
 * the drone simulator.
 * @author Luca Di Bello
 */
public class ComunicationTesting {
    public static CommandManager manager;

    public static void main(String[] args) {
        manager = new CommandManager();
        
        //Metodi bloccanti, vanno startati in un thread
        while(true){
            try{
                String[] commands = new String[]{CommandBuilder.ENABLE_COMMANDS,CommandBuilder.TAKEOFF,CommandBuilder.up(130)};
                manager.sendCommands(commands);

                Thread.sleep(2000);
            }
            catch(InterruptedException ex){}
        }
    }
}
