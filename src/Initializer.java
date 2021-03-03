import java.util.*;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  
import java.util.logging.*;

public class Initializer {
	public static void main(String[] args) {
		Logger logger = Logger.getLogger("MyLog"); 
	    FileHandler fh;  
		int testcase = 3;
		String filename;
		if(testcase ==1) {
			filename = "testcase1.log";
		}
		else if(testcase ==2) {
			filename = "testcase2.log";
		}
		else if(testcase ==3) {
			filename = "testcase3.log";
		}
		else {
			filename = "P2P.log";
		}
		try {
			
			fh = new FileHandler(filename); 
			logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(new MyFormatter()); 
	        logger.info("My first log");  
			
		}
		catch(Exception e) {
			e.printStackTrace();  
		}
		
		//TODO:configurable parameter..can be taken as input		
		int N = 2;
		int k = 1;
		
		//TODO:Take the list of nodeids and their port and addresses from a file
		
		//TODO: iterate over the nodes and form a p2p network
//		HashMap<Integer, Integer> p2p = new HashMap<Integer, Integer>();
//		p2p.put(1, 2);
//		p2p.put(2, 1);
		
		//TODO: Randomly assign the roles to the nodes, and the item they want to buy or sell
//		HashMap<Integer, int[]> p2pNetwork = new HashMap<Integer, Integer>();
		
		//Initializing the network
		Network network = new Network();
		
//		p2pNetwork = network.get_network();
		
		//TODO: Randomly Assign roles or assign according to the input
		HashMap<Integer, String> roles = new HashMap<Integer, String>();
		roles.put(1, "buyer");
		roles.put(2, "seller");
		
		//TODO: Randomly Assign roles or assign according to the input
		String peer1item = "Fish";
		String peer2item = "Boar";
		
		//if input comes, initialize with that value, otherwise 3
		
		//Randomly initialize the items in case args not passed
		
		//Initialize all the peers
		//TODO: get the associated port and ipaddress
		Node peer1 = new Node(1, "buyer", 8003, peer1item, network.get_associated_peer(1), testcase, logger);
		Node peer2 = new Node(2, "seller", 8004, peer2item, network.get_associated_peer(2), testcase, logger);
		
		//TODO:Check which is buyer, only call buyer.start
		peer1.start();
		peer2.start();
	}
}
