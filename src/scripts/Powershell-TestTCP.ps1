Param(
    [Parameter(Mandatory=$True)]
    [String]$Computername,
    [Int]$Port
    )

$dateTimeLog = (Get-Date -f yyyyMMdd_HHmmss)
#$Port = "443"
$Dest = $Computername
$LogFile = "C:\Users\Mathew\Desktop\Logging-Output\Ping-172.16.0.1-$dateTimeLog.txt"
    
    Write-Host "Starting test..." -ForegroundColor Gray

    #-- Give an initial readout of our situation for the log file and visual
        Test-NetConnection $Dest -Port $Port -WarningAction SilentlyContinue | Tee-Object $LogFile -Append
    
    #-- Move on to simple one line status as it continues testing
    Do {

        $Test = Test-NetConnection $Dest -Port $Port -WarningAction SilentlyContinue

        $dateTime = (Get-Date -f yyyyMMdd_HHmmss)   

        $message = "$dateTime - Testing $Dest : $Port ..."
        Write-Host $message -NoNewline

        If ($Test.TcpTestSucceeded -eq $True) {

            Write-Host " success!" -ForegroundColor Green
            Write-Output "$message success!" | Out-File $LogFile -Append

        } else {
            Write-Host " no response." -ForegroundColor Red
            Write-Output "$message no response." | Out-File $LogFile -Append

        } 
        Start-Sleep 2 
    } Until ($t -eq $False) 


