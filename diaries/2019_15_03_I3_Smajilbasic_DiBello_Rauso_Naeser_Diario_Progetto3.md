# PROGETTO Drone Control System | Diario di lavoro - 08.05.2019
##### Luca Di Bello, Fadil Smajilbasic, Andrea Rauso, Jari Näser
### Canobbio, 08.05.2019

## Lavori svolti

Jari:

Fadil:

Luca:

Andrea:



## Problemi riscontrati e soluzioni adottate

Per mettere apposto la questione del autoscroll nel panello di log, bastava aggiungere un MouseListener che quando l'utente entra con il mouse nella schermata o quando clicca da qualsiasi parte nellos chermo, il focus della finestra passa al JTextArea dove viene scritto il log.

`public void mouseClicked(MouseEvent e) {`
        `logTextArea.requestFocus();`
   ` }`


## Punto della situazione rispetto alla pianificazione
Siamo al passo con il gantt preventivo.

#### Programma di massima per la prossima giornata di lavoro
Chiedere se nei componenti dell'Hardware bisogna anche mettere i componenti
specifici dei vari PC.
