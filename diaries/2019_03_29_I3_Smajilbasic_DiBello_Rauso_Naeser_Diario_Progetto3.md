# PROGETTO Drone Control System | Diario di lavoro - 29.03.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 29.03.2019

## Lavori svolti

Jari:
Io ho continuato e terminato lo sviluppo ed implementazione dei metodi della SDK di tello nella classe CommandReader.
Inoltre ho anche implementato la risposta via pacchetto UDP ai metodi getter che finiscono per '?' ad esempio: 'battery?'.
Infine ho contribuito a migliorare la cominicazione e il funzionamento fra le varie classi una volta messe assieme.

Fadil:
Ho implementato il calcolo dell'angolo di rollio tra il pollice e il mignolo della mano destra e assieme a Luca abbiamo aggiunto la possibilità di assegnare dei parametri al DroneController grazie a un file di config
Luca:
Ho commentato e terminato l'implementazione della classe 'SettingsManager' (la classe che gestisce il file di config). Ho sviluppato anche una classe chiamata 'ControllerSettings', la quale si appoggia alla classe 'SettingsManager', che permette di accedere facilmente ai dati letti dal file di config tramite delle variabili
in sola lettura.

Andrea:
-

## Problemi riscontrati e soluzioni adottate
Fadil:
Avevo dei problemi mentre cercavo di leggere l'angolo di rollio beccheggio e imbardata, poiche' l'angolo letto non era quello corretto.

Ho trovato e implementato questa soluzione tramite StackOverflow, questo � il link: https://stackoverflow.com/a/9970297

## Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
Jari:
Continuare a sviluppare le classi CommandReader e Simulator con le features scritte sotto forma di commento nella classe CommandReader.
Luca:
-Una GUI per il controller che permetter� di cambiare delle impostazione sul volo "On Air" (quindi senza dover far atterrare e ripartire il drone)
-Implementare nella classe 'SettingsManager' dei metodi utili per la modifica delle impostazione nel file di config (senza dover modificarlo a mano)
