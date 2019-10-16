#--- OPTIONS THAT MAY BE NEEDED
# powershell.exe  -NoLogo -executionpolicy bypass

# HTML FILE UPDATE STATUS
start powershell.exe  C:\inetpub\wwwroot\scripts\PSNetMonHtmlFileUpdateStatus.ps1
Start-Sleep 3

# ICMP - CRITICAL DEVICE
start powershell.exe  C:\inetpub\wwwroot\scripts\PSNetMonICMPScript-Critical-Devices.ps1
Start-Sleep 3
    
# ICMP - SERVERS
start powershell.exe  C:\inetpub\wwwroot\scripts\PSNetMonICMPScript-Servers.ps1
Start-Sleep 3

# ICMP - TACLANES
start powershell.exe  C:\inetpub\wwwroot\scripts\PSNetMonICMPScript-Taclanes.ps1
Start-Sleep 3

# CHECK TOTAL ONLINE TACLANES
start powershell.exe   C:\inetpub\wwwroot\scripts\PSNetMonCheckTotalOnline-Taclanes.ps1
Start-Sleep 3

# REMOTE PORTS CHECKER
start powershell.exe  C:\inetpub\wwwroot\scripts\PSNetMonPortScript-Open-Ports-Services-Test.ps1
Start-Sleep 3

# 
#start powershell.exe C:\inetpub\wwwroot\scripts\
#Start-Sleep 3