# PROGETTO Drone Control System | Diario di lavoro - 13.03.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 13.03.2019

## Lavori svolti

Andrea:
Ho creato il mockup delle interfaccie che andremo ad utilizzare per mostrare i dati di posizione e rotazione del drone e ho cominciato lo sviluppo dei grafici in cui verranno visualizzati la posizione del drone dall'alto e di profilo

Jari:
Ho continuato con lo sviluppo della classe CommandReader nella quale sto finendo di simulare il movimento del drone al ricevimento dei vari comandi della SDK di Tello.

Fadil:
Ho modificato la classe LeapMotionReader rinominandola FrameHelper, poichè conteneva solo metodi utili a ricavare delle informazioni da un frame, in più ho aggiunte altre funzionalità a quella classe. Ho integrato le funzionalità della classe FrameHelper nella classe DroneController. Devo ancora correggere la classe CommandManager di Luca per farla funzionare con la struttura che abbiamo adesso.

Luca:
Ho completato la classe Commands, implementando tutti i comandi supportati dal drone DJI Tello. Il comando "curve" ed il comando "rc" non sono ancora stati implementati.

##  Problemi riscontrati e soluzioni adottate
Non abbiamo riscontrato nessun problema.

##  Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
Jari: Se possibile finire la classe CommandReader e fare dei primi test unendola a Simulator.
