import java.util.*;
import java.io.*;

public class Network {
	static HashMap<Integer, List<Integer>> p2p = new HashMap<Integer, List<Integer>>();
	final static String outputFilePath = "network.txt";
	String[] roles = {"seller","buyer"};
	String[] items = {"Boar", "Salt", "Fish"};
	File file = new File(outputFilePath);
	BufferedWriter bf = null;
	
	//Initializing the P2P network
	//Describes which peer is connected to which peers

	/*
	 * Here we initialize the network based upon the values of N and K specified in the constructor.
	 * There are 2 constructors - 
	 * 1. public Network(int N, int K)
	 *    This constructor deals with the default test-case in which we initialize the network randomly based on
	 *    the values of N and K which are specified in the config file.
	 * 2. public Network(int N, int K, int testcase)
	 *    This constructor deals with the test-case specific initialization of the network and initialized the
	 *    network accordingly.
	 *    Both the constructors initialize the network and accordingly write the network in the network.txt file
	 *    in the following format -  
	 *    0 [1, 2, 3] seller Fish 5
	 *    Here 0 represents the node_id
	 *    [1,2,3] represent the neighbors of the specified node
	 *    seller/buyer specifies the role of the node
	 *    Fish/Boar/Salt represents the item which the seller is selling or the buyer wants to buy
	 *    5 represents the initial count of items present with the seller (It will always be 5 except in testcase 2 
	 *    where it is 1 to implement the specified race condition)
	 * */
	
	
	
	
	public Network(int N, int K) {

		ArrayList<Integer> all_nodes = new ArrayList<>(N);
		for(int i = 0; i < N; i++){
		    all_nodes.add(i);
		}
		for(int i = 0;i<N;i++)
		{
			List<Integer> neighbors = new ArrayList<Integer>();
			ArrayList<Integer> all_nodes_copy = new ArrayList<Integer>(all_nodes);
			all_nodes_copy.remove(i);
			if(p2p.containsKey(i))
			{
				while(p2p.get(i).size() < K && all_nodes_copy.size() > 0)
				{
					Integer Random_Element = this.getRandomElement(all_nodes_copy);
					if(p2p.containsKey(Random_Element))
					{
						if(p2p.get(Random_Element).size() < K && !p2p.get(i).contains(Random_Element))
						{
							p2p.get(Random_Element).add(i);
							p2p.get(i).add(Random_Element);
						}
					}
					else
					{
						ArrayList<Integer> Random_Element_Neighbors = new ArrayList<Integer>();
						p2p.put(Random_Element, Random_Element_Neighbors);
						p2p.get(i).add(Random_Element);
						p2p.get(Random_Element).add(i);
					}
					all_nodes_copy.remove(Random_Element);
				}
				
			}
			else
			{
				ArrayList<Integer> i_neighbors = new ArrayList<Integer>();
				p2p.put(i, i_neighbors);
				while(p2p.get(i).size() < K && all_nodes_copy.size() > 0)
				{
					Integer Random_Element = this.getRandomElement(all_nodes_copy);
					if(p2p.containsKey(Random_Element))
					{
						if(p2p.get(Random_Element).size() < K && !p2p.get(i).contains(Random_Element))
						{
							p2p.get(Random_Element).add(i);
							p2p.get(i).add(Random_Element);
						}
					}
					else
					{
						ArrayList<Integer> Random_Element_Neighbors = new ArrayList<Integer>();
						p2p.put(Random_Element, Random_Element_Neighbors);
						p2p.get(i).add(Random_Element);
						p2p.get(Random_Element).add(i);
					}
					all_nodes_copy.remove(Random_Element);
				}
				
			}
		}
		try { 
			  
            bf = new BufferedWriter(new FileWriter(file));

            for (Map.Entry<Integer, List<Integer>> entry : 
                 p2p.entrySet()) {
            	int rnd_item = new Random().nextInt(items.length);
                int rnd_role = new Random().nextInt(roles.length);
                if(roles[rnd_role] == "seller")
            	{
                	bf.write(entry.getKey() + " "+ entry.getValue().toString().replaceAll("\\s", "") + " " + roles[rnd_role] + " " + items[rnd_item] + " " + 5);
            	}
                if(roles[rnd_role] == "buyer")
            	{
                	bf.write(entry.getKey() + " "+ entry.getValue().toString().replaceAll("\\s", "") + " " + roles[rnd_role] + " " + items[rnd_item]);
            	}
                bf.newLine(); 
            } 
  
            bf.flush(); 
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 
        finally { 
  
            try { 
  
                bf.close(); 
            } 
            catch (Exception e) { 
            } 
        }
	}
	
	public Network(int N, int K, int testcase) {

		if(testcase == 1)
		{
			/* One seller of boar, 3 buyers of boars, the remaining peers have no role.
			 * 0 -> seller of boar
			 * 1,2,3 -> buyer of boar
			 * 4,5 -> no role (initializing as sellers of other objects to maintain the structure of initialization)
			*/

			p2p.put(0, Arrays.asList(4,5));
			p2p.put(1, Arrays.asList(4));
			p2p.put(2, Arrays.asList(4));
			p2p.put(3, Arrays.asList(5));
			p2p.put(4, Arrays.asList(0,1,2));
			p2p.put(5, Arrays.asList(0,3));
			
			
			try { 
				  
	            // create new BufferedWriter for the output file 
	            bf = new BufferedWriter(new FileWriter(file));

	            bf.write(0 + " "+ p2p.get(0).toString().replaceAll("\\s", "") + " " + "seller" + " " + "Boar" + " " + 5);
	            bf.newLine(); 
	            bf.write(1 + " "+ p2p.get(1).toString().replaceAll("\\s", "") + " " + "buyer" + " " + "Boar");
	            bf.newLine(); 
	            bf.write(2 + " "+ p2p.get(2).toString().replaceAll("\\s", "") + " " + "buyer" + " " + "Boar");
	            bf.newLine(); 
	            bf.write(3 + " "+ p2p.get(3).toString().replaceAll("\\s", "") + " " + "buyer" + " " + "Boar");
	            bf.newLine(); 
	            bf.write(4 + " "+ p2p.get(4).toString().replaceAll("\\s", "") + " " + "no_role" + " " + "Fish" + " " + 5);
	            bf.newLine(); 
	            bf.write(5 + " "+ p2p.get(5).toString().replaceAll("\\s", "") + " " + "no_role" + " " + "Salt" + " " + 5);
                bf.newLine();
	  
	            bf.flush(); 
	        } 
	        catch (IOException e) { 
	            e.printStackTrace(); 
	        } 
	        finally { 
	  
	            try { 
	  
	                // always close the writer 
	                bf.close(); 
	            } 
	            catch (Exception e) { 
	            } 
	        }
			
		}
		if(testcase == 2)
		{
			/* One seller of fish, 3 buyers of fish, the remaining peers have no role.
			 * 0 -> seller of fish
			 * 1,2,3 -> buyer of fish
			 * Implementing a race condition between 1,2,3 to buy the single fish which is available with 0
			*/
			
			p2p.put(0, Arrays.asList(1,2,3));
			p2p.put(1, Arrays.asList(0));
			p2p.put(2, Arrays.asList(0));
			p2p.put(3, Arrays.asList(0));
			
			try { 
				  
	            // create new BufferedWriter for the output file 
	            bf = new BufferedWriter(new FileWriter(file));

	            bf.write(0 + " "+ p2p.get(0).toString().replaceAll("\\s", "") + " " + "seller" + " " + "Fish" + " " + 1);
	            bf.newLine(); 
	            bf.write(1 + " "+ p2p.get(1).toString().replaceAll("\\s", "") + " " + "buyer" + " " + "Fish");
	            bf.newLine(); 
	            bf.write(2 + " "+ p2p.get(2).toString().replaceAll("\\s", "") + " " + "buyer" + " " + "Fish");
	            bf.newLine(); 
	            bf.write(3 + " "+ p2p.get(3).toString().replaceAll("\\s", "") + " " + "buyer" + " " + "Fish");
	            bf.newLine();
	  
	            bf.flush();
	        } 
	        catch (IOException e) { 
	            e.printStackTrace(); 
	        } 
	        finally { 
	  
	            try { 
	  
	                // always close the writer 
	                bf.close(); 
	            } 
	            catch (Exception e) { 
	            } 
	        }	
		}	
	}
	
	/*Helper Functions below*/
	
	public Integer getRandomElement(ArrayList<Integer> list)
    {
        Random rand = new Random();
        int randomIndex = rand.nextInt(list.size());
        int response = list.get(randomIndex);
        return response;
    }
	
	public HashMap<Integer, List<Integer>> get_network() {
		return p2p;
	}
	
	public List<Integer> get_associated_peer(int peer_id) {
		return p2p.get(peer_id);
	}

}
