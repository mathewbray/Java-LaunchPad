$DesktopPath = [Environment]::GetFolderPath("Desktop")
$TimeStamp = Get-Date
$TimeStamp = $TimeStamp.ToString('MM-dd-yyyy_hh-mm-ss')
#$CSVFile = ".\output_$TimeStamp.csv"

# INITIAL MENU
    #  Output Type
        $Title = "Choose the Output Type..."
        $Info = "Do you want Response Time (for graphing) or simple UP/DOWN output???"
        $options = [System.Management.Automation.Host.ChoiceDescription[]] @("&Response Time", "&UP/DOWN")
        [int]$defaultchoice = 1
        $opt =  $host.UI.PromptForChoice($Title , $Info , $Options,$defaultchoice)

    #-- Seconds between Ping sessions
        $Seconds = Read-Host -Prompt 'Enter Seconds Between Ping Sessions (Default 300 (5min)): '
        if($Seconds -eq $null){ $Seconds = 300 }
        if($Seconds -eq ""){ $Seconds = 300 }
        Write-Host Will pause every $Seconds Seconds...

    #-- IP List
        $workstations = Read-Host -Prompt "IP List File ($DesktopPath\IP-List.txt): "
        if($workstations -eq $null){ $workstations = "$DesktopPath\IP-List.txt" }
        if($workstations -eq ""){ $workstations = "$DesktopPath\IP-List.txt" }

    #-- Output File
        $CSVFile = Read-Host -Prompt "IP List File ($DesktopPath\output_$TimeStamp.csv): "
        if($CSVFile -eq $null){ $CSVFile = "$DesktopPath\output_$TimeStamp.csv" }
        if($CSVFile -eq ""){ $CSVFile = "$DesktopPath\output_$TimeStamp.csv" }

        #cls
        Write-Host ""
        Write-Host ""
        Write-Host ""
        Write-Host ""
        Write-Host ""
        Write-Host ""

        

# LOAD IP-LIST
    #$workstations = ".\IP-List.txt"
    $Computers = (Get-Content $workstations) -notmatch '^#' 



# LOOP
while($true) {
    $Now = Get-Date
    $ResultObject = New-Object PSObject
    $ResultObject | Add-Member NoteProperty Time $Now
    $i = 1
    foreach($Computer in $Computers){
        Write-Progress -Activity "Ping Test" -Status "Pinging $Computer" -PercentComplete ($i/$Computers.Count*100)
        $TestResult = Test-Connection $Computer -Count 1 -ErrorAction SilentlyContinue
        if($TestResult.ResponseTime -eq $null){
            switch($opt)
                {
                0 { $ResponseTime = -5 }
                1 { $ResponseTime = "DOWN" }
                }
            
        } else {
            
            switch($opt)
                {
                0 { $ResponseTime = $TestResult.ResponseTime }
                1 { $ResponseTime = "up" }
                }
        }
        $ResultObject | Add-Member NoteProperty $Computer $ResponseTime
        $i++
    }
    Write-Progress -Activity "Ping Test" -Status "Write results to file" -PercentComplete 100
    $ResultObject | Format-Table -AutoSize
    Export-Csv -InputObject $ResultObject -Path $CSVFile -Append -NoTypeInformation
    

    for($DelayTime=0; $DelayTime -lt $Seconds; $DelayTime++){
        Write-Progress -Activity "Ping Test" -Status "Sleeping for $Seconds seconds..." -SecondsRemaining ($SleepTime-$DelayTime)
        Start-Sleep 1
    }
}