import java.util.HashMap;

public class Network {
	HashMap<Integer, int[]> p2p = new HashMap<Integer, int[]>();
	public Network() {
		//Read file and get the peers dictionary
		int[] x1= {2};
		int[] x2= {1};
		p2p.put(1, x1);
		p2p.put(2, x2);
	}
	
	public HashMap<Integer, int[]> get_network() {
		
		return p2p;
		
	}
	
	public int[] get_associated_peer(int peer_id) {
		return p2p.get(peer_id);
	}

}
