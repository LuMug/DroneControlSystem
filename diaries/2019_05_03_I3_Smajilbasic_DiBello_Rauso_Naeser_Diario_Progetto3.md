# PROGETTO Drone Control System | Diario di lavoro - 03.05.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 03.05.2019

## Lavori svolti

Jari:
Ho creato il diagramma delle classi del progetto DroneSimulator e ho continuato
con la documentazione.

Fadil:
Ho modificato il modo in quale il controller calcola l'intervallo di tempo tra un commando e l'altro, inoltre ho fatto altri test con il drone tutti con successo. L'integrazione tra il leapMotion e il drone è più che ottimale.

Luca:
Ho continuato la documentazione terminando il capitolo 'analisi e 
specifica dei requisiti'. Ho anche creato le classi 'FlightRecorder' e 
'FlightBuffer'.

Andrea:
Ho continuato la documentazione aggiungendo l'implementazione delle classi di DroneSimulator e l'aggiunta dei mockup delle interfacce.


## Problemi riscontrati e soluzioni adottate

Problema con l'aggiunta dell'autoscroll al panello che segna i comandi mandati:

Codice usato per testare:

`DefaultCaret caret = (DefaultCaret) logTextArea.getCaret();
caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
`

Non ha funzionato ma cercerò delle altre soluzioni

## Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
