import java.util.*;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  

public class Initializer {
	public static void main(String[] args) {
		int N = 2;
		HashMap<Integer, Integer> p2p = new HashMap<Integer, Integer>();
		p2p.put(1, 2);
		p2p.put(2, 1);
		HashMap<Integer, String> roles = new HashMap<Integer, String>();
		
		//Defining roles randomly
		int no_sellers = 1;
		int no_buyers = N-1;
		
		roles.put(1, "buyer");
		roles.put(2, "seller");
		
		Node peer1 = new Node(1, "buyer", 8003);
		Node peer2 = new Node(2, "seller", 8004);
		
		peer1.get_stub(8004, 2);
	}
}
