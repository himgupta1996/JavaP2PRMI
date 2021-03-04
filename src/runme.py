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

testcase_description={1:"Assign one peer to be a buyer of fish and another to be a seller of fish. Ensure that all fish is sold and restocked forever.",
                      2:"Assign one peer to be a buyer of fish and another to be a seller of boar. Ensure that nothing is sold.",
                      3:"Randomly assign buyer and seller roles. Ensure that items keep being sold throughout"}
### Testcases ###
for i in range(1,4):
    print("Running testcase: %s" % (1))
    print("Description: %s" % (testcase_description[i]))

    #Initializing the java processes
    p = subprocess.Popen("java Initializer %s" % (i))

    print("Waiting for 30 secs for some transactions to get completed.")
    time.sleep(30)
    print("Wait time completed. Killing the processes now.")

    ##Getting the process IDs
    output = subprocess.check_output("netstat -ano | findstr :8003", shell = True)
    pIds = str(output).split("\\r")[0].split()[-1]
    
    ##Killing the processes
    os.system("taskkill /PID %s /F"%(pIds))
    print("Terminated the process %s" % (pIds))

    print("Check the log file testcase%s.log for analysing the transactions"%(i))