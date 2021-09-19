@echo off
javac ControlArduino.java 2> err.txt
ECHO ERRORLEVEL = %ERRORLEVEL%
IF ERRORLEVEL 1 GOTO ERR
IF %ERRORLEVEL% EQU 0 GOTO OK
GOTO FINE
:OK
java ControlArduino
GOTO FINE
:ERR
more err.txt
del err.txt
:FINE