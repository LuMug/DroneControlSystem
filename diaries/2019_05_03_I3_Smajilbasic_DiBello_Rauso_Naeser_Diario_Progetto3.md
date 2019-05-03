# PROGETTO Drone Control System | Diario di lavoro - 03.05.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 03.05.2019

## Lavori svolti

Jari:


Fadil:
Ho modificato il modo in quale il controller calcola l'intervallo di tempo tra un commando e l'altro, inoltre ho fatto altri test con il drone tutti con successo. L'integrazione tra il leapMotion e il drone è più che ottimale.

Luca:


Andrea:



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
