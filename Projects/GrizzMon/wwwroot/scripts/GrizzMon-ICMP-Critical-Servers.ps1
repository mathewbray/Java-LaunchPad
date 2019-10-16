$pshost = get-host
$pswindow = $pshost.ui.rawui$pswindow.WindowTitle = "ICMP - Critical Servers"while($true) {#--- SET START TIME$stopWatch = [system.diagnostics.stopwatch]::startNew()
$stopWatch.Start()#Variables$CSS = get-content "..\css\theme.css"$dated = (Get-Date -format F)$HTMLHead =  "<Style>$CSS</style>"$HTMLHead = $HTMLHead + "<CENTER><Font size=5>Critical Servers</font></BR>"$hostListFile = "..\cfg\icmp-critical-servers.csv"$generatedFile = "..\gen\icmp-critical-servers.htm"$publishedFile = "..\gen-pub\icmp-critical-servers.htm"
#Check content and ping computers$icmpresults = get-content $hostListFile | Where {-not ($_.StartsWith('#'))} | foreach {  if (test-connection $_.split("{,}")[0] -count 2 -Quiet)  { New-Object psobject -Property @{   	IP = $_.split("{,}")[0]	Description = $_.split("{,}")[1]   	Status = "Online"       }   Write-Host "$_ is Online" -ForegroundColor Green }  else  {New-Object psobject -Property @{   	IP = $_.split("{,}")[0]	Description = $_.split("{,}")[1]   	Status = "Offline" 

   }      Write-Host "$_ is Offline" -ForegroundColor Red#Email Notification Script#{$smtpbody = "$machineName, $svcName, $svcState"#$smtpto = (get-content "..\cfg\sendmail.cfg")[1]#$smtpfrom = (get-content "..\cfg\sendmail.cfg")[3]#$smtpsubject = "GrizzMon Notification Host Down. $dated"#$smtpservername = (get-content "..\cfg\sendmail.cfg")[9]#Send-MailMessage -To $smtpto -From $smtpfrom -subject $smtpsubject -Body $smtpbody -SMTPServer $smtpservername
 }
}


$HTMLBody = @"<meta http-equiv="refresh" content="60">
<font size=1>Scanned: $Dated  -  Last scan took: ${minutes}:$seconds</font>"@
# CSV for reports#$countresults1=@()#$dated,$Icmpresults.Server,$Icmpresults.Desc,$Icmpresults.Status | Add-Content "..\reports\icmp.csv"
#  EXPORT TO HTML AND APPLY COLORED CELLS WITH REPLACEWrite-Host "EXPORTING TO HTML..." -ForegroundColor Yellow$icmpresults | ConvertTo-HTML -head $HTMLHead -Property IP,Description,Status -body $HTMLBody  |
Foreach {    if ($_ -like "*<td>Online</td>*" ) {        $_ -replace "<tr><td>","<tr bgcolor=#7CBB59><td>"     }    else {    $_ -replace "<tr><td>","<tr bgcolor=#EB2300><td>"    }} | out-file $generatedFile
#--- REMOVE HEADERS
(Get-Content $generatedFile).replace("<tr><th>IP</th><th>Description</th><th>Status</th></tr>","") | Set-Content $generatedFile -Force

#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force

#--- SLEEPWrite-Host "Sleeping 30 seconds..." -ForegroundColor YellowStart-Sleep 30

#--- SET VARIABLES FOR NEXT RUN
$minutes = $stopWatch.Elapsed.Minutes
$seconds = $stopWatch.Elapsed.Seconds

#--- DISPLAY TIME THE SCRIPT TOOK TO RUN
$stopWatch.Stop()
Write-Host "Elapsed Runtime:" $stopWatch.Elapsed.Minutes "minutes and" $stopWatch.Elapsed.Seconds "seconds." -ForegroundColor Cyan

}
            
