import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Node implements Hello {
	public String role;
	public int node_id;
	public int port_id;
	private int m=5;
	private String item;
	String items[] = {"Boar", "Salt", "Fish"};
	private int[] peers;
	
	public Node(int node_id, String role,  int port_id, String item, int[] peers) {
		this.role = role;
		this.node_id = node_id;
		this.port_id = port_id;
		this.item = item;
		this.peers = peers;
		try { 
	         // Instantiating the implementation class 
//	         ImplExample obj = new ImplExample(); 
	    
	         // Exporting the object of implementation class  
	         // (here we are exporting the remote object to the stub) 
	         Hello stub = (Hello) UnicastRemoteObject.exportObject(this, 0);  
	         
	         Registry registry = LocateRegistry.createRegistry(this.port_id); 
	         
//	         // Binding the remote object (stub) in the registry 
//	         Registry registry = LocateRegistry.getRegistry(); 
	         
	         registry.bind(String.valueOf(this.node_id), stub);  
	         System.out.println("Peer "+this.node_id+" ready"); 
	    } 
		catch (Exception e) { 
	         System.err.println("Peer exception: " + e.toString()); 
	         e.printStackTrace(); 
	    }
	}
	
	public void lookup_helper(String productname, int hopcount) {
		System.out.println("Called Lookup helper in peer "+this.node_id);
		System.out.println("My item is "+this.item);
		System.out.println("The product name is: "+ productname);
		System.out.println("The hpcount is "+ hopcount );
		
		System.out.println("My role is "+this.role);
		
		if(this.role == "seller") {
			System.out.println("My item is "+this.item);
			if(productname == this.item) {
				System.out.println("My m is "+this.m);
				if(this.m>0) {
					System.out.println("Item "+ productname +"found in the peer "+ this.node_id);
					this.reply(1, 2);
				}
			}
		}
		else {
			if(this.m==0){
				//TODO: Remove the hardcoded m
				this.m = 5;
			}
			this.lookup(productname, hopcount);
		}
		   
	   }
	   
    public void buy(int buyerId) {
	    System.out.println("Called Buy");
	    System.out.println("The buyer Id is: "+ buyerId);
	    this.m-=1;
	   
    }
	   
    public void reply_helper(int buyerId, int sellerId) {
	    System.out.println("Called reply helper");
	    System.out.println("The seller Id is: "+ sellerId);
	    if(buyerId == this.node_id) {
	    	System.out.println("I am peer "+ this.node_id + ". I found my seller in peer "+ sellerId);
	    	System.out.println("Let me now buy it");
	    	try {
				Registry registry = LocateRegistry.getRegistry("localhost", 8004); 
				Hello stub = (Hello) registry.lookup(String.valueOf(sellerId));
				stub.buy(this.node_id);	
				System.out.println("Successfully completed the transaction");
			}
			catch(Exception e) {
				System.err.println("Client exception: " + e.toString()); 
		        e.printStackTrace();
			}
	    }
	  //Get associated port and ip address of the <neighbour_peer>
		
    }
	
	private void lookup(String product_name, int hopcount) {
		System.out.println("Called lookup in the peer "+ this.node_id);
		if(hopcount>0) {
			for(int i= 0; i<this.peers.length;i++) {
				int neighbour_peer = peers[i];
				//Get associated port and ip address of the <neighbour_peer>
				String neighbour_ip = "127.0.0.1";
				int neighbour_port = 8004;
				try {
					Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
					Hello stub = (Hello) registry.lookup(String.valueOf(neighbour_peer));
					stub.lookup_helper(product_name, hopcount-1);
					
				}
				catch(Exception e) {
					System.err.println("Client exception: " + e.toString()); 
			        e.printStackTrace();
				}
				
			}
			
		}
	}
	
	private void reply(int buyerId, int sellerId) {
		System.out.println("In reply of peer "+ this.node_id);
		try{
			String neighbour_ip = "127.0.0.1";
			int neighbour_port = 8003;
		    Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
			Hello stub = (Hello) registry.lookup(String.valueOf(buyerId));
			stub.reply_helper(buyerId, sellerId);
		}
		catch(Exception e) {
			System.err.println("Peer exception: " + e.toString()); 
	        e.printStackTrace();
		}
	}
	
	public void start() {
		if(this.role == "buyer"){
			int i = 0;
			while(i!=1){
//				if (this.item == "") {
//					int rnd = new Random().nextInt(this.items.length);
//				    this.item = this.items[rnd];
//				}
//				//TODO: take the variable hopcount as input and check if it is greater than zero..If yes, call lookup
//				//TODO: remove the hardcoding
//				this.item = "Salt";
				this.lookup(this.item, 1);
				i+=1;
			}
		}
		if(this.role == "seller") {
			//TODO
			System.out.println("Now Starting the seller");
			this.item = "Salt";
		}
	}
	
}
