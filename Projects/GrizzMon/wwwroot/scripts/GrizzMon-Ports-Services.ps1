$pshost = get-host
$pswindow = $pshost.ui.rawui$pswindow.WindowTitle = "Open Ports-Services Test"while($true) {#--- SET START TIME$stopWatch = [system.diagnostics.stopwatch]::startNew()
$stopWatch.Start()#Variables$CSS = get-content "..\css\theme.css"$dated = (Get-Date -format F)$HTMLHead =  "<Style>$CSS</style>"$HTMLHead = $HTMLHead + "<CENTER><Font size=5>Open Port Test</font></BR>"$hostListFile = "..\cfg\ports-services.csv"$generatedFile = "..\gen\ports-services.htm"$publishedFile = "..\gen-pub\ports-services.htm"
#Check content and ping computers$testResults = get-content $hostListFile | Where {-not ($_.StartsWith('#'))} | foreach {  if (Test-NetConnection -ComputerName $_.split("{,}")[0] -Port $_.split("{,}")[1]  -InformationLevel Quiet)  { New-Object psobject -Property @{   	Host = $_.split("{,}")[0]	Port = $_.split("{,}")[1]    Description = $_.split("{,}")[2]   	Status = "Open"       }   Write-Host "$_ is Open" -ForegroundColor Green }  else  {New-Object psobject -Property @{   	Host = $_.split("{,}")[0]	Port = $_.split("{,}")[1]    Description = $_.split("{,}")[2]   	Status = "Closed" 

   }      Write-Host "$_ is Closed" -ForegroundColor Red#Email Notification Script#{$smtpbody = "$machineName, $svcName, $svcState"#$smtpto = (get-content "..\cfg\sendmail.cfg")[1]#$smtpfrom = (get-content "..\cfg\sendmail.cfg")[3]#$smtpsubject = "GrizzMon Notification Host Down. $dated"#$smtpservername = (get-content "..\cfg\sendmail.cfg")[9]#Send-MailMessage -To $smtpto -From $smtpfrom -subject $smtpsubject -Body $smtpbody -SMTPServer $smtpservername
 }
}

$HTMLBody = @"<meta http-equiv="refresh" content="60">
<font size=1>$Dated</font> &nbsp; <font size=1>$minutes Minutes &nbsp; $seconds Seconds</font>"@
# CSV for reports#$countresults1=@()#$dated,$Icmpresults.Server,$Icmpresults.Desc,$Icmpresults.Status | Add-Content "..\reports\icmp.csv"
#  EXPORT TO HTML AND APPLY COLORED CELLS WITH REPLACEWrite-Host "EXPORTING TO HTML..." -ForegroundColor Yellow$testResults | ConvertTo-HTML -head $HTMLHead -body $HTMLBody -Property Description,Host,Port,Status |
Foreach {    if ($_ -like "*<td>Open</td>*" ) {        $_ -replace "<tr><td>","<tr bgcolor=#7CBB59><td>"     }    else {    $_ -replace "<tr><td>","<tr bgcolor=#EB2300><td>"    }} | out-file $generatedFile
#--- REMOVE HEADERS
(Get-Content $generatedFile).replace("<tr><th>Description</th><th>Host</th><th>Port</th><th>Status</th></tr>","") | Set-Content $generatedFile -Force

#--- PUBLISH THE HTML FILE
Copy-Item $generatedFile $publishedFile -Force

#--- SLEEPWrite-Host "Sleeping 60 seconds..." -ForegroundColor YellowStart-Sleep 60

#--- SET VARIABLES FOR NEXT RUN
$minutes = $stopWatch.Elapsed.Minutes
$seconds = $stopWatch.Elapsed.Seconds

#--- DISPLAY TIME THE SCRIPT TOOK TO RUN
$stopWatch.Stop()
Write-Host "Elapsed Runtime:" $stopWatch.Elapsed.Minutes "minutes and" $stopWatch.Elapsed.Seconds "seconds." -ForegroundColor Cyan



}
            
