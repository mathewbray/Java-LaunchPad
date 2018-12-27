@echo off
rem This batch will find out maximum MTU between local and remote host.

SetLocal EnableDelayedExpansion
set /p SERVER=Enter IP/Hostname to sweep:
set /p MTU=Enter starting MTU:
set LASTGOOD=0
set LASTBAD=65536
set PACKETSIZE=28




rem Check server reachability.
ping -n 1 -l 0 -f -4 !SERVER! 1>nul
if !ERRORLEVEL! NEQ 0 (
  echo Error: cannot ping !SERVER!. Run "ping -n 1 -l 0 -f -4 !SERVER!" to see details.
  goto :error
)



:seek
rem Start looking for the maximum MTU.
ping -n 1 -l !MTU! -f -4 !SERVER! 1>nul
echo !MTU! bytes...
if !ERRORLEVEL! EQU 0 (
  echo Good...
  set /A LASTGOOD=!MTU!
  set /A "MTU=(!MTU! + !LASTBAD!) / 2"
  if !MTU! NEQ !LASTGOOD! goto :seek
) else (
  echo Bad...
  set /A LASTBAD=!MTU!  
  set /A "MTU=(!MTU! + !LASTGOOD!) / 2"
  if !MTU! NEQ !LASTBAD! goto :seek
)



rem Print the result.
set /A "MAXMTU=!LASTGOOD! + !PACKETSIZE!"
echo .
echo Maximum MTU for !SERVER!: !MAXMTU! bytes. 
echo *Note: 28 bytes are added to account for 20 IP and 8 ICMP echo request header bytes*

rem Export %MAXMTU% variable.
EndLocal & set MAXMTU=%MAXMTU%
echo.
pause
exit /B 0



:error
rem When something unexpected occurs.
EndLocal & set MAXMTU=-1
exit /B 1