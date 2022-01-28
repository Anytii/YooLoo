// History of Change
// vernr    |date  | who | lineno | what
//  V0.106  |200107| cic |    -   | add history of change

package common;

import java.io.Serializable;
import java.util.Arrays;
import java.util.logging.Logger;

import common.YoolooKartenspiel.Kartenfarbe;
import logging.Logging;

public class YoolooSpieler implements Serializable {

	private static final transient Logging LOGGER = new logging.Logging(YoolooSpieler.class.getName());


    private static final long serialVersionUID = 376078630788146549L;
    private String name;
    private Kartenfarbe spielfarbe;
    private int clientHandlerId = -1;
    private int punkte;
    private YoolooKarte[] aktuelleSortierung;
    private int Cheatversuche;
    private boolean sollCheaten;

    public YoolooSpieler(String name, int maxKartenWert) {
        this.name = name;
        this.punkte = 0;
        this.spielfarbe = null;
        this.aktuelleSortierung = new YoolooKarte[maxKartenWert];
      	this.Cheatversuche = 0;
      	this.sollCheaten = false;
    }


    // Sortierung wird zufuellig ermittelt
    public void sortierungFestlegen() {
        if (constants.Constants.getAutoStrat()) {
            aktuelleSortierung = strategies.Strategies.genStratRandom(this.aktuelleSortierung, this.name);
        } else {
            aktuelleSortierung = strategies.Strategies.genStratAI(this.aktuelleSortierung, this.name);
        }
        if(sollCheaten){
          cheaten();
        }
    }
  
    /**
	   * Es wird ein Kartenwert der aktuellen Sortierung unter 4 mit einem zuf�lligen Wert zwischen zwischen 8-10 ausgetauscht.
	   * Somit wird eine Karte doppelt ausgespielt.
	   */
	  public void cheaten()
  	{		
		  for(int i = 0; i < aktuelleSortierung.length; i++)
		  {
		  	if(aktuelleSortierung[i].getWert() <=3)
		  	{
			  	int neuerWert = 8 + (int)(Math.random() * ((10 - 8) + 1));
			  	aktuelleSortierung[i].setWert(neuerWert);
		  		break;			
		  	}
		  }
	  }


    public int erhaeltPunkte(int neuePunkte) {
	      int tmppunkt = this.punkte;
        this.punkte = this.punkte + neuePunkte;
		LOGGER.log(name + " hat " + tmppunkt + " P - erhaelt " + neuePunkte + " P - neue Summe: "+this.punkte+" P");
		return this.punkte;
	}

    @Override
    public String toString() {
        return "YoolooSpieler [name=" + name + ", spielfarbe=" + spielfarbe + ", puntke=" + punkte
                + ", altuelleSortierung=" + Arrays.toString(aktuelleSortierung) + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Kartenfarbe getSpielfarbe() {
        return spielfarbe;
    }

    public void setSpielfarbe(Kartenfarbe spielfarbe) {
        this.spielfarbe = spielfarbe;
    }

    public int getClientHandlerId() {
        return clientHandlerId;
    }

    public void setClientHandlerId(int clientHandlerId) {
        this.clientHandlerId = clientHandlerId;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int puntke) {
        this.punkte = puntke;
    }

    public YoolooKarte[] getAktuelleSortierung() {
        return aktuelleSortierung;
    }

    public void setAktuelleSortierung(YoolooKarte[] aktuelleSortierung) {
        this.aktuelleSortierung = aktuelleSortierung;
        if(sollCheaten) {
        	cheaten();
        }
    }

    public void stichAuswerten(YoolooStich stich) {
        LOGGER.log(stich.toString());
    }
  
    public void increaseCheatversuche()
	  {
		  Cheatversuche++;
	  }
	
	  public int getCheatversuche()
	  {
		  return Cheatversuche;
	  }
	
	  public void setSollCheaten(boolean neuSollCheaten)
	  {
		  sollCheaten = neuSollCheaten;
	  }
}
