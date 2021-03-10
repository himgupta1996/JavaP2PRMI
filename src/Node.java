import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

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
	
	//List of the sellers buyer gets after the full lookup
	private ArrayList<Integer> sellers = new ArrayList<Integer>();
	
	//List of the buyers, mostly including only the self node Id
	private ArrayList<Integer> buyers = new ArrayList<Integer>();
	
	private int testcase;
	private Logger logger;
	private int hopcount = 3;
	public static HashMap<Integer, String[]> config_lookup = new HashMap<Integer, String[]>();
	
	//Peer Constructor. Initializing the local variables
	//Starting the RMI registry and binding the peer stub to it
	public Node(int node_id, String role,  int port_id, String item, int[] peers, int testcase, Logger logger, int max_sell_items) {
		this.role = role;
		this.node_id = node_id;
		this.port_id = port_id;
		this.item = item;
		this.peers = peers;
		this.testcase = testcase;
		this.logger = logger;
		this.max_sell_items=max_sell_items;
		this.buyers.add(this.node_id);
		// TODO: RANDOMLY INITIALIZE 
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
	
//	TODO: generalize the the get_stub method
	
	//function to check the item availability, forwarding lookup request, and reply back to the buyer with the seller Id
	public void lookup_helper(String productname, int hopcount, ArrayList<Integer> buyers) {
		logger.info("Peer: "+ this.node_id +": buyers "+buyers);
		if(this.role.equals("seller") && productname.equals(this.item) && this.m>0) {
			logger.info("Peer:"+this.node_id+": I have "+this.m+" items of '"+this.item+"'");
			int last_node_index = buyers.size()-1;
    		int lastNodeId = buyers.get(last_node_index);
    		buyers.remove(last_node_index);
    		try {
    			logger.info("Peer: "+ this.node_id +": Sending back the reply to "+lastNodeId);
    			String neighbour_ip = Node.config_lookup.get(lastNodeId)[1];
				int neighbour_port = Integer.parseInt(config_lookup.get(lastNodeId)[0]);
				Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
				Hello stub = (Hello) registry.lookup(String.valueOf(lastNodeId));
				stub.reply_helper(buyers, this.node_id);
    		}
    		catch (Exception e){
    			logger.info("Peer: "+ this.node_id +": Couldn't connect to peer "+lastNodeId+ " to buy.");
    			System.err.println("Client exception: " + e.toString()); 
		        e.printStackTrace();
    		}  		
		}
		else {
			if(this.m == 0 && this.role.equals("seller")) {
				//Restocking the items
				logger.info("Peer:"+this.node_id+": My items are finished. Restocking them.");
				int rnd = new Random().nextInt(this.items.length);
			    this.item = this.items[rnd];
			    this.m = max_sell_items; 
			}
			if(hopcount>0){
				if(this.role.equals("seller")) {
					logger.info("Peer: "+this.node_id+": I don't have '"+productname+"'. I have '"+this.item+"'"+". Forwarding the message to my other peers.");
				}
				buyers.add(this.node_id);
				for(int i= 0; i<this.peers.length;i++) {
					int neighbour_peer = peers[i];
					logger.info("Peer: "+ this.node_id +": Checking if I can go to "+neighbour_peer+ " to buy.");
					if(!buyers.contains(neighbour_peer)){
						logger.info("Peer: "+ this.node_id +": Yes!! I can go to "+neighbour_peer+ " to buy.");
						String neighbour_ip = Node.config_lookup.get(neighbour_peer)[1];
						int neighbour_port = Integer.parseInt(Node.config_lookup.get(neighbour_peer)[0]);
						try {
							Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
							Hello stub = (Hello) registry.lookup(String.valueOf(neighbour_peer));
							stub.lookup_helper(productname, hopcount-1, buyers);
						}
						catch(Exception e) {
							logger.info("Peer: "+ this.node_id +": Couldn't connect to peer "+neighbour_peer+ " to buy.");
							System.err.println("Client exception: " + e.toString()); 
							System.err.println("Peer: "+ this.node_id +": Couldn't connect to peer "+neighbour_peer+ " to buy.");
						}
					}
				}
			}
			else {
				logger.info("Peer: "+this.node_id+": Hopcount is finished.");
			}
		}
		   
	}
	
	//if seller: decrements the stock item
	//if buyer: buys a particular item from the specified Seller
    public void buy(int sellerId) {

	    if(this.role.equals("seller")) {
		    this.m-=1;
	    }
	    else {

	    	logger.info("Peer: "+ this.node_id +": I found my seller in peer "+sellerId);
	    	try {
	    		logger.info("Peer: "+ this.node_id +": Buying '"+this.item+ "' from peer "+sellerId);
	    		String neighbour_ip = Node.config_lookup.get(sellerId)[1];
				int neighbour_port = Integer.parseInt(config_lookup.get(sellerId)[0]);
				Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
				Hello stub = (Hello) registry.lookup(String.valueOf(sellerId));
				stub.buy(this.node_id);	
				logger.info("Peer: "+this.node_id+ ": Successfully bought '"+ this.item + "' from "+sellerId);
				
				//Successfully bought the item, Now clearing the sellers before going for next buy
				this.sellers.clear();
			}
			catch(Exception e) {
				logger.info("Peer: "+ this.node_id +": Couldn't connect to peer "+sellerId+ " to buy.");
				System.err.println("Client exception: " + e.toString()); 
		        e.printStackTrace();
			}
	    }
	   
    }
	
    //A helper function to reply back to the peer from where the request originated
    public void reply_helper(ArrayList<Integer> trace_back_peers, int sellerId) {
    	logger.info("Peer: "+this.node_id+ ": traceback peers "+trace_back_peers);
    	if(trace_back_peers.size() == 0) {
    		//found the actual buyer
    		this.reply(this.node_id, sellerId);
    	}
    	else {
    		//trace back to actual buyer
    		int last_node_index = trace_back_peers.size()-1;
    		int lastNodeId = trace_back_peers.get(last_node_index);
    		trace_back_peers.remove(last_node_index);
    		try {
    			logger.info("Peer: "+ this.node_id +": Sending back the reply to "+lastNodeId);
    			String neighbour_ip = Node.config_lookup.get(lastNodeId)[1];
				int neighbour_port = Integer.parseInt(config_lookup.get(lastNodeId)[0]);
				Registry registry = LocateRegistry.getRegistry(neighbour_ip, neighbour_port); 
				Hello stub = (Hello) registry.lookup(String.valueOf(lastNodeId));
				stub.reply_helper(trace_back_peers, sellerId);
    		}
    		catch (Exception e){
    			System.err.println("Client exception: " + e.toString()); 
		        e.printStackTrace();
    		}
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
					stub.lookup_helper(product_name, hopcount-1, this.buyers);
				}
				catch(Exception e) {
					System.err.println("Client exception: " + e.toString()); 
					System.err.println("Peer: "+this.node_id+": Not able to contact the peer "+neighbour_peer);
				}
				
			}
		}
	}
	
	//To note all the sellers which are ready to sell the items to this buyer
	private void reply(int buyerId, int sellerId) {
		this.sellers.add(sellerId);
	}
	
	//Initiating the peer to start buying or selling
	public void start() {
		if(this.role == "buyer"){
			int i = 5;
			while(i>0){
				//choosing a item randomly to buy
//				int rnd = new Random().nextInt(this.items.length);
//			    this.item = this.items[rnd];
//				this.item = "Boar";

			    logger.info("Peer: "+this.node_id+ ": I want to buy '"+ this.item+"'");
				this.lookup(this.item, this.hopcount);
				logger.info("Peer: "+this.node_id+ ": The sellers are '"+ this.sellers+"'");
				if(this.sellers.isEmpty()) {
					//no seller found
					logger.info("Peer: "+this.node_id+ ": Couldn't find the item '"+ this.item+"'");
				}
				else {
					//choosing a seller randomly out of all the sellers who responded
					int rnd_seller = new Random().nextInt(this.sellers.size());
					this.buy(this.sellers.get(rnd_seller));
				
				}
				
				//Waiting for 1 second before going for next buy
				try {
				    Thread.sleep(1 * 1000);
				} catch (InterruptedException ie) {
				    Thread.currentThread().interrupt();
				}
				i--;
			}
		}
	}

	public static void config_lookup_initialize(HashMap<Integer, String[]> config_lookup_value) {
		config_lookup = config_lookup_value;
		
	}

	public static void main(String[] args){
		// int node_id, String role,  int port_id, String item, int[] peers, int testcase, Logger logger
		String node_id=args[0];
		String role;
		// ArrayList<Integer> PeerList = new ArrayList<Integer>();
		Integer[] PeerList;
		String item;
		int max_items;
		int port_id;
		// 0 [3,1] seller boar 5
		
		try {
			File network = new File("network.txt");
			Scanner networkScanner = new Scanner(network);
			// HashMap<Integer, String[]> config_lookup_value = new HashMap<Integer, String[]>();
			while (networkScanner.hasNextLine()) {
				String data = networkScanner.nextLine();
				String[] node_info = data.split(" ");
				if(node_info[0]!=node_id){
					continue;
				}
				node_info[1]=node_info[1].replace("[","");
        		node_info[1]=node_info[1].replace("]","");
				String[] s1=node_info[1].split(",");
				PeerList=new Integer[s1.length];
				for (int i = 0; i < s1.length; i++){
					PeerList[i]=Integer.valueOf(s1[i]);
				}

				role=node_info[2];
				item=node_info[3];
				max_items=Integer.valueOf(node_info[4]);
				break;
			}
			// Node.config_lookup_initialize(config_lookup_value);
			// configScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred. File not found.");
			e.printStackTrace();
		}

		try{
			File config= new File("config.txt");
			Scanner configScanner = new Scanner(network);
			while (networkScanner.hasNextLine()) {
				String data = networkScanner.nextLine();
				String[] node_info = data.split(" ");
				if(node_info[0]!=node_id){
					continue;
				}
				port_id=Integer.valueOf(node_info[1]);
			}	
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred. File not found.");
			e.printStackTrace();
		}
		Logger logger = Logger.getLogger("MyLog");
		String filename=node_id+".log";
		FileHandler fh;
		try{
			fh = new FileHandler(filename);
			logger.addHandler(fh);
			fh.setFormatter(new MyFormatter());
			logger.info("Logger Initialized");	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Node a=new Node(Integer.valueOf(node_id),role, port_id, "boar", PeerList, 0,logger,max_items);
		a.start();
	}
	
}

