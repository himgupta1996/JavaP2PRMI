import java.util.*;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  

public class Initializer {
	public static void main(String[] args) {
		
		//TODO:configurable parameter..can be taken as input		
		int N = 2;
		int k = 1;
		
		//TODO:Take the list of nodeids and their port and addresses from a file
		
		//TODO: iterate over the nodes and form a p2p network
		HashMap<Integer, Integer> p2p = new HashMap<Integer, Integer>();
		p2p.put(1, 2);
		p2p.put(2, 1);
		
		//TODO: Randomly assign the roles to the nodes, and the item they want to buy or sell
//		HashMap<Integer, int[]> p2pNetwork = new HashMap<Integer, Integer>();
		
		//Initializing the network
		Network network = new Network();
		
//		p2pNetwork = network.get_network();
		
		HashMap<Integer, String> roles = new HashMap<Integer, String>();
		roles.put(1, "buyer");
		roles.put(2, "seller");
		
		String peer1item = "Salt";
		String peer2item = "Salt";
		
		//Initialize all the peers
		//TODO: get the associated port and ipaddress
		Node peer1 = new Node(1, "buyer", 8003, peer1item, network.get_associated_peer(1));
		Node peer2 = new Node(2, "seller", 8004, peer2item, network.get_associated_peer(2));
		
		peer1.start();
		peer2.start();
	}
}
