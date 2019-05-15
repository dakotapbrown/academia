echo off && cls
cd C:\Users\dakot\Documents\School\VCU\'18 Fall\CMSC 401\Assignments\Assignment 1\src
javac MajorityElement.java
javac cmsc401.java

echo **************cmsc401 Output - Test 3**************
java cmsc401 < test3.txt
echo ***************************************************
echo.
echo. 
echo **********MajorityElement Output - Test 3**********
java MajorityElement < test3.txt
echo ***************************************************
echo.
echo. 
echo **************cmsc401 Output - Test 2**************
java cmsc401 < test2.txt
echo ***************************************************
echo.
echo. 
echo **********MajorityElement Output - Test 2**********
java MajorityElement < test2.txt
echo ***************************************************
echo.
echo. 
echo **************cmsc401 Output - Test 1**************
java cmsc401 < test1.txt
echo ***************************************************
echo.
echo. 
echo **********MajorityElement Output - Test 1**********
java MajorityElement < test1.txt
echo ***************************************************
cmd /k

