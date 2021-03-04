This repository contains the Milestone 1 implementation of the Lab 1 Project of 677 course at UMass Amherst. <br>
Contributors - <br>
Abhishek Lalwani (alalwani@umass.edu) <br>
Himanshu Gupta (hgupta@umass.edu) <br>
Kunal Chakrabarty (kchakrabarty@umass.edu) <br>

Please find the instructions below for testing the implementation.

# Instructions 

1. Change directory to `src`.
2. Execute the run_me testcase file with `python run_me.py` (Make sure you have Java installed on your system).

  
 # Expected Output
 
 1. All the java files will be compiled.
 2. Once compilation is successful, all testcases are run sequentially.
 3. Each test case runs for approximately 30 seconds, during which the transactions keep happening and seller restocks items when it runs out of them.
 4. In each testcase, all transactions are logged in files separated on the basis of test cases and displayed as output.

 # Source File Descriptions
 
 1. src/Hello.java: This is a interface file that defines the public function of peer object.
 2. src/Node.java: This is a Network Peer file which implements the interface as well as local functions related to the peer.
 3. src/Network.java: This file defines the P2P Network.  
 4. src/Initializer.java: This file is used to instantiate the peer nodes and make them ready to buy or sell items.
