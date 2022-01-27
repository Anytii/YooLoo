package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import common.YoolooKarte;
import common.YoolooKartenspiel;

/**
* Laedt und Speichert zuletzt genutzte Sortierungen fuer Spieler.
* 
* @author Kevin Strick
* 
*/
public class Spielerdaten {
	/**
	 * Liste von Maps, fuer alle Spieler, die schon ein Spiel gespielt haben, die zuletzt genutzte Sortierung beinhaltet.
	 */
	static List<Map<String, Object>> spielerDaten = new ArrayList<>();
	/**
	 * Dateipfad der JSON-Datei, in der die Datne gespeichert werden.
	 */
	static String path = "resources/data.json";
	
	/**
	 * Liest JSON-Datei aus und mapt diese in eine Liste von Maps.
	 * Diese Maps haben den Namen eines Spielers als String und eine Liste von Integern,
	 * die die zuletzt genutzte Sortierung dieses Spielers darstellt.
	 * 
	 * @throws StreamReaderException
	 * @throws DatabindException
	 * @throws IOException
	 **/
	public static void datenLaden() {
					
			File data = new File(path);
			ObjectMapper mapper = new ObjectMapper();
			try {
				spielerDaten = mapper.readValue(data, new TypeReference<List<Map<String, Object>>>(){});
			} catch (StreamReadException e) {
				e.printStackTrace();
			} catch (DatabindException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
	}
	
	/** 
	 * Liefet zu einem Spielernamen die zuletzt genutzte Sortierung als Array von YooLooKarten
	 * 
	 * @param name Name des Spielers, fuer den man eine Sortierung anfragt
	 * @param farbe KartenFarbe des Spielers, die er im aktuellen Spiel hat
	 * 
	 * @return: YoolooKarte[] Liefert ein Array von YooLooKarten, wenn eine Sortierung gespeichert war,
	 * 							lieft null, wenn keine Sortierung gespeichert war.
	 **/
	public static YoolooKarte[] getSortierungFuerSpieler(String name, YoolooKartenspiel.Kartenfarbe farbe) {
		YoolooKarte[] sortierung = null;
		for(Map<String, Object> spielerMap: spielerDaten) {
			if(name.equals(spielerMap.get("name")))
				sortierung =  getSortierungFuerWerte((List<Integer>)spielerMap.get("sortierung"), farbe);
		}
		return sortierung;
	}
	
	/** Baut aus einer Liste von Integern und einer Farbe eine Sortierung,
	 * bei der die Reihenfolge der Kartenwerte die gleiche Reihenfole haben, wie die Liste von Integern.
	 * 
	 * @param werte Reihenfolge der Kartenwerte, die von einem Spieler in der letzten Partie genutzt wurde.
	 * @param farbe KartenFarbe, die der Spieler im aktuellen Spiel hat.
	 * 
	 * @return YoolooKarte[]: Ein Array von YooLooKarten mit der aktuelle Farbe und in der gleichen Reihenfolge,
	 * 						  die in der uebergebenen Liste uebergeben wurde
	 **/
	private static YoolooKarte[] getSortierungFuerWerte(List<Integer> werte, YoolooKartenspiel.Kartenfarbe farbe) {
		if(werte == null || werte.size() == 0 || farbe == null)
			return null;
		YoolooKarte[] sortierung = new  YoolooKarte[werte.size()];
		for(int i=0; i<werte.size(); i++) {
			sortierung[i] = new YoolooKarte(farbe, werte.get(i));
		}
		return sortierung;
	}
	
	/** Speichert einen Spielernamen und die Reihenfolge von Kartenwerten in den 
	 * SpielerDaten-Liste. Ist bereits eine Sortierung fuer diesen Namen gespeichert, wird die gespeicherte Sortierung ueberschrieben.
	 * Anschließend wird die Liste in einer JSON-Datei gespeichert.
	 * 
	 * @param name Namen des Spielers, fuer den eine Sortierung gespeichert werden soll.
	 * @param aktuelleSortierung Reihenfolge von Kartenwerten, die aks Sortierung gespeichert werden soll.
	 *
	 * @throws StreamReaderException
	 * @throws DatabindException
	 * @throws IOException
	 **/
	public static void updateSpielerData(String name, List<Integer> aktuelleSortierung) {
		if(name == null || aktuelleSortierung == null)
			return;
		
		boolean updated = false;
		for(Map<String, Object> spielerMap: spielerDaten) {
			if(name.equals(spielerMap.get("name"))) {
				spielerMap.put("sortierung",aktuelleSortierung);
				updated = true;
			}
		}
		if(!updated) {
			Map<String, Object> spielerMap = new HashMap<>();
			spielerMap.put("name", name);
			spielerMap.put("sortierung", aktuelleSortierung);
			spielerDaten.add(spielerMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		try {
			writer.writeValue(new File(path), spielerDaten);
		} catch (StreamWriteException e) {
			e.printStackTrace();
		} catch (DatabindException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}