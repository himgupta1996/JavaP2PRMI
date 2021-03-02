// Implementing the remote interface 
public class ImplExample {  
   
   // Implementing the interface method 
//   public void printMsg() {  
//      System.out.println("This is an example RMI program");  
//   }  
   
//   public void lookup(String productname, int hopcount) {
//	   System.out.println("Called Lookup");
//	   System.out.println("The product name is: "+ productname);
//	   System.out.println("The hpcount is Patanahin" );
//	   
//   }
   public void lookup_helper(String productname, int hopcount) {
//	   this.lookup(productname, hopcount);
	   System.out.println("Called Lookup");
	   System.out.println("The product name is: "+ productname);
	   System.out.println("The hpcount is Patanahin" );
	   
   }
   
   public void buy(int sellerId) {
	   System.out.println("Called Buy");
	   System.out.println("The seller Id is: "+ sellerId);
	   
   }
   
   public void reply_helper(int buyerId, int sellerId) {
	   System.out.println("Called reply");
	   System.out.println("The seller Id is: "+ sellerId);
	   
   }
} 