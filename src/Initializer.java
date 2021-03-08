import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  
import java.util.logging.*;

public class Initializer {
	static {
		// Initializing the config Hashmap once the class loads from the config file
		// config_lookup is a static variable of the Node class which is used to lookup port number and the corresponding ip for a given node
		try {
			File config = new File("config.txt");
			Scanner configScanner = new Scanner(config);
			HashMap<Integer, String[]> config_lookup_value = new HashMap<Integer, String[]>();
			while (configScanner.hasNextLine()) {

				String data = configScanner.nextLine();
				String[] node_info = data.split(" ", 3);
				int node_id = Integer.parseInt(node_info[0]);
				config_lookup_value.put(node_id, new String[] { node_info[1], node_info[2] });
			}
			Node.config_lookup_initialize(config_lookup_value);
			configScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;
		
		/*  Arguments passed to this class represent the test case which is being executed
			1 corresponds to Testcase 1 (Refer to the Testcase document for further details)
		 	2 corresponds to Testcase 2 (Refer to the Testcase document for further details)
		 	3 corresponds to Testcase 3 (Refer to the Testcase document for further details)
		   	Based upon the passed argument, peers and the corresponding P2P network is initialized */
		
		
		//Testcase 1
		if (args[0].equals("1")) {
			String filename = "testcase1.log";
			Network network = new Network();
			HashMap<Integer, String> roles = new HashMap<>();

			// Setting up the logger
			try {

				fh = new FileHandler(filename);
				logger.addHandler(fh);
				fh.setFormatter(new MyFormatter());
				logger.info("Logger Initialized");

			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("First Testcase:");
			logger.info("Assign one peer to be a buyer of fish and another to be a seller of fish. Ensure that all fish is sold and restocked forever.");
			
			ArrayList<Node> PeerList = new ArrayList<Node>();
			
			//Initializing Node 1 (Peer 1)
			
			int node_id_1 = 0;
			roles.put(node_id_1, "buyer");
			int port_1 = Integer.parseInt(Node.config_lookup.get(node_id_1)[0]);
			String ip_1 = Node.config_lookup.get(node_id_1)[1];
			Node peer_1 = new Node(node_id_1, roles.get(node_id_1), port_1, "Fish", network.get_associated_peer(node_id_1), 1,
					logger);
			PeerList.add(peer_1);
			
			//Initializing Node 2 (Peer 2)
			
			int node_id_2 = 1;
			roles.put(node_id_2, "seller");
			int port_2 = Integer.parseInt(Node.config_lookup.get(node_id_2)[0]);
			String ip_2 = Node.config_lookup.get(node_id_2)[1];
			Node peer_2 = new Node(node_id_2, roles.get(node_id_2), port_2, "Boar", network.get_associated_peer(node_id_2), 1,
					logger);
			PeerList.add(peer_2);
			
			//Initializing Node 3 (Peer 3)
			
			int node_id_3 = 2;
			roles.put(node_id_3, "buyer");
			int port_3 = Integer.parseInt(Node.config_lookup.get(node_id_3)[0]);
			String ip_3 = Node.config_lookup.get(node_id_3)[1];
			Node peer_3 = new Node(node_id_3, roles.get(node_id_3), port_3, "Boar", network.get_associated_peer(node_id_3), 1,
					logger);
			PeerList.add(peer_3);
			
			// Starting the peers
			for (int i = 0; i < PeerList.size(); i++) {
				PeerList.get(i).start();
			}
		}
		
		//Testcase 2
		if (args[0].equals("2")) {
			
			String filename = "testcase2.log";
			Network network = new Network();
			HashMap<Integer, String> roles = new HashMap<>();

			// Setting up the logger
			try {
				fh = new FileHandler(filename);
				logger.addHandler(fh);
				fh.setFormatter(new MyFormatter());
				logger.info("Logger Initialized");

			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("Second Testcase:");
			logger.info("Assign one peer to be a buyer of fish and another to be a seller of boar. Ensure that nothing is sold.");

			ArrayList<Node> PeerList = new ArrayList<Node>();
			
			//Initializing Node 1 (Peer 1)
			
			int node_id_1 = 0;
			roles.put(node_id_1, "buyer");
			int port_1 = Integer.parseInt(Node.config_lookup.get(node_id_1)[0]);
			String ip_1 = Node.config_lookup.get(node_id_1)[1];
			Node peer_1 = new Node(node_id_1, roles.get(node_id_1), port_1, "Fish", network.get_associated_peer(node_id_1), 2,
					logger);
			PeerList.add(peer_1);
			
			//Initializing Node 2 (Peer 2)
			
			int node_id_2 = 1;
			roles.put(node_id_2, "seller");
			int port_2 = Integer.parseInt(Node.config_lookup.get(node_id_2)[0]);
			String ip_2 = Node.config_lookup.get(node_id_2)[1];
			Node peer_2 = new Node(node_id_2, roles.get(node_id_2), port_2, "Boar", network.get_associated_peer(node_id_2), 2,
					logger);
			PeerList.add(peer_2);
					
			
			// Starting the peers
			for (int i = 0; i < PeerList.size(); i++) {
				PeerList.get(i).start();
			}
			
		}
		
		//Testcase 3
		if (args[0].equals("3")) {
			String filename = "testcase3.log";
			Network network = new Network();
			HashMap<Integer, String> roles = new HashMap<>();

			// Setting up the logger
			try {
				fh = new FileHandler(filename);
				logger.addHandler(fh);
				fh.setFormatter(new MyFormatter());
				logger.info("Logger Initialized");

			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("Third Testcase:");
			logger.info("Randomly assign buyer and seller roles. Ensure that items keep being sold throughout.");

			ArrayList<Node> PeerList = new ArrayList<Node>();
			String[] list_of_items = new String[]{"Fish","Boar","Salt"};
			
			//Initializing Node 1 (Peer 1)
			
			int node_id_1 = 0;
			roles.put(node_id_1, "buyer");
			int port_1 = Integer.parseInt(Node.config_lookup.get(node_id_1)[0]);
			int random_1 = new Random().nextInt(list_of_items.length);
			String ip_1 = Node.config_lookup.get(node_id_1)[1];
			Node peer_1 = new Node(node_id_1, roles.get(node_id_1), port_1, list_of_items[random_1], network.get_associated_peer(node_id_1), 3,
					logger);
			PeerList.add(peer_1);
			
			//Initializing Node 2 (Peer 2)
			
			int node_id_2 = 1;
			roles.put(node_id_2, "seller");
			int port_2 = Integer.parseInt(Node.config_lookup.get(node_id_2)[0]);
			int random_2 = new Random().nextInt(list_of_items.length);
			String ip_2 = Node.config_lookup.get(node_id_2)[1];
			Node peer_2 = new Node(node_id_2, roles.get(node_id_2), port_2, list_of_items[random_2], network.get_associated_peer(node_id_2), 3,
					logger);
			PeerList.add(peer_2);
					
			// Starting the peers
			for (int i = 0; i < PeerList.size(); i++) {
				PeerList.get(i).start();
			}
		}
	}
}
