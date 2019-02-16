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
        //startStateListening();
        //startStateListening();
        
        while(true){
            try{
                manager.sendCommand("prova invio dati :)");
                Thread.sleep(1000);
            }
            catch(InterruptedException ex){}
        }
    }
    
    public static void startVideoListening(){
        manager.listenData(TelloComunicationData.TELLO_VIDEO_LISTEN_PORT, 2048);
    }
    
    public static void startStateListening(){
        manager.listenData(TelloComunicationData.TELLO_STATE_LISTEN_PORT, 2048);
    }
}
