# PROGETTO Drone Control System | Diario di lavoro - 29.03.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 29.03.2019

## Lavori svolti

Jari:
Io ho continuato e terminato lo sviluppo ed implementazione dei metodi della SDK di tello nella classe CommandReader.
Inoltre ho anche implementato la risposta via pacchetto UDP ai metodi getter che finiscono per '?' ad esempio: 'battery?'.
Infine ho contribuito a migliorare la cominicazione e il funzionamento fra le varie classi una volta messe assieme.

Fadil:

Luca:
-

Andrea:
-

## Problemi riscontrati e soluzioni adottate
Fadil:
Avevo dei problemi mentre cercavo di leggere l'angolo di rollio beccheggio e imbardata, poichè i l'angolo letto non era quello giusto.

Ho trovato e implementato questa soluzione su stackoverflow:
https://stackoverflow.com/a/9970297

## Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
Jari: Continuare a sviluppare le classi CommandReader e Simulator con le features scritte sotto forma di commento nella classe CommandReader.
