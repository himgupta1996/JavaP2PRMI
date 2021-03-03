import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.*;
public class Node implements Hello {
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
	
	public void lookup_helper(String productname, int hopcount, int buyerId) {
		System.out.println("Peer:"+this.node_id+" In lookup_helper");
		
		System.out.println("I have "+this.m+" items of "+this.item);
		
		if(this.role.equals("seller")) {
			if(productname.equals(this.item)) {
				if(this.m>0) {
					//TODO: remove the buyer Id hard coding
					logger.info("Peer:"+this.node_id+": I have "+this.m+" items of '"+this.item+"'");
					this.reply(buyerId, this.node_id);
				}
				else if(this.m == 0) {
					System.out.println("Peer:"+this.node_id+": My items are finished. Restocking them.");
					logger.info("Peer:"+this.node_id+": My items are finished. Restocking them.");
					//TODO: Remove the hardcoded m
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
	   
    public void buy(int sellerId) {
	    System.out.println("Peer:"+this.node_id+" In Buy.");
	    if(this.role.equals("seller")) {
		    this.m-=1;
	    }
	    else {
	    	System.out.println("Peer: "+ this.node_id + ": I found my seller in peer "+ sellerId);
	    	System.out.println("Going to buy it");
	    	logger.info("Peer: "+ this.node_id + ": I found my seller in peer "+ sellerId);
	    	try {
	    		logger.info("Peer: "+ this.node_id +": Buying '"+this.item+ "' from peer "+sellerId);
	    		//TODO: get the seller port Id and Ip address from the configuration file
	    		String neighbour_ip = "127.0.0.1";
				int neighbour_port = 8004;
				Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
				Hello stub = (Hello) registry.lookup(String.valueOf(sellerId));
				stub.buy(this.node_id);	
				System.out.println("Peer: "+ this.node_id +": Successfully completed the transaction");
				logger.info("Peer: "+this.node_id+ ": Successfully bought '"+ this.item + "' from "+sellerId);
				this.sellerId = -1;
			}
			catch(Exception e) {
				System.err.println("Client exception: " + e.toString()); 
		        e.printStackTrace();
			}
	    }
	   
    }
	   
    public void reply_helper(int buyerId, int sellerId) {
	    System.out.println("Peer:"+this.node_id+" In reply_helper");
	    if(buyerId == this.node_id) {
	    	this.sellerId = sellerId;
	    }
	    else {
	    	this.reply(buyerId, sellerId);
	    }
    }
	
	private void lookup(String product_name, int hopcount) {
		System.out.println("Peer:"+this.node_id+" In lookup");
		logger.info("Peer: "+this.node_id+": Looking up '"+product_name+ "' in my neighbour peers.");
		if(hopcount>0) {
			for(int i= 0; i<this.peers.length;i++) {
				int neighbour_peer = peers[i];
				//TODO: Get associated port and ip address of the <neighbour_peer>
				String neighbour_ip = "127.0.0.1";
				int neighbour_port = 8004;
				try {
					Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
					Hello stub = (Hello) registry.lookup(String.valueOf(neighbour_peer));
					stub.lookup_helper(product_name, hopcount-1, this.node_id);
					
				}
				catch(Exception e) {
					System.err.println("Client exception: " + e.toString()); 
			        e.printStackTrace();
				}
				
			}
		}
	}
	
	private void reply(int buyerId, int sellerId) {
		System.out.println("Peer:"+this.node_id+" In reply");
		try{
			//TODO: get the values dynamically
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
			while(i!=8){			
				if(this.testcase == 1 || this.testcase == 2) {
					this.item = "Fish";
				}
				else {
					int rnd = new Random().nextInt(this.items.length);
				    this.item = this.items[rnd];
				}

			    System.out.println("Peer: "+this.node_id+ ": I want to buy "+ this.item);
			    logger.info("Peer: "+this.node_id+ ": I want to buy '"+ this.item+"'");
				this.lookup(this.item, this.hopcount);
				if(this.sellerId !=-1) {
					this.buy(sellerId);
				}
				else {
					logger.info("Peer: "+this.node_id+ ": Couldn't find the item '"+ this.item+"'");
				}
				try {
				    Thread.sleep(3 * 1000);
				} catch (InterruptedException ie) {
				    Thread.currentThread().interrupt();
				}
				i+=1;
			}
		}
	}
	
}
