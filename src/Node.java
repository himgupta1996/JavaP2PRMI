import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Node extends ImplExample {
	public String role;
	public int node_id;
	public int port_id;
	
	public Node(int node_id, String role,  int port_id) {
		this.role = role;
		this.node_id = node_id;
		this.port_id = port_id;
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
	
	public void get_stub(int port_id, int node_id) {
		try {  
	         // Getting the registry 
	         Registry registry = LocateRegistry.getRegistry("localhost", port_id); 
	    
	         // Looking up the registry for the remote object 
	         Hello stub = (Hello) registry.lookup(String.valueOf(node_id)); 
	         
	         System.out.println(stub);
	    
	         // Calling the remote method using the obtained object 
	         String s = String.valueOf(node_id);
	         stub.call("boar",10, s);
//	         stub.lookup(s, 10); 
	         
	         // System.out.println("Remote method invoked"); 
	      } catch (Exception e) {
	         System.err.println("Client exception: " + e.toString()); 
	         e.printStackTrace(); 
	      } 
	}
	
}
