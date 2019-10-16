     $servers = Get-Content C:\TECH\Scripts\PowerShell\PSNetMon\cfg\porthosts.cfg
    $ports = Get-Content C:\TECH\Scripts\PowerShell\PSNetMon\cfg\portcfg.cfg
    $ServerPortStatus = “”

    ForEach ($Server in $Servers){
    ForEach ($Port in $Ports){
    $socket = new-object Net.Sockets.TcpClient
    $socket.Connect($server, $port)

    if ($socket.Connected) {
    $status = “Open”
    $socket.Close()
    }
    else {
    $status = “Closed”
    }
    $PortStat = $Server + “,” + $Port + “,” + $Status + “`n”
    $ServerPortStatus += $portStat

    }

    }

    $ServerPortStatus | Out-File C:\TECH\Scripts\PowerShell\PSNetMon\ServerPortStatus.htm 