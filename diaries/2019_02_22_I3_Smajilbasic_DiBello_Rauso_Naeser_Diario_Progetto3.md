# PROGETTO Drone Control System | Diario di lavoro - 22.02.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 22.02.2019

## Lavori svolti
Andrea:
Io ho finito l'analisi della libreria JFreeChart per la creazione dei grafici per il simulatore del drone.

Luca:
Io ho iniziato a sviluppare una classe che sfrutta le funzionalità implementate dalla classe "CommandManager" e "LeapMotionReader". Essa è chiamata "DroneController" e si occupa di leggere i dati dal Leap Motion ed inviarli tramite protocollo UDP al drone (simulatore) tramite la classe CommandManager.

Jari:
Io ho creato tutta la struttura dei metodi come quella fornita dalla SDK di tello nella classe CommandReader.java, inoltre sta lavorando sulla corretta interpretazione delle richieste dei vari metodi mandati via pacchetto UDP dalla classe Controller.java alla Simulator.java.

##  Problemi riscontrati e soluzioni adottate
Riguardo al problema dell'invio dei pacchetti dal simulatore verso il controller é stato risolto impostando una porta di ascolto nel controller che in precedenza non é stata impostata.

Luca durante lo sviluppo della classe "DroneController" si è reso conto che il suo ambiente di sviluppo (NetBeans) non era impostato correttamente per lo sviluppo utilizzando la libreria "LeapJava" (libreria per l'utilizzo di Leap Motion tramite il linguaggio Java). Quando Luca cercava di compilare il programma, il compilatore mostrava un warning con questa dicitura:
``"Native code library failed to load. java.lang.UnsatisfiedLinkError: no LeapJava in java.library.path".``
Grazie all'aiuto di Fadil Smajilbasic Luca è riuscito a trovare una soluzione, questi sono i vari step:
1) Scaricare nuovamente la libreria dal sito ufficiale
2) Aggiungere la libreria al progetto
3) Aggiungere una stringa di configurazione della VM la quale serve a specificare la posizione dei file .dll necessari per il corretto funzionamento della libreria "LeapJava":
``-Djava.library.path="path fino ai file dll della libreria"``
Stringa utilizzata da Luca:
``-Djava.library.path="C:\Users\luca6\Desktop\libs\x64\"``

4) Compilato (build) il progetto
5) Il programma adesso si avvia correttamente

##  Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
Luca cercherà di creare una versione beta del controller del drone (DroneController), la quale permetterà di controllare solo l'altitudine del drone.

Jari invece continuerà lo sviluppo della classe CommandReader.java nella quale cercherà di simulare l'esatto comportamento del drone tello attraverso varie variabili con i vari rispettivi metodi.
