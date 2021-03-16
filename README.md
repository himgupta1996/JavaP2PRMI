This repository contains implementation of the Lab 1 Project of COMPSCI 677 course at UMass Amherst. <br>
Contributors - <br>
Abhishek Lalwani (alalwani@umass.edu) <br>
Himanshu Gupta (hgupta@umass.edu) <br>
Kunal Chakrabarty (kchakrabarty@umass.edu) <br>

Please find the instructions below for testing the implementation.

# System requirement

Local machine (Windows),
Ec2 servers (Linux)

# Source File Descriptions
 
 1. src/Hello.java: This is a interface file that defines the public function of peer object.
 2. src/Node.java: This is a Network Peer file which implements the interface as well as local functions related to the peer. Running the command `java Node <peer_id>` instantiate a specific peer.
 3. src/Network.java: This file defines the P2P Network.  
 4. src/Initializer.java: This file reads the config.txt file and creates a network and put the network in the fie `src/network.txt`.
 5. src/config.txt: The first line in this file contains the number of peers in the network (N) and the maximum number of neighbours of each peer (k). Each subsequent line contains the peer ID, port and IP of each peer(For Example: '1 8011 127.0.0.1' if peer 1 is running on local machine).
 6. src/network.txt: Each line in this file contains information of a specific peer. It contains the Peer ID, a list of neighboring peers, role of the peer, the item peer wants to buy or sell, and the count of the item it starts with(if seller). (For Example:0 [3,5] buyer Salt).

# Instructions 

### To run the network locally

1.  Go to directory `src` which contains the source and config files.
2.  Modify the file `src/config.txt` as desired. 
3. Modify the file `src/network.txt` if you need a specific network of peers. Otherwise this will be generated randomly.
4. Run the follwoing command in the `src` directory:
   `python runme.py`.  
_USECASES:  _
a. To run the network for <T> seconds: 'python runme.py -t <T>'  
b. To provide a custom network: 'python runme.py -n <network.txt file path>'  
c. To kill existing processes: 'python runme.py -k yes'  
(Important: See the section `Usage of script runme.py` for more details.) 

### To run in EC2 servers present in same network

1. Create EC2 instances with key pair value, and get the private .pem file (We used Amazon Linux 2 AMI 2.0.20210303.0 x86_64 HVM gp2 (ami-0fc61db8544a617ed) for testing).
2. Edit the security group to ensure that the ports required by the peers to communicate are open.
3. Run the following command from the local terminal (this is to set password-less ssh from the local machine to the ec2 servers):
    `ssh -i <pem_file_path> ec2-user@<ec2_public_ip>` with the private pem file and the public IP address of the EC2 instance that has been set up. (This will add the ec2 server to the known_host file so that you can ssh from the script without the need of a password).
4. Run the following commands on the EC2 instance to install java libraries:
   + `sudo yum install java-1.8.0-openjdk`
   + `sudo yum install java-devel`
5. Go to directory `src` which contains the source and config files.
6. Modify the file `src/config.txt` as desired.
7. Modify the file `src/network.txt` if you need a specific network of peers. Otherwise this will be generated randomly.
8. Run the following command in `src` directory:   
   `python runme.py -pem <pem file used to ssh to all the machines>`
9. To check the logs of peers specific to each machine, go to the folder `~/P2Pnetwork/docs/` in the EC2 servers and look for the log file `peer<i>.log`.  
_USECASES:_  
a. To run the network for <T> seconds: 'python runme.py -pem <per_file_location> -t <T>'  
b. To provide a custom network: 'python runme.py -pem <pem_file_location> -n <network.txt_file_location>'  
c. To kill existing processes on remote: 'python runme.py -pem <per_file_location> -k yes'  

### Usage of script runme.py 
usage: runme.py [-h] [-pem PEMFILE] [-n NETWORK] [-k KILL] [-t TIME]

optional arguments:
  -h, --help            show this help message and exit  
  -pem PEMFILE, --pemfile PEMFILE Provide RSA Private key file  
  -n NETWORK, --network NETWORK Provide the network.txt file to use a custom P2P network.  
  -k KILL, --kill KILL  Provide yes to kill the processes on the ports specified in the config file.  
  -t TIME, --time TIME  Provide the time in seconds for which you want to run the processes.  

 
