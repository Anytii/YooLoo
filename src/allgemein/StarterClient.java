// History of Change
// vernr    |date  | who | lineno | what
//  V0.106  |200107| cic |    -   | add history of change

package allgemein;

import client.YoolooClient;

public class StarterClient {
	public static void main(String[] args) throws ClassNotFoundException {

		// Starte Client
		String hostname = "localhost";
//		String hostname = "10.101.136.237";
		int port = 44137;
		YoolooClient client = new YoolooClient(hostname, port, hostname);
		client.startClient();

	}
}
