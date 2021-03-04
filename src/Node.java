import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.*;

//Peer Node class implementation
public class Node implements Hello {
	
	//declaring peer local variables
	public String role;
	public int node_id;
	public int port_id;
	private int max_sell_items = 5;
	private int m=5;
	private String item;
	String items[] = {"Boar", "Salt", "Fish"};
	private int[] peers;
	private int sellerId = -1;
	private int testcase;
	private Logger logger;
	private int hopcount = 1;
	public static HashMap<Integer, String[]> config_lookup = new HashMap<Integer, String[]>();
	
	//Peer Constructor. Initializing the local variables
	//Starting the RMI registry and binding the peer stub to it
	public Node(int node_id, String role,  int port_id, String item, int[] peers, int testcase, Logger logger) {
		this.role = role;
		this.node_id = node_id;
		this.port_id = port_id;
		this.item = item;
		this.peers = peers;
		this.testcase = testcase;
		this.logger = logger;
		try { 	    
	         // Exporting the object of implementation class  
	         // (here we are exporting the remote object to the stub) 
	         Hello stub = (Hello) UnicastRemoteObject.exportObject(this, 0);  
	         
	         Registry registry = LocateRegistry.createRegistry(this.port_id);
	         
	         registry.bind(String.valueOf(this.node_id), stub);  
	         System.out.println("Peer "+this.node_id+" ready"); 
	         logger.info("Peer "+this.node_id+" ready");
	    } 
		catch (Exception e) { 
	         System.err.println("Peer exception: " + e.toString()); 
	         e.printStackTrace(); 
	    }
	}
	
	//function to check the item availability, forwarding lookup request, and reply back to the buyer with the seller Id
	public void lookup_helper(String productname, int hopcount, int buyerId) {
		
		if(this.role.equals("seller")) {
			if(productname.equals(this.item)) {
				if(this.m>0) {
					logger.info("Peer:"+this.node_id+": I have "+this.m+" items of '"+this.item+"'");
					this.reply(buyerId, this.node_id);
				}
				else if(this.m == 0) {
					
					logger.info("Peer:"+this.node_id+": My items are finished. Restocking them.");
					this.m = max_sell_items;
					if(this.testcase==1) {
						this.item = "Fish";
					}
					else if (this.testcase == 2) {
						this.item = "Boar";
					}
					else {
						int rnd = new Random().nextInt(this.items.length);
					    this.item = this.items[rnd];		
					}						
				}
			}
			else {
				logger.info("Peer: "+this.node_id+": I don't have '"+productname+"'. I have '"+this.item+"'");
				//TODO:This should further send to the network except from where it came
			}
		}
		else {
			this.lookup(productname, hopcount);
		}
		   
	   }
	
	//if seller: decrements the stock item
	//if buyer: buys a particular item from the specified Seller
    public void buy(int sellerId) {

	    if(this.role.equals("seller")) {
		    this.m-=1;
	    }
	    else {

	    	logger.info("Peer: "+ this.node_id + ": I found my seller in peer "+ sellerId);
	    	try {
	    		logger.info("Peer: "+ this.node_id +": Buying '"+this.item+ "' from peer "+sellerId);
	    		String neighbour_ip = Node.config_lookup.get(sellerId)[1];
				int neighbour_port = Integer.parseInt(config_lookup.get(sellerId)[0]);
				Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
				Hello stub = (Hello) registry.lookup(String.valueOf(sellerId));
				stub.buy(this.node_id);	
				logger.info("Peer: "+this.node_id+ ": Successfully bought '"+ this.item + "' from "+sellerId);
				
				this.sellerId = -1;
			}
			catch(Exception e) {
				System.err.println("Client exception: " + e.toString()); 
		        e.printStackTrace();
			}
	    }
	   
    }
	
    //A helper function to reply back to the peer from where the request originated
    public void reply_helper(int buyerId, int sellerId) {

	    if(buyerId == this.node_id) {
	    	this.sellerId = sellerId;
	    }
	    else {
	    	this.reply(buyerId, sellerId);
	    }
    }
	
    //To forward a lookup request to all the neighboring peers with the item buyer wants to buy
	private void lookup(String product_name, int hopcount) {

		logger.info("Peer: "+this.node_id+": Looking up '"+product_name+ "' in my neighbour peers.");
		if(hopcount>0) {
			for(int i= 0; i<this.peers.length;i++) {
				int neighbour_peer = peers[i];
				String neighbour_ip = Node.config_lookup.get(peers[i])[1];
				int neighbour_port = Integer.parseInt(Node.config_lookup.get(peers[i])[0]);
				try {
					Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
					Hello stub = (Hello) registry.lookup(String.valueOf(neighbour_peer));
					stub.lookup_helper(product_name, hopcount-1, this.node_id);
				}
				catch(Exception e) {
					System.err.println("Client exception: " + e.toString()); 
					System.err.println("Peer: "+this.node_id+": Not able to contact the peer "+neighbour_peer);
				}
				
			}
		}
	}
	
	//To trace back the request of buyer by calling the public interface of the peer from which the request was passed
	private void reply(int buyerId, int sellerId) {

		try{
			String neighbour_ip = Node.config_lookup.get(buyerId)[1];
			int neighbour_port = Integer.parseInt(Node.config_lookup.get(buyerId)[0]);
		    Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
			Hello stub = (Hello) registry.lookup(String.valueOf(buyerId));
			stub.reply_helper(buyerId, sellerId);
		}
		catch(Exception e) {
			System.err.println("Peer exception: " + e.toString()); 
	        e.printStackTrace();
		}
	}
	
	//Initiating the peer to start buying or selling
	public void start() {
		if(this.role == "buyer"){
			while(true){
				
				if(this.testcase == 1 || this.testcase == 2) {
					this.item = "Fish";
				}
				else {
					int rnd = new Random().nextInt(this.items.length);
				    this.item = this.items[rnd];
				}

			    logger.info("Peer: "+this.node_id+ ": I want to buy '"+ this.item+"'");
				this.lookup(this.item, this.hopcount);
				if(this.sellerId !=-1) {
					this.buy(sellerId);
				}
				else {
					logger.info("Peer: "+this.node_id+ ": Couldn't find the item '"+ this.item+"'");
				}
				try {
				    Thread.sleep(1 * 1000);
				} catch (InterruptedException ie) {
				    Thread.currentThread().interrupt();
				}
			}
		}
	}

	public static void config_lookup_initialize(HashMap<Integer, String[]> config_lookup_value) {
		config_lookup = config_lookup_value;
		
	}
	
}
