import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Node extends ImplExample {
	public String role;
	public int node_id;
	public int port_id;
	public int selling;
	public int buying;
	
	public Node(int node_id, String role,  int port_id, HashMap<Integer, Integer> p2p, int selling) {
		this.role = role;
		this.node_id = node_id;
		this.port_id = port_id;
		if(role=="seller") {
			this.selling=selling;
//			this.selling=(int)((Math.random()*(3-1)) + 1);
		} //assigning seller a random item to sell
		if(role=="buyer") {
			this.buying=selling;
		}
		
		try { 
	         // Instantiating the implementation class 
	         ImplExample obj = new ImplExample(); 
	    
	         // Exporting the object of implementation class  
	         // (here we are exporting the remote object to the stub) 
	         Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);  
	         
	         Registry registry = LocateRegistry.createRegistry(this.port_id); 
	         
//	         // Binding the remote object (stub) in the registry 
//	         Registry registry = LocateRegistry.getRegistry(); 
	         
	         registry.bind(String.valueOf(this.node_id), stub);  
	         System.out.println("Peer "+this.node_id+" ready"); 
	      } catch (Exception e) { 
	         System.err.println("Peer exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
	}
	
	
	
	public void search(String product, int hop_count) {
//		if(this.role=="seller") {
//			helper()
//		}
		
		
		
		
	}
	
	
	public void get_stub(int port_id, int node_id) {
		try {  
	         // Getting the registry 
	         Registry registry = LocateRegistry.getRegistry("localhost", port_id); 
	    
	         // Looking up the registry for the remote object 
	         Hello stub = (Hello) registry.lookup(String.valueOf(node_id)); 
	         
	         System.out.println(stub);
	    
	         // Calling the remote method using the obtained object 
	         String s = String.valueOf(node_id);
//	         stub.call("boar",10, s);
//	         stub.lookup(s, 10); 
	         
	         stub.lookup_helper(s, node_id, s);
	         
	         // System.out.println("Remote method invoked"); 
	      } catch (Exception e) {
	         System.err.println("Client exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
	}
	
}
