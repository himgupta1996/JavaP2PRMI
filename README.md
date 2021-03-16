This repository contains implementation of the Lab 1 Project of COMPSCI 677 course at UMass Amherst. <br>
Contributors - <br>
Abhishek Lalwani (alalwani@umass.edu) <br>
Himanshu Gupta (hgupta@umass.edu) <br>
Kunal Chakrabarty (kchakrabarty@umass.edu) <br>

Please find the instructions below for testing the implementation.

# Instructions 

### To run locally

1. Change directory to `src`.
2.  Modify the file `src/config.txt` as desired.The first line in this file contains the number of nodes and the maximum number of neighbours of each node. Each subsequent line contains the Node ID, port and IP of each Node.
3. Modify the file `src/network.txt` if you need a specific network of nodes. Otherwise this will be generated randomly. Each line in this file contains the Node ID, a list of peers of this node, it's role, the item it starts with, and the count of the item it starts with(if seller).
4. run `python runme.py`. (see usage for details) 
### To run in EC2 server

1. Create EC2 instances with key pair value, and get the private .pem file. We have used Amazon Linux 2 AMI 2.0.20210303.0 x86_64 HVM gp2 (ami-0fc61db8544a617ed) for testing
2. Edit the security group to ensure that the ports required by the nodes are open
3. Run the command from the terminal (this is to enable password less ssh and install java libraries on the server):
    `ssh -i <pem_file> ec2-user@<ec2_public_ip>` with the private pem file and the public IP address of the EC2 instance that has been set up. (This will add the ec2 server to the known host so that you can ssh from the script without the need of a password)
4. Run the following commands on the EC2 instance to install java libraries:
   + `sudo yum install java-1.8.0-openjdk`
   + `sudo yum install java-devel`
5. Change directory to `src`.
6. Modify the file `src/config.txt` as desired.The first line in this file contains the number of nodes and the maximum number of neighbours of each node. Each subsequent line contains the Node ID, port and IP of each Node. 
7. Modify the file `src/network.txt` if you need a specific network of nodes. Otherwise this will be generated randomly. Each line in this file contains the Node ID, a list of peers of this node, it's role, the item it starts with, and the count of the item it starts with(if seller).
8. Run on local machine where the git repository is cloned: 
   `python runme.py -pem <pem file used to ssh to all the machines>`
   + Mention the -n argument for custom network. (see usage for details). Specify the network file structure.
   + Check the logs in each machine in the folder `~/P2Pnetwork/docs/peer<i>.log`
9. To stop the peers:
   + Note the pid corresponding to the ports used by the processes by running the command: `netstat -tulpn`
   + Use the command to kill : `kill -9 <pid1> <pid2> …`
   + If you wan to test again, make sure to kill the older process by running the command: `python runme.py -pem <pem_file> -k yes`

### Usage of script run_me.py 
usage: runme.py [-h] [-pem PEMFILE] [-n NETWORK] [-k KILL] [-t TIME]

optional arguments:
  -h, --help            show this help message and exit
  -pem PEMFILE, --pemfile PEMFILE
                        Provide RSA Private key file
  -n NETWORK, --network NETWORK
                        Provide the network.txt file to use a custom P2P
                        network.
  -k KILL, --kill KILL  Provide yes to kill the processes on the ports
                        specified in the config file.
  -t TIME, --time TIME  Provide the time in seconds for which you want to run
                        the processes.


 # Source File Descriptions
 
 1. src/Hello.java: This is a interface file that defines the public function of peer object.
 2. src/Node.java: This is a Network Peer file which implements the interface as well as local functions related to the peer.
 3. src/Network.java: This file defines the P2P Network.  
 4. src/Initializer.java: This file is used to instantiate the peer nodes and make them ready to buy or sell items depending on the test case parameter passed.
