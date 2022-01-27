// History of Change
// vernr    |date  | who | lineno | what
//  V0.106  |200107| cic |    -   | add history of change

package allgemein;

import client.YoolooClient;

public class StarterClient {
	public static void main(String[] args) {

		// Starte Client
		String hostname = constants.Constants.getSERVERIP();
		int port = 44137;
		YoolooClient client = new YoolooClient(hostname, port);
		client.startClient();

	}
}
