package common;

import logging.Logging;
import server.YoolooServer;

import java.util.ArrayList;

public class YoolooRegeln {
	private ArrayList<YoolooKarte> gespielteKarten = new ArrayList<>();

	private static final transient Logging LOGGER = new logging.Logging(YoolooRegeln.class.getName());
	
	/**
	 * Die erste if Bedingungen fragt ab, ob der Server die Kontrolle der Regel eingestellt hat. (YoolooServer.RulesEnabled)
	 * Dann wird �berpr�ft, ob eine Karte das zweite mal gelegt wurde. 
	 * Wenn ja, erh�lt der Spieler f�r diesen Zug den Kartenwert von 0 und verliert somit automatisch den Stich.
	 * Wenn nicht geht es normal weiter.
	 * @param neueKarte ist die neue Spielkarte, die vom Client �bergeben wurde.
	 * @param clientHandlerId
	 * @param stichNummer ist der Rundennummer f�r den aktuellen Stich.
	 * @param isEnabled ist ein Boolean, welcher festlegt, ob die Regeleinhaltung ausgef�hrt werden soll. 
	 */
	public void ueberpruefeRegelDK(YoolooKarte neueKarte, int clientHandlerId, int stichNummer, boolean isEnabled, YoolooSpieler meinSpieler){
		if (isEnabled) {
			if(checkDoppelteKarte(neueKarte))
			{
				LOGGER.log("[ClientHandler" + clientHandlerId + "] Doppelte Karte " + neueKarte.toString() + " wurde gelegt. Spieler setzt f�r diese Runde aus!");
				LOGGER.log("[ClientHandler" + clientHandlerId + "] Die doppelte Karte wurde bereits in Stich " + getStichnummer(neueKarte.getWert()) +  " gelegt.");
				neueKarte.setWert(0);
				meinSpieler.increaseCheatversuche();
				LOGGER.log("[ClientHandler" + clientHandlerId + "] Das war der " + meinSpieler.getCheatversuche() + ". Cheatversuch des Spielers! ");
			} else {
				LOGGER.log("[ClientHandler" + clientHandlerId + "] Karte empfangen:" + neueKarte);
			}
		}
	}
	
	/**
	 * Es wird �berpr�ft ob eine Karte in den bereits gespielten Karten vorhanden ist.
	 * @param neueKarte ist die neue Spielkarte, die vom Client �bergeben wurde.
	 * @return True wenn die Karte bereits vorhanden ist, false wenn nicht.
	 */
	public boolean checkDoppelteKarte(YoolooKarte neueKarte)
	{
		boolean doppelt = false;			
		for (int i = 0; i < gespielteKarten.size(); i++) {
			if(neueKarte.getWert() == gespielteKarten.get(i).getWert())
			{
				doppelt = true;
			}
		}		
		return doppelt;
	}
	/**
	 * Die �bergebene neue YoolooKarte wird der Liste der gespielten Karten eines Spielers hinzugef�gt.
	 * @param neueKarte ist die neue Spielkarte, die vom Client �bergeben wurde.
	 */
	public void addGespielteKarte(YoolooKarte neueKarte)
	{
		gespielteKarten.add(neueKarte);
	}
	/**
	 * Gibt die Nummer des Stichs zur�ck, in welchem bereits die entsprechende doppelte Karte gelegt wurde.
	 * @param wert ist der Wert einer Karte f�r den der gespielte Stich herausgesucht werden soll.
	 * @return Stichnummer
	 */
	public int getStichnummer(int wert)
	{
		int stichnummer = -1;
		
		for (int i = 0; i < gespielteKarten.size(); i++) {
			if(gespielteKarten.get(i).getWert() == wert)
			{
				stichnummer = i;
			}
		}
		return stichnummer;
	}
}