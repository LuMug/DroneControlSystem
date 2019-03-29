# PROGETTO Drone Control System | Diario di lavoro - 22.03.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 22.03.2019

## Lavori svolti

Jari:
Assieme a Fadil e Luca abbiamo cercato di capire meglio il funzionamento del pitch, roll e yaw sul quale mi sono anche informato in modo dettagliato dovendo anche usarli per calcolare attitude e acceleration, e tof.
Inoltre abbiamo reso la connessione fra le classi Simulator e CommandManager molto stabile e funzionante anche su svariarte piattaforme come pc e sistemi operativi diversi.

Fadil:
Ho cambiato ancora modo per scoprire il movimento della mano, per il regolamento della altezza, adesso uso la velocità della amno per scoprire il movimento. Ho aggiunto i metodi utili, per rilevare il movimento della mano destra, alla classe FrameHelper che userò nella classe Dronecontroller per mandare i commandi relativi allo spostamento di rollio, beccheggio e imbardata.

Luca:
-

Andrea:
Ho collegato le classi di visualizzazione di dati al simulatore in modo che si possa vedere i valori mandati dal drone. Inoltre all'interno di queste classi ho cambiato il tipo di valore da float a int in modo da essere in regola con la struttura dei dati del simulatore.

## Problemi riscontrati e soluzioni adottate
Non abbiamo riscontrato nessun problema.

## Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
-
