import java.util.HashMap;

public class Network {
	HashMap<Integer, int[]> p2p = new HashMap<Integer, int[]>();
	public Network() {
		//Read file and get the peers dictionary
		int[] x0 = {1};
		int[] x1 = {0};
		p2p.put(0, x0);
		p2p.put(1, x1);
	}
	
	public HashMap<Integer, int[]> get_network() {
		return p2p;
	}
	
	public int[] get_associated_peer(int peer_id) {
		return p2p.get(peer_id);
	}

}
