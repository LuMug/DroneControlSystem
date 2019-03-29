# PROGETTO Drone Control System | Diario di lavoro - 29.03.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari NÃ¤ser
### Canobbio, 29.03.2019

## Lavori svolti

Jari:

Fadil:

Luca:
Ho commentato e terminato l'implementazione della classe 'SettingsManager' (la classe che gestisce il file di config). Ho sviluppato anche una classe chiamata 'ControllerSettings', la quale si appoggia alla classe 'SettingsManager', che permette di accedere facilmente ai dati letti dal file di config tramite delle variabili
in sola lettura. 

Andrea:
-

## Problemi riscontrati e soluzioni adottate
Fadil:
Avevo dei problemi mentre cercavo di leggere l'angolo di rollio beccheggio e imbardata, poiche' l'angolo letto non era quello corretto.

Ho trovato e implementato questa soluzione tramite StackOverflow, questo è il link: https://stackoverflow.com/a/9970297

## Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
Luca:
-Una GUI per il controller che permetterà di cambiare delle impostazione sul volo "On Air" (quindi senza dover far atterrare e ripartire il drone)
-Implementare nella classe 'SettingsManager' dei metodi utili per la modifica delle impostazione nel file di config (senza dover modificarlo a mano)