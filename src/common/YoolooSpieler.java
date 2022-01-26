// History of Change
// vernr    |date  | who | lineno | what
//  V0.106  |200107| cic |    -   | add history of change

package common;

import java.io.Serializable;
import java.util.Arrays;

import common.YoolooKartenspiel.Kartenfarbe;

public class YoolooSpieler implements Serializable {

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
		YoolooKarte[] neueSortierung = new YoolooKarte[this.aktuelleSortierung.length];
		for (int i = 0; i < neueSortierung.length; i++) {
			int neuerIndex = (int) (Math.random() * neueSortierung.length);
			while (neueSortierung[neuerIndex] != null) {
				neuerIndex = (int) (Math.random() * neueSortierung.length);
			}
			neueSortierung[neuerIndex] = aktuelleSortierung[i];
			// System.out.println(i+ ". neuerIndex: "+neuerIndex);
		}
		aktuelleSortierung = neueSortierung;
		if(sollCheaten) {
			cheaten();
		}
	}
	
	/**
	 * Es wird ein Kartenwert der aktuellen Sortierung unter 4 mit einem zufälligen Wert zwischen zwischen 8-10 ausgetauscht.
	 * Somit wird eine Karte doppelt ausgespielt.
	 */
	public void cheaten()
	{		
		for(int i = 0; i < aktuelleSortierung.length; i++)
		{
			if(aktuelleSortierung[i].getWert() <=3)
			{
				int neuerWert = 7 + (int)(Math.random() * ((10 - 7) + 1));
				aktuelleSortierung[i].setWert(neuerWert);
				break;			
			}
		}
	}

	public int erhaeltPunkte(int neuePunkte) {
		System.out.print(name + " hat " + punkte + " P - erhaelt " + neuePunkte + " P - neue Summe: ");
		this.punkte = this.punkte + neuePunkte;
		System.out.println(this.punkte);
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
	}

	public void stichAuswerten(YoolooStich stich) {
		System.out.println(stich.toString());

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
