// History of Change
// vernr    |date  | who | lineno | what
//  V0.106  |200107| cic |    -   | add history of change

package common;

import java.util.ArrayList;
import java.util.logging.Logger;


public class YoolooKartenspiel {

	private static Logger LOGGER = new logging.Logging(YoolooKartenspiel.class.getName()).getLogger();
	
	public enum Kartenfarbe {
		Gelb, Rot, Gruen, Blau, Orange, Pink, Violett, Tuerkis
	}

	private String Spielname = "Yooloo";

	public final static int minKartenWert = 1;
	public final static int maxKartenWert = 10;

	protected YoolooKarte[][] spielkarten;
	protected int anzahlFarben = YoolooKartenspiel.Kartenfarbe.values().length;
	protected int anzahlWerte = maxKartenWert;
	ArrayList<YoolooSpieler> spielerliste = new ArrayList<YoolooSpieler>();


	
	/**
	 * Erstellen einer neuen Spielumgebung Definition des Spielnamens der
	 * Spielkarten
	 */
	public YoolooKartenspiel() {

		setSpielname("Yooloo" + System.currentTimeMillis());
		LOGGER.info("[YoolooKartenSpiel] Spielname: " + getSpielname()); // TODO evtl loeschen

		spielerliste.clear();
		spielkarten = new YoolooKarte[anzahlFarben][anzahlWerte];

		for (int farbe = 0; farbe < anzahlFarben; farbe++) {
			for (int wert = 0; wert < anzahlWerte; wert++) {
				spielkarten[farbe][wert] = new YoolooKarte(Kartenfarbe.values()[farbe], (wert + 1));
			}
		}
		LOGGER.info("Je " + anzahlWerte + " Spielkarten fuer " + anzahlFarben + " Spieler zeugt");
	}

	public void listeSpielstand() {
		if (spielerliste.isEmpty()) {
			LOGGER.info("(Noch) Keine Spieler registriert");
		} else {
			for (YoolooSpieler yoolooSpieler : spielerliste) {
				LOGGER.info(yoolooSpieler.toString());
			}
		}

	}

	/**
	 * Luesst einen neuen Spieler an dem Spiel teilnehmen /Fuer lokale Simulationsmit
	 * mit Zuordnung der Farbe
	 * 
	 * @param name
	 * @return
	 */
	public YoolooSpieler spielerRegistrieren(String name) {
		YoolooSpieler neuerSpieler = new YoolooSpieler(name, maxKartenWert);
		Kartenfarbe[] farben = Kartenfarbe.values();
		neuerSpieler.setSpielfarbe(farben[spielerliste.size()]);
		YoolooKarte[] spielerkarten = spielkarten[spielerliste.size()];
		neuerSpieler.setAktuelleSortierung(spielerkarten);
		this.spielerliste.add(neuerSpieler);
		LOGGER.info("Debug; Spieler " + name + " registriert als : " + neuerSpieler);
		return neuerSpieler;
	}

	/**
	 * Luesst ein YoolooSpieler aus ClientServer Variante an dem Spiel teilnehmen Der
	 * Spieler erhuelt die Farbe korrespondierend zur ClientHandlerID
	 * 
	 * @param spielerName
	 * @return
	 */
	public YoolooSpieler spielerRegistrieren(YoolooSpieler neuerSpieler) {
		Kartenfarbe[] farben = Kartenfarbe.values();
		neuerSpieler.setSpielfarbe(farben[neuerSpieler.getClientHandlerId()]);
		YoolooKarte[] kartenDesSpielers = spielkarten[neuerSpieler.getClientHandlerId()];
		neuerSpieler.setAktuelleSortierung(kartenDesSpielers);
		this.spielerliste.add(neuerSpieler); // nur fuer Simulation noetig!
		LOGGER.info("Debug; Spielerobject registriert als : " + neuerSpieler);
		return neuerSpieler;
	}

