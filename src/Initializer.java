import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  
import java.util.logging.*;

public class Initializer {
	public static int N = 0;
	public static int K = 0;
	static {
		// Initializing the config Hashmap once the class loads from the config file
		// config_lookup is a static variable of the Node class which is used to lookup port number and the corresponding ip for a given node
		try {
			File config = new File("config.txt");
			Scanner configScanner = new Scanner(config);
			String[] first_data = configScanner.nextLine().split(" ", 2);;
			N = Integer.parseInt(first_data[0]);
			K = Integer.parseInt(first_data[1]);
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
			1 corresponds to Test-case 1 (Refer to the Test-case document for further details)
		 	2 corresponds to Test-case 2 (Refer to the Test-case document for further details)
		   	No argument represent the default test case of random initialization based upon the value of K and N specified in the config file
		   	Based upon the passed argument, peers and the corresponding P2P network is initialized */
		
		
		//Test-case 1 and 2
		if (args.length > 0)
		{
			if (args[0].equals("1"))
			{
				String filename = "testcase1.log";
				Network network = new Network(N,K,1);
				// Setting up the logger;
				try {

					fh = new FileHandler(filename);
					logger.addHandler(fh);
					fh.setFormatter(new MyFormatter());
					logger.info("Logger Initialized");

				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.info("First Testcase:");
				logger.info("One seller of boar, 3 buyers of boars, the remaining peers have no role. Fix the neighborhood structure so that buyers and sellers are 2-hop away in the peer-to-peer overlay network. Ensure that all items are sold and restocked and that all buyers can buy forever.");
				
				/*
				Node Initialization here
				
				ArrayList<Node> PeerList = new ArrayList<Node>();
				
				//Initializing Node 1 (Peer 1)
				
				int node_id_1 = 0;
				roles.put(node_id_1, "buyer");
				int port_1 = Integer.parseInt(Node.config_lookup.get(node_id_1)[0]);
				Node peer_1 = new Node(node_id_1, roles.get(node_id_1), port_1, "Fish", network.get_associated_peer(node_id_1), 1,
						logger);
				PeerList.add(peer_1);
				
				//Initializing Node 2 (Peer 2)
				
				int node_id_2 = 1;
				roles.put(node_id_2, "seller");
				int port_2 = Integer.parseInt(Node.config_lookup.get(node_id_2)[0]);
				Node peer_2 = new Node(node_id_2, roles.get(node_id_2), port_2, "Fish", network.get_associated_peer(node_id_2), 1,
						logger);
				PeerList.add(peer_2);
						
				
				// Starting the peers
				for (int i = 0; i < PeerList.size(); i++) {
					PeerList.get(i).start();
				}
				*/
			}
			if (args[0].equals("2"))
			{
				String filename = "testcase1.log";
				Network network = new Network(N,K,2);
				// Setting up the logger;
				try {

					fh = new FileHandler(filename);
					logger.addHandler(fh);
					fh.setFormatter(new MyFormatter());
					logger.info("Logger Initialized");

				} catch (Exception e) {
					e.printStackTrace();
				}
				logger.info("Second Testcase:");
				logger.info("Simulate a race condition in buy() wherein a seller has stock of 1 for an item but then replies to multiple buyers.");
				
				// Node Initialization here
			}
		}
		else //default case
		{
			String filename = "testcase1.log";
			Network network = new Network(N,K);
			// Setting up the logger;
			try {

				fh = new FileHandler(filename);
				logger.addHandler(fh);
				fh.setFormatter(new MyFormatter());
				logger.info("Logger Initialized");

			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("Default Testcase:");
			logger.info("Constructing a random network based on the value of N and K specified in the config file.");
			
			// Node Initialization here
		}
	}
}
