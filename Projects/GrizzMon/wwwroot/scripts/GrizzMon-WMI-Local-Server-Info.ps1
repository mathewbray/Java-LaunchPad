$pshost = get-host
$pswindow = $pshost.ui.rawui
$pswindow.WindowTitle = "Local Server Info"
while($true) {

#--- SET START TIME
$stopWatch = [system.diagnostics.stopwatch]::startNew()
$stopWatch.Start()

#--- VARIABLES
$CSS = get-content "C:\inetpub\wwwroot\css\theme.css"
$dated = (Get-Date -format F)
$HTMLHead =  "<Style>$CSS</style>"
$generatedFile = "C:\inetpub\wwwroot\gen\wmi-local-server-info.htm"
$publishedFile = "C:\inetpub\wwwroot\gen-pub\wmi-local-server-info.htm"
$Result = @() 

#--- ACQUIRE SERVER INFO
$AVGProc = Get-WmiObject win32_processor | 
Measure-Object -property LoadPercentage -Average | Select Average
$OS = gwmi -Class win32_operatingsystem  |
Select-Object @{Name = "MemoryUsage"; Expression = {“{0:N2}” -f ((($_.TotalVisibleMemorySize - $_.FreePhysicalMemory)*100)/ $_.TotalVisibleMemorySize) }}
$vol = Get-WmiObject -Class win32_Volume -Filter "DriveLetter = 'C:'" |
Select-object @{Name = "C PercentUsed"; Expression = {“{0:N2}” -f  ((($_.Capacity - $_.FreeSpace ) / $_.Capacity)*100) } }
  
#--- PROCESS RESULTS
$Result += [PSCustomObject] @{ 
        CPULoad = $AVGProc.Average
        MemLoad = $OS.MemoryUsage
        CDrive = $vol.'C PercentUsed'
    }

$Outputreport = "<HTML><TITLE> Server Health Report </TITLE>
                    <style>$CSS .fonttable, .fonttable TD, .fonttable TH
                    {
                    font-size:8pt;
                    padding-top:0px;
                    padding-bottom:0px;
                    padding-left:3px;
                    padding-right:3px;
                    text-align:center;
                    }
                    </style>
                    <meta http-equiv=""refresh"" content=""60"">
                    <Table class=""fonttable"" style=""vertical-align:top; margin-left: 0px; margin-top: -8px;  margin-bottom: -5px; float:left; "" BORDER=0>
                    <TR bgcolor=#bcbcab align=center>
                    <TD>CPU</TD>
                    <TD>Memory</TD>
                    <TD>C:</TD></TR>"
                        
    Foreach($Entry in $Result) 
    { 
          #convert raw data to percentages
          $CPUAsPercent = "$($Entry.CPULoad)%"
          $MemAsPercent = "$($Entry.MemLoad)%"
          $CDriveAsPercent = "$($Entry.CDrive)%"

          $OutputReport += "<TR>"

          # check CPU load
          if(($Entry.CPULoad) -ge 80) 
          {
              $OutputReport += "<TD bgcolor=#EB2300 align=center>$($CPUAsPercent)</TD>"
          } 
          elseif((($Entry.CPULoad) -ge 70) -and (($Entry.CPULoad) -lt 80))
          {
              $OutputReport += "<TD bgcolor=#FFCB02 align=center>$($CPUAsPercent)</TD>"
          }
          else
          {
              $OutputReport += "<TD bgcolor=#7CBB59 align=center>$($CPUAsPercent)</TD>" 
          }

          # check RAM load
          if(($Entry.MemLoad) -ge 80)
          {
              $OutputReport += "<TD bgcolor=#EB2300 align=center>$($MemAsPercent)</TD>"
          }
          elseif((($Entry.MemLoad) -ge 70) -and (($Entry.MemLoad) -lt 80))
          {
              $OutputReport += "<TD bgcolor=#FFCB02 align=center>$($MemAsPercent)</TD>"
          }
          else
          {
              $OutputReport += "<TD bgcolor=#7CBB59 align=center>$($MemAsPercent)</TD>"
          }

          # check C: Drive Usage
          if(($Entry.CDrive) -ge 80)
          {
              $OutputReport += "<TD bgcolor=#EB2300 align=center>$($CDriveAsPercent)</TD>"
          }
          elseif((($Entry.CDrive) -ge 70) -and (($Entry.CDrive) -lt 80))
          {
              $OutputReport += "<TD bgcolor=#FFCB02 align=center>$($CDriveAsPercent)</TD>"
          }
          else
          {
              $OutputReport += "<TD bgcolor=#7CBB59 align=center>$($CDriveAsPercent)</TD>"
          }

          $OutputReport += "</TR>"
    }

    $OutputReport += "</Table></BODY></HTML>" 
        
 
$Outputreport | out-file $generatedFile

#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force

#--- DISPLAY TIME THE SCRIPT TOOK TO RUN
$stopWatch.Stop()
Write-Host "Elapsed Runtime:" $stopWatch.Elapsed.Minutes "minutes and" $stopWatch.Elapsed.Seconds "seconds." -ForegroundColor Cyan

#--- SLEEP 60 SECONDS
Write-Host "Sleeping 60 seconds..." -ForegroundColor Yellow
Start-Sleep 60


}

#--- SEND EMAIL  
#$smtpServer = "yoursmtpserver.com"
#$smtpFrom = "fromemailaddress@test.com"
#$smtpTo = "receipentaddress@test.com"
#$messageSubject = "Servers Health report"
#$message = New-Object System.Net.Mail.MailMessage $smtpfrom, $smtpto
#$message.Subject = $messageSubject
#$message.IsBodyHTML = $true
#$message.Body = "<head><pre>$style</pre></head>"
#$message.Body += Get-Content C:\scripts\test.htm
#$smtp = New-Object Net.Mail.SmtpClient($smtpServer)
#$smtp.Send($message)
