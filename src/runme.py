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
### Testcases ###
for i in range(1,4):
    print("Running testcase: %s" % (1))
    p = subprocess.Popen("java Initializer %s" % (i))
    # subprocess.call("java Initializer", shell=True)
    # os.system("java Initializer")
    print("Waiting for 30 secs for some transactions to get completed.")
    time.sleep(30)
    print("Wait time completed. Killing the processes now.")
    output = subprocess.check_output("netstat -ano | findstr :8003", shell = True)
    pId = str(output).split("\\r")[0].split()[-1]
    print("The process is %s" % (pId))
    os.system("taskkill /PID %s /F"%(pId))
    print("Terminated the process %s" % (pId))
    print("Check the log file testcase%s.log for analysing the transactions"%(i))