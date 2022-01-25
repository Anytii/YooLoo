//start lze
package common;

import java.util.ArrayList;

public class YoolooRegeln {
	private ArrayList<YoolooKarte> gespielteKarten = new ArrayList<>();
	
	/**
	 * Lässt überprüfen, ob eine Karte das zweite mal gelegt wurde. Wenn ja, erhält der Spieler für diesen Zug den Kartenwert von 0 und verliert somit automatisch den Stich.
	 * @param neueKarte
	 * @param clientHandlerId
	 * @param stichNummer
	 * @param isEnabled
	 */
	public void überprüfeRegelDK(YoolooKarte neueKarte, int clientHandlerId, int stichNummer, boolean isEnabled){
		if (isEnabled) {
			testKartenwert(stichNummer, neueKarte);
			if(checkDoppelteKarte(neueKarte))
			{
				System.out.println("[ClientHandler" + clientHandlerId + "] Doppelte Karte " + neueKarte.toString() + " wurde gelegt. Spieler setzt für diese Runde aus!");
				System.out.println("[ClientHandler" + clientHandlerId + "] Die doppelte Karte wurde bereits in Stich " + getStichnummer(neueKarte.getWert()) +  " gelegt.");
				neueKarte.setWert(0);
			} else {
				System.out.println("[ClientHandler" + clientHandlerId + "] Karte empfangen:" + neueKarte);
			}
		}
	}
	
	/**
	 * Es wird überprüft ob eine Karte in den bereits gespielten Karten vorhanden ist.
	 * @param neueKarte
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
	 * Die übergebene neue YoolooKarte wird der Liste der gespielten Karten eines Spielers hinzugefügt.
	 * @param neueKarte
	 */
	public void addGespielteKarte(YoolooKarte neueKarte)
	{
		gespielteKarten.add(neueKarte);
	}
	public int getStichnummer(int wert)
	{
		for (int i = 0; i < gespielteKarten.size(); i++) {
			if(gespielteKarten.get(i).getWert() == wert)
			{
				return i;
			}
		}
		return 999;
	}
	/**
	 * Diese funktion ist zum Testen der überprüfeRegelDK da. Sie setzt einen Wert manuel auf den Kartenwert 2 und soll damit bewirken, dass eine Karte doppel gelegt wird.
	 * @param stichNummer
	 * @param neueKarte
	 */
	public static void testKartenwert(int stichNummer, YoolooKarte neueKarte)
	{
		if(stichNummer == 1)
		{
			neueKarte.setWert(2);
		}
	}	
}
//stop lze