	@Override
	public String toString() {
		return "YoolooKartenspiel [anzahlFarben=" + anzahlFarben + ", anzahlWerte=" + anzahlWerte + ", getSpielname()="
				+ getSpielname() + "]";
	}

	// nur fuer Simulation / local
	public void spielerSortierungFestlegen() {
		for (int i = 0; i < spielerliste.size(); i++) {
			spielerliste.get(i).sortierungFestlegen();
		}
	}

	// nur fuer Simulation / local
	public void spieleRunden() {
		// Schleife ueber Anzahl der Karten
		for (int i = 0; i < anzahlWerte; i++) {
			LOGGER.info("Runde " + (i + 1));
			// Schleife ueber Anzahl der Spieler
			YoolooKarte[] stich = new YoolooKarte[spielerliste.size()];

			for (int j = 0; j < spielerliste.size(); j++) {
				YoolooKarte aktuelleKarte = spielerliste.get(j).getAktuelleSortierung()[i];
				stich[j] = aktuelleKarte;
				LOGGER.info(spielerliste.get(j).getName() + " spielt " + aktuelleKarte.toString());
			}
			int stichgewinner = berechneGewinnerIndex(stich);
			if (stichgewinner>=0) {
				spielerliste.get(stichgewinner).erhaeltPunkte(i + 1);
			}
			
		}
	}

	public int berechneGewinnerIndexV1_Buggy(YoolooKarte[] karten) {

		int limitWert = maxKartenWert + 1;
		int maxWert = 0;
		int anzahlKartenMitMaxWert = 0;
		for (int i = 0; i < karten.length; i++) {
			LOGGER.info(i + ":" + karten[i].getWert() + " \n");
		}
		while (anzahlKartenMitMaxWert != 1) {
			maxWert = 0;
			for (int i = 0; i < karten.length; i++) {
				YoolooKarte yoolooKarte = karten[i];
				if (maxWert < yoolooKarte.getWert() && yoolooKarte.getWert() < limitWert) {
					maxWert = yoolooKarte.getWert();
				}
			}
			anzahlKartenMitMaxWert = 0;
			for (int i = 0; i < karten.length; i++) {
				YoolooKarte yoolooKarte = karten[i];
				if (maxWert == yoolooKarte.getWert()) {
					anzahlKartenMitMaxWert++;
					limitWert = yoolooKarte.getWert();
				}
			}
			if (limitWert == 0 && anzahlKartenMitMaxWert == 0) {
				return -1;
			}
		}
		int gewinnerIndex = -1;
		for (int i = 0; i < karten.length; i++) {
			YoolooKarte yoolooKarte = karten[i];
			if (yoolooKarte.getWert() == maxWert) {
				gewinnerIndex = i;
			}
		}
		return gewinnerIndex;
	}

	public int berechneGewinnerIndex(YoolooKarte[] karten) {
		int maxwert = 0;
		for (int i = 0; i < karten.length; i++) {
			LOGGER.info(i + ":" + karten[i].getWert() + " ");
			if (maxwert < karten[i].getWert())
				maxwert = karten[i].getWert();
		}
		int gewinnerIndex = -1;
		while (maxwert > 0) {
			int anzahlKartenMitMaxwert = 0;
			for (int i = 0; i < karten.length; i++) {
				if (karten[i].getWert() == maxwert) {
					anzahlKartenMitMaxwert++;
					gewinnerIndex = i;
				}
			}
			if (anzahlKartenMitMaxwert != 1) {
				maxwert--;
				gewinnerIndex = -1;

			} else {
				LOGGER.info("gewinnerIndex: " + gewinnerIndex);
				return gewinnerIndex;
			}
		}
		LOGGER.info("Kein gewinnerIndex: ermittelt" + gewinnerIndex);
		return gewinnerIndex;
	}
	
   public String getSpielname() {
   return Spielname;
}

public void setSpielname(String spielname) {
   Spielname = spielname;
}
 

}
