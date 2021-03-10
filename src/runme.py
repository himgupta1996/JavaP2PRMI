# -*- coding: utf-8 -*-
"""
Created on Wed Mar  3 16:05:24 2021
@author: Himanshu
"""
import os
import subprocess
import time
### Compilation ###
print("Compiling the java files.")
os.system("javac *.java")
print("Compilation successfull.")
time.sleep(2)

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

# for line in lines:

for i in range(0,3):
    print("Running testcase: %s" % (i))
    print("Description: %s" % (testcase_description[i]))

    if(i==1):
        os.system("java Initializer %s" % (i))
        for j in range(0,6):
            subprocess.Popen("java Node %s %s" % (j,i))
        print("Waiting for 30 secs for some transactions to get completed.")
        time.sleep(30)
        print("Wait time completed. Killing the processes now.")
        for j in range(0,6):
            output = subprocess.check_output("netstat -ano | findstr :"+str(ports[j]), shell = True)
            pIds = str(output).split("\\r")[0].split()[-1]
            
            ##Killing the processes
            os.system("taskkill /PID %s /F"%(pIds))
            print("Terminated the process %s" % (pIds))

    if(i==2):
        os.system("java Initializer %s" % (i))
        for j in range(0,4):
            subprocess.Popen("java Node %s %s" % (j,i))
        print("Waiting for 5 secs for some transactions to get completed.")
        time.sleep(5)
        print("Wait time completed. Killing the processes now.")
        for j in range(0,4):
            output = subprocess.check_output("netstat -ano | findstr :"+str(ports[j]), shell = True)
            pIds = str(output).split("\\r")[0].split()[-1]
            
            ##Killing the processes
            os.system("taskkill /PID %s /F"%(pIds))
            print("Terminated the process %s" % (pIds))
        
    else:
        os.system("java Initializer")
        n=int(lines[0][0])
        for j in range(0,n):
            subprocess.Popen("java Node %s %s" % (j,i))
        print("Waiting for 30 secs for some transactions to get completed.")
        time.sleep(30)
        print("Wait time completed. Killing the processes now.")
        for j in range(0,4):
            output = subprocess.check_output("netstat -ano | findstr :"+str(ports[j]), shell = True)
            pIds = str(output).split("\\r")[0].split()[-1]
            
            ##Killing the processes
            os.system("taskkill /PID %s /F"%(pIds))
            print("Terminated the process %s" % (pIds))



# for i in range(1,3):
#     print("Running testcase: %s" % (i))
#     p = subprocess.Popen("java Initializer %s" % (i))
# ### Testcases ###
# for i in range(1,4):
#     print("Running testcase: %s" % (1))
#     print("Description: %s" % (testcase_description[i]))

#     #Initializing the java processes
#     p = subprocess.Popen("java Initializer %s" % (i))

#     print("Waiting for 30 secs for some transactions to get completed.")
#     time.sleep(30)
#     print("Wait time completed. Killing the processes now.")

#     ##Getting the process IDs
#     output = subprocess.check_output("netstat -ano | findstr :8003", shell = True)
#     pIds = str(output).split("\\r")[0].split()[-1]
    
#     ##Killing the processes
#     os.system("taskkill /PID %s /F"%(pIds))
#     print("Terminated the process %s" % (pIds))

#     print("Check the log file testcase%s.log for analysing the transactions"%(i))