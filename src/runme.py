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
### Compilation ###
print("Compiling the java files.")
os.system("javac *.java")
print("Compilation successfull.")
time.sleep(2)

#checking if random argument has been passed
if(len(sys.argv)>1):
    k=0
else:
    k=1

# mapping from ports to nodes running on those ports
ports={}

with open('config.txt', 'r') as myfile:
    lines = myfile.readlines()

for j in range(len(lines)):
    if(j==0):
        continue
    ports[lines[j].split(' ')[0]]=lines[j].split(' ')[1]


testcase_description={0:"Constructing a random network based on the value of N and K specified in the config file.",
                      1:"One seller of boar, 3 buyers of boars, the remaining peers have no role. Fix the neighborhood structure so that buyers and sellers are 2-hop away in the peer-to-peer overlay network. Ensure that all items are sold and restocked and that all buyers can buy forever.",
                      2:"Simulate a race condition in buy() wherein a seller has stock of 1 for an item but then replies to multiple buyers."}


if(k==0):
    print("Running testcase: %s" % (0))
    print("Description: %s" % (testcase_description[0]))
    os.system("java Initializer")
    n=int(lines[0].split(" ")[0])
    #initiating the n nodes 
    for j in range(0,n):
        subprocess.Popen("java Node %s %s" % (j,0))
    print("Waiting for 20 secs for some transactions to get completed.")
    time.sleep(40)
    print("Wait time completed. Killing the processes now.")
    for j in range(0,n):
        output = subprocess.check_output("netstat -ano | findstr :"+ports[str(j)], shell = True)
        pIds = str(output).split("\\r")[0].split()[-1]
        
        ##Killing the processes
        os.system("taskkill /PID %s /F"%(pIds))
        print("Terminated the process %s" % (pIds))
    
    print("Check the logfiles in logs/testcase%s for analysing the transactions" % (0))

else:
    for i in range(k,3):
        print("Running testcase: %s" % (i))
        print("Description: %s" % (testcase_description[i]))

        if(i==1):
            
            os.system("java Initializer %s" % (i))
            for j in range(0,6):
                subprocess.Popen("java Node %s %s" % (j,i))
            print("Waiting for 20 secs for some transactions to get completed.")
            time.sleep(40)
            print("Wait time completed. Killing the processes now.")
            for j in range(0,6):
                output = subprocess.check_output("netstat -ano | findstr :"+ports[str(j)], shell = True)
                pIds = str(output).split("\\r")[0].split()[-1]
                
                ##Killing the processes
                os.system("taskkill /PID %s /F"%(pIds))
                print("Terminated the process %s" % (pIds))

        if(i==2):
            order=[1,2,3,0]
            os.system("java Initializer %s" % (i))
            for j in order:
                subprocess.Popen("java Node %s %s" % (j,i))
            print("Waiting for 5 secs for some transactions to get completed.")
            time.sleep(5)
            print("Wait time completed. Killing the processes now.")
            for j in order:
                output = subprocess.check_output("netstat -ano | findstr :"+ports[str(j)], shell = True)
                pIds = str(output).split("\\r")[0].split()[-1]
                
                ##Killing the processes
                os.system("taskkill /PID %s /F"%(pIds))
                print("Terminated the process %s" % (pIds))
        
        print("Check the logfiles in logs/testcase%s for analysing the transactions" % (i))
            
        
