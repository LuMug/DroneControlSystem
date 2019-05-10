# Drone Control System [METTERE TITOLO?]

#### Da Fare:
- 1.4: Riguardare
- 1.5: Aggiungere eventuali requisiti
- 2.0: Da fare (Attualmente header di default)
- 2.4: Da fare
- 3.1: Fare tutta la parte di implementazione Luca e Fadil
- 3.2: Guardare se c'è tutto
- 4.1: Cambiare header (ancora default)
- 4.1: Fare test-case
- 4.3: Da fare
- 6.0: Da fare
- 6.1: da fare
- 6.2: da fare
- 8.1: da fare
- Sommario: aggiornarlo
- Discutere per titolo doc

---

1. [Introduzione](#1.-introduzione)

    1.1 [Informazioni sul progetto](#1.1-informazioni-sul-progetto)

    1.2 [Abstract](#1.2-abstract)

    1.3[Scopo](#1.3-scopo)

  - [Analisi](#analisi)

    1.4 [Analisi del dominio](#1.4-analisi-del-dominio)

    1.5 [Analisi dei mezzi](#1.5-analisi-dei-mezzi)

    1.6 [Analisi e specifica dei requisiti](#1.6-analisi-e-specifica-dei-requisiti)

    1.7 [Pianificazione](#1.7-pianificazione)

2. [Progettazione](#2.-progettazione)

    2.1 [Design dell’architettura del sistema](#2.1-design-dell’architettura-del-sistema)

    2.2 [Schema logico](#2.2-schema-logico)

    2.3 [Design delle interfacce](#2.3-design-delle-interfacce)

    2.4 [Design procedurale](#2.4-design-procedurale)

3. [Implementazione](#3.-implementazione)

    3.1 [Drone Controller](#3.1-drone-controller)

    3.2 [Drone Simulator](#3.2-drone-simulator)

4. [Test](#4.-test)

    4.1 [Protocollo di test](#4.1-protocollo-di-test)

    4.2 [Risultati test](#4.2-risultati-test)

    4.3 [Mancanze/limitazioni conosciute](#4.3-mancanze/limitazioni-conosciute)

5. [Consuntivo](#5.-consuntivo)

6. [Conclusioni](#6.-conclusioni)

    6.1 [Sviluppi futuri](#6.1-sviluppi-futuri)

    6.2 [Considerazioni personali](#6.2-considerazioni-personali)

7. [Bibliografia](#7.-bibliografia)

    7.1 [Sitografia](#7.1-sitografia)

8. [Allegati](#8.-allegati)

## 1. Introduzione
---

### 1.1 Informazioni sul progetto

  Il progetto è gestito e realizzato dagli allievi Luca Di Bello, Fadil Smajlbasic, Jari Näser, Andrea Rauso (studenti di informatica all’Arti e Mestieri di Trevano) sotto la supervisione del professor Luca Muggiasca. Abbiamo a disposizione un dispositivo chiamato "Leap Motion", il quale permette il tracking di precisione delle mani e dei loro movimenti e il drone DJI Tello sul quale effettueremo tutti i test della nostra libreria.
  La realizzazione del progetto inizia il 13 febbraio 2019 (13/02/19) e dovrà essere consegnato entro il 15 maggio 2019 (15/05/19).

### 1.2 Abstract

  Nowadays drones are really popular in various areas such as personal use and professional environment.
  To make this awesome quadcopters accessible to all ages in a really fun way as a team we decided to create a library that connects a sensor that reads the user's hands motions, transforms them in commands and sends them to the drone.
  With our product it's really easy to fly a drone with your own hand's motions.

### 1.3 Scopo

  Lo scopo di questo progetto è di creare un sistema che permette di controllare il drone *DJI Tello* tramite un dispositivo chiamato [Leap Motion](https://www.leapmotion.com "Leap Motion official website") (un sensore che permette il tracking dei movimenti delle mani in modo preciso). Non disponendo di un drone *DJI Tello* verrà simulato il funzionamento tramite un'altra applicazione la quale mostrerà in 4 riquadri le quali rappresenteranno in 2d le seguenti informazioni: imbardata, beccheggio, rollio ed altitudine.
  Entrambe le applicazioni devono essere scritte nel linguaggio Java ed il drone deve venir controllato utilizzando entrambe le mani. Una mano si occupa dell'altitudine ed altre funzioni utili alla guida mentre l'altra si occupa dei movimenti del drone, ovvero imbardata, beccheggio e rollio.

## Analisi

### 1.4 Analisi del dominio

  Questo genere di prodotto orientato verso entusiasti di aviazione ed informatica
  attualmente esiste solamente progettato e scritto privatamente in vari linguaggi come
  Ruby, Python e PHP ma non ancora in Java.
  ***EVENTUALMENTE AGGIUNGERE TESTO SE NECESSARIO***

### 1.5 Analisi e specifica dei requisiti

  Il committente necessita di un sistema che comprende un simulatore di volo e di un controller. Il simulatore di volo simulerà il funzionamento
  del drone *DJI Tello* mentre il controller si occuperà di controllare il drone all'interno della simulazione utilizzando il controller *Leap Motion*. Il drone verrà controllato con entrambe le mani: Una mano si occuperà di controllare l'altitudine del drone mentre l'altra mano si occuperà dei movimenti del drone, quindi imbardata, rollio e beccheggio.
  I comandi inviati dal controller verso il drone simulato verranno inviati rispettando il protocollo di comunicazione ufficiale fornito da *ryzerobotics.com*.

  |ID  |REQ-001                                         |
  |----|------------------------------------------------|
  |**Nome**    |Controller in Java|
  |**Priorità**|1                     |
  |**Versione**|1.0                   |
  |            |**Sotto requisiti**|
  |**001**      | Controllo del drone tramite Leap Motion |
  |**002**      | Visualizzazione in streaming video riportato dal drone |
  |**003**      | Possibilità di registrazione del volo in modo da poterlo ripetere |
  |**004**      | Deve ottenere tutte le informazioni del drone per poi renderle disponibili in una pagina web sotto forma di statistica |
  |**005**      | Codice ben commentato (Inglese o Italiano)|

  |ID  |REQ-002                                         |
  |----|------------------------------------------------|
  |**Nome**    |Simulazione DJI Tello in Java|
  |**Priorità**|1                     |
  |**Versione**|1.0                   |
  |            |**Sotto requisiti**|
  |**001**      | Visualizzazione in 2d dell'imbardata, beccheggio, rollio ed altitudine del drone |
  |**002**      | Codice ben commentato (Inglese o Italiano)|

  |ID  |REQ-003                                         |
  |----|------------------------------------------------|
  |**Nome**    |File di config per controller|
  |**Priorità**|2                     |
  |**Versione**|1.0                   |
  |            |**Sotto requisiti**|
  |**001**      | Modificabile tramite GUI in modo semplice e veloce |
  |**002**      | Le impostazioni devono venir caricate automaticamente all'avvio del programma |
  |**002**      | Possibilità di manipolare le impostazioni salvate all'interno del file anche tramite codice |
  |**002**      | Codice ben commentato (Inglese o Italiano)|

### 1.6 Pianificazione

Questa è la pianificazione e struttura che abbiamo cercato di rispettare per
tutto il percorso di questo progetto.
![alt Gantt Preventivo](..\media\img\Immagine_Gantt_Preventivo.png)

### 1.7 Analisi dei mezzi

### 1.7.1 Software
Per la realizzazione di questo progetto abbiamo usato come software:
- GitHub 2.20.1: Punto di riferimento per tutto il team sul quale si carica
continuamente il lavoro fatto attraverso commit in un sistema di versioning.
- GitHub Desktop 1.6.5: Programma per effettuare il push e pull di commit
attraverso un'interfaccia grafica.
- Atom 1.36.1: Editore di testo per scrivere principalmente la documentazione e
risolvere conflitti.
- NetBeans 8.2:  IDE per sviluppare tutto il codice scritto in Java.
- VisualStudio Code 1.33.1: Editore di testo usato in tutti i contesti.
- SDK LeapMotion 2.3.1: Libreria che permette alle classi di Java di leggere
i vari movimenti delle mani dal sensore LeapMotion.
- GanttProject 2.8.9: Software per creare una progettazione delle tempistiche
 per il progetto.

### 1.7.2 Hardware
Per poter realizzare questo progetto abbiamo usato il seguente materiale:
- Sensore LeapMotion.
- Drone DJI Tello.
- Apple MacBook Pro 2015 con OSX Mojave.
- Asus VivoBook 2015 con Windows 10.
- Asus ROG GL702VM con Windows 10.
- HP Pavilion CS-0800 con Linux Ubuntu 19.04.


## 2 Progettazione
---

Questo capitolo descrive esaustivamente come deve essere realizzato il
prodotto fin nei suoi dettagli. Una buona progettazione permette
all’esecutore di evitare fraintendimenti e imprecisioni
nell’implementazione del prodotto.

### 2.1 Design dell’architettura del sistema

Nell'immagine sottostante viene rappresentata la struttura delle classi del Controller del drone

![alt DroneController UML](Class%20Diagrams\DroneController.png)

Nell'immagine sottostante viene rappresentata la struttura delle classi del Simulatore del drone

![alt DroneSimulator UML](Class%20Diagrams\DroneSimulator.png)

### 2.2 Schema logico.
Nell'immagine sottostante viene rappresentato lo schema logico di questo progetto.<br>
Sul lato sinistro si può vedere il sensore ed il controller che fanno da client, successivamente i dati rilevati verranno mandati attraverso un socket UDP in Java al Simulatore oppure al Drone stesso.

![alt SchemaLogico Progetto](..\media\img\SchemaLogico.png)

### 2.3 Design delle interfacce

Interfaccia della posizione del drone

![alt Interfaccia posizione](..\media\mockup\MockPosizioneDallAlto.png)

Interfaccia della rotazione del drone
![alt Interfaccia rotazione](..\media\mockup\MockRotazioneAssi.png)

### 2.4 Design procedurale [DA FARE!!!!!]

Descrive i concetti dettagliati dell’architettura/sviluppo utilizzando
ad esempio:

-   Diagrammi di flusso e Nassi.

-   Tabelle.

-   Classi e metodi.

-   Tabelle di routing

-   Diritti di accesso a condivisioni …

Questi documenti permetteranno di rappresentare i dettagli procedurali
per la realizzazione del prodotto.

## 3 Implementazione
---

### 3.1 Drone Controller

### 3.2 Drone Simulator

#### 3.2.1 TelloChartFrame

Questa classe ha lo scopo di mostrare all'interno di un JFrame le informazioni sulla posizione del drone sui 3 assi con una vista di profilo e una vista dall'alto e la rotazione del drone sui 3 assi di rotazione (beccheggio, imbardata e rollio).
La rappresentazione delle informazioni avvengono tramite grafici cartesiani per la posizione e tramite grafico a barre per la rotazione, i grafici sono stati costruiti grazie alla libreria gratuita JFreeChart.



#### 3.2.2 Simulator

La classe Simulator permette di ricevere tutte le richiese e i comandi in entrata sulla porta del socket 8889.
La classe filtra, legge e controlla i comandi in entrata in modo da poter inoltrare il contenuto verso la classe CommandReader.

#### 3.2.3 CommandReader

La classe CommmandReader riceve il metodo richiesto via socket per poi chiamare il rispettivo metodo per simulare nel miglior modo possibile il comportamento del drone.

#### 3.2.4 BatteryThread

La classe BatteryThread monitora e gestisce la durata del volo del drone e la sua batteria.
Quando la batteria del drone equivale allo 0%, questo comincierà automaticamente ad atterare attraverso il metodo emergency() della classe CommandReader.

```java
public class BatteryThread extends Thread{

    /**
     * CommandReader class that manages the drone's movements.
     */
    private CommandReader cr;

    /**
     * Boolean that specifies if the drone should keep flying.
     */
    private boolean keepFlying;

    public BatteryThread(CommandReader cr){
        this.cr = cr;
        keepFlying = true;
    }

    @Override
    public void run(){
        while(keepFlying){
            try{
                if(cr.getBattery() == 0){
                    cr.emergency();
                    keepFlying = false;
                }else{
                    Thread.sleep(1000);
                }
            }catch(InterruptedException ie){
                System.out.println("BatteryThread has been interrupted.");
            }
        }
    }  
}
```

#### 3.2.5 PacketReceivingCheckerThread

La classe PacketReceivingCkerThread controlla la frequenza di ricezione dei comandi della classe Simulator.
Se per 15 secondi non viene ricevuto un qualsiasi pacchetto il drone comincierà automaticamente ad atterrare attraverso il metodo emergency() della classe CommandReader.

```java
public class PacketReceivingCheckerThread extends Thread{

    /**
     * Time to wait before calling CommandReader's emergency() method.
     */
    private final int SECONDS_TO_WAIT = 15;
    /**
     * CommandReader that contains all the methods to control the drone.
     */
    private CommandReader cr;

    public PacketReceivingCheckerThread(CommandReader cr){
        this.cr = cr;
    }

    @Override
    public void run(){
        try{
            long startTime = System.currentTimeMillis();
            while((System.currentTimeMillis() - startTime)/1000 < SECONDS_TO_WAIT){
                Thread.sleep(500);
            }
            cr.emergency();
        }catch(InterruptedException ie){
            System.err.println("PacketReceivingCheckerThread has been interrupted.");
        }
    }  
}
```

## 4 Test
---
### 4.1 Protocollo di test

Definire in modo accurato tutti i test che devono essere realizzati per
garantire l’adempimento delle richieste formulate nei requisiti. I test
fungono da garanzia di qualità del prodotto. Ogni test deve essere
ripetibile alle stesse condizioni.


|Test Case      | TC-001                               |
|---------------|--------------------------------------|
|**Nome**       |Import a card, but not shown with the GUI |
|**Riferimento**|REQ-012                               |
|**Descrizione**|Import a card with KIC, KID and KIK keys with no obfuscation, but not shown with the GUI |
|**Prerequisiti**|Store on local PC: Profile\_1.2.001.xml (appendix n\_n) and Cards\_1.2.001.txt (appendix n\_n) |
|**Procedura**     | - Go to “Cards manager” menu, in main page click “Import Profiles” link, Select the “1.2.001.xml” file, Import the Profile - Go to “Cards manager” menu, in main page click “Import Cards” link, Select the “1.2.001.txt” file, Delete the cards, Select the “1.2.001.txt” file, Import the cards |
|**Risultati attesi** |Keys visible in the DB (OtaCardKey) but not visible in the GUI (Card details) |

### 4.2 Risultati test

Tabella riassuntiva in cui si inseriscono i test riusciti e non del
prodotto finale. Se un test non riesce e viene corretto l’errore, questo
dovrà risultare nel documento finale come riuscito (la procedura della
correzione apparirà nel diario), altrimenti dovrà essere descritto
l’errore con eventuali ipotesi di correzione.

### 4.3 Mancanze/limitazioni conosciute

Descrizione con motivazione di eventuali elementi mancanti o non
completamente implementati, al di fuori dei test case. Non devono essere
riportati gli errori e i problemi riscontrati e poi risolti durante il
progetto.

## 5 Consuntivo
---

Rispetto al diagramma iniziale abbiamo in parte unito le due fasi di progettazione ed
implementazione visto che era un lavoro spesso asincrono per ogni membro del team, di conseguenza non conoscendo ancora la natura di svariati componenti utilizzati per realizzare questo prodotto abbiamo optato per questa soluzione potendo così informarci e lavorare sul progetto contemporaneamente.
Per il resto abbiamo allungato un po' la fase dei test dovendo raffinare ed osservare i movimenti del drone e l'invio dei vari comandi via socket.
![alt Gantt Consuntivo](..\media\img\Immagine_Gantt_Consuntivo.png)

## 6 Conclusioni
---
Quali sono le implicazioni della mia soluzione? Che impatto avrà?
Cambierà il mondo? È un successo importante? È solo un’aggiunta
marginale o è semplicemente servita per scoprire che questo percorso è
stato una perdita di tempo? I risultati ottenuti sono generali,
facilmente generalizzabili o sono specifici di un caso particolare? ecc

### 6.1 Sviluppi futuri
  Migliorie o estensioni che possono essere sviluppate sul prodotto.

### 6.2 Considerazioni personali
  Cosa ho imparato in questo progetto? ecc

## 7 Bibliografia
---

### 7.1 Sitografia

-   http://standards.ieee.org/guides/style/section7.html, *IEEE
    Standards Style Manual*, 13.02.2019 - 10.05.2019.
-   http://www.jfree.org/jfreechart/, *JFreeChart*, 13.02.2019 - 10.05.2019
-   https://github.com/jfree/jfreechart, *A 2D chart library for Java applications (JavaFX, Swing or server-side)*, 13.02.2019 - 10.05.2019
-   http://www.jfree.org/jfreechart/api/javadoc/overview-summary.html, *JFreeChart 1.5.0 API*, 13.02.2019 - 10.05.2019
-   https://stackoverflow.com/, *StackOverflow*, 13.02.2019 - 10.05.2019
- https://www.leapmotion.com/, *Leap Motion*, 13.02.2019 - 10.05.2019.
-   https://www.ryzerobotics.com/tello, *Tello SDK*, 13.02.2019 - 10.05.2019


## 8 Allegati -> DA CONTROLLARE
---
Elenco degli allegati, esempio:

-   Diari di lavoro

-   Codici sorgente/documentazione macchine virtuali

-   Istruzioni di installazione del prodotto (con credenziali
    di accesso) e/o di eventuali prodotti terzi

-   Documentazione di prodotti di terzi

-   Eventuali guide utente / Manuali di utilizzo

-   Mandato e/o Qdc

-   Prodotto

-   …
