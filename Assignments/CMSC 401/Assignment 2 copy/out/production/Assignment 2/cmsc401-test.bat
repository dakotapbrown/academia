echo off && cls
cd C:\Users\dakot\Documents\School\VCU\'18 Fall\CMSC 401\Assignments\Assignment 2\src
javac MedianFinder.java
javac cmsc401.java

echo **************cmsc401 Output - Test 1**************
java cmsc401 < test1.txt
echo ***************************************************
echo.
echo. 
echo *************Expected Output - Test 1**************
java MedianFinder < test1.txt
echo ***************************************************
echo.
echo. 
echo.
echo. 
echo **************cmsc401 Output - Test 2**************
java cmsc401 < test2.txt
echo ***************************************************
echo.
echo. 
echo *************Expected Output - Test 2**************
java MedianFinder < test2.txt
echo ***************************************************
echo.
echo.
echo. 
echo. 
echo **************cmsc401 Output - Test 3**************
java cmsc401 < test3.txt
echo ***************************************************
echo.
echo. 
echo *************Expected Output - Test 3**************
java MedianFinder < test3.txt 
echo ***************************************************
echo. 
echo. 
echo.
echo. 
echo **************cmsc401 Output - Test 4**************
java cmsc401 < test4.txt
echo ***************************************************
echo.
echo. 
echo *************Expected Output - Test 4**************
java MedianFinder < test4.txt 
echo ***************************************************
echo. 
echo. 
echo.
echo. 
echo **************cmsc401 Output - Test 5**************
java cmsc401 < test5.txt
echo ***************************************************
echo.
echo. 
echo *************Expected Output - Test 5**************
java MedianFinder < test5.txt 
echo ***************************************************
pause
exit

