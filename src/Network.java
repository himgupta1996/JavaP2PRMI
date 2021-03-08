import java.util.HashMap;

public class Network {
	HashMap<Integer, int[]> p2p = new HashMap<Integer, int[]>();
	
	//Initializing the 2 node P2P network
	//Describes which peer is connected to which peers
	public Network() {
		int[] x0 = {1,2};
		int[] x1 = {0,2};
		int[] x2 = {0,1};
		p2p.put(0, x0);
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
