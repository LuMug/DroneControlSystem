# PROGETTO Drone Control System | Diario di lavoro - 03.04.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 03.04.2019

## Lavori svolti

Jari:
Ho continuato con il raffinamento della comunicazione fra le varie classi dando feedback e modificando quello necessario e mi sono informato in modo approfondito sul drone tello per poter simulare al meglio tutte le sue funzionalità e movimenti.

Fadil:
Ho implementato il controllo completo del drone usando le estremità della mano per il calcolo dei angoli di rollio, beccheggio e imbardata della mano destra. Usando le estremità e un paoio di calcoli matematici l'angolo letto risulta molto più preciso che usando i metodi forniti dall'SDK del LeapMotion.
Ho fatto un paio di test insieme a Jari per controllare il giusto funzionamento della classe DroneController avendo implementato questi nuovi metodi.

Luca:
Ho finito la classe che gestisce le impostazioni sul file di config (SettingManager).

Andrea:
-

## Problemi riscontrati e soluzioni adottate
Un calcolo preciso dei angoli della mano destra
Soluzione trovata su due siti:

- https://stackoverflow.com/questions/2676719/Calculating-the-angle-between-the-line-defined-by-two-points
- https://math.stackexchange.com/questions/1201337/finding-the-angle-between-two-points

Codice:
`Math.toDegrees(Math.atan2(Y1 - Y2, X1 - X2));`



## Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
-
