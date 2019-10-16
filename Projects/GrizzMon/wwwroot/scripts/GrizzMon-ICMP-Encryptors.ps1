$pshost = get-host
$pswindow = $pshost.ui.rawui$pswindow.WindowTitle = "ICMP - Encryptors"while($true) {#--- SET START TIME$stopWatch = [system.diagnostics.stopwatch]::startNew()
$stopWatch.Start()#Variables$CSS = get-content "..\css\theme.css"$dated = (Get-Date -format F)$HTMLHead =  "<Style>$CSS</style>"$HTMLHead = $HTMLHead + "<CENTER><Font size=5>Encryptor CT Interfaces</font></BR>"$hostListFile = "..\cfg\icmp-encryptors.csv"$generatedFile = "..\gen\icmp-encryptors.htm"$publishedFile = "..\gen-pub\icmp-encryptors.htm"
#Check content and ping computers$icmpresults = get-content $hostListFile | Where {-not (($_.StartsWith('#')) -or ($_.split("{,}")[1] -eq $null))} | foreach {     #--- UPTIME FILE VARIABLE    $ipAddress = $_.split("{,}")[1]    $ipAddressFiltered = $ipAddress -replace ":","_"    $uptimeFile = "..\gen-not-monitored\encryptor-uptime\$ipAddressFiltered.uptime.txt"    #--- TRACKING FILE VARIABLE    $trackingDate = Get-Date    $trackingDate = $trackingDate.ToShortDateString()    $trackingDate = "$trackingDate,1"    $uptimeTrackingFile = "..\gen-not-monitored\encryptor-tracker\UptimeData.$ipAddressFiltered.csv"
 if (($ipAddress -ne $null) -and (Test-Connection $ipAddress -count 2 -Quiet))  {  #--- DEVICE IS ONLINE    #--- OUTPUT UPTIME TO LAST_UPTIME FILE    $dated | Out-File $uptimeFile -Force    #--- OUTPUT UPTIME TO DAILY TRACKING FILE        #--- CREATE TRACKING FILE IF IT DOES NOT EXIST            If (Test-Path $uptimeTrackingFile){
            #--- File exists
        }Else{
            #--- File does not exist - create it 
            $noRecordMessage = "sep=,"
            $noRecordMessage | Out-File $uptimeTrackingFile -Force
        }        #--- RETRIVE LAST LINE IN TRACKING FILE        $uptimeFileLastLine = Get-Content $uptimeTrackingFile | Select-Object -Last 1        #--- IF LAST LINE IS NOT TODAY, APPEND TODAYS DATE WITH ",1" FOR GRAPHING        if($uptimeFileLastLine -ne $trackingDate){            $trackingDateChartData = "$trackingDate,1"            $trackingDate | Out-File $uptimeTrackingFile -Append -Force        }        #--- CREATE OBJECT    New-Object psobject -Property @{        Serial = $_.split("{,}")[0]   	    IP = $_.split("{,}")[1]	    Description = $_.split("{,}")[2]	    POC = $_.split("{,}")[3]   	    Status = "Online"        Last_Uptime = $dated        Chart_Data = "<a href=`"../gen-not-monitored/encryptor-tracker/UptimeData.$ipAddressFiltered.csv`">Chart Data</a>"   }   Write-Host "$_ is Online" -ForegroundColor Green }  else  {#--- DEVICE IS DOWN/OFFLINE    #--- CREATE UPTIME FILE IF IT DOESN'T EXIST

        If (Test-Path $uptimeFile){
            #--- File exists - retrieve uptime
            $uptimeFileData = Get-Content $uptimeFile
        }Else{
            #--- File does not exist - create it 
            $noRecordMessage = "No Record"
            $noRecordMessage | Out-File $uptimeFile
            $uptimeFileData = Get-Content $uptimeFile        }     #--- CREATE TRACKING FILE IF IT DOES NOT EXIST            If (Test-Path $uptimeTrackingFile){
            #--- File exists
        }Else{
            #--- File does not exist - create it 
            $noRecordMessage = "sep=,"
            $noRecordMessage | Out-File $uptimeTrackingFile -Force
        }    #--- CREATE OBJECT    New-Object psobject -Property @{        Serial = $_.split("{,}")[0]        IP = $_.split("{,}")[1]	    Description = $_.split("{,}")[2]	    POC = $_.split("{,}")[3]   	    Status = "Offline"        Last_Uptime = $uptimeFileData
        Chart_Data = "<a href=`"../gen-not-monitored/encryptor-tracker/UptimeData.$ipAddressFiltered.csv`">Chart Data</a>"

   }      Write-Host "$_ is Offline" -ForegroundColor Red#Email Notification Script#{$smtpbody = "$machineName, $svcName, $svcState"#$smtpto = (get-content "..\cfg\sendmail.cfg")[1]#$smtpfrom = (get-content "..\cfg\sendmail.cfg")[3]#$smtpsubject = "GrizzMon Notification Host Down. $dated"#$smtpservername = (get-content "..\cfg\sendmail.cfg")[9]#Send-MailMessage -To $smtpto -From $smtpfrom -subject $smtpsubject -Body $smtpbody -SMTPServer $smtpservername
 }
}
$HTMLBody = @"<meta http-equiv="refresh" content="60">
<font size=1>Scanned: $Dated  -  Last scan took: ${minutes}:$seconds</font>"@
# CSV for reports#$countresults1=@()#$dated,$Icmpresults.Server,$Icmpresults.Desc,$Icmpresults.Status | Add-Content "..\reports\icmp.csv"
#---  Export to HTML    Write-Host "EXPORTING TO HTML..." -ForegroundColor Yellow    $icmpresults | ConvertTo-HTML -head $HTMLHead -body $HTMLBody -Property Serial,IP,Description,POC,Status,Last_Uptime,Chart_Data |
    #--- APPLY COLORED CELLS WITH REPLACE
    Foreach {        if ($_ -like "*<td>Online</td>*" ) {            $_ -replace "<tr><td>","<tr bgcolor=#7CBB59><td>"         }        else {        $_ -replace "<tr><td>","<tr bgcolor=#EB2300><td>"        }    } |     Foreach {        #--- MAKE LINKS CLICKABLE        $_ -replace '&gt;','>' -replace '&lt;','<' -replace '&quot;','"'        } |    out-file $generatedFile
#--- REMOVE HEADERS
#(Get-Content $generatedFile).replace("<tr><th>IP</th><th>Location</th><th>POC</th><th>Status</th></tr>","") | Set-Content $generatedFile -Force

#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force

#--- SLEEP#Write-Host "Sleeping 30 seconds..." -ForegroundColor Yellow#Start-Sleep 30

#--- SET VARIABLES FOR NEXT RUN
$minutes = $stopWatch.Elapsed.Minutes
$seconds = $stopWatch.Elapsed.Seconds
#--- DISPLAY TIME THE SCRIPT TOOK TO RUN
$stopWatch.Stop()
Write-Host "Elapsed Runtime:" $stopWatch.Elapsed.Minutes "minutes and" $stopWatch.Elapsed.Seconds "seconds." -ForegroundColor Cyan

}
            
