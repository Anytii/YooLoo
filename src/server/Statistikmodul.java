package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.*;
import java.util.List;
import common.YoolooSpieler;

public class Statistikmodul {
	String path = ("resources/json/");
	private ArrayList<YoolooClientHandler> Handler = new ArrayList<>();

	/**
	 * In dieser Methode wird die Spiel Historie erstellt, falls diese noch nicht
	 * vorhanden sein sollte.
	 */
	public void create_history() {
		File file = new File(path + "history.json");
		if (file.exists() == false) {
			try {
				FileWriter writer = new FileWriter(path + "history.json");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * In dieser Methode wird die Spielhistorie geupdated und danach die Methode
	 * create_stats aufgerufen um aus der Spielhistorie eine Statistik zu generieren
	 * 
	 * @param Clienthandlerlist Liste von YoolooClienthandler
	 */
	public void update_history(ArrayList<YoolooClientHandler> Clienthandlerlist) {
		try {
			for(int i=0; i<Clienthandlerlist.size(); i++) {
				while (Clienthandlerlist.get(i).getPlayer() == null) {
					Thread.sleep(5000);
				}
			}
			sortClienthandlerbyPlace(Clienthandlerlist);
			JSONParser jsonParser = new JSONParser();
			int matchnr = 0;
			JSONArray json = new JSONArray();
			JSONObject obj = new JSONObject();
			JSONObject last_entry = new JSONObject();
			File file = new File(path + "history.json");
//			Auslesen der vorhandenden Einträge um die Matchnr zu bestimmen und übernehem der alten Daten in das neue JSON Objekt
			if (file.exists() == true) {
				obj = (JSONObject) jsonParser.parse(new FileReader(path + "/history.json"));
				json = (JSONArray) obj.get("matches");
				last_entry = (JSONObject) json.get(json.size() - 1);
				matchnr = Integer.parseInt(last_entry.get("Nr").toString());

			} else {
				create_history();
			}
//			Füllen des JSON Objekt mit relevanten Daten
			JSONObject new_entry = new JSONObject();
			new_entry.put("Nr", matchnr + 1);
			new_entry.put("Gewinner", getWinner(Clienthandlerlist));
			JSONArray players = new JSONArray();
			for (int i = 0; i < Clienthandlerlist.size(); i++) {
				JSONObject player_json = new JSONObject();
				YoolooSpieler player = Clienthandlerlist.get(i).getPlayer();
				if (player != null) {
					player_json.put("Farbe", player.getSpielfarbe().toString());
					player_json.put("Punkte", player.getPunkte());
					player_json.put("Name", player.getName());
					players.add(player_json);
				}
			}
//			Sammeln der der Daten im JSON Objekt um diese als String in die Datei zu schreiben.
			new_entry.put("Spieler", players);
			json.add(new_entry);
			obj.put("matches", json);
			FileWriter writer = new FileWriter(path + "/history.json");
			writer.write(obj.toJSONString());
			writer.close();
			for (int i = 0; i < Clienthandlerlist.size(); i++) {
				if(Clienthandlerlist.get(i).getPlayer()!=null) {
					create_stats(Clienthandlerlist.get(i).getPlayer().getName());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	};

	/**
	 * In dieser Methode wird die Statistik erstellt bzw. geupdatet.
	 * 
	 * @param Player Name des Spielers für den die Statisktik geupdated werden soll.
	 */
	public void create_stats(String Player) {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject history = (JSONObject) jsonParser.parse(new FileReader(path + "/history.json"));
			JSONObject stats_obj = new JSONObject();
			JSONArray stats_array = new JSONArray();
			JSONArray array_to_push = (JSONArray) new JSONArray();
			File file = null;
			file = new File(path + "/stats.json");
			FileWriter writer = null;
			ArrayList<String> Spieler = new ArrayList<>();
//			setzen von Spielernamen nach Platzierung im Array Spieler.
			for (int i = 0; i < Handler.size(); i++) {
				Spieler.add(Handler.get(i).getPlayer().getName());
			}
//			Prüfen ob es die Datei bereits gibt
			if (file.exists() == true) {
				stats_obj = (JSONObject) jsonParser.parse(new FileReader(path + "/stats.json"));
			} else {
			}
//			Holen der Arrays im JSON
			stats_array = (JSONArray) stats_obj.get("stats");
			JSONArray matches = (JSONArray) history.get("matches");
			JSONObject found_player = null;
//			Falls es bereits eine JSON gibt, wird diese nach dem Spieler, welche in die Methode gegeben wurde durchsucht.
//			Dabei werden alle Spieler, die nicht in der aktuellen Session sind in das final Array übertragen, damit diese am Ende auch beibehalten werden.
			if (stats_array != null) {
				for (int i = 0; i < stats_array.size(); i++) {
					JSONObject find_player = (JSONObject) stats_array.get(i);
					if (find_player.get("Name").equals(Player)) {
						found_player = new JSONObject();
						found_player = (JSONObject) stats_array.get(i);
					} else {
						if (!Spieler.contains(find_player.get("Name")) || !find_player.get("Name").equals(Player)) {
							array_to_push.add(find_player);
						}
					}
				}
			}
//			Variabeln, welche später in das JSON übertragen werden.
			int placement = 0;
			int total_spiele = 0;
			int total_punkte = 0;
			int total_players = 0;
			int total_wins = 0;
			double relative_wins = 0;
			double average_points = 0;
			double average_placement = 0;
			int highscore_points = 0;
			int highscore_place = 20;
			int total_palcements = 0;
			int last_played = 0;
//			Falls es den gesuchten Spieler schon gibt werden seine Statistiken gelesen.
			if (found_player != null) {
				total_spiele = Math.toIntExact((Long) found_player.get("totale_Spiele"));
				total_punkte = Math.toIntExact((Long) found_player.get("totale_Punkte"));
				total_players = Math.toIntExact((Long) found_player.get("totale_Spieler"));
				total_wins = Math.toIntExact((Long) found_player.get("totale_siege"));
				last_played = Math.toIntExact((Long) found_player.get("letzes_Spiel"));
				highscore_place = Math.toIntExact((Long) found_player.get("best_placement"));
				total_palcements = Math.toIntExact((Long) found_player.get("total_plätze"));
			}
			for (int i = 0; i < Handler.size(); i++) {
				if (Player.equals(Handler.get(i).getPlayer().getName()))
					placement = i + 1;
			}
			JSONObject cur_match = new JSONObject();
			JSONObject player_stats = new JSONObject();
//			Hier wird zuerst über die Spielhistory geloopt, dann werde die Spieler für jedes Spiel gelesen und falls der gesuchte Spieler in diesem Spiel war,
//			würd geprüft ob diese Match bereits in der Statistik ist. Falls das nicht der Fall sein sollte wird die Statistik entsprechend aktualisiert 
			for (int i = 0; i < matches.size(); i++) {
				cur_match = (JSONObject) matches.get(i);
				JSONArray players = (JSONArray) cur_match.get("Spieler");
				for (int j = 0; j < players.size(); j++) {
					JSONObject player = (JSONObject) players.get(j);
					String test = (String) player.get("Name");
					if (Player.equals(test)) {
						if (Math.toIntExact((Long) cur_match.get("Nr")) > last_played) {
							player_stats.put("letzes_Spiel", cur_match.get("Nr"));
							total_palcements = total_palcements + placement;
							player_stats.put("total_plätze", total_palcements);
							total_spiele++;
							player_stats.put("totale_Spiele", total_spiele);
							total_players = total_players + (int) players.size();
							player_stats.put("totale_Spieler", total_players);
							double avg_player = (double) total_spiele / (double) total_players;
							player_stats.put("durchschnitt_Spieler_pro_runde", (double) (avg_player));
							String winner = (String) cur_match.get("Gewinner");
							if (winner.equals(Player)) {
								total_wins++;
							}
							int int_punkte = Math.toIntExact((Long) player.get("Punkte"));
							player_stats.put("totale_siege", total_wins);
							total_punkte = total_punkte + int_punkte;
							player_stats.put("totale_Punkte", total_punkte);
							average_points = (double) total_punkte / (double) total_spiele;
							player_stats.put("durschnit_Punkte", average_points);
							if (highscore_points < int_punkte) {
								highscore_points = int_punkte;
							}
							if (highscore_place > placement) {
								player_stats.put("best_placement", placement);
							} else {
								player_stats.put("best_placement", highscore_place);
							}
							average_placement = (double) total_spiele / (double) total_palcements;
							player_stats.put("average_place", average_placement);
							relative_wins = (double) total_wins / (double) total_spiele;
							player_stats.put("relative_wins", relative_wins);
							player_stats.put("highscore", highscore_points);
							player_stats.put("Name", player.get("Name"));
						}
					}
				}
			}
//			Hier wird das erzeugte JSON Objekt in die eine Datei mit dem Namen stats geschrieben.
			JSONObject result = new JSONObject();
			array_to_push.add(player_stats);
			result.put("stats", array_to_push);
			writer = new FileWriter(path + "/stats.json");
			writer.write(result.toJSONString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Diese Methode sortiert die Clienthandler nach der Punktzahl die jeder Spieler erreicht hat.
	 * @param Clienthandlerlist Liste von YoolooClientHandler
	 */
	public void sortClienthandlerbyPlace(ArrayList<YoolooClientHandler> Clienthandlerlist) {
		List<Integer> score = new ArrayList<>();
		ArrayList<YoolooClientHandler> player_map = new ArrayList<>();
		for (int i = 0; i < Clienthandlerlist.size(); i++) {
			if(Clienthandlerlist.get(i).getPlayer()!=null) {
				score.add(Clienthandlerlist.get(i).getPlayer().getPunkte());
			}
		}
		Collections.sort(score, Collections.reverseOrder());
		for (int j = 0; j < score.size(); j++) {
			for (int i = 0; i < Clienthandlerlist.size(); i++) {
				if((Clienthandlerlist.get(i).getPlayer()!=null)) {
					if (Clienthandlerlist.get(i).getPlayer().getPunkte() == score.get(j)) {
						player_map.add(Clienthandlerlist.get(i));
					}
				}
			}

		}
		Handler = player_map;
	}

	/**
	 * Diese Methode ist dazu da die wichtigen Informationen der generiert json in
	 * einem gut leserlichen Format auszugeben.
	 * 
	 * @param Player Playername ist der Name des Spieler, dessen Statistik angezeigt
	 *               werden soll.
	 */
	public void read_json(String Player) {
		try {
			JSONParser jsonParser = new JSONParser();
			JSONObject stats_obj = new JSONObject();
			File file = null;
			file = new File(path + "/stats.json");
			if (file.exists() == true) {
				stats_obj = (JSONObject) jsonParser.parse(new FileReader(path + "/stats.json"));
			}
			JSONArray stats_array = new JSONArray();
			stats_array = (JSONArray) stats_obj.get("stats");
			JSONObject found_player = null;
			if (stats_array != null) {
				for (int i = 0; i < stats_array.size(); i++) {
					JSONObject find_player = (JSONObject) stats_array.get(i);
					if (find_player.get("Name").equals(Player)) {
						found_player = new JSONObject();
						found_player = (JSONObject) stats_array.get(i);
					}
				}
			}
			System.out.print("Spieler:");
			System.out.println((String) found_player.get("Name"));
			System.out.print("Gespielte Spiele:");
			System.out.println("" + found_player.get("totale_Spiele"));
			System.out.print("Avg. Spieler/Spiel:");
			System.out.println("" + found_player.get("durchschnitt_Spieler_pro_runde"));
			System.out.print("Gewonnene Punkte Gesamt:");
			System.out.println("" + found_player.get("totale_Punkte"));
			System.out.print("Avg. Punkte/Spiel:");
			System.out.println("" + found_player.get("durschnit_Punkte"));
			System.out.print("Highscore Punkte:");
			System.out.println("" + found_player.get("highscore"));
			System.out.print("Highscore Platz:");
			System.out.println("" + found_player.get("best_placement"));
			System.out.print("Totale Siege:");
			System.out.println("" + found_player.get("totale_siege"));
			System.out.print("Avg. Platz:");
			System.out.println("" + found_player.get("average_place"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Hier wird geprüft welcher Spieler gewonnen hat.
	 * 
	 * @param Clienthandlerlist eine List der Clienthandler
	 * @return Der Spielername des Gewinners
	 */
	public String getWinner(ArrayList<YoolooClientHandler> Clienthandlerlist) {
		String result = "";
		int points = 0;
		for (int i = 0; i < Clienthandlerlist.size(); i++) {
			if(Clienthandlerlist.get(i).getPlayer()!=null) {
				YoolooSpieler player = Clienthandlerlist.get(i).getPlayer();
				if (player.getPunkte() > points) {
					points = player.getPunkte();
					result = player.getName();
				}
			}
		}

		return result;
	}
}
