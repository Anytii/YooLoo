package allgemein;
import java.util.Scanner;

import server.Statistikmodul;

public class read_json {
	public static void main(String[] args) {
		Scanner myObj = new Scanner(System.in);
		System.out.println("Bitte Spieler eingeben:");
	    // String input
	    String name = myObj.nextLine();
	    Statistikmodul json = new Statistikmodul();
	    json.read_json(name);
	    myObj.close();
	}
		
}
