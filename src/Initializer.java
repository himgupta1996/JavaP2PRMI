import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.registry.LocateRegistry; 
import java.rmi.registry.Registry;  
import java.util.logging.*;

public class Initializer {
	public static int N = 0;
	public static int K = 0;
	static {
		// Initializing the config Hashmap once the class loads from the config file
		// config_lookup is a static variable of the Node class which is used to lookup port number and the corresponding ip for a given node
		try {
			File config = new File("config.txt");
			Scanner configScanner = new Scanner(config);
			String[] first_data = configScanner.nextLine().split(" ", 2);;
			N = Integer.parseInt(first_data[0]);
			K = Integer.parseInt(first_data[1]);
			configScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;

		/*  Random initialization based upon the value of K and N specified in the config file */

			String filename = "../docs/description.log";
			Network network = new Network(N,K);
			// Setting up the logger;
			try {

				fh = new FileHandler(filename);
				logger.addHandler(fh);
				fh.setFormatter(new MyFormatter());
				logger.info("Logger Initialized");

			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info("Default Testcase:");
			logger.info("Constructing a random network based on the value of N and K specified in the config file.");
	}
}
