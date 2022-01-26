package server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import common.YoolooKarte;
import common.YoolooKartenspiel;

public class Spielerdaten {
	static List<Map<String, Object>> spielerListe = new ArrayList<>();
	
	
	/*
	 * Liest JSON-Datei aus und mapt diese in eine Liste von Maps.
	 * Maps haben einen String namen und eine Liste<Integer> mit der zuletzt genutzten sortierung
	 */
	public static void datenLaden() {
		try {				
			File data = new File("resources/data.json");
			ObjectMapper mapper = new ObjectMapper();
			spielerListe = mapper.readValue(data, new TypeReference<List<Map<String, Object>>>(){});
		} catch(Exception e) {}
	}
	
	/* PARAMS: name und Farbe eines Spielers
	 * RETURNS: YoolooKarte[] mit übergebener Farbe und der, von diesem Speiler, zuletzt genutzen Sortierung
	 * 			null, wenn keine Sortierung gespeichert
	 */
	public static YoolooKarte[] getSortierungFuerSpieler(String name, YoolooKartenspiel.Kartenfarbe farbe) {
		YoolooKarte[] sortierung = null;
		for(Map<String, Object> spielerMap: spielerListe) {
			if(name.equals(spielerMap.get("name")))
				sortierung =  getSortierungFuerWerte((List<Integer>)spielerMap.get("sortierung"), farbe);
		}
		return sortierung;
	}
	
	/* PARAMS: Liste mit Werten der zuletzt genutzten Sortierung und Aktuelle Farbe des Spielers
	 * RETURNS YoolooKarte[] mit aktueller Farbe und der Sortierung, die beim letzten mal genutzt wurde
	 */
	private static YoolooKarte[] getSortierungFuerWerte(List<Integer> werte, YoolooKartenspiel.Kartenfarbe farbe) {
		if(werte == null || werte.size() == 0)
			return null;
		YoolooKarte[] sortierung = new  YoolooKarte[werte.size()];
		for(int i=0; i<werte.size(); i++) {
			sortierung[i] = new YoolooKarte(farbe, werte.get(i));
		}
		return sortierung;
	}
	
	/* PARAMS: Name und aktuelle Sortierung eines Spielers
	 * Ist bereits ein Spieler mit diesem Namen in der Liste,
	 * wird die für diesen Speiler gespeicherte Sortieung überschrieben.
	 * Ist noch kein Spieler mit diesem Namen in der Liste,
	 * wird ein neuer Eintrag fuer diesen Spieler gespeichert.
	 * 
	 * Anschließend wird die Liste in einer JSON-Datei gespeichert.
	 * */
	public static void updateSpielerData(String name, List<Integer> aktuelleSortierung) {
		// TODO Auto-generated method stub
		boolean updated = false;
		for(Map<String, Object> spielerMap: spielerListe) {
			if(name.equals(spielerMap.get("name"))) {
				spielerMap.put("sortierung",aktuelleSortierung);
				updated = true;
			}
		}
		if(!updated) {
			Map<String, Object> spielerMap = new HashMap<>();
			spielerMap.put("name", name);
			spielerMap.put("sortierung", aktuelleSortierung);
			spielerListe.add(spielerMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		try {
			writer.writeValue(new File("resources/data.json"), spielerListe);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
