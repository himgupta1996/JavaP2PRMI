# -*- coding: utf-8 -*-
"""
Created on Wed Mar  3 16:05:24 2021
@author: Himanshu , Kunal, Abhishek 
"""

#importing libraries
import os
import subprocess
import time
import sys
import re
import pexpect
import pexpect.popen_spawn
import argparse

# Construct the argument parser
ap = argparse.ArgumentParser()
# Add the arguments to the parser
ap.add_argument("-pem", "--pemfile", required=False,
   help="Provide RSA Private key file")
ap.add_argument("-n", "--network", required=False,
   help="Provide the network.txt file to use a custom P2P network.")
ap.add_argument("-k", "--kill", required=False, default="no",
   help="Provide yes to kill the processes on the ports specified in the config file.")
ap.add_argument("-t", "--time", required=False,
   help="Provide the time in seconds for which you want to run the processes.")

args = vars(ap.parse_args())

# Marking the pem file location from the argument passed
pem_file = ""
if args['pemfile']:
    print("The pem file passed is %s" % (args['pemfile']))
    pem_file = args['pemfile']

#Reading the config file where the IPs and Ports of different peers are mentioned
with open('config.txt', 'r') as myfile:
    lines = myfile.readlines()

# getting the peers details in a dictionary
nodes={}

for j in range(len(lines)):
    if(j==0):
        continue
    line_args = re.split('\s', lines[j])
    nodes[line_args[0]] = {}
    nodes[line_args[0]]['port']=line_args[1]
    nodes[line_args[0]]['ip']=line_args[2]

## Getting number of nodes and maximun number of possible connections from each peer
N = lines[0].split(" ")[0]

if args['kill'] == "no":
    ## Generating the network.txt file which defines the P2P Network if a custom file is not file in the arguments
    if not args['network']:
        os.system("javac *.java")
        os.system("java Initializer")

    for node, node_info in nodes.items():
        
        ##If the node is to be deployed on the remote server
        if node_info['ip'] not in ["localhost", "127.0.0.1", "0.0.0.0"]:

            if pem_file == '':
                raise("Provide the PEM file to run the remote commands. usage: runme.py [-h] [-pem PEMFILE] [-n NETWORK] [-k KILL] [-t TIME]")

            node_info['user_name'] = 'ec2-user'

            '''Pushing the config.txt, network.txt and all the source files to the specified remote instances'''
        
            command = r'ssh -i '+ pem_file + ' ' + node_info['user_name'] + '@' + node_info['ip']
            child = pexpect.popen_spawn.PopenSpawn(command)
            try:
                child.expect('$')
            except Exception as e:
                print("Log in failed for the remote server %s.")
                print("Please make sure the remote host is added to the known_hosts file in .ssh folder.")

            print("###### Deploying the code on the server %s ######" % (node_info['ip']))
            child.sendline('mkdir P2Pnetwork\n')
            child.expect("$")
            child.sendline('mkdir P2Pnetwork/src\n')
            child.expect("$")
            child.sendline('mkdir P2Pnetwork/docs\n')
            child.expect("$")

            command2 = 'scp -i '+ pem_file +' *.* ' +node_info['user_name'] + '@' + node_info['ip']+':/home/ec2-user/P2Pnetwork/src/\n'
            time.sleep(5)        
            print(command2)
            os.system(command2)
            os.system(command2)
            print("###### Successfully deployed the code on the server %s ######" % (node_info['ip']))

            print("###### Compiling java files and starting the peer %s run ######" % (node))
            child.sendline('cd /home/ec2-user/P2Pnetwork/src\n')
            child.expect('$')

            child.sendline('javac *.java\n')
            child.expect('$')
            child.sendline('java Node '+ node +'\n')
            try:
                child.expect("Ready")
                print("###### Successfully started the node %s on server %s ######"%(node, node_info['ip']))
            except Exception as e:
                print("?????? The node %s failed to start on server %s ??????"%(node, node_info['ip']))
        else:
            # os.system("javac *.java")
            subprocess.Popen("java Node %s" % (node), shell = True)
            print("###### Started the peer %s on local machine ######" % (node))

    print("------ All the nodes Successfully deployed. ------")

    if args["time"]:
        print("###### Waiting for the nodes to run for %s seconds." % (args["time"]))
        time.sleep(int(args["time"]))
        args["kill"] = "yes"

if args['kill'] == "yes":
    print("###### Terminating all the peers ######")
    for node, node_info in nodes.items():
        if node_info['ip'] not in ["localhost", "127.0.0.1", "0.0.0.0"]:
            node_info['user_name'] = 'ec2-user'
            command = r'ssh -i '+ pem_file + ' ' + node_info['user_name'] + '@' + node_info['ip']
            child = pexpect.popen_spawn.PopenSpawn(command)
            try:
                child.expect('$')
            except Exception as e:
                print("Log in failed for the remote server %s.")
                print("Please make sure the remote host is added to the known_hosts file in .ssh folder.")
            child.sendline('sudo kill -9 $(sudo lsof -t -i:%s)\n' % (node_info['port']))
            child.expect("$")
            print("Terminated the peer %s" % (node))
        else:
            output = subprocess.check_output("netstat -ano | findstr :"+node_info['port'], shell = True)
            pIds = str(output).split("\\r")[0].split()[-1]
        
            ##Killing the processes
            os.system("taskkill /PID %s /F"%(pIds))
            print("Terminated the peer %s" % (node))
