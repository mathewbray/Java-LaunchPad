$pshost = get-host
$pswindow = $pshost.ui.rawui$pswindow.WindowTitle = "ICMP - Taclanes"while($true) {#--- SET START TIME$stopWatch = [system.diagnostics.stopwatch]::startNew()
$stopWatch.Start()#Variables$CSS = get-content "C:\inetpub\wwwroot\css\theme.css"$dated = (Get-Date -format F)$HTMLHead =  "<Style>$CSS</style>"$HTMLHead = $HTMLHead + "<CENTER><Font size=5>Taclane CT Interfaces</font></BR>"$hostListFile = "C:\inetpub\wwwroot\cfg\icmp-taclanes-master.cfg"$generatedFile = "C:\inetpub\wwwroot\gen\icmptaclanes.htm"$publishedFile = "C:\inetpub\wwwroot\gen-pub\icmptaclanes.htm"
#Check content and ping computers$icmpresults = get-content $hostListFile | Where {-not ($_.StartsWith('#'))} | foreach {  if (test-connection $_.split("{,}")[0] -count 2 -Quiet)  { New-Object psobject -Property @{   	IP = $_.split("{,}")[0]	Description = $_.split("{,}")[1]	POC = $_.split("{,}")[2]   	Status = "Online"       }   Write-Host "$_ is Online" -ForegroundColor Green }  else  {New-Object psobject -Property @{   	IP = $_.split("{,}")[0]	Description = $_.split("{,}")[1]	POC = $_.split("{,}")[2]   	Status = "Offline" 

   }      Write-Host "$_ is Offline" -ForegroundColor Red#Email Notification Script#{$smtpbody = "$machineName, $svcName, $svcState"#$smtpto = (get-content "C:\inetpub\wwwroot\cfg\sendmail.cfg")[1]#$smtpfrom = (get-content "C:\inetpub\wwwroot\cfg\sendmail.cfg")[3]#$smtpsubject = "GrizzMon Notification Host Down. $dated"#$smtpservername = (get-content "C:\inetpub\wwwroot\cfg\sendmail.cfg")[9]#Send-MailMessage -To $smtpto -From $smtpfrom -subject $smtpsubject -Body $smtpbody -SMTPServer $smtpservername
 }
}
$HTMLBody = @"<meta http-equiv="refresh" content="30">
"@
# CSV for reports#$countresults1=@()#$dated,$Icmpresults.Server,$Icmpresults.Desc,$Icmpresults.Status | Add-Content "C:\inetpub\wwwroot\reports\icmp.csv"
#  Export to HTMLWrite-Host "EXPORTING TO HTML..." -ForegroundColor Yellow$icmpresults | ConvertTo-HTML -head $HTMLHead -body $HTMLBody -Property IP,Description,POC,Status |
#--- APPLY COLORED CELLS WITH REPLACE
Foreach {    if ($_ -like "*<td>Online</td>*" ) {        $_ -replace "<tr><td>","<tr bgcolor=#7CBB59><td>"     }    else {    $_ -replace "<tr><td>","<tr bgcolor=#EB2300><td>"    }} | out-file $generatedFile
#--- REMOVE HEADERS
#(Get-Content $generatedFile).replace("<tr><th>IP</th><th>Location</th><th>POC</th><th>Status</th></tr>","") | Set-Content $generatedFile -Force

#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force
#--- DISPLAY TIME THE SCRIPT TOOK TO RUN
$stopWatch.Stop()
Write-Host "Elapsed Runtime:" $stopWatch.Elapsed.Minutes "minutes and" $stopWatch.Elapsed.Seconds "seconds." -ForegroundColor Cyan

}
            
