// Implementing the remote interface 
public class ImplExample implements Hello {  
   
   // Implementing the interface method 
   public void printMsg() {  
      System.out.println("This is an example RMI program");  
   }  
   
   public void lookup(String productname, int hopcount) {
	   System.out.println("Called Lookup");
	   System.out.println("The product name is: "+ productname);
	   System.out.println("The hpcount is Patanahin" );
	   
   }
   
   public void buy(int sellerId) {
	   System.out.println("Called Buy");
	   System.out.println("The seller Id is: "+ sellerId);
	   
   }
   
   public void reply(int buyerId, int sellerId) {
	   System.out.println("Called reply");
	   System.out.println("The seller Id is: "+ sellerId);
	   
   }
} 